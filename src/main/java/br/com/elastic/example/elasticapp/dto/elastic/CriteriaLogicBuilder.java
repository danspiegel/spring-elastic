package br.com.elastic.example.elasticapp.dto.elastic;

import com.google.gson.Gson;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.script.Script;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static br.com.elastic.example.elasticapp.utils.ElasticSearchUtil.*;
import static org.elasticsearch.index.query.QueryBuilders.*;

public class CriteriaLogicBuilder {

    private final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

    public CriteriaLogicBuilder with(String attributeName, Object... value) {
        if (isValid(value)) {
            boolQuery.must(termsQuery(attributeName, value));
        }
        return this;
    }

    public CriteriaLogicBuilder withMatch(String attributeName, Object value) {
        if (!ObjectUtils.isEmpty(value)) {
            boolQuery.must(matchQuery(attributeName, value));
        }
        return this;
    }

    public CriteriaLogicBuilder or(String attributeName, Object... value) {
        if (isValid(value)) {
            boolQuery.should(termQuery(attributeName, value));
        }
        return this;
    }

    public CriteriaLogicBuilder or(SimpleEntry<String, Object>... entries) {
        if (!ObjectUtils.isEmpty(entries)) {
            BoolQueryBuilder shoulds = boolQuery();
            boolean existsCondition = false;
            for (SimpleEntry<String, Object> entry : entries) {
                if (!ObjectUtils.isEmpty(entry.getValue())) {
                    shoulds.should(termQuery(entry.getKey(), entry.getValue()));
                    existsCondition = true;
                }
            }
            if (existsCondition) {
                boolQuery.must(shoulds);
            }
        }
        return this;
    }

    public CriteriaLogicBuilder between(String attributeName, Object valueBegin, Object valueEnd) {
        return between(attributeName, valueBegin, valueEnd, null);
    }

    public CriteriaLogicBuilder between(
            String attributeName, Object valueBegin, Object valueEnd, String format) {
        if (!ObjectUtils.isEmpty(valueBegin) && ObjectUtils.isEmpty(valueEnd)) {
            gte(attributeName, valueBegin, format);
        }
        if (ObjectUtils.isEmpty(valueBegin) && !ObjectUtils.isEmpty(valueEnd)) {
            lte(attributeName, valueEnd, format);
        }
        if (!ObjectUtils.isEmpty(valueBegin) && !ObjectUtils.isEmpty(valueEnd)) {
            RangeQueryBuilder range = rangeQuery(attributeName).from(valueBegin).to(valueEnd);
            if (StringUtils.isNotBlank(format)) {
                range.format(format);
            }
            boolQuery.filter(range);
        }
        return this;
    }

    public CriteriaLogicBuilder gte(String attributeName, Object valueGte) {
        return gte(attributeName, valueGte, null);
    }

    public CriteriaLogicBuilder gte(String attributeName, Object valueGte, String format) {
        if (!ObjectUtils.isEmpty(valueGte)) {
            RangeQueryBuilder gte = rangeQuery(attributeName).gte(valueGte);
            if (StringUtils.isNotBlank(format)) {
                gte.format(format);
            }
            boolQuery.filter(gte);
        }
        return this;
    }

    public CriteriaLogicBuilder lte(String attributeName, Object valueLte) {
        return lte(attributeName, valueLte, null);
    }

    public CriteriaLogicBuilder lte(String attributeName, Object valueLte, String format) {
        if (!ObjectUtils.isEmpty(valueLte)) {
            RangeQueryBuilder lte = rangeQuery(attributeName).lte(valueLte);
            if (StringUtils.isNotBlank(format)) {
                lte.format(format);
            }
            boolQuery.filter(lte);
        }
        return this;
    }

    public CriteriaLogicBuilder withTerms(String attributeName, Collection<?> value) {
        if (!ObjectUtils.isEmpty(value) && isValid(value.toArray())) {
            boolQuery.must(termsQuery(attributeName, value.toArray()));
        }
        return this;
    }

    public CriteriaLogicBuilder withTermsKeyword(String attributeName, Collection<?> value) {
        return withTerms(attributeName.concat(KEYWORD), value);
    }

    public CriteriaLogicBuilder and(BoolQueryBuilder andQuery) {
        if (andQuery != null) {
            boolQuery.must(andQuery);
        }
        return this;
    }

    private boolean isValid(Object... args) {
        return args != null && !Arrays.stream(args).anyMatch(Objects::isNull);
    }

    public BoolQueryBuilder build() {
        return boolQuery;
    }

    public CriteriaLogicBuilder filterWithScript(Script script) {
        boolQuery.filter(QueryBuilders.scriptQuery(script));
        return this;
    }

    public CriteriaLogicBuilder filterWithScriptRegex(
            String regex, String fieldQuery, List<?> objects) {
        Gson gson = new Gson();
        String objectFilter = gson.toJson(objects, List.class);
        String clauseWhere =
                DEF_MATCHER
                        .concat(regex)
                        .concat(MATCHER_DOC.concat(fieldQuery).concat(VALUE).concat(RETURN_MATCHER));
        filterWithScript(Script.parse(String.format(clauseWhere, objectFilter)));
        return this;
    }

    public CriteriaLogicBuilder withKeyword(String field, Object... value) {
        return with(field.concat(KEYWORD), value);
    }

}
