package app;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ApiHandler implements HttpHandler {
    private final static String HEALTHY_RESPONSE = "Exporter is Healthy.";
    private static final String UTF_8 = "UTF-8";
    private final LocalByteArray response = new LocalByteArray();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getRawQuery();
        String contextPath = exchange.getHttpContext().getPath();

        ByteArrayOutputStream outPutStream = outputStream();
        OutputStreamWriter osw = new OutputStreamWriter(outPutStream, Charset.forName(UTF_8));
        osw.write(HEALTHY_RESPONSE);
        
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        exchange.getResponseHeaders().set("Content-Type", "application/json");

        osw.close();
    }
    
    private ByteArrayOutputStream outputStream() {
        ByteArrayOutputStream response = this.response.get();
        response.reset();
        return response;
    }
}
