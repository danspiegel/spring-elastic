package br.com.elastic.example.elasticapp.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "pessoa", createIndex = false)
public class Pessoa {

    @Id
    @Field("id")
    private Long id;

    @Field("nome")
    private String nome;

    @Field("sobrenome")
    private String sobrenome;

    @Field("idade")
    private Integer idade;

}
