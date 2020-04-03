package com.github.parzonka.schemist.aggregate;

import java.util.Iterator;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface AggregateService<A extends Aggregate<T>, T> {

  String getCollectionId();

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
    getRepository().validateAndSave(aggregate);
    return aggregate;
  }

  default A modifyAggregate(UUID id, JsonNode data) {
    final A aggregate = getRepository().getById(id);
    aggregate.setData(data);
    getRepository().validateAndSave(aggregate);
    return aggregate;
  }

  /**
   * Clients may change on react upon changed aggregate during CRUD modification.
   * 
   * @param aggregate
   * @return
   */
  default A onModifiedAggregate(A aggregate) {
    return aggregate;
  }

  default A partiallyUpdateAggregate(UUID id, JsonNode update) {
    final A aggregate = getRepository().getById(id);
    final JsonNode merged = merge(aggregate.getData(), update);
    aggregate.setData(merged);
    getRepository().validateAndSave(aggregate);
    return aggregate;
  }

  static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {

    Iterator<String> fieldNames = updateNode.fieldNames();
    while (fieldNames.hasNext()) {

      String fieldName = fieldNames.next();
      JsonNode jsonNode = mainNode.get(fieldName);
      // if field exists and is an embedded object
      if (jsonNode != null && jsonNode.isObject()) {
        merge(jsonNode, updateNode.get(fieldName));
      } else {
        if (mainNode instanceof ObjectNode) {
          // Overwrite field
          JsonNode value = updateNode.get(fieldName);
          ((ObjectNode) mainNode).put(fieldName, value);
        }
      }

    }

    return mainNode;
  }

}
