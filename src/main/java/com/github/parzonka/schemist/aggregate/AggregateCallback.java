package com.github.parzonka.schemist.aggregate;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class AggregateCallback implements BeforeConvertCallback<Aggregate<?>> {

  @Override
  public Aggregate<?> onBeforeConvert(final Aggregate<?> aggregate) {
    if (aggregate.getId() == null) {
      aggregate.setId(UUID.randomUUID());
      log.debug("New " + aggregate + " is going to be stored.");
    } else {
      log.debug("Existing " + aggregate + " is going to be updated.");
    }
    return aggregate;
  }

}
