package Xigua.Getapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Getapi {
    public static String get(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while((inputLine = in.readLine())!= null) {
            response.append(inputLine);
            response.append("\n");
        }

        in.close();

        return response.toString();
    }

    public static String readContent(final String stringURL) throws IOException {
        final HttpURLConnection httpConnection = (HttpURLConnection) new URL(stringURL).openConnection();
        httpConnection.setConnectTimeout(10000);
        httpConnection.setReadTimeout(10000);
        httpConnection.setRequestMethod("GET");
        httpConnection.setRequestProperty("User-Agent", "FUCKEDGE");
        HttpURLConnection.setFollowRedirects(true);
        httpConnection.setDoOutput(true);
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), StandardCharsets.UTF_8));
        final StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}
