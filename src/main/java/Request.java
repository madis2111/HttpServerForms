//public class Main {
//    public static void main(String[] args){
//        final var server = new Server();
//        // код инициализации сервера (из вашего предыдущего ДЗ)
//
//        // добавление хендлеров (обработчиков)
//        server.addHandler("GET", "/messages", new Handler() {
//            public void handle(Request request, BufferedOutputStream responseStream) {
//                // TODO: handlers code
//            }
//        });
//        server.addHandler("POST", "/messages", new Handler() {
//            public void handle(Request request, BufferedOutputStream responseStream) {
//                // TODO: handlers code
//            }
//        });
//
//        server.listen(9999);
//    }
//}

import java.io.InputStream;

public class Request {

    private String type;
    private String head;
    private InputStream body;

    public Request(String type, String head, InputStream body) {
        this.type = type;
        this.head = head;
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public InputStream getBody() {
        return body;
    }

    public void setBody(InputStream body) {
        this.body = body;
    }
}