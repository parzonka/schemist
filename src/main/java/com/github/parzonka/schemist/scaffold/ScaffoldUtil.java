package com.github.parzonka.schemist.scaffold;

public class ScaffoldUtil {

  public static String path(String packageName) {
    return "src/java/java/" + packageName.replace(".", "/");

  }

}
