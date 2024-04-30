package br.com.elastic.example.elasticapp.dto;

import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.StringJoiner;

@Slf4j
@SuperBuilder(setterPrefix = "with")
@SuppressWarnings("java:S3740")
public class LogMessageDTO {

  private String msg;
  private String identifier;
  private String step;
  private Object customRecord;
  private String exception;

  @Override
  public String toString() {
    StringJoiner message = new StringJoiner(",");
    if (StringUtils.isNotEmpty(this.msg)) {
      message.add(String.format("msg=%s", this.msg));
    }
    if (StringUtils.isNotEmpty(this.identifier)) {
      message.add(String.format("identifier=%s", this.identifier));
    }
    if (StringUtils.isNotEmpty(this.step)) {
      message.add(String.format("step=%s", this.step));
    }

    if (StringUtils.isNotEmpty(String.valueOf(this.customRecord))) {
      message.add(String.format("custom record=%s", this.customRecord));
    }

    if (StringUtils.isNotEmpty(this.exception)) {
      message.add(String.format("exception=%s", this.exception));
    }
    return message.toString();
  }
}
