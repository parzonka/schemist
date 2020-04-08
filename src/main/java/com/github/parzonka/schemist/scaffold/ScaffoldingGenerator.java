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
    final String aggregate = GenerateAggregate.generateAggregate(aggregateSpec, packageName);
    write(aggregateSpec, aggregate, "Aggregate");
    final String repository = GenerateRepository.generateRepository(aggregateSpec, packageName);
    write(aggregateSpec, repository, "Repository");
    System.out.println(repository);
  }

  @SneakyThrows
  private void write(final AggregateSpec aggregateSpec, final String content, final String name) {
    final String dir = prefix + packageName.replace(".", "/");
    final Path dirPath = Paths.get(dir);
    final Path filePath = Paths.get(dir + "/" + aggregateSpec.getLocalizedSingular() + name + ".java");
    Files.createDirectories(dirPath);
    Files.writeString(filePath, content, Charset.forName("UTF-8"));
  }

}
