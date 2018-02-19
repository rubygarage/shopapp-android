package com.shopify.api

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.client.shop.gateway.Api
import com.client.shop.gateway.ApiCallback
import com.client.shop.gateway.entity.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shopify.api.adapter.*
import com.shopify.api.call.MutationCallWrapper
import com.shopify.api.call.QueryCallWrapper
import com.shopify.api.entity.AccessData
import com.shopify.api.entity.ApiCountry
import com.shopify.api.entity.ApiCountryResponse
import com.shopify.api.ext.isSingleOptions
import com.shopify.api.retrofit.CountriesService
import com.shopify.api.retrofit.RestClient
import com.shopify.buy3.*
import com.shopify.graphql.support.ID
import com.shopify.util.AssetsReader
import net.danlew.android.joda.JodaTimeAndroid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


class ShopifyApi(private val context: Context, baseUrl: String, accessToken: String) : Api {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val graphClient: GraphClient = GraphClient.builder(context)
        .shopDomain(baseUrl)
        .accessToken(accessToken)
        .build()

    private val retrofit: Retrofit = RestClient.providesRetrofit()

    init {
        JodaTimeAndroid.init(context)
    }

    companion object {
        private const val ITEMS_COUNT = 250
        private const val EMAIL = "email"
        private const val ACCESS_TOKEN = "access_token"
        private const val EXPIRES_DATE = "expires_date"
        private const val RETRY_HANDLER_DELAY = 500L
        private const val RETRY_HANDLER_MAX_COUNT = 10
        private const val UNAUTHORIZED_ERROR = "Unauthorized"
        private const val PRODUCT_TYPE_FILTER_KEY = "product_type:"
        private const val TITLE_FILTER_KEY = "title:"
        private const val AND_LOGICAL_KEY = "AND"
        private const val REST_OF_WORLD = "Rest of World"
        private const val COUNTRIES_FILE_NAME = "countries.json"
    }

    private fun saveSession(accessData: AccessData) {
        preferences.edit()
            .putString(EMAIL, accessData.email)
            .putString(ACCESS_TOKEN, accessData.accessToken)
            .putLong(EXPIRES_DATE, accessData.expiresAt)
            .apply()
    }

    private fun getSession(): AccessData? {
        val email = preferences.getString(EMAIL, null)
        val accessToken = preferences.getString(ACCESS_TOKEN, null)
        val expiresDate = preferences.getLong(EXPIRES_DATE, 0)
        val isExpired = expiresDate <= System.currentTimeMillis()
        return if (email != null && accessToken != null && !isExpired) {
            AccessData(
                email,
                accessToken,
                expiresDate
            )
        } else {
            null
        }
    }

    private fun removeSession() {
        preferences.edit()
            .remove(EMAIL)
            .remove(ACCESS_TOKEN)
            .remove(EXPIRES_DATE)
            .apply()
    }

    override fun getProductList(perPage: Int, paginationValue: Any?, sortBy: SortType?,
                                keyword: String?, excludeKeyword: String?,
                                callback: ApiCallback<List<Product>>) {
        val reverse = sortBy == SortType.RECENT
        var phrase = keyword
        if (sortBy == SortType.TYPE && keyword != null) {
            phrase = "-$TITLE_FILTER_KEY\"$excludeKeyword\" $AND_LOGICAL_KEY $PRODUCT_TYPE_FILTER_KEY\"$keyword\""
        }
        queryProducts(perPage, paginationValue, phrase, reverse, sortBy, callback)
    }

    override fun searchProductList(perPage: Int, paginationValue: Any?,
                                   searchQuery: String, callback: ApiCallback<List<Product>>) {
        queryProducts(perPage, paginationValue, searchQuery, false, SortType.NAME, callback)
    }

    override fun getProduct(id: String, callback: ApiCallback<Product>) {

        val nodeId = ID(id)
        val query = Storefront.query { queryBuilder ->
            queryBuilder
                .shop { shopQueryBuilder -> shopQueryBuilder.paymentSettings({ it.currencyCode() }) }
                .node(nodeId) { nodeQuery ->
                    nodeQuery.id()
                    nodeQuery.onProduct { productQuery ->
                        getDefaultProductQuery(productQuery)
                            .descriptionHtml()
                            .images({ it.first(ITEMS_COUNT) }, { imageConnectionQuery ->
                                imageConnectionQuery.edges({ imageEdgeQuery ->
                                    imageEdgeQuery.node({ imageQuery ->
                                        imageQuery
                                            .id()
                                            .src()
                                            .altText()
                                    })
                                })
                            })
                            .variants({ it.first(ITEMS_COUNT) }) { productVariantConnectionQuery ->
                                productVariantConnectionQuery
                                    .edges { productVariantEdgeQuery ->
                                        productVariantEdgeQuery
                                            .node { productVariantQuery ->
                                                getDefaultProductVariantQuery(productVariantQuery)
                                            }
                                    }
                            }

                    }
                }
        }


        val call = graphClient.queryGraph(query)
        call.enqueue(object : QueryCallWrapper<Product>(callback) {
            override fun adapt(data: Storefront.QueryRoot): Product {
                return ProductAdapter.adapt(data.shop, data.node as Storefront.Product)
            }
        })
    }

    override fun getCategoryList(perPage: Int, paginationValue: Any?, callback: ApiCallback<List<Category>>) {

        val query = Storefront.query { rootQuery ->
            rootQuery.shop { shopQuery ->
                shopQuery.collections({ args ->
                    args.first(ITEMS_COUNT)
                    if (paginationValue != null) {
                        args.after(paginationValue.toString())
                    }
                }) {
                    it.edges {
                        it.cursor()
                            .node {
                                it.title()
                                    .description()
                                    .updatedAt()
                                    .image({
                                        it.id()
                                            .src()
                                            .altText()
                                    })
                            }
                    }
                }
            }
        }

        val call = graphClient.queryGraph(query)
        call.enqueue(object : QueryCallWrapper<List<Category>>(callback) {
            override fun adapt(data: Storefront.QueryRoot): List<Category> {
                return CategoryListAdapter.adapt(data.shop)
            }
        })
    }

    override fun getCategoryDetails(id: String, perPage: Int, paginationValue: Any?, sortBy: SortType?,
                                    callback: ApiCallback<Category>) {

        val reverse = sortBy == SortType.RECENT || sortBy == SortType.PRICE_HIGH_TO_LOW

        val nodeId = ID(id)
        val query = Storefront.query { rootQuery ->
            rootQuery
                .shop { shopQuery -> shopQuery.paymentSettings({ it.currencyCode() }) }
                .node(nodeId) { nodeQuery ->
                    nodeQuery
                        .id()
                        .onCollection { builder ->
                            builder.title()
                                .description()
                                .descriptionHtml()
                                .updatedAt()
                                .image({ imageQuery ->
                                    imageQuery
                                        .id()
                                        .src()
                                        .altText()
                                })
                                .products({ args ->
                                    args.first(ITEMS_COUNT)
                                    if (paginationValue != null) {
                                        args.after(paginationValue.toString())
                                    }
                                    val key = getProductCollectionSortKey(sortBy)
                                    if (key != null) {
                                        args.sortKey(key)
                                    }
                                    args.reverse(reverse)
                                }, { productConnectionQuery ->
                                    productConnectionQuery.edges({ productEdgeQuery ->
                                        productEdgeQuery
                                            .cursor()
                                            .node({ productQuery ->
                                                getDefaultProductQuery(productQuery)
                                                    .images({ it.first(1) }, { imageConnectionQuery ->
                                                        imageConnectionQuery.edges({ imageEdgeQuery ->
                                                            imageEdgeQuery.node({ imageQuery ->
                                                                imageQuery
                                                                    .id()
                                                                    .src()
                                                                    .altText()
                                                            })
                                                        })
                                                    })
                                                    .variants({ it.first(ITEMS_COUNT) }) { productVariantConnectionQuery ->
                                                        productVariantConnectionQuery.edges { productVariantEdgeQuery ->
                                                            productVariantEdgeQuery.node({ it.price() })
                                                        }
                                                    }
                                            })
                                    })
                                })
                        }
                }
        }

        val call = graphClient.queryGraph(query)
        call.enqueue(object : QueryCallWrapper<Category>(callback) {
            override fun adapt(data: Storefront.QueryRoot): Category {
                return CategoryAdapter.adapt(data.shop, data.node as Storefront.Collection)
            }
        })
    }

    override fun getShopInfo(callback: ApiCallback<Shop>) {

        val query = Storefront.query { rootQuery ->
            rootQuery.shop { shopQuery ->
                shopQuery
                    .name()
                    .description()
                    .privacyPolicy { shopPolicyQuery -> shopPolicyQuery.title().body().url() }
                    .refundPolicy { refundPolicyQuery -> refundPolicyQuery.title().body().url() }
                    .termsOfService { termsOfServiceQuery -> termsOfServiceQuery.title().body().url() }
            }
        }

        val call = graphClient.queryGraph(query)
        call.enqueue(object : QueryCallWrapper<Shop>(callback) {
            override fun adapt(data: Storefront.QueryRoot): Shop {
                return ShopAdapter.adapt(data)
            }
        })
    }

    override fun getArticle(id: String, callback: ApiCallback<Article>) {
        val nodeId = ID(id)
        val query = Storefront.query {
            it.node(nodeId) {
                it.onArticle {
                    getDefaultArticleQuery(it)
                }
            }
        }

        val call = graphClient.queryGraph(query)
        call.enqueue(object : QueryCallWrapper<Article>(callback) {
            override fun adapt(data: Storefront.QueryRoot): Article {
                return ArticleAdapter.adapt(data.node as Storefront.Article)
            }
        })
    }


    override fun getArticleList(perPage: Int, paginationValue: Any?, sortBy: SortType?,
                                reverse: Boolean, callback: ApiCallback<List<Article>>) {
        val query = Storefront.query { rootQuery ->
            rootQuery.shop { shopQuery ->
                shopQuery.articles({ args ->
                    args.first(perPage)
                    if (paginationValue != null) {
                        args.after(paginationValue.toString())
                    }
                    val key = getArticleSortKey(sortBy)
                    if (key != null) {
                        args.sortKey(key)
                    }
                    args.reverse(reverse)
                }) { articleConnectionQuery ->
                    articleConnectionQuery.edges { articleEdgeQuery ->
                        articleEdgeQuery.cursor().node {
                            getDefaultArticleQuery(it)
                        }
                    }
                }
            }
        }

        val call = graphClient.queryGraph(query)
        call.enqueue(object : QueryCallWrapper<List<Article>>(callback) {
            override fun adapt(data: Storefront.QueryRoot): List<Article> {
                return ArticleListAdapter.adapt(data.shop.articles.edges)
            }
        })
    }

    override fun signUp(firstName: String, lastName: String, email: String,
                        password: String, phone: String, callback: ApiCallback<Unit>) {

        val customerCreateInput = Storefront.CustomerCreateInput(email, password)
            .setFirstName(firstName)
            .setLastName(lastName)

        if (phone.isNotBlank()) {
            customerCreateInput.phone = phone
        }

        val customerQuery = Storefront.CustomerCreatePayloadQueryDefinition {
            it.customer { getDefaultCustomerQuery(it) }
                .userErrors { getDefaultUserErrors(it) }
        }

        val mutationQuery = Storefront.mutation { query -> query.customerCreate(customerCreateInput, customerQuery) }
        val call = graphClient.mutateGraph(mutationQuery)
        call.enqueue(object : GraphCall.Callback<Storefront.Mutation> {
            override fun onResponse(response: GraphResponse<Storefront.Mutation>) {
                val error = ErrorAdapter.adaptErrors(response.errors())
                if (error != null) {
                    callback.onFailure(error)
                } else {
                    response.data()?.customerCreate?.let { customerCreate ->
                        val userError = ErrorAdapter.adaptUserError(customerCreate.userErrors)
                        if (userError != null) {
                            callback.onFailure(userError)
                        } else if (customerCreate.customer != null) {
                            val tokenResponse = requestToken(email, password)
                            if (tokenResponse != null) {
                                tokenResponse.first?.let {
                                    callback.onResult(Unit)
                                }
                                tokenResponse.second?.let {
                                    callback.onFailure(it)
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(error: GraphError) {
                callback.onFailure(ErrorAdapter.adapt(error))
            }
        })
    }

    override fun signIn(email: String, password: String, callback: ApiCallback<Unit>) {
        val tokenResponse = requestToken(email, password)
        if (tokenResponse != null) {
            tokenResponse.first?.let {
                callback.onResult(Unit)
            }
            tokenResponse.second?.let {
                callback.onFailure(it)
            }
        } else {
            callback.onFailure(Error.Content())
        }
    }

    override fun isLoggedIn(callback: ApiCallback<Boolean>) {
        callback.onResult(getSession() != null)
    }

    override fun forgotPassword(email: String, callback: ApiCallback<Unit>) {

        val mutationQuery = Storefront.mutation {
            it.customerRecover(email, {
                it.userErrors { getDefaultUserErrors(it) }
            })
        }

        graphClient.mutateGraph(mutationQuery).enqueue(object : GraphCall.Callback<Storefront.Mutation> {
            override fun onFailure(error: GraphError) {
                callback.onFailure(ErrorAdapter.adapt(error))
            }

            override fun onResponse(response: GraphResponse<Storefront.Mutation>) {
                val error = ErrorAdapter.adaptErrors(response.errors())
                val userError = ErrorAdapter.adaptUserError(response.data()?.customerRecover?.userErrors)
                when {
                    error != null -> callback.onFailure(error)
                    userError != null -> callback.onFailure(userError)
                    else -> callback.onResult(Unit)
                }
            }
        })
    }

    override fun signOut(callback: ApiCallback<Unit>) {
        removeSession()
        callback.onResult(Unit)
    }

    override fun getCustomer(callback: ApiCallback<Customer?>) {

        val accessData = getSession()

        if (accessData != null) {
            val query = Storefront.query {
                it.customer(accessData.accessToken) { getDefaultCustomerQuery(it) }
            }
            graphClient.queryGraph(query).enqueue(object : QueryCallWrapper<Customer>(callback) {
                override fun adapt(data: Storefront.QueryRoot): Customer? {
                    val adaptee = data.customer
                    return if (adaptee != null) {
                        adaptee.let { CustomerAdapter.adapt(it) }
                    } else {
                        removeSession()
                        null
                    }
                }
            })
        } else {
            callback.onFailure(Error.NonCritical(UNAUTHORIZED_ERROR))
        }
    }

    override fun createCustomerAddress(address: Address, callback: ApiCallback<String>) {

        val session = getSession()
        if (session == null) {
            callback.onFailure(Error.NonCritical(UNAUTHORIZED_ERROR))
        } else {
            val mailingAddressInput = Storefront.MailingAddressInput()
                .setAddress1(address.address)
                .setAddress2(address.secondAddress)
                .setCity(address.city)
                .setProvince(address.state)
                .setCountry(address.country)
                .setFirstName(address.firstName)
                .setLastName(address.lastName)
                .setPhone(address.phone)
                .setZip(address.zip)

            val mutation = Storefront.mutation {
                it.customerAddressCreate(session.accessToken, mailingAddressInput, {
                    it.customerAddress { it.firstName() }
                    it.userErrors { getDefaultUserErrors(it) }
                })
            }

            graphClient.mutateGraph(mutation).enqueue(object : MutationCallWrapper<String>(callback) {
                override fun adapt(data: Storefront.Mutation?): String? {
                    return data?.customerAddressCreate?.let {
                        val userError = ErrorAdapter.adaptUserError(it.userErrors)
                        if (userError != null) {
                            callback.onFailure(userError)
                        } else {
                            return it.customerAddress.id.toString()
                        }
                        return null
                    }
                }
            })
        }
    }

    override fun editCustomerAddress(addressId: String, address: Address, callback: ApiCallback<Unit>) {

        val session = getSession()
        if (session == null) {
            callback.onFailure(Error.NonCritical(UNAUTHORIZED_ERROR))
        } else {
            val mutation = Storefront.mutation {

                val mailingAddressInput = Storefront.MailingAddressInput()
                    .setAddress1(address.address)
                    .setAddress2(address.secondAddress)
                    .setCity(address.city)
                    .setProvince(address.state)
                    .setCountry(address.country)
                    .setFirstName(address.firstName)
                    .setLastName(address.lastName)
                    .setPhone(address.phone)
                    .setZip(address.zip)

                it.customerAddressUpdate(session.accessToken, ID(addressId), mailingAddressInput, {
                    it.customerAddress { it.firstName() }
                    it.userErrors { getDefaultUserErrors(it) }
                })
            }

            graphClient.mutateGraph(mutation).enqueue(object : MutationCallWrapper<Unit>(callback) {
                override fun adapt(data: Storefront.Mutation?): Unit? {
                    return data?.customerAddressUpdate?.let {
                        val userError = ErrorAdapter.adaptUserError(it.userErrors)
                        if (userError != null) {
                            callback.onFailure(userError)
                        } else {
                            return Unit
                        }
                        return null
                    }
                }
            })
        }
    }

    override fun deleteCustomerAddress(addressId: String, callback: ApiCallback<Unit>) {

        val session = getSession()
        if (session == null) {
            callback.onFailure(Error.NonCritical(UNAUTHORIZED_ERROR))
        } else {
            val mutation = Storefront.mutation {
                it.customerAddressDelete(ID(addressId), session.accessToken, {
                    it.deletedCustomerAddressId()
                    it.userErrors { getDefaultUserErrors(it) }
                })
            }

            graphClient.mutateGraph(mutation).enqueue(object : MutationCallWrapper<Unit>(callback) {
                override fun adapt(data: Storefront.Mutation?): Unit? {
                    return data?.customerAddressDelete?.let {
                        val userError = ErrorAdapter.adaptUserError(it.userErrors)
                        if (userError != null) {
                            callback.onFailure(userError)
                        } else {
                            return Unit
                        }
                        return null
                    }
                }
            })
        }
    }

    override fun setDefaultShippingAddress(addressId: String, callback: ApiCallback<Unit>) {
        val session = getSession()
        if (session == null) {
            callback.onFailure(Error.NonCritical(UNAUTHORIZED_ERROR))
        } else {
            val mutation = Storefront.mutation {
                it.customerDefaultAddressUpdate(session.accessToken, ID(addressId), {
                    it.customer { it.firstName() }
                    it.userErrors { getDefaultUserErrors(it) }
                })
            }

            graphClient.mutateGraph(mutation).enqueue(object : MutationCallWrapper<Unit>(callback) {
                override fun adapt(data: Storefront.Mutation?): Unit? {
                    return data?.customerDefaultAddressUpdate?.let {
                        val userError = ErrorAdapter.adaptUserError(it.userErrors)
                        if (userError != null) {
                            callback.onFailure(userError)
                        } else {
                            return Unit
                        }
                        return Unit
                    }
                }
            })
        }
    }

    override fun getCountries(callback: ApiCallback<List<Country>>) {
        val countryService = retrofit.create(CountriesService::class.java)

        countryService.recentDrives().enqueue(object : Callback<ApiCountryResponse> {
            override fun onResponse(call: Call<ApiCountryResponse>?, response: Response<ApiCountryResponse>?) {
                if (response != null) {

                    if (response.isSuccessful && response.body() != null) {
                        val countries = CountryListAdapter.adapt(response.body()?.countries)
                        if (countries.any { it.name == REST_OF_WORLD }) {
                            callback.onResult(CountryListAdapter.adapt(getAllCountriesList()))
                        }
                    }

                    if (response.errorBody() != null) {
                        callback.onFailure(Error.Content())
                    }
                }
            }

            override fun onFailure(call: Call<ApiCountryResponse>?, t: Throwable?) {
                callback.onFailure(Error.Content())
            }
        })
    }

    private fun getAllCountriesList(): List<ApiCountry> {
        val countriesType = object : TypeToken<List<ApiCountry>>() {}.type
        return Gson().fromJson(AssetsReader.read(ShopifyApi.COUNTRIES_FILE_NAME, context), countriesType)
    }

    private fun requestToken(email: String, password: String): Pair<AccessData?, Error?>? {

        val accessTokenInput = Storefront.CustomerAccessTokenCreateInput(email, password)
        val accessTokenQuery = Storefront.CustomerAccessTokenCreatePayloadQueryDefinition { queryDefinition ->
            queryDefinition.customerAccessToken { accessTokenQuery -> accessTokenQuery.accessToken().expiresAt() }
                .userErrors { getDefaultUserErrors(it) }
        }

        val mutationQuery = Storefront.mutation { query -> query.customerAccessTokenCreate(accessTokenInput, accessTokenQuery) }
        val call = graphClient.mutateGraph(mutationQuery)
        return call.execute().data()?.customerAccessTokenCreate?.let {
            val accessData = it.customerAccessToken?.let {
                val accessData = AccessData(email, it.accessToken, it.expiresAt.millis)
                saveSession(accessData)
                accessData
            }

            val error = ErrorAdapter.adaptUserError(it.userErrors)
            Pair(accessData, error)
        }
    }

    override fun createCheckout(cartProductList: List<CartProduct>, callback: ApiCallback<Checkout>) {

        val input = Storefront.CheckoutCreateInput().setLineItems(
            cartProductList.map {
                Storefront.CheckoutLineItemInput(it.quantity, ID(it.productVariant.id))
            }
        )

        val mutateQuery = Storefront.mutation {
            it.checkoutCreate(input) {
                it.checkout { getDefaultCheckoutQuery(it) }
                    .userErrors { getDefaultUserErrors(it) }
            }
        }

        graphClient.mutateGraph(mutateQuery).enqueue(object : MutationCallWrapper<Checkout>(callback) {
            override fun adapt(data: Storefront.Mutation?): Checkout? {
                return data?.checkoutCreate?.let {
                    val userError = ErrorAdapter.adaptUserError(it.userErrors)
                    if (userError != null) {
                        callback.onFailure(userError)
                    } else {
                        return CheckoutAdapter.adapt(it.checkout)
                    }
                    return null
                }
            }
        })
    }

    override fun getCheckout(checkoutId: String, callback: ApiCallback<Checkout>) {
        val query = Storefront.query({
            it.node(ID(checkoutId), {
                it.onCheckout {
                    getDefaultCheckoutQuery(it)
                }
            })
        })

        graphClient.queryGraph(query).enqueue(object : QueryCallWrapper<Checkout>(callback) {
            override fun adapt(data: Storefront.QueryRoot): Checkout {
                val checkout = data.node as Storefront.Checkout
                return CheckoutAdapter.adapt(checkout)
            }
        })
    }

    override fun setShippingAddress(checkoutId: String, address: Address, callback: ApiCallback<Checkout>) {

        val mailingAddressInput = Storefront.MailingAddressInput()
            .setAddress1(address.address)
            .setAddress2(address.secondAddress)
            .setCity(address.city)
            .setProvince(address.state)
            .setCountry(address.country)
            .setFirstName(address.firstName)
            .setLastName(address.lastName)
            .setPhone(address.phone)
            .setZip(address.zip)

        val checkoutQuery = Storefront.mutation {
            it.checkoutShippingAddressUpdate(mailingAddressInput, ID(checkoutId), {
                it.checkout { getDefaultCheckoutQuery(it) }
                it.userErrors { getDefaultUserErrors(it) }
            })
        }

        graphClient.mutateGraph(checkoutQuery).enqueue(object : MutationCallWrapper<Checkout>(callback) {
            override fun adapt(data: Storefront.Mutation?): Checkout? {
                return data?.checkoutShippingAddressUpdate?.let {
                    val userError = ErrorAdapter.adaptUserError(it.userErrors)
                    if (userError != null) {
                        callback.onFailure(userError)
                    } else {
                        return CheckoutAdapter.adapt(data.checkoutShippingAddressUpdate.checkout)
                    }
                    return null
                }
            }
        })
    }

    override fun getShippingRates(checkoutId: String, callback: ApiCallback<List<ShippingRate>>) {

        val retryHandler = RetryHandler.delay(RETRY_HANDLER_DELAY, TimeUnit.MILLISECONDS)
            .maxCount(RETRY_HANDLER_MAX_COUNT)
            .whenResponse<Storefront.QueryRoot> {
                val checkout = (it.data()?.node as? Storefront.Checkout)
                checkout?.availableShippingRates?.let { !it.ready } ?: true
            }
            .build()

        val query = Storefront.query {
            it.node(ID(checkoutId), {
                it.onCheckout {
                    it.availableShippingRates {
                        it.ready()
                            .shippingRates {
                                it.title()
                                    .price()
                                    .handle()
                            }
                    }
                }
            })
        }

        graphClient.queryGraph(query).enqueue(object : GraphCall.Callback<Storefront.QueryRoot> {

            override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
                val checkout = (response.data()?.node as? Storefront.Checkout)
                callback.onResult(checkout?.availableShippingRates?.shippingRates?.let {
                    it.map { ShippingRateAdapter.adapt(it) }
                } ?: emptyList())
            }

            override fun onFailure(error: GraphError) {
                callback.onFailure(ErrorAdapter.adapt(error))
            }
        }, null, retryHandler)
    }

    override fun selectShippingRate(checkoutId: String, shippingRate: ShippingRate, callback: ApiCallback<Checkout>) {

        val checkoutQuery = Storefront.mutation {
            it.checkoutShippingLineUpdate(ID(checkoutId), shippingRate.handle, {
                it.userErrors { getDefaultUserErrors(it) }
                    .checkout {
                        getDefaultCheckoutQuery(it)
                    }
            })
        }

        graphClient.mutateGraph(checkoutQuery).enqueue(object : MutationCallWrapper<Checkout>(callback) {
            override fun adapt(data: Storefront.Mutation?): Checkout? {
                return data?.checkoutShippingLineUpdate?.let {
                    val userError = ErrorAdapter.adaptUserError(it.userErrors)
                    if (userError != null) {
                        callback.onFailure(userError)
                    } else {
                        return CheckoutAdapter.adapt(it.checkout)
                    }
                    return null
                }
            }
        })
    }

    override fun getAcceptedCardTypes(callback: ApiCallback<List<CardType>>) {

        val query = Storefront.query {
            it.shop { it.paymentSettings { it.acceptedCardBrands() } }
        }
        graphClient.queryGraph(query).enqueue(object : QueryCallWrapper<List<CardType>>(callback) {
            override fun adapt(data: Storefront.QueryRoot): List<CardType> {
                val adaptee = data.shop.paymentSettings.acceptedCardBrands
                return CardAdapter.adapt(adaptee)
            }
        })
    }

    override fun getCardToken(card: Card, callback: ApiCallback<String>) {

        val vaultCallback = object : ApiCallback<String> {
            override fun onResult(result: String) {
                val cardClient = CardClient()
                val creditCard = CreditCard.builder()
                    .firstName(card.firstName)
                    .lastName(card.lastName)
                    .number(card.cardNumber)
                    .expireMonth(card.expireMonth)
                    .expireYear(card.expireYear)
                    .verificationCode(card.verificationCode)
                    .build()
                cardClient.vault(creditCard, result).enqueue(object : CreditCardVaultCall.Callback {

                    override fun onResponse(token: String) {
                        callback.onResult(token)
                    }

                    override fun onFailure(error: IOException) {
                        callback.onFailure(Error.Content())
                    }
                })
            }

            override fun onFailure(error: Error) {
                callback.onFailure(error)
            }
        }

        val query = Storefront.query {
            it.shop {
                it.paymentSettings { it.cardVaultUrl() }
            }
        }
        val call = graphClient.queryGraph(query)
        call.enqueue(object : QueryCallWrapper<String>(vaultCallback) {
            override fun adapt(data: Storefront.QueryRoot): String =
                data.shop?.paymentSettings?.cardVaultUrl ?: ""
        })
    }

    override fun completeCheckoutByCard(checkout: Checkout, email: String, address: Address, creditCardVaultToken: String,
                                        callback: ApiCallback<Order>) {

        val amount = checkout.totalPrice
        val idempotencyKey = UUID.randomUUID().toString()
        val billingAddress = Storefront.MailingAddressInput()

        billingAddress.setAddress1(address.address)
            .setCity(address.city)
            .setProvince(address.state)
            .setCountry(address.country)
            .setFirstName(address.firstName)
            .setLastName(address.lastName)
            .setPhone(address.phone).zip = address.zip

        val cardPayCallback = object : ApiCallback<Boolean> {
            override fun onResult(result: Boolean) {
                completeCheckout(checkout.checkoutId, callback)
            }

            override fun onFailure(error: Error) {
                callback.onFailure(error)
            }
        }

        val creditCardPaymentInput = Storefront.CreditCardPaymentInput(amount, idempotencyKey,
            billingAddress, creditCardVaultToken)

        val mutationQuery = Storefront.mutation {
            it.checkoutEmailUpdate(ID(checkout.checkoutId), email, { it.checkout { it.totalPrice() } })
            it.checkoutCompleteWithCreditCard(ID(checkout.checkoutId), creditCardPaymentInput) {
                it.payment {
                    it.ready().errorMessage()
                }.checkout {
                        it.ready()
                    }.userErrors { getDefaultUserErrors(it) }
            }
        }

        graphClient.mutateGraph(mutationQuery).enqueue(object : MutationCallWrapper<Boolean>(cardPayCallback) {
            override fun adapt(data: Storefront.Mutation?): Boolean? {
                return data?.checkoutCompleteWithCreditCard?.let {
                    val userError = ErrorAdapter.adaptUserError(it.userErrors)
                    if (userError != null) {
                        cardPayCallback.onFailure(userError)
                        null
                    } else {
                        if (it.checkout?.ready == true) true else null
                    }
                }
            }
        })
    }

    private fun completeCheckout(checkoutId: String, callback: ApiCallback<Order>) {

        val query = Storefront.query {
            it.node(ID(checkoutId), {
                it.onCheckout({
                    it.order { getDefaultOrderQuery(it) }
                })
            })
        }

        val retryHandler = RetryHandler.delay(RETRY_HANDLER_DELAY, TimeUnit.MILLISECONDS)
            .maxCount(RETRY_HANDLER_MAX_COUNT)
            .whenResponse<Storefront.QueryRoot> {
                val checkout = (it.data()?.node as? Storefront.Checkout)
                checkout?.order == null
            }
            .build()

        graphClient.queryGraph(query).enqueue(object : QueryCallWrapper<Order>(callback) {
            override fun adapt(data: Storefront.QueryRoot): Order? {
                val checkout = data.node as? Storefront.Checkout
                val order = checkout?.order
                return order?.let { OrderAdapter.adapt(checkout.order) }
            }
        }, null, retryHandler)
    }

    private fun getProductSortKey(sortType: SortType?): Storefront.ProductSortKeys? {
        if (sortType != null) {
            return when (sortType) {
                SortType.NAME -> Storefront.ProductSortKeys.TITLE
                SortType.RECENT -> Storefront.ProductSortKeys.CREATED_AT
                SortType.RELEVANT -> Storefront.ProductSortKeys.RELEVANCE
                SortType.TYPE -> Storefront.ProductSortKeys.PRODUCT_TYPE
                else -> null
            }
        }
        return null
    }

    private fun getCollectionSortKey(sortType: SortType?): Storefront.CollectionSortKeys? {
        if (sortType != null) {
            return when (sortType) {
                SortType.NAME -> Storefront.CollectionSortKeys.TITLE
                SortType.RECENT -> Storefront.CollectionSortKeys.UPDATED_AT
                SortType.RELEVANT -> Storefront.CollectionSortKeys.RELEVANCE
                else -> null
            }
        }
        return null
    }

    private fun getProductCollectionSortKey(sortType: SortType?): Storefront.ProductCollectionSortKeys? {
        if (sortType != null) {
            return when (sortType) {
                SortType.NAME -> Storefront.ProductCollectionSortKeys.TITLE
                SortType.RECENT -> Storefront.ProductCollectionSortKeys.CREATED
                SortType.RELEVANT -> Storefront.ProductCollectionSortKeys.RELEVANCE
                SortType.PRICE_HIGH_TO_LOW,
                SortType.PRICE_LOW_TO_HIGH -> Storefront.ProductCollectionSortKeys.PRICE
                else -> null
            }
        }
        return null
    }

    private fun getArticleSortKey(sortType: SortType?): Storefront.ArticleSortKeys? {
        if (sortType != null) {
            return when (sortType) {
                SortType.NAME -> Storefront.ArticleSortKeys.TITLE
                SortType.RECENT -> Storefront.ArticleSortKeys.UPDATED_AT
                SortType.RELEVANT -> Storefront.ArticleSortKeys.RELEVANCE
                else -> null
            }
        }
        return null
    }

    override fun getOrders(perPage: Int, paginationValue: Any?, callback: ApiCallback<List<Order>>) {
        val token = getSession()!!.accessToken
        val query = Storefront.query { root ->
            root.customer(token) { customer ->
                customer.orders({ args ->
                    args.first(perPage)
                    if (paginationValue != null) {
                        args.after(paginationValue.toString())
                    }
                    args.reverse(true)
                }
                ) { connection ->
                    connection.edges { edge ->
                        edge.cursor().node { node ->
                            getDefaultOrderQuery(node)
                        }
                    }
                }
            }
        }

        val call = graphClient.queryGraph(query)
        call.enqueue(object : QueryCallWrapper<List<Order>>(callback) {
            override fun adapt(data: Storefront.QueryRoot): List<Order> =
                OrderListAdapter.adapt(data.customer.orders)
        })

    }

    override fun getOrder(orderId: String, callback: ApiCallback<Order>) {

        val nodeId = ID(orderId)
        val query = Storefront.query { root ->
            root.node(nodeId) {
                it.onOrder {
                    getDefaultOrderQuery(it)
                        .subtotalPrice()
                        .totalShippingPrice()
                        .shippingAddress {
                            getDefaultAddressQuery(it)
                        }
                }
            }
        }

        val call = graphClient.queryGraph(query)
        call.enqueue(object : QueryCallWrapper<Order>(callback) {
            override fun adapt(data: Storefront.QueryRoot): Order {
                (data.node as Storefront.Order).lineItems.edges.forEach {
                    if (it.node.variant.product.isSingleOptions()) {
                        it.node.variant.selectedOptions = null
                    }
                }
                return OrderAdapter.adapt(data.node as Storefront.Order)
            }
        })

    }

    override fun editCustomerInfo(firstName: String, lastName: String, phone: String, callback: ApiCallback<Customer>) {
        val session = getSession()
        if (session == null) {
            callback.onFailure(Error.NonCritical(UNAUTHORIZED_ERROR))
        } else {

            val customerInput = Storefront.CustomerUpdateInput()
                .setFirstName(firstName)
                .setLastName(lastName)

            if (phone.isNotBlank()) {
                customerInput.phone = phone
            }

            val mutateQuery = getDefaultCustomerUpdateMutationQuery(session.accessToken, customerInput)

            graphClient.mutateGraph(mutateQuery).enqueue(object : MutationCallWrapper<Customer>(callback) {
                override fun adapt(data: Storefront.Mutation?): Customer? {
                    return data?.customerUpdate?.let {
                        val userError = ErrorAdapter.adaptUserError(it.userErrors)
                        if (userError != null) {
                            callback.onFailure(userError)
                        } else {
                            return CustomerAdapter.adapt(it.customer)
                        }
                        return null
                    }
                }
            })
        }
    }

    override fun changePassword(password: String, callback: ApiCallback<Unit>) {
        val session = getSession()
        if (session == null) {
            callback.onFailure(Error.NonCritical(UNAUTHORIZED_ERROR))
        } else {

            val customerInput = Storefront.CustomerUpdateInput()
                .setPassword(password)

            val mutateQuery = getDefaultCustomerUpdateMutationQuery(session.accessToken, customerInput)

            graphClient.mutateGraph(mutateQuery).enqueue(object : MutationCallWrapper<Unit>(callback) {
                override fun adapt(data: Storefront.Mutation?): Unit? {
                    return data?.customerUpdate?.let {
                        val userError = ErrorAdapter.adaptUserError(it.userErrors)
                        if (userError != null) {
                            callback.onFailure(userError)
                        } else {
                            val tokenResponse = requestToken(it.customer.email, password)
                            if (tokenResponse != null) {
                                tokenResponse.first?.let {
                                    callback.onResult(Unit)
                                }
                                tokenResponse.second?.let {
                                    callback.onFailure(it)
                                }
                            } else {
                                callback.onFailure(Error.Content())
                            }
                        }
                        return Unit
                    }
                }
            })

        }
    }

    override fun updateCustomerSettings(isAcceptMarketing: Boolean, callback: ApiCallback<Unit>) {
        val session = getSession()
        if (session == null) {
            callback.onFailure(Error.NonCritical(UNAUTHORIZED_ERROR))
        } else {

            val customerInput = Storefront.CustomerUpdateInput()
                .setAcceptsMarketing(isAcceptMarketing)

            val mutateQuery = getDefaultCustomerUpdateMutationQuery(session.accessToken, customerInput)

            graphClient.mutateGraph(mutateQuery).enqueue(object : MutationCallWrapper<Unit>(callback) {
                override fun adapt(data: Storefront.Mutation?): Unit? {
                    return data?.customerUpdate?.let {
                        val userError = ErrorAdapter.adaptUserError(it.userErrors)
                        if (userError != null) {
                            callback.onFailure(userError)
                        }
                        return Unit
                    }
                }
            })

        }
    }

    private fun getDefaultCustomerUpdateMutationQuery(token: String, customerInput: Storefront.CustomerUpdateInput?): Storefront.MutationQuery? {
        return Storefront.mutation {
            it.customerUpdate(token, customerInput, {
                it.customer { getDefaultCustomerQuery(it) }
                it.userErrors { getDefaultUserErrors(it) }
            })
        }
    }

    private fun getDefaultOrderQuery(orderQuery: Storefront.OrderQuery): Storefront.OrderQuery {
        return orderQuery
            .orderNumber()
            .totalPrice()
            .email()
            .currencyCode()
            .processedAt()
            .lineItems({ it.first(ITEMS_COUNT) }) { lineItemsQuery ->
                lineItemsQuery.edges { productVariantConnectionQuery ->
                    productVariantConnectionQuery.node { node ->
                        node
                            .title()
                            .quantity()
                            .variant { productVariantQuery ->
                                getDefaultProductVariantQuery(productVariantQuery)
                            }
                    }
                }
            }
    }

    private fun queryProducts(perPage: Int, paginationValue: Any?, searchQuery: String?,
                              reverse: Boolean, sortBy: SortType?,
                              callback: ApiCallback<List<Product>>) {

        val query = Storefront.query { rootQuery ->
            rootQuery.shop { shopQuery ->
                shopQuery
                    .paymentSettings({ it.currencyCode() })
                    .products({ args ->
                        args.first(perPage)
                        if (paginationValue != null) {
                            args.after(paginationValue.toString())
                        }
                        val key = getProductSortKey(sortBy)
                        if (key != null) {
                            args.sortKey(key)
                        }
                        args.reverse(reverse)
                        if (searchQuery != null) {
                            args.query(searchQuery)
                        }
                    }
                    ) { productConnectionQuery ->
                        productConnectionQuery.edges { productEdgeQuery ->
                            productEdgeQuery.cursor().node { productQuery ->
                                getDefaultProductQuery(productQuery)
                                    .images({ it.first(1) }, { imageConnectionQuery ->
                                        imageConnectionQuery.edges({ imageEdgeQuery ->
                                            imageEdgeQuery.node({ imageQuery ->
                                                imageQuery
                                                    .id()
                                                    .src()
                                                    .altText()
                                            })
                                        })
                                    })
                                    .variants({ it.first(ITEMS_COUNT) }) { productVariantConnectionQuery ->
                                        productVariantConnectionQuery.edges { productVariantEdgeQuery ->
                                            productVariantEdgeQuery.node({ it.price() })
                                        }
                                    }
                            }
                        }
                    }
            }
        }

        val call = graphClient.queryGraph(query)
        call.enqueue(object : QueryCallWrapper<List<Product>>(callback) {
            override fun adapt(data: Storefront.QueryRoot): List<Product> =
                ProductListAdapter.adapt(data.shop, data.shop.products)
        })
    }

    private fun getDefaultProductQuery(productQuery: Storefront.ProductQuery): Storefront.ProductQuery {
        return productQuery.title()
            .description()
            .descriptionHtml()
            .vendor()
            .productType()
            .createdAt()
            .updatedAt()
            .tags()
            .options({ args -> args.first(ITEMS_COUNT) }, { optionsQuery ->
                optionsQuery
                    .name()
                    .values()
            })
    }

    private fun getDefaultProductVariantQuery(productVariantQuery: Storefront.ProductVariantQuery): Storefront.ProductVariantQuery {
        return productVariantQuery
            .title()
            .price()
            .weight()
            .weightUnit()
            .availableForSale()
            .selectedOptions({ optionsQuery -> optionsQuery.name().value() })
            .image({ imageQuery ->
                imageQuery
                    .id()
                    .src()
                    .altText()
            })
            .product({ productQuery ->
                productQuery
                    .images({ it.first(1) }, { imageConnectionQuery ->
                        imageConnectionQuery.edges({ imageEdgeQuery ->
                            imageEdgeQuery.node({ imageQuery ->
                                imageQuery
                                    .id()
                                    .src()
                                    .altText()
                            })
                        })
                    })
                    .options({ optionsQuery -> optionsQuery.name().values() })
            })
    }

    private fun getDefaultArticleQuery(articleQuery: Storefront.ArticleQuery): Storefront.ArticleQuery {
        return articleQuery.title()
            .content()
            .contentHtml()
            .tags()
            .publishedAt()
            .url()
            .image({
                it.id()
                    .src()
                    .altText()
            })
            .author {
                it.firstName()
                    .lastName()
                    .name()
                    .email()
                    .bio()
            }
            .blog({ it.title() })
    }

    private fun getDefaultCheckoutQuery(checkoutQuery: Storefront.CheckoutQuery): Storefront.CheckoutQuery {
        return checkoutQuery.webUrl()
            .email()
            .requiresShipping()
            .totalPrice()
            .subtotalPrice()
            .totalTax()
            .currencyCode()
            .shippingAddress { getDefaultAddressQuery(it) }
            .shippingLine { getDefaultShippingRateQuery(it) }
    }

    private fun getDefaultAddressQuery(addressQuery: Storefront.MailingAddressQuery): Storefront.MailingAddressQuery {
        return addressQuery.country()
            .firstName()
            .lastName()
            .address1()
            .address2()
            .city()
            .province()
            .zip()
            .phone()
    }

    private fun getDefaultShippingRateQuery(shippingRateQuery: Storefront.ShippingRateQuery): Storefront.ShippingRateQuery {
        return shippingRateQuery.handle()
            .price()
            .title()
    }

    private fun getDefaultCustomerQuery(customerQuery: Storefront.CustomerQuery): Storefront.CustomerQuery {
        return customerQuery.id()
            .defaultAddress { getDefaultAddressQuery(it) }
            .addresses({ it.first(ITEMS_COUNT) }, {
                it.edges {
                    it.cursor().node {
                        getDefaultAddressQuery(it)
                    }
                }
            })
            .firstName()
            .lastName()
            .email()
            .phone()
            .acceptsMarketing()
    }

    private fun getDefaultUserErrors(userErrorQuery: Storefront.UserErrorQuery): Storefront.UserErrorQuery {
        return userErrorQuery
            .field()
            .message()
    }
}
