package br.com.elastic.example.elasticapp.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EnvironmentElasticSearchEnum {

    LOCAL("local");

    private final String[] springProfiles;

    EnvironmentElasticSearchEnum(String... springProfiles) {
        this.springProfiles = springProfiles;
    }

    public static EnvironmentElasticSearchEnum fromSpringProfile(String springProfile) {
        return Arrays.stream(EnvironmentElasticSearchEnum.values())
                .filter(env -> Arrays.asList(env.getSpringProfiles()).contains(springProfile))
                .findFirst()
                .orElse(null);
    }

}
