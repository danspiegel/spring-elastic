package br.com.elastic.example.elasticapp.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@ConditionalOnExpression("${spring.elasticsearch.enable}")
@EnableElasticsearchRepositories(basePackages = "br.com.elastic.example.elasticapp.repository")
public class ElasticSearchConfig {

    @Value("${spring.elasticsearch.address}")
    private String elasticAddress;

    @Value("${spring.elasticsearch.port}")
    private Integer elasticPort;

    @Value(value = "${spring.elasticsearch.protocol}")
    private String protocol;

    @Value(value = "${spring.elasticsearch.user}")
    private String user;

    @Value(value = "${spring.elasticsearch.password}")
    private String password;

    @Value(value = "${spring.elasticsearch.ignore.certificate}")
    private Boolean ignoreCertificate;

    @Value(value = "${spring.elasticsearch.timeout}")
    private Integer timeout;

    @Bean
    public RestHighLevelClient client() {
        RestClientBuilder restClient = getRestClient();
        return new RestHighLevelClient(restClient);
    }

    private RestClientBuilder getRestClient() {
        RestClientBuilder restClientBuilder =
                RestClient.builder(new HttpHost(elasticAddress, elasticPort, protocol));
        if (ignoreCertificate) {
            restClientBuilder.setHttpClientConfigCallback(
                    httpAsyncClientBuilder ->
                            httpAsyncClientBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier()));
        }

        if (user != null && !user.trim().isEmpty()) {
            CredentialsProvider credentialsProvider = getCredentialsProvider();
            restClientBuilder.setHttpClientConfigCallback(
                    httpClientBuilder ->
                            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }
        restClientBuilder.setRequestConfigCallback(
                requestConfigBuilder ->
                        requestConfigBuilder.setConnectTimeout(timeout).setSocketTimeout(timeout));
        return restClientBuilder;
    }

    private CredentialsProvider getCredentialsProvider() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY, new UsernamePasswordCredentials(user, password));
        return credentialsProvider;
    }

    @Bean(name = {"elasticsearchOperations", "elasticsearchTemplate"})
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }

}
