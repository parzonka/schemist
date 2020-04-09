package com.github.parzonka.schemist.aggregate;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.parzonka.schemist.schema.SchemaUtil;
import com.github.parzonka.schemist.web.WebContext;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;

public abstract class AggregateService<A extends Aggregate<T>, T> {

  private WebContext webContext;

  @Autowired
  void setWebContext(WebContext webContext) {
    System.out.println("WebContext injected " + webContext);
    this.webContext = webContext;
  }

  public abstract String getCollectionId();

  public abstract AggregateRepository<A, T> getRepository();

  /**
   * Factory method: Return a new aggregate instance.
   *
   * @return
   */
  public abstract A newAggregate();

  /**
   * Adds a new aggregate. Most of the time, this will be persisted.
   *
   * @param data
   * @return
   */
  A saveNewAggregate(JsonNode data) {
    A aggregate = newAggregate();
    aggregate.setData(data);
    validateAndSave(aggregate);
    return aggregate;
  }

  A modifyAggregate(UUID id, JsonNode data) {
    final A aggregate = getById(id);
    aggregate.setData(data);
    validateAndSave(aggregate);
    return aggregate;
  }

  /**
   * Clients may change on react upon changed aggregate during CRUD modification.
   * 
   * @param aggregate
   * @return
   */
  A onModifiedAggregate(A aggregate) {
    return aggregate;
  }

  A partiallyUpdateAggregate(UUID id, JsonNode update) {
    final A aggregate = getById(id);
    final JsonNode merged = merge(aggregate.getData(), update);
    aggregate.setData(merged);
    validateAndSave(aggregate);
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

  A handleCommandById(UUID aggregateId, String commandId, JsonNode command) {
    final A aggregate = getById(aggregateId);
    final A handled = handleCommand(aggregate, commandId, command);
    getRepository().save(handled);
    return handled;
  }

  A handleCommand(A aggregate, String commandId, JsonNode command) {
    return aggregate;
  }

  protected A addNew(Supplier<A> supplier) {
    final A aggregate = supplier.get();
    return validateAndSave(aggregate);
  }

  protected A mutate(UUID id, Consumer<T> consumer) {
    final Optional<A> optAggregate = getRepository().findById(id);
    if (!optAggregate.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aggregate " + id + " not found");
    }
    final A aggregate = optAggregate.get();
    aggregate.mutate(consumer);
    return validateAndSave(aggregate);
  }

  protected A findAggregate(UUID id) {
    final A a = getRepository().findById(null)
        .get();
    final Optional<A> optAggregate = getRepository().findById(id);
    if (!optAggregate.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aggregate " + id + " not found");
    }
    final A aggregate = optAggregate.get();
    return aggregate;
  }

  protected A getById(UUID id) {
    return getRepository().findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aggregate " + id + " not found"));
  }

  protected A validateAndSave(A aggregate) {
    validate(aggregate);
    getRepository().save(aggregate);
    return aggregate;
  }

  void validate(A aggregate) {
    final String simpleName = aggregate.getType()
        .getSimpleName();
    final String shortName = simpleName.toLowerCase();
    final String urlPrefix = webContext.getHttp();
    final String schemaUri = urlPrefix + "/schema/" + shortName + ".yaml";
    final JsonSchema jsonSchema = SchemaUtil.getJsonSchemaFromUrl(schemaUri);
    final Set<ValidationMessage> errors = jsonSchema.validate(aggregate.getData());
    if (errors.size() > 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.toString());
    }
  }

}
