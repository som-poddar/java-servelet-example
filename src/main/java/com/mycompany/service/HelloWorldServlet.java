package com.mycompany.service;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.prometheus.client.Counter;

public class HelloWorldServlet extends HttpServlet {
    static final String[] days = new String[] { "Mon", "Tue", "Wed", "Wed", "Fri", "Fri" };
    static final String[] months = new String[] { "Jan", "Feb", "Marc", "Apr" };

    static final Counter requests = Counter.build()
            .name("hello_worlds_total")
            .help("Number of hello worlds served.")
            .labelNames("day", "month").register();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("{ \"message\": \"hello world! \"}");

        // Increment the number of requests.
        requests.labels(getRandomDay(), getRandomMonth()).inc();
    }

    public static String getRandomDay() {
        int rnd = new Random().nextInt(days.length);
        return days[rnd];
    }

    public static String getRandomMonth() {
        int rnd = new Random().nextInt(months.length);
        return months[rnd];
    }
}
