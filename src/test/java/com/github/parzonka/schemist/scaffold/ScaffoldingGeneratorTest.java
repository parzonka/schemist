package com.github.parzonka.schemist.scaffold;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import com.github.parzonka.schemist.aggregate.AggregateSpec;
import com.github.parzonka.testapp.schema.Person;

import lombok.SneakyThrows;

public class ScaffoldingGeneratorTest {

  @Test
  public void should_generate_person_sources() throws Exception {
    final AggregateSpec aggregateSpec = AggregateSpec.builder()
        .aggregateType(Person.class)
        .localizedSingular("Person")
        .localizedPlural("Persons")
        .schemaUrl("/schema/person.yaml")
        .build();
    final ScaffoldingGenerator scaffoldingGenerator = ScaffoldingGenerator.builder()
        .prefix("target/generated/")
        .packageName("com.github.parzonka.testapp")
        .build();

    // when
    scaffoldingGenerator.generateFiles(aggregateSpec);

    // then
    assertGeneratedIsEqualsToTestappSource("com/github/parzonka/testapp/person/PersonAggregate.java");
    assertGeneratedIsEqualsToTestappSource("com/github/parzonka/testapp/person/PersonService.java");
    assertGeneratedIsEqualsToTestappSource("com/github/parzonka/testapp/person/PersonRepository.java");
    assertGeneratedIsEqualsToTestappMigration("db/migration/R__aggregates.sql");
  }

  @SneakyThrows
  private static void assertGeneratedIsEqualsToTestappSource(String type) {
    final String expected = Files.readString(Paths.get("src/test/java/" + type));
    final String generated = Files.readString(Paths.get("target/generated/java/" + type));
    Assert.assertEquals(expected, generated);
  }

  @SneakyThrows
  private static void assertGeneratedIsEqualsToTestappMigration(String migration) {
    final String expected = Files.readString(Paths.get("src/test/resources/" + migration));
    final String generated = Files.readString(Paths.get("target/generated/resources/" + migration));
    Assert.assertEquals(expected, generated);
  }

}
