import io.vertx.ceylon.core {
  vertx,
  VertxOptions,
  Vertx
}
import ceylon.language.meta.declaration {
  Module
}
import ceylon.file { ... }
import java.lang { System }
shared void runModuleVerticle(Module mod, Boolean clustered = false) {

  // Determine current path
  Path path = mod.qualifiedName.split((ch) => ch == '.').fold(current.childPath("source"))((path, name) => path.childPath(name));
  value p = "``path.string``";
  System.setProperty("vertx.cwd", p);
  
  if (!clustered) {
    deployVerticle(vertx.vertx(), mod);
  } else {
    vertx.clusteredVertx(VertxOptions {
      clustered = true;
    }, (ar) {
      switch (ar)
      case (is Vertx) {
        deployVerticle(vertx.vertx(), mod);
      }
      else {
        ar.printStackTrace();
      }
    });
  }
}

void deployVerticle(Vertx vertx, Module mod) {
  vertx.deployVerticle("ceylon:``mod.name``/``mod.version``", (String|Throwable ar) {
    if (is String ar) {
      print("Deployed ``mod.name`` example");
    } else {
      print("``mod.name`` deploy failure:");
      ar.printStackTrace();
    }
  });
}