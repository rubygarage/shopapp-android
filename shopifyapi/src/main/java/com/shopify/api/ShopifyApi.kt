package com.shopify.api

import android.content.Context
import com.shopapicore.ApiCallback
import com.shopapicore.ShopApi
import com.shopapicore.entity.*
import com.shopify.api.adapter.*
import com.shopify.buy3.GraphClient
import com.shopify.buy3.Storefront
import com.shopify.graphql.support.ID

class ShopifyApi(context: Context, baseUrl: String, accessToken: String) : ShopApi {

    private val graphClient: GraphClient = GraphClient.builder(context)
            .shopDomain(baseUrl)
            .accessToken(accessToken)
            .build()

    companion object {
        private val ITEMS_COUNT = 250
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
                                    .variants(ITEMS_COUNT
                                    ) { productVariantConnectionQuery ->
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
        }

        val call = graphClient.queryGraph(query)
        call.enqueue(object : CallWrapper<Product>(callback) {
            override fun adapt(data: Storefront.QueryRoot): Product {
                return ProductAdapter(data.shop, data.node as Storefront.Product)
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
        call.enqueue(object : CallWrapper<List<Category>>(callback) {
            override fun adapt(data: Storefront.QueryRoot): List<Category> {
                return CategoryListAdapter(data.shop)
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
        call.enqueue(object : CallWrapper<Category>(callback) {
            override fun adapt(data: Storefront.QueryRoot): Category {
                return CategoryAdapter(data.shop, data.node as Storefront.Collection)
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
        call.enqueue(object : CallWrapper<Shop>(callback) {
            override fun adapt(data: Storefront.QueryRoot): Shop {
                return ShopAdapter(data)
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
        call.enqueue(object : CallWrapper<List<Article>>(callback) {
            override fun adapt(data: Storefront.QueryRoot): List<Article> {
                return ArticleListAdapter(data.shop.articles.edges)
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
        call.enqueue(object : CallWrapper<List<Product>>(callback) {
            override fun adapt(data: Storefront.QueryRoot): List<Product> {
                return ProductListAdapter(data.shop, data.shop.products.edges)
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
