package com.mycompany.app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.service.HealthServlet;
import com.mycompany.service.HelloWorldServlet;

import io.prometheus.client.exporter.MetricsServlet;
import io.prometheus.client.hotspot.DefaultExports;

public class App {
  // private static final Logger logger = LoggerFactory.getLogger(App.class);
  // private static Logger logger = (Logger) LoggerFactory
  // .getLogger(App.class);

  public static void main(String[] args) throws Exception {
    // Logger logger = LogManager.getLogger("CONSOLE_JSON_APPENDER");
    Logger logger = LoggerFactory.getLogger("jsonLogger");
    logger.info("Server Starting ...");
    logger.debug("another degug statement");

    Server server = new Server(1234);
    ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/");
    server.setHandler(context);

    // methods for api consumer
    context.addServlet(new ServletHolder(new HelloWorldServlet()), "/message");
    context.addServlet(new ServletHolder(new HealthServlet()), "/health");

    // methods for prom scraper
    context.addServlet(new ServletHolder(new MetricsServlet()), "/metrics");

    // Add metrics about CPU, JVM memory etc.
    DefaultExports.initialize();

    // Start the webserver.
    logger.debug("Server starting");
    server.start();
    server.join();
    logger.debug("Server Ready");
  }
}
