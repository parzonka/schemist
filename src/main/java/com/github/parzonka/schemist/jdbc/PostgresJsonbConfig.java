package com.github.parzonka.schemist.jdbc;

import java.util.Arrays;

import org.postgresql.util.PGobject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

/**
 * Necessary converters to read and write JSONB. Autoconfigured.
 */
@Configuration
public class PostgresJsonbConfig extends AbstractJdbcConfiguration {

  final static ObjectMapper objectMapper = new ObjectMapper();

  @Bean
  public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcOperations operations) {
    return new NamedParameterJdbcTemplate(operations);
  }

  @Override
  public JdbcCustomConversions jdbcCustomConversions() {

    return new JdbcCustomConversions(
        Arrays.asList(JsonNodeReadingConverter.INSTANCE, JsonNodeWritingConverter.INSTANCE));

  }

  @ReadingConverter
  enum JsonNodeReadingConverter implements Converter<PGobject, JsonNode> {

    INSTANCE;

    @SneakyThrows
    @Override
    public JsonNode convert(final PGobject source) {
      final String value = source.getValue();
      return objectMapper.readValue(value, JsonNode.class);
    }
  }

  @WritingConverter
  enum JsonNodeWritingConverter implements Converter<JsonNode, PGobject> {

    INSTANCE;

    @SneakyThrows
    @Override
    public PGobject convert(final JsonNode jsonNode) {
      final PGobject target = new PGobject();
      target.setType("JSONB");
      target.setValue(objectMapper.writeValueAsString(jsonNode));
      return target;
    }
  }
}
