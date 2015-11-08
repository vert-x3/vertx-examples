Vert.x JCA Examples
===

The Vert.x JCA Example project provides a JEE compliant application that
enables to you deploy the application into a [Wildfly](http://wildfly.org) application server. While simple in implementation, the example provides a good point of departure for your own development. 

Prerequisites
---
The Vert.x JCA example requires the installation of the Vert.x JCA adapter. Please
see [the README ](https://github.com/vert-x3/vertx-jca/blob/master/README.md) for installation details.

The Vert.x JCA example project assumes the use of the JEE compliant [Wildfly Application Server
](http://wildfly.org). If you haven't already done so, please download and install the application server. Once installed, you will need to set your JBOSS_HOME environment variable to point to the Wildfly installation you would like to use

`export JBOSS_HOME=[YOUR WILDFLY INSTALLATION]`

While you can start the Wildfly Application server directly from the example project, because the example project requires the use of the full application server, it is best to start it from the command line:

`cd $JBOSS_HOME/bin`
`./standalone.sh -c standalone-full.xml`

Note, we are starting the full Wildfly installation.

At this point, you are ready to build and deploy the Vert.x JCA example application.

Components
---
The Vert.x JCA example project consists of three Maven projects

* <b> ejb
	* A simple MDB listening on a configured Vert.x address</b>
* <b> war
	* A web application containing HTML based resources and JSP page to handling sending of Vert.x messages via the Vert.x JCA adapter</b>
* <b> ear
	* A JEE EAR archive containing the above components, as well as including the Vert.x JCA resource adapter</b>
  

Building The JEE Application
---
The Vert.x JCA example project is configured and built via [Apache Maven](http://maven.apache.org). Simply execute the following command

`mvn package wildfly:deploy`


This will package the JEE compliant EAR file (which will include the JCA adapter as an internal component) and deploy it to your running WildFly instance. 

The project uses the [Wildfly Maven Plugin](https://docs.jboss.org/wildfly/plugins/maven/latest/) which provides for fine grained control of the WildFly application server, as well as deploying the JEE artifact to running server.


Running The JEE Application
---
Once the JEE application has deployed, navigate using your preferred browser to

[localhost:8080/vertx-jca-examples-web](http://localhost:8080/vertx-jca-examples-web/)

If you deployed the application with no errors, the page will load. You are given options for the following:

* **Vert.x Connection Factory JNDI Name**
	* The JNDI name of the Connection Factory. The default is java:/eis/VertxConnectionFactory 

* **Address:**
	* The address on which the deployed MDB is listening. The default is inbound-address 

* **Message:**
	* The message you would like to send. The default is Hello, World!

Hitting the send button will send the message through the Vert.x EventBus. If there are no errors, the feedback status box will indicate as such with a simple OK response. 


Notes
---
Obviously the above is a trivial example, and certainly won't win any awards for style or substance. However, the messaging patterns used are quite typical for an enterprise application and while the application may not seem complex, it provides a working example of JEE JCA integration with an EIS (Vert.x in this case). The example can easily be expanded upon to include other Vert.x or JEE components as your requirements grown in scope and complexity.

As always, contributions and suggestions are always welcome. 

*Thank You!*

