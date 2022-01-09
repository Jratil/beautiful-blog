package co.jratil.bloges.entity

/**
 * @author jun
 * @created 2021/12/12 16:46
 */
data class AuthorEsRequest(
    var authorId: Int? = null,
    var authorName: String? = null,
    var authorAccount: String? = null,
    var pageIndex: Int = 0,
    var pageSize: Int = 10
)