import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URIBuilder;


import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {


        final Server server = new Server();
        server.addHandler("GET", "/classic.html", new Handler() {
              public void handle(Request request, BufferedOutputStream responseStream) {
                final Path filePath = Path.of(".", "public", "/classic.html");

                try {
                    final String mimeType = Files.probeContentType(filePath);

                    String textPath = ".public/classic.html";
                    Path pathObject = Path.of(textPath);
                    final String template = Files.readString(pathObject);

                    final byte[] content = template.replace("{time}", LocalDateTime.now().toString()

                    ).getBytes();
                    responseStream.write(("HTTP/1.1 200 OK\r\n" + "Content-Type: " + mimeType + "\r\n" + "Content-Length: " + content.length + "\r\n" + "Connection: close\r\n" + "\r\n").getBytes());
                    responseStream.write(content);
                    responseStream.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        server.addHandler("GET", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {


                try {
                    String textPath = request.getHead();


                    NameValuePair nameValue = new URIBuilder(textPath).getFirstQueryParam("page");
                    String value = nameValue.getValue();

                    Path pathObject = Path.of(".public/" + value);


                    final String mimeType = Files.probeContentType(pathObject);

                    final long length = Files.size(pathObject);
                    responseStream.write(("HTTP/1.1 200 OK\r\n" + "Content-Type: " + mimeType + "\r\n" + "Content-Length: " + length + "\r\n" + "Connection: close\r\n" + "\r\n").getBytes());
                    Files.copy(pathObject, responseStream);
                    responseStream.flush();
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

        server.listen(9999);
    }
}

