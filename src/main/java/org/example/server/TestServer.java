package org.example.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestServer {
    public static void main(String[] args) throws IOException {
        // Create an HTTP server listening on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Serve the HTML interface
        server.createContext("/", exchange -> {
            String response = Files.readString(Path.of("src/main/resources/index.html"));
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        // Handle API requests
        server.createContext("/api/square", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String[] params = query.split("=");
            if (params.length == 2 && "number".equals(params[0])) {
                try {
                    int number = Integer.parseInt(params[1]);
                    int square = number * number;
                    String response = String.format("$ %d^2 = %d $", number, square);
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } catch (NumberFormatException e) {
                    exchange.sendResponseHeaders(400, 0);
                    exchange.getResponseBody().close();
                }
            } else {
                exchange.sendResponseHeaders(400, 0);
                exchange.getResponseBody().close();
            }
        });

        // Start the server
        server.setExecutor(null); // Use the default executor
        server.start();
        System.out.println("Server started on http://localhost:8080");
    }
}
