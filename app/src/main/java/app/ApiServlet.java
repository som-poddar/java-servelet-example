package app;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;

public class ApiServlet extends HttpServlet {
    private static final String NAMESPACE_JAVA_APP = "servlet_example";
    private Counter counter;
    private Gauge gauge;
    private Histogram histogram;
    private Summary summary;

    public void init() {
        System.out.println("Hello from Servlet on init");
        CollectorRegistry registry = CollectorRegistry.defaultRegistry;
        counter = counter(registry);
        gauge = guage(registry);
        histogram = histogram();
        summary = summary(registry);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        populateMetric(this.counter, this.gauge, this.histogram, this.summary);

        response.getWriter().println("{ \"status\": \"ok (populate Metric)\"}");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
    }

    private static void populateMetric(Counter ctr, Gauge gauge, Histogram histogram, Summary summary) {
        ctr.inc(rand(0, 15));
        gauge.set(rand(-5, 10));
        histogram.observe(rand(0, 5));
        summary.observe(rand(0, 5));
        System.out.println("populateMetric done.");
    }

    private static Summary summary(CollectorRegistry registry) {
        return Summary.build()
                .namespace(NAMESPACE_JAVA_APP)
                .name("s")
                .help("s summary")
                .register(registry);
    }

    private static Histogram histogram() {
        return Histogram.build()
                .namespace(NAMESPACE_JAVA_APP)
                .name("h")
                .help("h help")
                .register();
    }

    private static Gauge guage(CollectorRegistry registry) {
        return Gauge.build()
                .namespace("java")
                .name("g")
                .help("g healp")
                .register(registry);
    }

    private static Counter counter(CollectorRegistry registry) {
        return Counter.build()
                .namespace(NAMESPACE_JAVA_APP)
                .name("a")
                .help("a help")
                .register(registry);
    }

    private static double rand(double min, double max) {
        return min + (Math.random() * (max - min));
    }
}
