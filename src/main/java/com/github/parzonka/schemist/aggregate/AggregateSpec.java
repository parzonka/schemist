package com.github.parzonka.schemist.aggregate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AggregateSpec {

  Class<?> aggregateType;
  String schemaUrl;
  String localizedPlural;
  String localizedSingular;
  String resourceName;

  public String getPackageName() {
    return aggregateType.getPackageName();
  }

}
