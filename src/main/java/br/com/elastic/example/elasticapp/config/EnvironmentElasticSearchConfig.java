package br.com.elastic.example.elasticapp.config;

import br.com.elastic.example.elasticapp.enums.EnvironmentElasticSearchEnum;
import br.com.elastic.example.elasticapp.exception.InvalidEnvironmentException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EnvironmentElasticSearchConfig {

    @Autowired
    private Environment environment;

    @Value("${spring.elastic.search.simulator.profile}")
    private String profileSimulatorElasticSearch;

    public EnvironmentElasticSearchEnum getEnvironment() {
        if (ObjectUtils.isNotEmpty(profileSimulatorElasticSearch)) {
            return getEnvironment(profileSimulatorElasticSearch);
        }
        return getEnvironment(this.environment.getActiveProfiles());
    }

    public EnvironmentElasticSearchEnum getEnvironment(String... profiles) {
        EnvironmentElasticSearchEnum env =
                Arrays.stream(profiles)
                        .map(EnvironmentElasticSearchEnum::fromSpringProfile)
                        .reduce(null, (e1, e2) -> (e2 != null ? e2 : e1));

        if (env == null) {
            throw new InvalidEnvironmentException(InvalidEnvironmentException.INVALID_ENVIROMENT_MESSAGE);
        }

        return env;
    }

}
