package br.com.elastic.example.elasticapp.exception;

import br.com.elastic.example.elasticapp.dto.LogMessageDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ElasticSearchHandlerException extends RuntimeException {

  public static final String SORT_REQUIRED =
      "Atributos de ordenacao eh obrigatorio para consulta paginada.";

  public ElasticSearchHandlerException(String message) {
    super(message);
    log.debug(
        LogMessageDTO.builder()
            .withStep("elastic_search_handler")
            .withMsg(getMessage())
            .build()
            .toString());
  }
}
