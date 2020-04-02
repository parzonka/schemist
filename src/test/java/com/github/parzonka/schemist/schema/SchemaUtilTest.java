package com.github.parzonka.schemist.schema;

import java.util.Set;

import com.github.parzonka.schemist.schema.SchemaUtil;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;

public class SchemaUtilTest {

  @Test
  public void shouldValidate() throws Exception {
    final JsonSchema personSchema = SchemaUtil.getYamlSchemaFromClasspath("schema/person.schema.yaml");
    final JsonNode person = SchemaUtil.readJsonFromClasspath("instance/person.json");
    final Set<ValidationMessage> errors = personSchema.validate(person);
    Assert.assertEquals(0, errors.size());
  }

  @Test
  public void shouldInvalidate() throws Exception {
    final JsonSchema personSchema = SchemaUtil.getYamlSchemaFromClasspath("schema/person.schema.yaml");
    final JsonNode person = SchemaUtil.readJsonFromClasspath("instance/person-invalid.json");
    final Set<ValidationMessage> errors = personSchema.validate(person);
    Assert.assertEquals(1, errors.size());
    Assert.assertEquals("[$.age: must have a minimum value of 0]", errors.toString());
  }

  @Test
  public void shouldValidatePersons() throws Exception {
    final JsonSchema schema = SchemaUtil.getYamlSchemaFromClasspath("schema/persons.schema.yaml");
    final JsonNode instance = SchemaUtil.readJsonFromClasspath("instance/persons.json");
    final Set<ValidationMessage> errors = schema.validate(instance);
    Assert.assertEquals(0, errors.size());
  }

  @Test
  public void shouldInvalidatePersons() throws Exception {
    final JsonSchema schema = SchemaUtil.getYamlSchemaFromClasspath("schema/persons.schema.yaml");
    final JsonNode instance = SchemaUtil.readJsonFromClasspath("instance/persons-invalid.json");
    final Set<ValidationMessage> errors = schema.validate(instance);
    Assert.assertEquals(1, errors.size());
  }

}
