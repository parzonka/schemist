package com.github.parzonka.schemist.schema;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.parzonka.schemist.web.WebContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SchemaController {

  private final WebContext webContext;

  @GetMapping("schema/**")
  public JsonNode allDirectories(HttpServletRequest request) {
    final String path = request.getRequestURI()
        .split(request.getContextPath() + "/schema/")[1];
    log.debug("Requesting schema at path: {}", path);
    final JsonNode jsonNode = SchemaUtil.readYamlFromClasspath("schema/" + path);
    final ObjectNode objectNode = (ObjectNode) jsonNode;
    objectNode.put("$id", webContext.getHttp() + "/schema/" + path);
    objectNode.put("$schema", "http://json-schema.org/draft-07/schema#");
    return jsonNode;
  }

}
