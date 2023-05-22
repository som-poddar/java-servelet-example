package app;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.common.TextFormat;

public class ApiServlet extends HttpServlet {
    private static final String NAMESPACE_JAVA_APP="servlet_example";
    // private final CollectorRegistry registry;
    private Counter counter;

    public void init() {
        System.out.println("Hello from Servlet on init");
        CollectorRegistry registry = CollectorRegistry.defaultRegistry;
        counter = counter(registry);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                    populateMetric(this.counter);
                    response.getWriter().println("{ \"status\": \"ok (populateMetric)\"}");
                    response.setStatus(HttpServletResponse.SC_OK);

                /*
                catch (InterruptedException e) {
                    e.printStackTrace();
                    response.getWriter().println("{ \"status\": \"not ok \"}");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }*/
        response.setContentType("application/json");
    }

        private static void populateMetric(Counter ctr) {
        // Gauge gauge = guage(registry);
        // Histogram histogram = histogram();
        // Summary summary = summary(registry);
                    ctr.inc(rand(0,15));
                    // gauge.set(rand(-5, 10));
                    // histogram.observe(rand(0, 5));
                    // summary.observe(rand(0, 5));
                            System.out.println("counter incremented.");
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
