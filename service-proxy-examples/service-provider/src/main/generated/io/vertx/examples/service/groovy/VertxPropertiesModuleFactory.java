package io.vertx.examples.service.groovy;

import groovy.lang.MetaMethod;
import org.codehaus.groovy.runtime.m12n.ExtensionModule;
import org.codehaus.groovy.runtime.m12n.MetaInfExtensionModule;
import org.codehaus.groovy.runtime.m12n.PropertiesModuleFactory;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class VertxPropertiesModuleFactory extends PropertiesModuleFactory {

  @Override
  public ExtensionModule newModule(Properties properties, ClassLoader classLoader) {
    try {
      classLoader.loadClass("io.vertx.lang.groovy.GroovyVerticleFactory");
      return MetaInfExtensionModule.newModule(properties, classLoader);
    } catch (Throwable t) {
      String moduleName = properties.getProperty("moduleName");
      String moduleVersion = properties.getProperty("moduleVersion");
      return new ExtensionModule(moduleName, moduleVersion) {
        @Override
        public List<MetaMethod> getMetaMethods() {
          return Collections.emptyList();
        }
      };
    }
  }

}
