import org.apache.hc.core5.net.URIBuilder;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private ExecutorService pool;

    private Map<String, Map<String, Handler>> handlersMap;

    public Server() {
        pool = Executors.newFixedThreadPool(64);
        handlersMap = new ConcurrentHashMap<>();
    }

    public void addHandler(String type, String path, Handler handler) {
        if (!handlersMap.containsKey(type)) {
            Map<String, Handler> pathToHandlerMap = new ConcurrentHashMap<>();
            pathToHandlerMap.put(path, handler);
            handlersMap.put(type, pathToHandlerMap);
        } else {
            handlersMap.get(type).put(path, handler);
        }
    }

    public void listen(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                final Socket socket = serverSocket.accept();
                submitClient(socket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void submitClient(Socket socket) {

        pool.submit(() -> {
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); final BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {
                final String requestLine = in.readLine();
                final String[] parts = requestLine.split(" ");
                String type = parts[0];
                String path = parts[1];

                String pathWithoutParams = new URIBuilder(path).getPath();

                if (parts.length != 3) {
                    return;
                }

                Request request = new Request(type, path, socket.getInputStream());
                Handler handler = handlersMap.get(type).get(pathWithoutParams);
                if (handler == null) {
                    out.write(("HTTP/1.1 404 Not Found\r\n" + "Content-Length: 0\r\n" + "Connection: close\r\n" + "\r\n").getBytes());
                    out.flush();
                }
                handler.handle(request, out);

            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }
}