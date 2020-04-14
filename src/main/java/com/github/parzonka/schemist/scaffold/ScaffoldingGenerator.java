package com.github.parzonka.schemist.scaffold;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.parzonka.schemist.aggregate.AggregateSpec;

import lombok.Builder;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
@Builder
public class ScaffoldingGenerator {

  String packageName;
  @Builder.Default
  String prefix = "";
  @Builder.Default
  String schemaPackage = "/schema";

  public void generateFiles(AggregateSpec aggregateSpec) {
    write(aggregateSpec, Templates.fillTemplate(Templates.aggregateTemplate(), aggregateSpec, packageName),
        "Aggregate");
    write(aggregateSpec, Templates.fillTemplate(Templates.repositoryTemplate(), aggregateSpec, packageName),
        "Repository");
    write(aggregateSpec, Templates.fillTemplate(Templates.serviceTemplate(), aggregateSpec, packageName), "Service");
  }

  @SneakyThrows
  private void write(final AggregateSpec aggregateSpec, final String content, final String name) {
    final String dir = prefix + packageName.replace(".", "/") + "/" + aggregateSpec.getLocalizedSingular()
        .toLowerCase();
    final Path dirPath = Paths.get(dir);
    final Path filePath = Paths.get(dir + "/" + aggregateSpec.getLocalizedSingular() + name + ".java");
    Files.createDirectories(dirPath);
    Files.writeString(filePath, content, Charset.forName("UTF-8"));
  }

}
