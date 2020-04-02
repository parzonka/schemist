package com.github.parzonka.schemist.aggregate;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@NoRepositoryBean
public interface AggregateRepository<A extends Aggregate<T>, T> extends CrudRepository<A, UUID> {

  String getCollectionId();

  default A addNew(Supplier<A> supplier) {
    final A aggregate = supplier.get();
    return save(aggregate);
  }

  default A mutate(UUID id, Consumer<T> consumer) {
    final Optional<A> optAggregate = findById(id);
    if (!optAggregate.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aggregate " + id + " not found");
    }
    final A aggregate = optAggregate.get();
    aggregate.mutate(consumer);
    save(aggregate);
    return aggregate;
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

}
