package org.example.springwithmockcodegen.generator;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EndpointProvider {
  public static void main(String[] args) {
    final EndpointProvider endpointProvider = new EndpointProvider();
    final List<String> allEndpoints = endpointProvider.getAllEndpoints();
    System.out.println(allEndpoints);
  }

  public List<String> getAllEndpoints() {
    List<String> endpoints = new ArrayList<>();

    // Scan all classes in your project package or specific packages
    List<Class<?>> classes = ScanPackage.scanClasses("org.example.springwithmockcodegen");

    for (Class<?> clazz : classes) {
      // Check if the class is a controller or has request mappings
      if (clazz.isAnnotationPresent(RestController.class) /*|| clazz.isAnnotationPresent(Controller.class)*/) {
        String basePath = "";
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
          RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
          basePath = requestMapping.value()[0];
        }

        // Inspect methods for request mappings
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
          System.out.println("method = " + method);
          if (method.isAnnotationPresent(RequestMapping.class) ||
              method.isAnnotationPresent(GetMapping.class) ||
              method.isAnnotationPresent(PostMapping.class) ||
              method.isAnnotationPresent(PutMapping.class) ||
              method.isAnnotationPresent(DeleteMapping.class)) {

            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            PutMapping putMapping = method.getAnnotation(PutMapping.class);
            DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);

            String[] paths;
            if (requestMapping != null) {
              paths = requestMapping.value();
            } else if (getMapping != null) {
              paths = getMapping.value();
            } else if (postMapping != null) {
              paths = postMapping.value();
            } else if (putMapping != null) {
              paths = putMapping.value();
            } else if (deleteMapping != null) {
              paths = deleteMapping.value();
            } else {
              continue;
            }


            for (String path : paths) {
              final String endpoint = basePath + "/" + path;
              ClassGenerator.generate(endpoint, clazz, method);
              endpoints.add(endpoint);
            }
          }
        }
      }
    }
    return endpoints;
  }

}