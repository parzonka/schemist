package com.github.parzonka.testapp.person;

import com.github.parzonka.schemist.aggregate.AggregateRepository;
import com.github.parzonka.testapp.schema.Person;

public interface PersonRepository extends AggregateRepository<PersonAggregate, Person> {

}
