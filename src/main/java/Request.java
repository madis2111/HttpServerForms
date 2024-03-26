import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URIBuilder;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    private String type;
    private String head;
    private Map<String,String> params;
    private InputStream body;

    public Request(String type, String head, InputStream body) {
        this.type = type;
        this.head = head;
        this.body = body;
        this.params = new HashMap<>();

        try {
            URIBuilder uriBuilder = new URIBuilder(head);
            List<NameValuePair> pairs = uriBuilder.getQueryParams();
            for (NameValuePair pair : pairs) {
                params.put(pair.getName(), pair.getValue());
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getQueryParam(String paramName) {
        return params.get(paramName);
    }

    public Map<String,String> getQueryParams() {
        return params;
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