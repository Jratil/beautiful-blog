package co.jratil.bloges.wrapper.condition

import co.jratil.blogapi.entity.dataobject.Author
import co.jratil.blogcommon.enums.ResponseEnum
import co.jratil.blogcommon.exception.GlobalException
import co.jratil.blogcommon.util.FieldUtils.method2Filed
import co.jratil.bloges.entity.AuthorEsRequest
import org.elasticsearch.index.query.QueryBuilders
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery

/**
 * @author wenjunjun9
 * @created 2021/12/12 16:48
 */
class AuthorConditionWrapper : ConditionWrapper() {

    override fun <T> all(t: T): NativeSearchQuery {

        if (t !is AuthorEsRequest) {
            throw GlobalException(ResponseEnum.PARAM_ERROR)
        }

        val request = t as AuthorEsRequest
        val boolQueryBuilder = QueryBuilders.boolQuery()
        if (request.authorId != null) {
            boolQueryBuilder.must(
                QueryBuilders.termQuery(
                    method2Filed(Author::getAuthorId.name),
                    request.authorId
                )
            )
        }
        if (request.authorName != null) {
            boolQueryBuilder.must(
                QueryBuilders.matchQuery(
                    method2Filed(Author::getAuthorName.name),
                    request.authorName
                )
            )
        }
        if (request.authorAccount != null) {
            boolQueryBuilder.must(
                QueryBuilders.termQuery(
                    method2Filed(Author::getAuthorAccount.name),
                    request.authorAccount
                )
            )
        }
        return NativeSearchQuery(boolQueryBuilder)
    }
}