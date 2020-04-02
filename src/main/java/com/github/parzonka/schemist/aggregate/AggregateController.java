package com.github.parzonka.schemist.aggregate;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AggregateController {

  private final List<AggregateRepository<? extends Aggregate<?>, ?>> aggregateRepositories;
  private final List<AggregateService<? extends Aggregate<?>, ?>> aggregateServices;

  @GetMapping("/{collectionId}")
  public Iterable<? extends Aggregate<?>> getAllByCollectionId(@PathVariable String collectionId) {
    return findRepo(collectionId).findAll();
  }

  @GetMapping("/{collectionId}/{aggregateId}")
  public Aggregate<?> getAggregate(@PathVariable String collectionId, @PathVariable UUID aggregateId) {
    return findRepo(collectionId).getById(aggregateId);
  }

  @PostMapping("/{collectionId}")
  public Aggregate<?> addNewAggregate(@PathVariable String collectionId, @RequestBody JsonNode requestBody) {
    log.debug("Requesting to add new aggregate: {}", requestBody);
    final Aggregate<?> aggregate = findService(collectionId).saveNewAggregate(requestBody);
    return aggregate;
  }

  @PutMapping("/{collectionId}/{aggregateId}")
  public Aggregate<?> modifyAggregate(@PathVariable String collectionId, @PathVariable UUID aggregateId,
      @RequestBody JsonNode requestBody) {
    log.debug("Requesting to modify aggregate {} using  : {}", aggregateId, requestBody);
    return findService(collectionId).modifyAggregate(aggregateId, requestBody);
  }

  public AggregateRepository<? extends Aggregate<?>, ?> findRepo(String collectionId) {
    return findService(collectionId).getRepository();
  }

  public AggregateService<? extends Aggregate<?>, ?> findService(String collectionId) {
    if (collectionId == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return aggregateServices.stream()
        .filter(ar -> collectionId.equals(ar.getCollectionId()))
        .findAny()
        .orElseThrow(() -> {
          final String message = "Collection '" + collectionId + "' not found";
          log.debug(message + ". We know the following services: " + aggregateServices);
          return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        });
  }

}
