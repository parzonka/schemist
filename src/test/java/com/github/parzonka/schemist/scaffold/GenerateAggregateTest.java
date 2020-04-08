package com.github.parzonka.schemist.scaffold;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import com.github.parzonka.schemist.aggregate.AggregateSpec;
import com.github.parzonka.testapp.person.Person;

public class GenerateAggregateTest {

  @Test
  public void should_generate() throws Exception {
    final AggregateSpec aggregateSpec = AggregateSpec.builder()
        .aggregateType(Person.class)
        .localizedSingular("Person")
        .localizedPlural("Persons")
        .schemaUrl("/schema/person.yaml")
        .build();
    writeAggregate(aggregateSpec, "target/generated/");
    return;
  }

  private void writeAggregate(final AggregateSpec a, String prefix) throws IOException {
    final String aggregate = GenerateAggregate.aggregate(a, getClass().getPackageName());
    final String dir = "target/generated/src/test/java/" + getClass().getPackageName()
        .replace(".", "/");
    Path dirPath = Paths.get(dir);
    Path filePath = Paths.get(dir + "/" + a.getLocalizedSingular() + "Aggregate.java");
    Files.createDirectories(dirPath);
    Files.writeString(filePath, aggregate, Charset.forName("UTF-8"));
  }

}
