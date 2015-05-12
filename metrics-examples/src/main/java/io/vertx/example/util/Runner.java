package io.vertx.example.util;

import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Runner {

  public static final VertxOptions DROPWIZARD_OPTIONS = new VertxOptions().
      setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true));

  private static final String METRICS_EXAMPLES_DIR = "metrics-examples";
  private static final String METRICS_EXAMPLES_JAVA_DIR = METRICS_EXAMPLES_DIR + "/src/main/java/";
  private static final String METRICS_EXAMPLES_JS_DIR = METRICS_EXAMPLES_DIR + "/src/main/js/";
  private static final String METRICS_EXAMPLES_GROOVY_DIR = METRICS_EXAMPLES_DIR + "/src/main/groovy/";
  private static final String METRICS_EXAMPLES_RUBY_DIR = METRICS_EXAMPLES_DIR + "/src/main/rb/";

  public static void runClusteredExample(Class clazz) {
    ExampleRunner.runJavaExample(METRICS_EXAMPLES_JAVA_DIR, clazz, true);
  }

  public static void runExample(Class clazz) {
    ExampleRunner.runJavaExample(METRICS_EXAMPLES_JAVA_DIR, clazz, false);
  }

  // JavaScript examples

  public static void runJSExample(String scriptName) {
    ExampleRunner.runScriptExample(METRICS_EXAMPLES_JS_DIR, scriptName, DROPWIZARD_OPTIONS);
  }

  static class JSMetricsDashboardRunner {
    public static void main(String[] args) {
      Runner.runJSExample("io/vertx/example/metrics/dashboard/dashboard.js");
    }
  }

  // Groovy examples

  public static void runGroovyExample(String scriptName) {
    ExampleRunner.runScriptExample(METRICS_EXAMPLES_GROOVY_DIR, scriptName, DROPWIZARD_OPTIONS);
  }

  static class GroovyMetricsDashboardRunner {
    public static void main(String[] args) {
      Runner.runGroovyExample("io/vertx/example/metrics/dashboard/dashboard.groovy");
    }
  }

  // Ruby examples

  public static void runRubyExample(String scriptName) {
    ExampleRunner.runScriptExample(METRICS_EXAMPLES_RUBY_DIR, scriptName, DROPWIZARD_OPTIONS);
  }

  static class RubyMetricsDashboardRunner {
    public static void main(String[] args) {
      Runner.runRubyExample("io/vertx/example/metrics/dashboard/dashboard.rb");
    }
  }
}
