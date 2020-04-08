package com.github.parzonka.schemist.scaffold;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.parzonka.schemist.aggregate.AggregateSpec;

import lombok.Builder;
import lombok.SneakyThrows;
import lombok.Value;

@Value
@Builder
public class ScaffoldingGenerator {

  String packageName;
  @Builder.Default
  String prefix = "";

  public void generateFiles(AggregateSpec aggregateSpec) {
    final String aggregate = GenerateAggregate.aggregate(aggregateSpec, packageName);
    writeAggregate(aggregateSpec, aggregate);
  }

  @SneakyThrows
  private void writeAggregate(final AggregateSpec aggregateSpec, final String aggregate) {
    final String dir = path();
    final Path dirPath = Paths.get(dir);
    final Path filePath = Paths.get(dir + "/" + aggregateSpec.getLocalizedSingular() + "Aggregate.java");
    Files.createDirectories(dirPath);
    Files.writeString(filePath, aggregate, Charset.forName("UTF-8"));
  }

  private String path() {
    return "src/main/java/" + packageName.replace(".", "/");
  }

}
