package com.shopify.api

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.apicore.ApiCallback
import com.domain.entity.*
import com.domain.network.Api
import com.shopify.api.adapter.*
import com.shopify.api.call.MutationCallWrapper
import com.shopify.api.call.QuaryCallWrapper
import com.shopify.api.entity.AccessData
import com.shopify.buy3.GraphClient
import com.shopify.buy3.Storefront
import com.shopify.graphql.support.ID
import net.danlew.android.joda.JodaTimeAndroid


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
                                    .images(ITEMS_COUNT, { imageConnectionQuery ->
                                        imageConnectionQuery.edges({ imageEdgeQuery ->
                                            imageEdgeQuery.node({ imageQuery ->
                                                imageQuery
                                                        .id()
                                                        .src()
                                                        .altText()
                                            })
                                        })
                                    })
                                    .variants(ITEMS_COUNT) { productVariantConnectionQuery ->
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
                shopQuery.collections(perPage, { args ->
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
                                            .products(ITEMS_COUNT, { args ->
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
                                                                        .images(1, { imageConnectionQuery ->
                                                                            imageConnectionQuery.edges({ imageEdgeQuery ->
                                                                                imageEdgeQuery.node({ imageQuery ->
                                                                                    imageQuery
                                                                                            .id()
                                                                                            .src()
                                                                                            .altText()
                                                                                })
                                                                            })
                                                                        })
                                                                        .variants(1) { productVariantConnectionQuery ->
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
                shopQuery.articles(perPage, { args ->
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
                    if (customerCreate.userErrors.isNotEmpty()) {
                        callback.onFailure(Error.NonCritical(data.customerCreate
                                .userErrors
                                .first()
                                .message))
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

    private fun requestToken(email: String, password: String): Pair<AccessData?, Error.NonCritical?>? {

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
            val error = it.userErrors?.let { it.firstOrNull()?.let { Error.NonCritical(it.message) } }
            Pair(accessData, error)
        }
    }

    override fun createCheckout(cartProductList: List<CartProduct>, callback: ApiCallback<Pair<String, String>>) {

        val input = Storefront.CheckoutCreateInput().setLineItems(
                cartProductList.map {
                    Storefront.CheckoutLineItemInput(it.quantity, ID(it.productVariant.id))
                }
        )

        val query = Storefront.mutation { mutationQuery ->
            mutationQuery
                    .checkoutCreate(input
                    ) { createPayloadQuery ->
                        createPayloadQuery
                                .checkout { checkoutQuery ->
                                    checkoutQuery
                                            .webUrl()
                                }
                                .userErrors { userErrorQuery ->
                                    userErrorQuery
                                            .field()
                                            .message()
                                }
                    }
        }

        graphClient.mutateGraph(query).enqueue(object : MutationCallWrapper<Pair<String, String>>(callback) {
            override fun adapt(data: Storefront.Mutation?): Pair<String, String>? {
                return data?.let {
                    if (!it.checkoutCreate.userErrors.isEmpty()) {
                        callback.onFailure(Error.NonCritical(data.customerCreate
                                .userErrors
                                .first()
                                .message))
                        null
                    } else {
                        val checkoutId = it.checkoutCreate.checkout.id.toString()
                        val checkoutWebUrl = it.checkoutCreate.checkout.webUrl
                        return Pair(checkoutId, checkoutWebUrl)
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
                        .products(perPage, { args ->
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
                                            .images(1, { imageConnectionQuery ->
                                                imageConnectionQuery.edges({ imageEdgeQuery ->
                                                    imageEdgeQuery.node({ imageQuery ->
                                                        imageQuery
                                                                .id()
                                                                .src()
                                                                .altText()
                                                    })
                                                })
                                            })
                                            .variants(1) { productVariantConnectionQuery ->
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
            override fun adapt(data: Storefront.QueryRoot): List<Product> {
                return ProductListAdapter.adapt(data.shop, data.shop.products)
            }
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
