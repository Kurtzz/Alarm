package pl.edu.agh.io.alarm.gcm;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class RestCommunication {

    private final String endpointURL;

    public RestCommunication(String endpointURL) {
        this.endpointURL = endpointURL;
    }

    public ConnectionResponse execute(Map<String, String> body, String method) throws IOException {
        HttpURLConnection connection = sendRequest(body, method);
        int responseCode = connection.getResponseCode();
        return new ConnectionResponse(responseCode, connection.getInputStream());
    }

    @NonNull
    private HttpURLConnection sendRequest(Map<String, String> body, String method) throws IOException {
        URL serverUrl = new URL(endpointURL);
        JSONObject bodyObject = new JSONObject(body);
        String requestBody = bodyObject.toString();

        HttpURLConnection connection = getConnection(method, serverUrl);

        connection.getOutputStream().write(requestBody.getBytes());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();
        return connection;
    }

    @NonNull
    private HttpURLConnection getConnection(String method, URL serverUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        return connection;
    }

    public static class ConnectionResponse {
        private final int status;
        private final InputStream responseStream;

        public ConnectionResponse(int status, InputStream responseStream) {
            this.status = status;
            this.responseStream = responseStream;
        }

        public int getStatus() {
            return status;
        }

        public InputStream getResponseStream() {
            return responseStream;
        }
    }
}
