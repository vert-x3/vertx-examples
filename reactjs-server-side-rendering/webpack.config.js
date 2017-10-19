var _package = require('./package.json');
var fs = require('fs');
var path = require('path');
var webpack = require('webpack');

var javaDependencies = _package.javaDependencies || {};

if ('install' === process.env.npm_lifecycle_event) {
  // generate pom.xml file
  var pom =
    '<?xml version="1.0" encoding="UTF-8"?>\n' +
    '<project xmlns="http://maven.apache.org/POM/4.0.0"\n' +
    '         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n' +
    '         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">\n' +
    '\n' +
    '  <modelVersion>4.0.0</modelVersion>\n' +
    '  <packaging>pom</packaging>\n' +
    '\n' +
    '  <groupId>' + _package.name + '</groupId>\n' +
    '  <artifactId>' + _package.name + '</artifactId>\n' +
    '  <version>' + _package.version + '</version>\n' +
    '\n' +
    '  <name>' + _package.name + '</name>\n' +
    '  <description>' + (_package.description || '') + '</description>\n' +
    '\n' +
    '  <dependencies>\n';

  for (dep in javaDependencies) {
    if (javaDependencies.hasOwnProperty(dep)) {
      pom +=
        '    <dependency>\n' +
        '      <groupId>' + dep.split(':')[0] + '</groupId>\n' +
        '      <artifactId>' + dep.split(':')[1] + '</artifactId>\n' +
        '      <version>' + javaDependencies[dep] + '</version>\n' +
        '    </dependency>\n';
    }
  }

  pom +=
    '  </dependencies>\n' +
    '  <build>\n' +
    '    <plugins>\n' +
    '      <plugin>\n' +
    '        <groupId>org.apache.maven.plugins</groupId>\n' +
    '        <artifactId>maven-dependency-plugin</artifactId>\n' +
    '        <version>2.10</version>\n' +
    '        <executions>\n' +
    '          <execution>\n' +
    '            <id>unpack-dependencies</id>\n' +
    '            <phase>package</phase>\n' +
    '            <goals>\n' +
    '              <goal>unpack-dependencies</goal>\n' +
    '            </goals>\n' +
    '            <configuration>\n' +
    '              <includes>**/*.js</includes>\n' +
    '              <outputDirectory>${project.basedir}/../node_modules</outputDirectory>\n' +
    '              <overWriteReleases>false</overWriteReleases>\n' +
    '              <overWriteSnapshots>true</overWriteSnapshots>\n' +
    '            </configuration>\n' +
    '          </execution>\n' +
    '        </executions>\n' +
    '      </plugin>\n' +
    '      <plugin>\n' +
    '        <groupId>org.apache.maven.plugins</groupId>\n' +
    '        <artifactId>maven-shade-plugin</artifactId>\n' +
    '        <version>2.3</version>\n' +
    '        <executions>\n' +
    '          <execution>\n' +
    '            <phase>package</phase>\n' +
    '            <goals>\n' +
    '              <goal>shade</goal>\n' +
    '            </goals>\n' +
    '            <configuration>\n' +
    '              <transformers>\n' +
    '                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">\n' +
    '                  <manifestEntries>\n' +
    '                    <Main-Class>io.vertx.core.Launcher</Main-Class>\n' +
    '                    <Main-Verticle>' + _package.mainVerticle + '</Main-Verticle>\n' +
    '                  </manifestEntries>\n' +
    '                </transformer>\n' +
    '                <transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">\n' +
    '                  <resource>META-INF/services/io.vertx.core.spi.VerticleFactory</resource>\n' +
    '                </transformer>\n' +
    '              </transformers>\n' +
    '              <outputFile>${project.basedir}/../run.jar</outputFile>\n' +
    '            </configuration>\n' +
    '          </execution>\n' +
    '        </executions>\n' +
    '      </plugin>\n' +
    '    </plugins>\n' +
    '  </build>\n' +
    '</project>\n';

  // mkdir -p .vertx
  fs.mkdir(path.resolve(__dirname, '.vertx'), function (err) {
    if (!err || (err && err.code === 'EEXIST')) {
      // generate pom.xml
      fs.writeFile(path.resolve(__dirname, '.vertx/pom.xml'), pom, function (err) {
        if (err) {
          console.error(err);
          process.exit(1);
        }
      });
    } else {
      if (err) {
        console.error(err);
        process.exit(1);
      }
    }
  });
}

// exclude vert.x modules
var vertxModules = [
  function (context, request, callback) {
    if (/^vertx-js\//.test(request)) {
      return callback(null, 'commonjs ' + request);
    }
    callback();
  }
];

for (dep in javaDependencies) {
  if (javaDependencies.hasOwnProperty(dep)) {
    var mavenDep = dep.split(':');
    // exclude the meta-package
    if (mavenDep[1] !== 'vertx-lang-js') {
      vertxModules.push(function (context, request, callback) {
        if (new RegExp('^' + mavenDep[1] + '-js/').test(request)) {
          return callback(null, 'commonjs ' + request);
        }
        callback();
      });
    }
  }
}

var vertxConfig = {

  entry: path.resolve(__dirname, 'src/server/index.js'),

  output: {
    filename: _package.mainVerticle
  },

  externals: vertxModules,

  module: {
    loaders: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loader: 'babel-loader',
        query: {
          presets: ['es2015', 'react']
        }
      }
    ]
  }
};

var webConfig = {

  entry: path.resolve(__dirname, 'src/client/index.js'),

  devtool: 'source-map',

  output: {
    filename: 'webroot/bundle.js'
  },

  module: {
    loaders: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loader: 'babel-loader',
        query: {
          presets: ['es2015', 'react']
        }
      }
    ]
  }
};


module.exports = [vertxConfig, webConfig];
