package com.github.parzonka.schemist.aggregate;

import com.fasterxml.jackson.databind.JsonNode;

public interface AggregateService<A extends Aggregate<T>, T> {

  AggregateRepository<A, T> getRepository();

  /**
   * Factory method: Return a new aggregate instance.
   * 
   * @return
   */
  A newAggregate();

  /**
   * Adds a new aggregate. Most of the time, this will be persisted.
   * 
   * @param data
   * @return
   */
  default A saveNewAggregate(JsonNode data) {
    A aggregate = newAggregate();
    aggregate.setData(data);
    getRepository().save(aggregate);
    return aggregate;
  }

}
