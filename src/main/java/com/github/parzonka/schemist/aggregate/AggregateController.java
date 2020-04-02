package com.github.parzonka.schemist.aggregate;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AggregateController {

  private final List<AggregateRepository<? extends Aggregate<?>, ?>> aggregateRepositories;

  @GetMapping("/{collectionId}")
  public Iterable<? extends Aggregate<?>> getAllByCollectionId(@PathVariable String collectionId) {
    return findRepo(collectionId).findAll();
  }

  @GetMapping("/{collectionId}/{aggregateId}")
  public Aggregate<?> getAggregate(@PathVariable String collectionId, @PathVariable UUID aggregateId) {
    return findRepo(collectionId).getById(aggregateId);
  }

  public AggregateRepository<? extends Aggregate<?>, ?> findRepo(String collectionId) {
    if (collectionId == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return aggregateRepositories.stream()
        .filter(ar -> collectionId.equals(ar.getCollectionId()))
        .findAny()
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Collection " + collectionId + " not found"));
  }

}
