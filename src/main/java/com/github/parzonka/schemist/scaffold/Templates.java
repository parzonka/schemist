package com.github.parzonka.schemist.scaffold;

import com.github.parzonka.schemist.aggregate.AggregateSpec;

import lombok.SneakyThrows;

public class Templates {

  public static final String PACKAGE = "package PACKAGE.SINGULAR_LC;\n";
  public static final String NEWLINE = "\n";

  @SneakyThrows
  public static String aggregateTemplate() {
    StringBuilder sb = new StringBuilder();
    sb.append(PACKAGE);
    sb.append(NEWLINE);
    sb.append("import org.springframework.data.relational.core.mapping.Table;\n");
    sb.append(NEWLINE);
    sb.append("import com.github.parzonka.schemist.aggregate.Aggregate;\n");
    sb.append("import QUALIFIED_TYPE;\n");
    sb.append(NEWLINE);
    sb.append("@Table(\"SINGULAR_LC\")\n");
    sb.append("public class SINGULARAggregate extends Aggregate<TYPE> {\n");
    sb.append(NEWLINE);
    sb.append("  public SINGULARAggregate() {\n");
    sb.append("    super(TYPE.class);\n");
    sb.append("  }\n");
    sb.append("}\n");
    return sb.toString();
  }

  @SneakyThrows
  public static String repositoryTemplate() {
    StringBuilder sb = new StringBuilder();
    sb.append(PACKAGE);
    sb.append(NEWLINE);
    sb.append("import com.github.parzonka.schemist.aggregate.AggregateRepository;\n");
    sb.append("import QUALIFIED_TYPE;\n");
    sb.append(NEWLINE);
    sb.append("public interface SINGULARRepository extends AggregateRepository<SINGULARAggregate, TYPE> {\n");
    sb.append(NEWLINE);
    sb.append("}\n");
    return sb.toString();
  }

  @SneakyThrows
  public static String serviceTemplate() {
    StringBuilder sb = new StringBuilder();
    sb.append(PACKAGE);
    sb.append(NEWLINE);
    sb.append("import org.springframework.stereotype.Service;\n");
    sb.append(NEWLINE);
    sb.append("import com.github.parzonka.schemist.aggregate.AggregateService;\n");
    sb.append("import QUALIFIED_TYPE;\n");
    sb.append(NEWLINE);
    sb.append("import lombok.Getter;\n");
    sb.append("import lombok.RequiredArgsConstructor;\n");
    sb.append(NEWLINE);
    sb.append("@Service\n");
    sb.append("@RequiredArgsConstructor\n");
    sb.append("public class SINGULARService extends AggregateService<SINGULARAggregate, TYPE> {\n");
    sb.append(NEWLINE);
    sb.append("  @Getter\n");
    sb.append("  private final SINGULARRepository repository;\n");
    sb.append(NEWLINE);
    sb.append("  @Override\n");
    sb.append("  public String getCollectionId() {\n");
    sb.append("    return \"PLURAL_LC\";\n");
    sb.append("  }\n");
    sb.append(NEWLINE);
    sb.append("  @Override\n");
    sb.append("  public SINGULARAggregate newAggregate() {\n");
    sb.append("    return new SINGULARAggregate();\n");
    sb.append("  }\n");
    sb.append(NEWLINE);
    sb.append("}\n");
    return sb.toString();
  }

  public static String postgresTemplate() {
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE TABLE IF NOT EXISTS SINGULAR_LC (\n");
    sb.append("   ID UUID NOT NULL PRIMARY KEY,\n");
    sb.append("   data jsonb\n");
    sb.append(");\n");
    return sb.toString();
  }

  public static String fillTemplate(String template, AggregateSpec aggregateSpec, String packageName) {
    return template.replaceAll("PACKAGE", packageName)
        .replaceAll("SINGULAR_LC", aggregateSpec.getLocalizedSingular()
            .toLowerCase())
        .replaceAll("SINGULAR", aggregateSpec.getLocalizedSingular())
        .replaceAll("PLURAL_LC", aggregateSpec.getLocalizedPlural()
            .toLowerCase())
        .replaceAll("PLURAL", aggregateSpec.getLocalizedPlural())
        .replaceAll("QUALIFIED_TYPE", aggregateSpec.getAggregateType()
            .getName())
        .replaceAll("TYPE", aggregateSpec.getAggregateType()
            .getSimpleName());
  }

}
