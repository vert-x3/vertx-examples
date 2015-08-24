package io.vertx.example.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vertx.codegen.Case;
import io.vertx.codetrans.CodeTranslator;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.lang.groovy.GroovyLang;
import io.vertx.codetrans.lang.js.JavaScriptLang;
import io.vertx.codetrans.lang.ruby.RubyLang;
import io.vertx.core.Verticle;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * A processor plugin generate scripts from {@link io.vertx.core.Verticle} class. It scans all the compiled
 * classes and tries to generate corresponding scripts for each class.<p/>
 * <p>
 * The script is named after the verticle fqn using the last atom of the package name and the lower
 * cased class name, for example : {@code examples.http.Server} maps to {@code http/server.js},
 * {@code http/server.groovy}, etc...<p/>
 * <p>
 * The processor is only active when the option {@code codetrans.output} is set to a valid directory where the scripts
 * will be written. A log <i>codetrans.log</i> will also be written with the processor activity.
 * <p>
 * The processor can be configured using the {@code condetrans.config} property targeting a JSON file. The JSON file
 * contains a set of exclusions and is structured as follows:
 * <p>
 * <code><pre>
 *     {
 *       "excludes": [
 *        {
 *          "package" : "the (java) package to exclude",
 *          "langs" : ["lang1", "lang2"]
 *        }
 *       ]
 *     }
 * </pre></code>
 * <p>
 * The {@code package} element is mandatory. {@code Langs} is optional. When not set, all languages are skipped.
 * Languages are identified by their <em>extensions</em>.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 * @author <a href="mailto:clement@apache.org">Clement Escoffier</a>
 */
public class CodeTransProcessor extends AbstractProcessor {

  private File outputDir;
  private CodeTranslator translator;
  private List<Lang> langs;
  private Set<File> folders = new HashSet<>(); // The copied folders so we don't do the job twice
  private PrintWriter log;
  private ObjectNode config;

  @Override
  public Set<String> getSupportedOptions() {
    return Collections.singleton("codetrans.output");
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton("*");
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    String outputOption = processingEnv.getOptions().get("codetrans.output");
    if (outputOption != null) {
      outputDir = new File(outputOption);
    }
    translator = new CodeTranslator(processingEnv);
    langs = Arrays.asList(new JavaScriptLang(), new GroovyLang(), new RubyLang());

    String configFile = processingEnv.getOptions().get("codetrans.config");
    if (configFile != null) {
      ObjectMapper mapper = new ObjectMapper()
          .enable(JsonParser.Feature.ALLOW_COMMENTS)
          .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
      File file = new File(configFile);
      try {
        config = (ObjectNode) mapper.readTree(file);
      } catch (IOException e) {
        System.err.println("[ERROR] Cannot read configuration file " + file.getAbsolutePath() + " : " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  private PrintWriter getLogger() throws Exception {
    if (log == null) {
      log = new PrintWriter(new FileWriter(new File(outputDir, "codetrans.log"), false), true);
    }
    return log;
  }

  private void copyDirRec(File srcFolder, File dstFolder, PrintWriter log) throws Exception {
    if (!folders.contains(dstFolder)) {
      folders.add(dstFolder);
      Path srcPath = srcFolder.toPath();
      Path dstPath = dstFolder.toPath();
      SimpleFileVisitor<Path> copyingVisitor = new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
          Path targetPath = dstPath.resolve(srcPath.relativize(dir));
          if (!Files.exists(targetPath)) {
            log.println("Creating dir " + targetPath);
            Files.createDirectory(targetPath);
          }
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path srcFile, BasicFileAttributes attrs) throws IOException {
          if (!srcFile.getFileName().toString().endsWith(".java")) {
            log.println("Copying resource " + srcFile + " to " + dstPath);
            Path dstFile = dstPath.resolve(srcPath.relativize(srcFile));
            Files.copy(srcFile, dstFile, StandardCopyOption.REPLACE_EXISTING);
          }
          return FileVisitResult.CONTINUE;
        }
      };
      Files.walkFileTree(srcPath, copyingVisitor);
    }
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      if (log != null) {
        log.close();
      }
      return false;
    }
    if (outputDir != null && (outputDir.exists() || outputDir.mkdirs())) {
      List<ExecutableElement> methodElts = new ArrayList<>();
      try {
        PrintWriter log = getLogger();

        // Process all verticles automatically
        TypeMirror verticleType = processingEnv.getElementUtils().getTypeElement(Verticle.class.getName()).asType();
        for (Element rootElt : roundEnv.getRootElements()) {
          Set<Modifier> modifiers = rootElt.getModifiers();
          if (rootElt.getKind() == ElementKind.CLASS &&
              !modifiers.contains(Modifier.ABSTRACT) &&
              modifiers.contains(Modifier.PUBLIC) &&
              processingEnv.getTypeUtils().isSubtype(rootElt.asType(), verticleType)) {
            TypeElement typeElt = (TypeElement) rootElt;
            for (Element enclosedElt : typeElt.getEnclosedElements()) {
              if (enclosedElt.getKind() == ElementKind.METHOD) {
                ExecutableElement methodElt = (ExecutableElement) enclosedElt;
                if (methodElt.getSimpleName().toString().equals("start") && methodElt.getParameters().isEmpty()) {
                  methodElts.add(methodElt);
                }
              }
            }
          }
        }

        // Process CodeTranslate annotations
        roundEnv.getElementsAnnotatedWith(CodeTranslate.class).forEach(annotatedElt -> {
          methodElts.add((ExecutableElement) annotatedElt);
        });

        // Generate
        for (ExecutableElement methodElt : methodElts) {
          TypeElement typeElt = (TypeElement) methodElt.getEnclosingElement();
          FileObject obj = processingEnv.getFiler().getResource(StandardLocation.SOURCE_PATH, "", typeElt.getQualifiedName().toString().replace('.', '/') + ".java");
          File srcFolder = new File(obj.toUri()).getParentFile();
          String filename = Case.SNAKE.format(Case.CAMEL.parse(typeElt.getSimpleName().toString()));
          for (Lang lang : langs) {
            if (isSkipped(typeElt, lang)) {
              log.write("Skipping " + lang.getExtension() + " translation for " + typeElt.getQualifiedName() + "#" +
                  methodElt.getSimpleName());
              continue;
            }
            String folderPath = processingEnv.getElementUtils().getPackageOf(typeElt).getQualifiedName().toString().replace('.', '/');
            File dstFolder = new File(new File(outputDir, lang.getExtension()), folderPath);
            if (dstFolder.exists() || dstFolder.mkdirs()) {
              try {
                String translation = translator.translate(methodElt, lang);
                File f = new File(dstFolder, filename + "." + lang.getExtension());
                Files.write(f.toPath(), translation.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                log.println("Generated " + f.getAbsolutePath());
                copyDirRec(srcFolder, dstFolder, log);
              } catch (Exception e) {
                log.println("Skipping generation of " + typeElt.getQualifiedName());
                e.printStackTrace(log);
              }
            }
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   * Checks whether the generation of the given class to the given lang is explicitly excluded. Exclusions are
   * managed in the configuration file. If no configuration file are provided, the translation is not skipped.
   *
   * @param type the type
   * @param lang the language
   * @return {@code true} if the translation is skipped, {@code false} otherwise.
   */
  private boolean isSkipped(TypeElement type, Lang lang) {
    if (config == null) {
      // no config, no exclusions
      return false;
    }
    ArrayNode excludes = (ArrayNode) config.get("excludes");
    for (JsonNode exclude : excludes) {
      // Structure:
      // {
      //   "package": "the package to exclude", (mandatory)
      //   "langs": ["lang 1", "lang 2"]
      // }
      // If not langs - skip all languages
      String pck = exclude.get("package").asText();
      ArrayNode langs = (ArrayNode) exclude.get("langs");
      if (type.getQualifiedName().toString().startsWith(pck) && isLanguageSkipped(langs, lang)) {
        return true;
      }
    }
    return false;
  }

  private boolean isLanguageSkipped(ArrayNode langs, Lang lang) {
    if (langs == null) {
      // If not langs, exclude all.
      return true;
    }
    for (JsonNode node : langs) {
      if (node.asText().equalsIgnoreCase(lang.getExtension())) {
        return true;
      }
    }
    return false;
  }
}
