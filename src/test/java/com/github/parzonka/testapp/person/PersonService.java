package com.github.parzonka.testapp.person;

import org.springframework.stereotype.Service;

import com.github.parzonka.schemist.aggregate.AggregateService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonService extends AggregateService<PersonAggregate, Person> {

  @Getter
  private final PersonRepository repository;

  @Override
  public String getCollectionId() {
    return "persons";
  }

  @Override
  public PersonAggregate newAggregate() {
    return new PersonAggregate();
  }

}
