package com.github.parzonka.schemist.scaffold;

import javax.lang.model.element.Modifier;

import org.springframework.data.relational.core.mapping.Table;

import com.github.parzonka.schemist.aggregate.Aggregate;
import com.github.parzonka.schemist.aggregate.AggregateSpec;
import com.squareup.javapoet.*;

import lombok.SneakyThrows;

public class GenerateAggregate {

  @SneakyThrows
  public static String generateAggregate(AggregateSpec aggregateSpec, String packageName) {

    MethodSpec constructor = MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PUBLIC)
        .addStatement("super($N.class)", aggregateSpec.getAggregateType()
            .getSimpleName())
        .build();

    TypeSpec aggregate = TypeSpec.classBuilder(aggregateSpec.getLocalizedSingular() + "Aggregate")
        .addModifiers(Modifier.PUBLIC)
        .superclass(ParameterizedTypeName.get(Aggregate.class, aggregateSpec.getAggregateType()))
        .addAnnotation(tableAnnotation(aggregateSpec.getLocalizedPlural()))
        .addMethod(constructor)
        .build();

    JavaFile javaFile = JavaFile.builder(packageName, aggregate)
        .build();

    return javaFile.toString();
  }

  static AnnotationSpec tableAnnotation(String tableName) {
    return AnnotationSpec.builder(Table.class)
        .addMember("value", "$S", tableName)
        .build();
  }

}
