package test;

import com.github.parzonka.schemist.aggregate.Aggregate;
import com.github.parzonka.testapp.person.Person;
import org.springframework.data.relational.core.mapping.Table;

@Table("Persons")
public class PersonAggregate extends Aggregate<Person> {
  public PersonAggregate() {
    super(Person.class);
  }
}
