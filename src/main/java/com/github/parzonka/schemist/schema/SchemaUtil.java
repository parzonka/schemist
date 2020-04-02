package com.github.parzonka.schemist.schema;

import java.io.InputStream;
import java.net.URI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SchemaUtil {

  private static ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
  private static ObjectMapper jsonMapper = new ObjectMapper();

  public static JsonSchema getJsonSchemaFromClasspath(String name) throws Exception {
    final JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    final InputStream is = getInputStreamFromClasspath(name);
    final JsonSchema schema = factory.getSchema(is);
    return schema;
  }

  public static JsonSchema getYamlSchemaFromClasspath(String path) throws Exception {
    final JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    final JsonNode jsonNode = readYamlFromClasspath(path);
    final JsonSchema schema = factory.getSchema(jsonNode);
    return schema;
  }

  @SneakyThrows
  public static JsonSchema getJsonSchemaFromUrl(String url) {
    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    final URI uri = new URI(url);
    JsonSchema schema = factory.getSchema(uri);
    return schema;
  }

  @SneakyThrows
  public static JsonNode readYamlFromClasspath(String path) {
    final InputStream is = getInputStreamFromClasspath(path);
    log.info("Looking for YAML at classpath: " + path);
    final JsonNode jsonNode = yamlMapper.readTree(is);
    return jsonNode;
  }

  @SneakyThrows
  public static JsonNode readJsonFromClasspath(String path) {
    final InputStream is = getInputStreamFromClasspath(path);
    final JsonNode jsonNode = jsonMapper.readTree(is);
    return jsonNode;
  }

  private static InputStream getInputStreamFromClasspath(final String name) {
    return Thread.currentThread()
        .getContextClassLoader()
        .getResourceAsStream(name);
  }

}
