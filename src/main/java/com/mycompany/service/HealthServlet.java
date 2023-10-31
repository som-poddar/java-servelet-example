package com.mycompany.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthServlet extends HttpServlet {
        // private static final Logger logger = LoggerFactory.getLogger(App.class);
        private static final Logger logger = LoggerFactory.getLogger("jsonLogger");

        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
                        throws ServletException, IOException {

                resp.getWriter().println("Ok");
                resp.setContentType("application/json");

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println("{ \"status\": \"ok\"}");
                // System.out.println("print this line to console");
                logger.info("health-check complete");
        }
}
