package com.github.parzonka.schemist.scaffold;

import org.junit.Test;

import com.github.parzonka.schemist.aggregate.AggregateSpec;
import com.github.parzonka.testapp.person.Person;

public class ScaffoldingGeneratorTest {

  @Test
  public void should_generate() throws Exception {
    final AggregateSpec aggregateSpec = AggregateSpec.builder()
        .aggregateType(Person.class)
        .localizedSingular("Person")
        .localizedPlural("Persons")
        .schemaUrl("/schema/person.yaml")
        .build();
    final ScaffoldingGenerator scaffoldingGenerator = ScaffoldingGenerator.builder()
        .prefix("target/generated")
        .packageName("test")
        .build();
    scaffoldingGenerator.generateFiles(aggregateSpec);
  }

}
