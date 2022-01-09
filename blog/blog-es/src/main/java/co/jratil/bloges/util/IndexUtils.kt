package co.jratil.bloges.util

import co.jratil.bloges.enums.IndexEnum
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates

/**
 * @author jun
 * @created 2021/12/26 18:20
 * @version 1.0
 */
object IndexUtils {

    @JvmStatic
    fun indexName(enum: IndexEnum): IndexCoordinates {
        return IndexCoordinates.of(enum.index)
    }
}