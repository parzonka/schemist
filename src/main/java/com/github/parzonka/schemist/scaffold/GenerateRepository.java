package com.github.parzonka.schemist.scaffold;

import com.github.parzonka.schemist.aggregate.AggregateSpec;

import lombok.SneakyThrows;

public class GenerateRepository {

  @SneakyThrows
  public static String generateRepository(AggregateSpec aggregateSpec, String packageName) {
    StringBuilder sb = new StringBuilder();
    sb.append("package PACKAGE;\n");
    sb.append("\n");
    sb.append("import com.github.parzonka.schemist.aggregate.AggregateRepository;\n");
    sb.append("import PACKAGE.SINGULARAggregate;\n");
    sb.append("import TYPE;\n");
    sb.append("\n");
    sb.append("public interface SINGULARRepository extends AggregateRepository<SINGULARAggregate, SINGULAR> {\n");
    sb.append("\n");
    sb.append("}\n");
    final String template = sb.toString();
    return template.replaceAll("PACKAGE", packageName)
        .replaceAll("SINGULAR", aggregateSpec.getLocalizedSingular())
        .replaceAll("TYPE", aggregateSpec.getAggregateType()
            .getName());
  }

}
