package br.com.elastic.example.elasticapp.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BucketSelectorExpressionDTO {

    private String bucketSelectorName;
    private String fieldInQueryName;
    private String aliasFieldInQuery;
    private String expression;

}
