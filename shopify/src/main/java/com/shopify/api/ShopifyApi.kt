package com.shopify.api

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.apicore.ApiCallback
import com.domain.entity.*
import com.domain.network.Api
import com.google.android.gms.wallet.FullWallet
import com.shopify.ShopifyWrapper
import com.shopify.api.adapter.*
import com.shopify.api.call.MutationCallWrapper
import com.shopify.api.call.QuaryCallWrapper
import com.shopify.api.entity.AccessData
import com.shopify.buy3.*
import com.shopify.buy3.pay.PayAddress
import com.shopify.buy3.pay.PayCart
import com.shopify.buy3.pay.PayHelper
import com.shopify.buy3.pay.PaymentToken
import com.shopify.entity.Checkout
import com.shopify.entity.ShippingRate
import com.shopify.graphql.support.ID
import net.danlew.android.joda.JodaTimeAndroid
import java.io.IOException
import java.math.BigDecimal
import java.util.*


class ShopifyApi(context: Context, baseUrl: String, accessToken: String) : Api {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val graphClient: GraphClient = GraphClient.builder(context)
            .shopDomain(baseUrl)
            .accessToken(accessToken)
            .build()

    init {
        JodaTimeAndroid.init(context)
    }

    companion object {
        private val ITEMS_COUNT = 250
        private const val EMAIL = "email"
        private const val ACCESS_TOKEN = "access_token"
        private const val EXPIRES_DATE = "expires_date"
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
        return if (email != null && accessToken != null) {
            AccessData(
                    email,
                    accessToken,
                    expiresDate
            )
        } else {
            null
        }
    }

    override fun getProductList(perPage: Int, paginationValue: Any?, sortBy: SortType?, reverse: Boolean,
                                callback: ApiCallback<List<Product>>) {
        queryProducts(perPage, paginationValue, null, reverse, sortBy, callback)
    }

    override fun searchProductList(perPage: Int, paginationValue: Any?,
                                   searchQuery: String, callback: ApiCallback<List<Product>>) {
        queryProducts(perPage, paginationValue, searchQuery, false, SortType.RELEVANT, callback)
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
                                                                productVariantQuery
                                                                        .title()
                                                                        .availableForSale()
                                                                        .price()
                                                                        .weight()
                                                                        .weightUnit()
                                                                        .selectedOptions({ optionsQuery -> optionsQuery.name().value() })
                                                                        .image({ imageQuery ->
                                                                            imageQuery
                                                                                    .id()
                                                                                    .src()
                                                                                    .altText()
                                                                        })
                                                            }
                                                }
                                    }
                                    .options({ args -> args.first(ITEMS_COUNT) }, { optionsQuery ->
                                        optionsQuery
                                                .name()
                                                .values()
                                    })
                        }
                    }
        }

        val call = graphClient.queryGraph(query)
        call.enqueue(object : QuaryCallWrapper<Product>(callback) {
            override fun adapt(data: Storefront.QueryRoot): Product {
                return ProductAdapter.adapt(data.shop, data.node as Storefront.Product)
            }
        })
    }

    override fun getCategoryList(perPage: Int, paginationValue: Any?, sortBy: SortType?,
                                 reverse: Boolean, callback: ApiCallback<List<Category>>) {

        val query = Storefront.query { rootQuery ->
            rootQuery.shop { shopQuery ->
                shopQuery.collections({ args ->
                    args.first(ITEMS_COUNT)
                    if (paginationValue != null) {
                        args.after(paginationValue.toString())
                    }
                    val key = getCollectionSortKey(sortBy)
                    if (key != null) {
                        args.sortKey(key)
                    }
                    args.reverse(reverse)
                }) { collectionConnectionQuery ->
                    collectionConnectionQuery.edges { collectionEdgeQuery ->
                        collectionEdgeQuery
                                .cursor()
                                .node { collectionQuery ->
                                    collectionQuery
                                            .title()
                                            .description()
                                            .updatedAt()
                                            .image({ imageQuery ->
                                                imageQuery
                                                        .id()
                                                        .src()
                                                        .altText()
                                            })
                                }
                    }
                }
            }
        }

        val call = graphClient.queryGraph(query)
        call.enqueue(object : QuaryCallWrapper<List<Category>>(callback) {
            override fun adapt(data: Storefront.QueryRoot): List<Category> {
                return CategoryListAdapter.adapt(data.shop)
            }
        })
    }

    override fun getCategoryDetails(id: String, perPage: Int, paginationValue: Any?, sortBy: SortType?,
                                    reverse: Boolean, callback: ApiCallback<Category>) {

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
                                                                        .variants({ it.first(1) }) { productVariantConnectionQuery ->
                                                                            productVariantConnectionQuery.edges { productVariantEdgeQuery ->
                                                                                productVariantEdgeQuery.node({
                                                                                    it.price()
                                                                                    it.title()
                                                                                })
                                                                            }
                                                                        }
                                                            })
                                                })
                                            })
                                }
                    }
        }

        val call = graphClient.queryGraph(query)
        call.enqueue(object : QuaryCallWrapper<Category>(callback) {
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
        call.enqueue(object : QuaryCallWrapper<Shop>(callback) {
            override fun adapt(data: Storefront.QueryRoot): Shop {
                return ShopAdapter.adapt(data)
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
                        articleEdgeQuery.cursor().node { articleQuery ->
                            articleQuery
                                    .title()
                                    .content()
                                    .tags()
                                    .publishedAt()
                                    .url()
                                    .image({ imageQuery ->
                                        imageQuery
                                                .id()
                                                .src()
                                                .altText()
                                    })
                                    .author { articleAuthorQuery ->
                                        articleAuthorQuery
                                                .firstName()
                                                .lastName()
                                                .name()
                                                .email()
                                                .bio()
                                    }
                                    .blog({ it.title() })
                        }
                    }
                }
            }
        }

        val call = graphClient.queryGraph(query)
        call.enqueue(object : QuaryCallWrapper<List<Article>>(callback) {
            override fun adapt(data: Storefront.QueryRoot): List<Article> {
                return ArticleListAdapter.adapt(data.shop.articles.edges)
            }
        })
    }

    override fun signUp(firstName: String, lastName: String, email: String, password: String,
                        callback: ApiCallback<Customer>) {

        val customerCreateInput = Storefront.CustomerCreateInput(email, password)
                .setFirstName(firstName)
                .setLastName(lastName)

        val customerQuery = Storefront.CustomerCreatePayloadQueryDefinition { queryDefinition ->
            queryDefinition.customer {
                it
                        .id()
                        .firstName()
                        .lastName()
                        .email()
            }.userErrors {
                it
                        .field()
                        .message()
            }
        }

        val mutationQuery = Storefront.mutation { query -> query.customerCreate(customerCreateInput, customerQuery) }
        val call = graphClient.mutateGraph(mutationQuery)
        call.enqueue(object : MutationCallWrapper<Customer>(callback) {
            override fun adapt(data: Storefront.Mutation?): Customer? {
                return data?.customerCreate?.let { customerCreate ->
                    val userError = ErrorAdapter.adaptUserError(customerCreate.userErrors)
                    if (userError != null) {
                        callback.onFailure(userError)
                    } else if (customerCreate.customer != null) {
                        val tokenResponse = requestToken(email, password)
                        if (tokenResponse != null) {
                            tokenResponse.first?.let {
                                return CustomerAdapter.adapt(customerCreate.customer)
                            }
                            tokenResponse.second?.let {
                                callback.onFailure(it)
                            }
                        }
                    }
                    return null
                }
            }
        })
    }

    override fun signIn(email: String, password: String, callback: ApiCallback<Customer>) {

        val tokenResponse = requestToken(email, password)

        if (tokenResponse != null) {
            tokenResponse.first?.let {
                val query = Storefront.query { rootQuery ->
                    rootQuery.customer(it.accessToken, { customer ->
                        customer
                                .id()
                                .firstName()
                                .lastName()
                                .email()
                    })
                }

                val call = graphClient.queryGraph(query)
                call.enqueue(object : QuaryCallWrapper<Customer>(callback) {
                    override fun adapt(data: Storefront.QueryRoot): Customer {
                        return CustomerAdapter.adapt(data.customer)
                    }
                })
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

    override fun getCustomer(callback: ApiCallback<Customer>) {

    }

    private fun requestToken(email: String, password: String): Pair<AccessData?, Error?>? {

        val accessTokenInput = Storefront.CustomerAccessTokenCreateInput(email, password)
        val accessTokenQuery = Storefront.CustomerAccessTokenCreatePayloadQueryDefinition { queryDefinition ->
            queryDefinition.customerAccessToken { accessTokenQuery -> accessTokenQuery.accessToken().expiresAt() }
                    .userErrors { errorsQuery ->
                        errorsQuery
                                .field()
                                .message()
                    }
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

    fun createCheckout(cartProductList: List<CartProduct>, callback: ApiCallback<Checkout>) {

        val input = Storefront.CheckoutCreateInput().setLineItems(
                cartProductList.map {
                    Storefront.CheckoutLineItemInput(it.quantity, ID(it.productVariant.id))
                }
        )

        val query = Storefront.mutation { mutationQuery ->
            mutationQuery
                    .checkoutCreate(input) { createPayloadQuery ->
                        createPayloadQuery
                                .checkout { checkoutQuery ->
                                    checkoutQuery
                                            .webUrl()
                                            .email()
                                            .requiresShipping()
                                            .totalPrice()
                                            .subtotalPrice()
                                            .totalTax()
                                }
                                .userErrors { userErrorQuery ->
                                    userErrorQuery
                                            .field()
                                            .message()
                                }
                    }
        }

        graphClient.mutateGraph(query).enqueue(object : MutationCallWrapper<Checkout>(callback) {
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

    fun getShippingRates(checkoutId: String, email: String, address: Address, callback: ApiCallback<List<ShippingRate>>) {

        val mailingAddressInput = Storefront.MailingAddressInput()
                .setAddress1(address.address)
                .setCity(address.city)
                .setCountry(address.country)
                .setFirstName(address.firstName)
                .setLastName(address.lastName)
                .setPhone(address.phone)
                .setZip(address.zip)

        val checkoutQuery = Storefront.mutation {
            it.checkoutEmailUpdate(ID(checkoutId), email, { it.checkout { } })
                    .checkoutShippingAddressUpdate(mailingAddressInput, ID(checkoutId), { it.checkout { } })
        }

        val checkoutCallback = object : GraphCall.Callback<Storefront.Mutation> {
            override fun onFailure(error: GraphError) {
                callback.onFailure(ErrorAdapter.adapt(error))
            }

            override fun onResponse(response: GraphResponse<Storefront.Mutation>) {
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
                        checkout?.availableShippingRates?.shippingRates?.let {
                            callback.onResult(it.map { ShippingRateAdapter.adapt(it) })
                        }
                    }

                    override fun onFailure(error: GraphError) {
                        callback.onFailure(ErrorAdapter.adapt(error))
                    }
                })
            }
        }
        graphClient.mutateGraph(checkoutQuery).enqueue(checkoutCallback)
    }

    fun selectShippingRate(checkoutId: String, shippingRate: ShippingRate, callback: ApiCallback<Checkout>) {

        val checkoutQuery = Storefront.mutation {
            it.checkoutShippingLineUpdate(ID(checkoutId), shippingRate.handle, {
                it.userErrors { it.field().message() }
                        .checkout {
                            it
                                    .webUrl()
                                    .requiresShipping()
                                    .subtotalPrice()
                                    .totalPrice()
                                    .totalTax()
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

    fun payByCard(card: Card, callback: ApiCallback<String>) {

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
        call.enqueue(object : QuaryCallWrapper<String>(vaultCallback) {
            override fun adapt(data: Storefront.QueryRoot): String =
                    data.shop?.paymentSettings?.cardVaultUrl ?: ""
        })
    }

    fun completeCheckoutByCard(checkout: Checkout, address: Address, creditCardVaultToken: String,
                               callback: ApiCallback<Boolean>) {

        val amount = checkout.totalPrice
        val idempotencyKey = UUID.randomUUID().toString()
        val billingAddress = Storefront.MailingAddressInput()

        billingAddress.setAddress1(address.address)
                .setCity(address.city)
                .setCountry(address.country)
                .setFirstName(address.firstName)
                .setLastName(address.lastName)
                .setPhone(address.phone).zip = address.zip

        val creditCardPaymentInput = Storefront.CreditCardPaymentInput(amount, idempotencyKey,
                billingAddress, creditCardVaultToken)

        val mutationQuery = Storefront.mutation {
            it.checkoutCompleteWithCreditCard(ID(checkout.checkoutId), creditCardPaymentInput) {
                it.payment {
                    it.ready().errorMessage()
                }.checkout {
                    it.ready()
                }.userErrors {
                    it.message().field()
                }
            }
        }

        graphClient.mutateGraph(mutationQuery).enqueue(object : MutationCallWrapper<Boolean>(callback) {
            override fun adapt(data: Storefront.Mutation?): Boolean? {
                return data?.checkoutCompleteWithCreditCard?.let {
                    val userError = ErrorAdapter.adaptUserError(it.userErrors)
                    if (userError != null) {
                        callback.onFailure(userError)
                        null
                    } else {
                        val checkoutReady = it.checkout?.ready ?: false
                        val paymentReady = it.payment?.ready ?: false
                        checkoutReady && paymentReady
                    }
                }
            }
        })
    }

    fun createAndroidPayCart(checkout: Checkout, cartProductList: List<CartProduct>, callback: ApiCallback<PayCart>) {

        val shopCallback = object : GraphCall.Callback<Storefront.QueryRoot> {
            override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
                val currency = response.data()?.shop?.paymentSettings?.currencyCode?.name ?: ""
                val countryCode = response.data()?.shop?.paymentSettings?.countryCode?.name ?: ""

                val payCartBuilder = PayCart.builder()
                        .merchantName(ShopifyWrapper.BASE_URL)
                        .currencyCode(currency)
                        .shippingAddressRequired(checkout.requiresShipping)
                        .countryCode(countryCode)
                        .subtotal(checkout.subtotalPrice)
                        .totalPrice(checkout.totalPrice)
                        .taxPrice(checkout.taxPrice)

                for (cartProduct in cartProductList) {
                    val productVariant = cartProduct.productVariant
                    val price = productVariant.price.toDoubleOrNull() ?: 0.0
                    payCartBuilder.addLineItem(productVariant.title, cartProduct.quantity, BigDecimal.valueOf(price))
                }

                callback.onResult(payCartBuilder.build())
            }

            override fun onFailure(error: GraphError) {
                callback.onFailure(ErrorAdapter.adapt(error))
            }
        }

        val query = Storefront.query {
            it.shop {
                it.paymentSettings {
                    it.currencyCode()
                            .countryCode()
                }
            }
        }
        val call = graphClient.queryGraph(query)
        call.enqueue(shopCallback)
    }

    fun completeCheckoutByAndroid(checkout: Checkout, payCart: PayCart, fullWallet: FullWallet, callback: ApiCallback<Boolean>) {

        val paymentToken: PaymentToken = PayHelper.extractPaymentToken(fullWallet, ShopifyWrapper.ANDROID_PAY_PUBLIC_KEY)
        val billingAddress = PayAddress.fromUserAddress(fullWallet.buyerBillingAddress)
        val idempotencyKey: String = UUID.randomUUID().toString()

        val mailingAddressInput = Storefront.MailingAddressInput()
                .setAddress1(billingAddress.address1)
                .setAddress2(billingAddress.address2)
                .setCity(billingAddress.city)
                .setCountry(billingAddress.country)
                .setFirstName(billingAddress.firstName)
                .setLastName(billingAddress.lastName)
                .setPhone(billingAddress.phone)
                .setProvince(billingAddress.province)
                .setZip(billingAddress.zip)

        val input = Storefront.TokenizedPaymentInput(payCart.totalPrice, idempotencyKey,
                mailingAddressInput, paymentToken.token, "android_pay")
                .setIdentifier(paymentToken.publicKeyHash).setAmount(checkout.totalPrice)

        val query = Storefront.mutation {
            it.checkoutCompleteWithTokenizedPayment(ID(checkout.checkoutId), input, {
                it.payment({ it.ready().errorMessage() })
                        .checkout({ it.ready() })
                        .userErrors({ it.field().message() })
            })
        }

        graphClient.mutateGraph(query).enqueue(object : MutationCallWrapper<Boolean>(callback) {
            override fun adapt(data: Storefront.Mutation?): Boolean? {
                return data?.checkoutCompleteWithTokenizedPayment?.let {
                    val userError = ErrorAdapter.adaptUserError(it.userErrors)
                    if (userError != null) {
                        callback.onFailure(userError)
                        null
                    } else {
                        val checkoutReady = it.checkout?.ready ?: false
                        val paymentReady = it.payment?.ready ?: false
                        checkoutReady && paymentReady
                    }
                }
            }
        })
    }

    private fun getProductSortKey(sortType: SortType?): Storefront.ProductSortKeys? {
        if (sortType != null) {
            return when (sortType) {
                SortType.NAME -> Storefront.ProductSortKeys.TITLE
                SortType.RECENT -> Storefront.ProductSortKeys.CREATED_AT
                SortType.RELEVANT -> Storefront.ProductSortKeys.RELEVANCE
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
            }
        }
        return null
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
                                            .variants({ it.first(1) }) { productVariantConnectionQuery ->
                                                productVariantConnectionQuery.edges { productVariantEdgeQuery ->
                                                    productVariantEdgeQuery.node({
                                                        it.price()
                                                        it.title()
                                                    })
                                                }
                                            }
                                }
                            }
                        }
            }
        }

        val call = graphClient.queryGraph(query)
        call.enqueue(object : QuaryCallWrapper<List<Product>>(callback) {
            override fun adapt(data: Storefront.QueryRoot): List<Product> =
                    ProductListAdapter.adapt(data.shop, data.shop.products)
        })
    }

    private fun getDefaultProductQuery(productQuery: Storefront.ProductQuery): Storefront.ProductQuery {
        return productQuery
                .title()
                .description()
                .descriptionHtml()
                .vendor()
                .productType()
                .createdAt()
                .updatedAt()
                .tags()
    }
}
