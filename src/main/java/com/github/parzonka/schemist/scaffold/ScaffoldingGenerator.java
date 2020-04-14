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
    writeJavaSource(aggregateSpec, Templates.fillTemplate(Templates.aggregateTemplate(), aggregateSpec, packageName),
        "Aggregate");
    writeJavaSource(aggregateSpec, Templates.fillTemplate(Templates.repositoryTemplate(), aggregateSpec, packageName),
        "Repository");
    writeJavaSource(aggregateSpec, Templates.fillTemplate(Templates.serviceTemplate(), aggregateSpec, packageName),
        "Service");
    writeFlywayMigration(Templates.fillTemplate(Templates.postgresTemplate(), aggregateSpec, packageName));
  }

  @SneakyThrows
  private void writeJavaSource(final AggregateSpec aggregateSpec, final String content, final String name) {
    final String dir = prefix + "java/" + packageName.replace(".", "/") + "/" + aggregateSpec.getLocalizedSingular()
        .toLowerCase();
    final Path dirPath = Paths.get(dir);
    final Path filePath = Paths.get(dir + "/" + aggregateSpec.getLocalizedSingular() + name + ".java");
    Files.createDirectories(dirPath);
    Files.writeString(filePath, content, Charset.forName("UTF-8"));
  }

  @SneakyThrows
  private void writeFlywayMigration(final String content) {
    final String dir = prefix + "resources/db/migration";
    final Path dirPath = Paths.get(dir);
    final Path filePath = Paths.get(dir + "/R__aggregates.sql");
    Files.createDirectories(dirPath);
    Files.writeString(filePath, content, Charset.forName("UTF-8"));
  }

}
