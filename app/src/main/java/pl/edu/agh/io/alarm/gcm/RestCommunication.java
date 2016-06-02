package pl.edu.agh.io.alarm.gcm;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

public class RestCommunication {

    private final String endpointURL;

    public RestCommunication(String endpointURL) {
        this.endpointURL = endpointURL;
    }

    public int execute(Map<String, String> body, String method) throws IOException {
        URL serverUrl = new URL(endpointURL);
        JSONObject bodyObject = new JSONObject(body);
        String requestBody = bodyObject.toString();
        HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");

        connection.getOutputStream().write(requestBody.getBytes());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();
        int responseCode = connection.getResponseCode();
        connection.getInputStream();
        return responseCode;
    }
}
