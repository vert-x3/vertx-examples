package io.vertx.example.util;

import io.vertx.codetrans.CodeTranslator;
import io.vertx.codetrans.GroovyLang;
import io.vertx.codetrans.JavaScriptLang;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.RubyLang;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.core.Verticle;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A processor plugin generate scripts from {@link io.vertx.core.Verticle} class. It scans all the compiled
 * classes and tries to generate corresponding scripts for each class.<p/>
 *
 * The script is named after the verticle fqn using the last atom of the package name and the lower
 * cased class name, for example : {@code examples.http.Server} maps to {@code http/server.js},
 * {@code http/server.groovy}, etc...<p/>
 *
 * The processor is only active when the option {@code codetrans.output} is set to a valid directory where the scripts
 * will be written. A log <i>codetrans.log</i> will also be written with the processor activity.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CodeTransProcessor extends AbstractProcessor {

  private File outputDir;
  private CodeTranslator translator;
  private List<Lang> langs;
  private Set<File> folders = new HashSet<>(); // The copied folders so we don't do the job twice
  private PrintWriter log;

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
          if(!Files.exists(targetPath)){
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
          String fileName = typeElt.getSimpleName().toString().toLowerCase();
          for (Lang lang : langs) {
            String folderPath = processingEnv.getElementUtils().getPackageOf(typeElt).getQualifiedName().toString().replace('.', '/');
            File dstFolder = new File(new File(outputDir, lang.getExtension()), folderPath);
            if (dstFolder.exists() || dstFolder.mkdirs()) {
              try {
                String translation = translator.translate(methodElt, lang);
                File f = new File(dstFolder, fileName + "." + lang.getExtension());
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
        e.printStackTrace();;
      }
      return true;
    } else {
      return false;
    }
  }
}
