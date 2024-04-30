package br.com.elastic.example.elasticapp.repository;

import br.com.elastic.example.elasticapp.config.EnvironmentElasticSearchConfig;
import br.com.elastic.example.elasticapp.domain.Pessoa;
import br.com.elastic.example.elasticapp.dto.elastic.CriteriaLogicBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

import static br.com.elastic.example.elasticapp.utils.ElasticSearchUtil.fieldWithKeyword;
import static br.com.elastic.example.elasticapp.utils.ElasticSearchUtil.getContentsSearchHit;

@Repository
public class PessoaRepository {

    @Autowired
    private EnvironmentElasticSearchConfig environmentElasticSearchComponent;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public List<Pessoa> findPessoa(String name) {
        BoolQueryBuilder searchQuery =
                new CriteriaLogicBuilder()
                        .withTerms(fieldWithKeyword("nome"), Arrays.asList(name))
                        .build();
        NativeSearchQuery nativeSearchQuery =
                new NativeSearchQueryBuilder().withQuery(searchQuery).build();
        return getContentsSearchHit(
                elasticsearchOperations.search(nativeSearchQuery, Pessoa.class));
    }

    public void savePessoa(Pessoa pessoa) {
        elasticsearchOperations.save(pessoa);
    }

}
