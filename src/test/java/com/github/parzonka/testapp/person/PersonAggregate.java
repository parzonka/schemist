package com.github.parzonka.testapp.person;

import org.springframework.data.relational.core.mapping.Table;

import com.github.parzonka.schemist.aggregate.Aggregate;
import com.github.parzonka.testapp.schema.Person;

@Table("person")
public class PersonAggregate extends Aggregate<Person> {

  public PersonAggregate() {
    super(Person.class);
  }
}
