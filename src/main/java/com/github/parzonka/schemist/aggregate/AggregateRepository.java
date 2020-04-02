package com.github.parzonka.schemist.aggregate;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.github.parzonka.schemist.schema.SchemaUtil;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;

@NoRepositoryBean
public interface AggregateRepository<A extends Aggregate<T>, T> extends CrudRepository<A, UUID> {

  default A addNew(Supplier<A> supplier) {
    final A aggregate = supplier.get();
    return validateAndSave(aggregate);

  }

  default A mutate(UUID id, Consumer<T> consumer) {
    final Optional<A> optAggregate = findById(id);
    if (!optAggregate.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aggregate " + id + " not found");
    }
    final A aggregate = optAggregate.get();
    aggregate.mutate(consumer);
    return validateAndSave(aggregate);
  }

  default A findAggregate(UUID id) {
    final A a = findById(null).get();
    final Optional<A> optAggregate = findById(id);
    if (!optAggregate.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aggregate " + id + " not found");
    }
    final A aggregate = optAggregate.get();
    return aggregate;
  }

  default A getById(UUID id) {
    return findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aggregate " + id + " not found"));
  }

  default A validateAndSave(A aggregate) {
    validate(aggregate);
    save(aggregate);
    return aggregate;
  }

  default void validate(A aggregate) {
    final String simpleName = aggregate.getType()
        .getSimpleName();
    final String shortName = simpleName.toLowerCase();
    final String schemaUri = "http://localhost:8080/schema/" + shortName + ".yaml";
    final JsonSchema jsonSchema = SchemaUtil.getJsonSchemaFromUrl(schemaUri);
    final Set<ValidationMessage> errors = jsonSchema.validate(aggregate.getData());
    if (errors.size() > 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.toString());
    }
  }

}
