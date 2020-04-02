package com.github.parzonka.schemist.aggregate;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.*;

/**
 * Wraps an DDD aggregate.
 * 
 * @param <T>
 */
@RequiredArgsConstructor
public class Aggregate<T> {

  private final static ObjectMapper MAPPER = new ObjectMapper();

  @JsonIgnore
  @Getter
  @Transient
  private final Class<T> type;

  @Id
  @Getter
  @Setter(AccessLevel.PACKAGE)
  UUID id;

  @Getter(AccessLevel.PACKAGE)
  @Setter(AccessLevel.PACKAGE)
  JsonNode data;

  public JsonNode getData() {
    return data;
  }

  /**
   * Mutates the domain object in-place (reference semantics).
   * 
   * This method is thread-safe.
   * 
   * @param consumer
   */
  public synchronized void mutate(Consumer<T> consumer) {
    final T value = getDomainObject();
    consumer.accept(value);
  }

  /**
   * Consumes and returns a transformed domain object (value semantics).
   * 
   * This method is thread-safe.
   * 
   * @param unaryOperator
   */
  public synchronized void transform(UnaryOperator<T> unaryOperator) {
    final T value = getDomainObject();
    final T result = unaryOperator.apply(value);
    setDomainObject(result);
  }

  /**
   * @return the domain object
   */
  @JsonIgnore
  @SneakyThrows
  public T getDomainObject() {
    return MAPPER.treeToValue(data, type);
  }

  /**
   * Sets the domain object
   * 
   * @param value
   */
  public void setDomainObject(T value) {
    this.data = MAPPER.valueToTree(value);
  }

  public String toString() {
    return "[" + type.getName() + ":" + id == null ? "<no-id>" : id + "]";
  }

}
