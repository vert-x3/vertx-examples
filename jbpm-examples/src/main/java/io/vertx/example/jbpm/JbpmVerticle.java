package io.vertx.example.jbpm;

import io.vertx.core.AbstractVerticle;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JbpmVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(JbpmVerticle.class);

  @Override
  public void start() {
    LOGGER.info("Starting JbpmVerticle");

    /* Starting KieContainer and invoking a process */
    KieContainer container = KieServices.Factory.get().getKieClasspathContainer();
    KieSession ksession = container.newKieSession("sampleSession");
    WorkflowProcessInstance p = (WorkflowProcessInstance) ksession.startProcess("com.sample.bpmn.hello");
    LOGGER.info("Generated Process ID: " + p.getId());
  }


}
