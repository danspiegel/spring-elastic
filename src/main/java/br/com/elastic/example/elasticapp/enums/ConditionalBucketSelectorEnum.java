package br.com.elastic.example.elasticapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConditionalBucketSelectorEnum {

    GTE(" >= "),
    LTE(" <= "),
    EQ(" = "),
    NEQ(" <> "),
    GT(" > "),
    LT(" < ");

    private String conditional;

}
