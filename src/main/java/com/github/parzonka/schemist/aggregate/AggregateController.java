package com.github.parzonka.schemist.aggregate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

  private final List<AggregateService<? extends Aggregate<?>, ?>> aggregateServices;
  private final Optional<AggregateSpecRegistry> aggregateSpecRegistry;

  @GetMapping
  public List<AggregateSpec> getAll() {
    if (aggregateSpecRegistry.isPresent()) {
      return aggregateSpecRegistry.get()
          .getAggregateSpecs();
    } else {
      return Collections.emptyList();
    }
  }

  @GetMapping("/{collectionId}")
  public Iterable<? extends Aggregate<?>> getAllByCollectionId(@PathVariable String collectionId) {
    return findRepo(collectionId).findAll();
  }

  @GetMapping("/{collectionId}/{aggregateId}")
  public Aggregate<?> getAggregate(@PathVariable String collectionId, @PathVariable UUID aggregateId) {
    return findService(collectionId).getById(aggregateId);
  }

  @PostMapping("/{collectionId}")
  public Aggregate<?> addNewAggregate(@PathVariable String collectionId, @RequestBody JsonNode requestBody) {
    log.debug("POST of new aggregate requested with body: {}", requestBody);
    final Aggregate<?> aggregate = findService(collectionId).saveNewAggregate(requestBody);
    return aggregate;
  }

  @PutMapping("/{collectionId}/{aggregateId}")
  public Aggregate<?> putAggregate(@PathVariable String collectionId, @PathVariable UUID aggregateId,
      @RequestBody JsonNode requestBody) {
    log.debug("PUT of aggregate {} requested with body: {}", aggregateId, requestBody);
    return findService(collectionId).modifyAggregate(aggregateId, requestBody);
  }

  @PatchMapping("/{collectionId}/{aggregateId}")
  public Aggregate<?> patchAggregate(@PathVariable String collectionId, @PathVariable UUID aggregateId,
      @RequestBody JsonNode requestBody) {
    log.debug("PATCH of aggregate {} requested with body: {}", aggregateId, requestBody);
    return findService(collectionId).partiallyUpdateAggregate(aggregateId, requestBody);
  }

  @DeleteMapping("/{collectionId}/{aggregateId}")
  public void deleteAggregate(@PathVariable String collectionId, @PathVariable UUID aggregateId,
      @RequestBody JsonNode requestBody) {
    log.debug("DELETE of aggregate {} requested", aggregateId, requestBody);
    findRepo(collectionId).deleteById(aggregateId);
  }

  @PostMapping("/{collectionId}/{aggregateId}/{commandId}")
  public Aggregate<?> applyCommand(@PathVariable String collectionId, @PathVariable UUID aggregateId,
      @PathVariable String commandId, @RequestBody JsonNode requestBody) {
    log.debug("POST of command '{}' to be handled by aggregate '{}':'{}' command with body: {}", commandId,
        collectionId, aggregateId, requestBody);
    final Aggregate<?> aggregate = findService(collectionId).handleCommandById(aggregateId, commandId, requestBody);
    return aggregate;
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
          final String message = "Collection endpoint '" + collectionId
              + "' not found. Available collection endpoints: " + aggregateServices.stream()
                  .map(as -> as.getCollectionId())
                  .collect(Collectors.toList());
          log.debug(message);
          return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        });
  }

}
