package com.github.parzonka.schemist.scaffold;

import javax.lang.model.element.Modifier;

import org.springframework.data.relational.core.mapping.Table;

import com.github.parzonka.schemist.aggregate.Aggregate;
import com.squareup.javapoet.*;

import lombok.SneakyThrows;

public class GenerateAggregate {

  @SneakyThrows
  public static void main(String[] args) {

    MethodSpec constructor = MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PUBLIC)
        .addStatement("super($N.class)", "Greeting")
        .build();

    TypeSpec aggregate = TypeSpec.classBuilder("SomeAggregate")
        .addModifiers(Modifier.PUBLIC)
        .addSuperinterface(ParameterizedTypeName.get(Aggregate.class, String.class))
        .addAnnotation(tableAnnotation("SomeTable"))
        .addMethod(constructor)
        .build();

    JavaFile javaFile = JavaFile.builder("com.example", aggregate)
        .build();

    javaFile.writeTo(System.out);
  }

  static AnnotationSpec tableAnnotation(String tableName) {
    return AnnotationSpec.builder(Table.class)
        .addMember("value", "$S", tableName)
        .build();

  }

}
