package br.com.elastic.example.elasticapp.utils;

import br.com.elastic.example.elasticapp.dto.BucketSelectorDTO;
import br.com.elastic.example.elasticapp.dto.BucketSelectorExpressionDTO;
import br.com.elastic.example.elasticapp.enums.ConditionalBucketSelectorEnum;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.composite.ParsedComposite;
import org.elasticsearch.search.aggregations.pipeline.BucketSelectorPipelineAggregationBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;

import java.util.*;
import java.util.stream.Collectors;

public class ElasticSearchUtil {

    public static final String FORMAT_STRICT_DATE_OPTIONAL_TIME = "strict_date_optional_time";
    public static final String EMPTY = "";
    public static final Integer MAX_LIMIT = 10000;
    public static final String KEYWORD = ".keyword";
    public static final String REGEX_EXTRACT_BIN_CARD_NUMBER = "/(.*?)(?:X.*)/";
    public static final String DEF_MATCHER = "def matcher = ";
    public static final String MATCHER_DOC = ".matcher(doc['";
    public static final String VALUE = "'].value);";
    public static final String RETURN_MATCHER =
            " return matcher.matches() ? %s.contains(matcher.group(1)) : false";
    public static final String PARAMS = "params";
    public static final String DOC_COUNT = "doc_count";
    public static final String COUNT = "_count";
    public static final String BUCKET = "bucket";

    private ElasticSearchUtil() {}

    public static <T> List<T> getContentsSearchHit(SearchHits<T> results) {
        return results.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    public static <T> List<T> getContentsSearchHitsIterator(SearchHitsIterator<T> groupComposite) {
        List<T> contents = new ArrayList<>();
        Iterator<SearchHit<T>> iterator = groupComposite.stream().iterator();
        while (iterator.hasNext()) {
            SearchHit<T> searchHit = iterator.next();
            contents.add(searchHit.getContent());
        }
        return contents;
    }

    public static <T> List<T> getAggregationsComposite(Aggregations aggregations) {
        List buckets = new ArrayList<>();
        Iterator<Aggregation> iterator = aggregations.iterator();
        iterator.forEachRemaining(buckets::add);
        return buckets;
    }

    public static List getBucketsComposite(List<ParsedComposite> aggregations) {
        return aggregations.stream()
                .map(MultiBucketsAggregation::getBuckets)
                .collect(Collectors.toList())
                .stream()
                .reduce(new ArrayList<>(), ElasticSearchUtil::getReduceBuckets);
    }

    private static List getReduceBuckets(
            List<? extends MultiBucketsAggregation.Bucket> list,
            List<? extends MultiBucketsAggregation.Bucket> e) {
        List itens = new ArrayList();
        e.iterator().forEachRemaining(itens::add);
        list.addAll(itens);
        return list;
    }

    public static <T> Optional<T> first(List<T> objects) {
        Iterator<T> iterator = objects.stream().iterator();
        if (iterator.hasNext()) {
            return Optional.ofNullable(iterator.next());
        }
        return Optional.empty();
    }

    public static BucketSelectorPipelineAggregationBuilder minDocCount(Integer limit) {
        HashMap<String, String> map = new HashMap<>();
        map.put(DOC_COUNT, COUNT);
        String conditional = "params.doc_count >= ".concat(limit.toString());
        Script scriptMetric = Script.parse(conditional);
        return PipelineAggregatorBuilders.bucketSelector(BUCKET, map, scriptMetric);
    }

    public static BucketSelectorPipelineAggregationBuilder maxDocCount(Integer limit) {
        HashMap<String, String> map = new HashMap<>();
        map.put(DOC_COUNT, COUNT);
        String conditional = "params.doc_count <= ".concat(limit.toString());
        Script scriptMetric = Script.parse(conditional);
        return PipelineAggregatorBuilders.bucketSelector(BUCKET, map, scriptMetric);
    }

    public static BucketSelectorPipelineAggregationBuilder whereInBucket(
            BucketSelectorDTO bucketSelectorDTO) {
        HashMap<String, String> map = new HashMap<>();
        map.put(bucketSelectorDTO.getAliasFieldInQuery(), bucketSelectorDTO.getFieldInQueryName());
        ConditionalBucketSelectorEnum conditionalBucketSelectorEnum =
                bucketSelectorDTO.getConditionalBucketSelectorEnum();
        Object valueInWhere = bucketSelectorDTO.getValueInWhere();
        String conditional =
                PARAMS
                        .concat(bucketSelectorDTO.getAliasFieldInQuery())
                        .concat(conditionalBucketSelectorEnum.getConditional())
                        .concat(valueInWhere.toString());
        Script scriptMetric = Script.parse(conditional);
        return PipelineAggregatorBuilders.bucketSelector(
                bucketSelectorDTO.getBucketSelectorName(), map, scriptMetric);
    }

    public static BucketSelectorPipelineAggregationBuilder whereInBucket(
            BucketSelectorExpressionDTO bucketSelectorExpressionDTO) {
        HashMap<String, String> map = new HashMap<>();
        map.put(
                bucketSelectorExpressionDTO.getAliasFieldInQuery(),
                bucketSelectorExpressionDTO.getFieldInQueryName());
        Script scriptMetric = Script.parse(bucketSelectorExpressionDTO.getExpression());
        return PipelineAggregatorBuilders.bucketSelector(
                bucketSelectorExpressionDTO.getBucketSelectorName(), map, scriptMetric);
    }

    public static Script regexInAggregation(String regex, String fieldQuery) {
        String clauseWhere =
                "def var ="
                        .concat(regex)
                        .concat(MATCHER_DOC.concat(fieldQuery).concat(VALUE))
                        .concat("return var.matches() ? var.group(1) : 'no match';");
        return Script.parse(clauseWhere);
    }

    public static String fieldWithKeyword(String field) {
        return field.concat(KEYWORD);
    }

    public static BucketSelectorPipelineAggregationBuilder greaterDocCount(Integer limit) {
        HashMap<String, String> map = new HashMap<>();
        map.put(DOC_COUNT, COUNT);
        String conditional = "params.doc_count > ".concat(limit.toString());
        Script scriptMetric = Script.parse(conditional);
        return PipelineAggregatorBuilders.bucketSelector(BUCKET, map, scriptMetric);
    }

}
