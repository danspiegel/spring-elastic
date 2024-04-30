package br.com.elastic.example.elasticapp.config;

import br.com.elastic.example.elasticapp.exception.ElasticSearchHandlerException;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.Map;

public abstract class ElasticSearchHandler {

    private ElasticSearchHandler() {}

    public static SearchSourceBuilder searchAfter(
            Map<String, SortOrder> sortFields, QueryBuilder query, Object[] searchAfter, int size) {
        return searchAfterBuilder(sortFields, query, searchAfter, size);
    }

    private static SearchSourceBuilder searchAfterBuilder(
            Map<String, SortOrder> sortFields, QueryBuilder query, Object[] searchAfter, int size) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        if (sortFields.isEmpty()) {
            throw new ElasticSearchHandlerException(ElasticSearchHandlerException.SORT_REQUIRED);
        }
        sortFields.forEach(builder::sort);
        builder.size(size);
        builder.query(query);
        if (ArrayUtils.isNotEmpty(searchAfter)) {
            builder.searchAfter(searchAfter);
        }
        return builder;
    }

}
