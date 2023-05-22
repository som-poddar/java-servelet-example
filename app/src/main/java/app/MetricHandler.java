package app;

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

public class MetricHandler implements HttpHandler {
    private static final String QUERY_PARAM_SEPERATOR = "&";
    private static final String NAMESPACE_JAVA_APP = "java_app";
    private static final String UTF_8 = "UTF-8";

    private static class LocalByteArray extends ThreadLocal<ByteArrayOutputStream> {
        @Override
        protected ByteArrayOutputStream initialValue() {
            return new ByteArrayOutputStream(1 << 20);
        }
    }

    private final static String HEALTHY_RESPONSE = "Exporter is Healthy.";

    private final CollectorRegistry registry;
    private final LocalByteArray response = new LocalByteArray();

    MetricHandler(CollectorRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getRawQuery();
        String contextPath = exchange.getHttpContext().getPath();

        ByteArrayOutputStream outPutStream = outputStream();

        writeToStream(query, contextPath, outPutStream);
        writeHeaders(exchange);

        gzipStream(exchange, outPutStream);

        exchange.close();
        System.out.println("Handled :" + contextPath);
    }

    private void gzipStream(HttpExchange exchange, ByteArrayOutputStream outPutStream) throws IOException {
        final GZIPOutputStream os = new GZIPOutputStream(exchange.getResponseBody());

        try {
            outPutStream.writeTo(os);
        } finally {
            os.close();
        }
    }

    private void writeHeaders(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", TextFormat.CONTENT_TYPE_004);
        exchange.getResponseHeaders().set("Content-Encoding", "gzip");
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
    }

    private void writeToStream(String query, String contextPath, ByteArrayOutputStream outPutStream)
            throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(outPutStream, Charset.forName(UTF_8));
        if ("/-/healthy".equals(contextPath)) {
            osw.write(HEALTHY_RESPONSE);
        } else {
            TextFormat.write004(osw, registry.filteredMetricFamilySamples(parseQuery(query)));
        }
        osw.close();
    }

    private ByteArrayOutputStream outputStream() {
        ByteArrayOutputStream response = this.response.get();
        response.reset();
        return response;
    }

    private static Set<String> parseQuery(String query) throws IOException {
        Set<String> names = new HashSet<String>();
        if (query != null) {
            String[] pairs = query.split(QUERY_PARAM_SEPERATOR);
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx != -1 && URLDecoder.decode(pair.substring(0, idx), UTF_8).equals("name[]")) {
                    names.add(URLDecoder.decode(pair.substring(idx + 1), UTF_8));
                }
            }
        }
        return names;
    }
}
