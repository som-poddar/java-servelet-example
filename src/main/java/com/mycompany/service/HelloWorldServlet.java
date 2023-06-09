package com.mycompany.service;

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

public class HelloWorldServlet extends HttpServlet {
    static final Counter requests = Counter.build()
    .name("hello_worlds_total")
    .help("Number of hello worlds served.").register();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
    throws ServletException, IOException {                    
            resp.setContentType("application/json");
            
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("{ \"message\": \"hello world! \"}");

            // Increment the number of requests.
            requests.inc();
        }
    }
    