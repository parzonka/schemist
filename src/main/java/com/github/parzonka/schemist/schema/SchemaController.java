package com.github.parzonka.schemist.schema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.parzonka.schemist.aggregate.ValidationUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schema")
@RequiredArgsConstructor
public class SchemaController {

  @GetMapping("/{path}")
  public JsonNode getAllByCollectionId(@PathVariable String path) {
    return ValidationUtil.readYamlFromClasspath("schema/" + path);
  }

}
