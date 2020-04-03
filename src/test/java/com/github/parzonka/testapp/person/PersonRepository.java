package com.github.parzonka.testapp.person;

import com.github.parzonka.schemist.aggregate.AggregateRepository;

public interface PersonRepository extends AggregateRepository<PersonAggregate, Person> {

}
