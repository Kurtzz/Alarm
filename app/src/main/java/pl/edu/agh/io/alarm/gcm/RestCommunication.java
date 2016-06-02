package pl.edu.agh.io.alarm.gcm;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

        Log.i("REST", "Request: " + requestBody + ", URL: " + endpointURL);

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
        private final String response;
        public ConnectionResponse(int status, InputStream responseStream) throws IOException {
            this.status = status;

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int b;
            while((b=responseStream.read()) != -1) {
                bos.write(b);
            }
            this.response = new String(bos.toByteArray());
            responseStream.close();
        }

        public int getStatus() throws IOException {
            return status;
        }

        public String getResponseAsString() throws IOException {
            return response;
        }
    }
}
