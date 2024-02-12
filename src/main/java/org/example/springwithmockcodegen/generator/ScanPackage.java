package org.example.springwithmockcodegen.generator;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

class ScanPackage {
  static List<Class<?>> scanClasses(String basePackage) {
    final ArrayList<Class<?>> classes = new ArrayList<>();

    try {
      final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      final String path = basePackage.replace('.', '/');
      final Enumeration<URL> resources = classLoader.getResources(path);

      while (resources.hasMoreElements()) {
        final URL url = resources.nextElement();
        final File file = new File(url.toURI());

        traverse(basePackage, classes, file.listFiles());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return classes;
  }

  private static void traverse(String basePackage, ArrayList<Class<?>> classes, File[] files)
      throws ClassNotFoundException {
    System.out.println("basePackage = " + basePackage);

    if (files == null) {
      return;
    }

    for (File classFile : files) {
      if (classFile.isDirectory()) {
        traverse(basePackage + "." +classFile.getName(), classes, classFile.listFiles());
      } else {
        String className = basePackage + "." + classFile.getName().replace(".class", "");
        final Class<?> clazz = Class.forName(className);
        classes.add(clazz);
      }
    }
  }
}
