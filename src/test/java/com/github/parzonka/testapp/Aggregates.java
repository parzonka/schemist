package com.github.parzonka.testapp;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.github.parzonka.schemist.aggregate.AggregateSpec;
import com.github.parzonka.schemist.aggregate.AggregateSpecRegistry;
import com.github.parzonka.testapp.schema.Person;

@Component
public class Aggregates implements AggregateSpecRegistry {

  @Override
  public List<AggregateSpec> getAggregateSpecs() {
    return Arrays.asList(AggregateSpec.builder()
        .aggregateType(Person.class)
        .localizedSingular("Person")
        .localizedPlural("Persons")
        .schemaUrl("/schema/person.yaml")
        .build());
  }
}
