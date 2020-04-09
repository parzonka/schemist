package com.github.parzonka.schemist.aggregate;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AggregateRepository<A extends Aggregate<T>, T> extends CrudRepository<A, UUID> {

}
