import java.io.BufferedOutputStream;

@FunctionalInterface
public interface Handler {      // обработчик (запроса request)

    void handle(Request request, BufferedOutputStream bufferedOutputStream);

}
