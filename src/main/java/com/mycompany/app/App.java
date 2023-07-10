package com.mycompany.app;

import io.prometheus.client.Counter;
import io.prometheus.client.exporter.MetricsServlet;
import io.prometheus.client.hotspot.DefaultExports;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import java.io.IOException;

// import org.apache.log4j.LogManager;
// import org.apache.log4j.Logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycompany.service.HealthServlet;
import com.mycompany.service.HelloWorldServlet;

public class App {
    public static void main( String[] args ) throws Exception {
      Logger logger = LogManager.getLogger("CONSOLE_JSON_APPENDER");
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
