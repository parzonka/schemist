package com.github.parzonka.schemist.aggregate;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;

public class ValidationUtilTest {

  @Test
  public void shouldValidate() throws Exception {
    final JsonSchema personSchema = ValidationUtil.getYamlSchemaFromClasspath("schema/person.schema.yaml");
    final JsonNode person = ValidationUtil.readJsonFromClasspath("instance/person.json");
    final Set<ValidationMessage> errors = personSchema.validate(person);
    Assert.assertEquals(0, errors.size());
  }

  @Test
  public void shouldInvalidate() throws Exception {
    final JsonSchema personSchema = ValidationUtil.getYamlSchemaFromClasspath("schema/person.schema.yaml");
    final JsonNode person = ValidationUtil.readJsonFromClasspath("instance/person-invalid.json");
    final Set<ValidationMessage> errors = personSchema.validate(person);
    Assert.assertEquals(1, errors.size());
    Assert.assertEquals("[$.age: must have a minimum value of 0]", errors.toString());

  }

}
