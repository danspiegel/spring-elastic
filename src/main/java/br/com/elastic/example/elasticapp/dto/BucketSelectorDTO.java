package br.com.elastic.example.elasticapp.dto;

import br.com.elastic.example.elasticapp.enums.ConditionalBucketSelectorEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BucketSelectorDTO {

    private String bucketSelectorName;
    private String fieldInQueryName;
    private String aliasFieldInQuery;
    private ConditionalBucketSelectorEnum conditionalBucketSelectorEnum;
    private Object valueInWhere;

}
