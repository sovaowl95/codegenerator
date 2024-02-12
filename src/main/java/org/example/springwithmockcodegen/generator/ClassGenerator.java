package org.example.springwithmockcodegen.generator;

import org.springframework.javapoet.ClassName;
import org.springframework.javapoet.FieldSpec;
import org.springframework.javapoet.JavaFile;
import org.springframework.javapoet.MethodSpec;
import org.springframework.javapoet.ParameterSpec;
import org.springframework.javapoet.TypeName;
import org.springframework.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class ClassGenerator {
  static void generate(String endpoint, Class<?> clazz, Method method) {
    final String packageName = "com.example";
    final String className = getNameMock(clazz.getSimpleName());

    final MethodSpec myMethod = generateMethod(endpoint, method);
    final TypeSpec myClass = generateClass(className, myMethod);
    generateJavaClassFile(packageName, myClass);
  }

  private static TypeSpec generateClass(String className, MethodSpec myMethod) {
    return TypeSpec.classBuilder(className)
                   .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                   .addField(getFieldSpec())
                   .addMethod(myMethod)
                   .build();
  }

  private static FieldSpec getFieldSpec() {
    final ClassName className = ClassName.get("org.example.springwithmockcodegen", "TestController");
    return FieldSpec.builder(className, "fieldName")
                    .build();
  }

  private static MethodSpec generateMethod(String endpoint, Method method) {
    final ParameterSpec dtoArgument = ParameterSpec.builder(method.getReturnType(), "dto").build();

    return MethodSpec.methodBuilder(getNameMock(method.getName()))
                     .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                     .addParameter(dtoArgument)
                     .returns(TypeName.VOID)
//                     .addStatement(getMethodBody(), dtoArgument)
                     .addStatement("fieldName.mock(dto); \n//" + endpoint + "")
                     .build();
  }

  private static String getMethodBody() {
    return "$T mockResult = mock().someMethod(argument)";
//    //language=java
//    return """
//        System.out.println(\"something\");
//        //%s
//        """.formatted("www.xxx.zzz://dns.com/" + endpoint);
  }

  private static String getNameMock(String methodName) {
    return "mock" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
  }

  private static void generateJavaClassFile(String packageName, TypeSpec myClass) {
    final JavaFile javaFile = JavaFile.builder(packageName, myClass)
                                      .build();
    try {
      javaFile.writeTo(new File("src"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
