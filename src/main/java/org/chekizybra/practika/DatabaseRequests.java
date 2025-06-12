package org.chekizybra.practika;

import netscape.javascript.JSObject;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class DatabaseRequests {
    private static final String SUPABASE_URL = "https://clpazhgxqhodorvxfiac.supabase.co";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNscGF6aGd4cWhvZG9ydnhmaWFjIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0ODQ1MjI0NiwiZXhwIjoyMDY0MDI4MjQ2fQ.4OR8Y6yJD4Q83kZco079wI1-QQ5npM-gzrkZXNEIm8w";
    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public static String post(JSONObject jsonchek,String table) throws IOException {
        String json = jsonchek.toString();

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(SUPABASE_URL +"/rest/v1/"+ table +"?select=*")
                .post(body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=representation")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    public static String get(String table,String select) throws IOException {
        Request request = new Request.Builder()
                .url(SUPABASE_URL +"/rest/v1/" + table  + select)
                .get()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    public static String get(String select) throws IOException {
        Request request = new Request.Builder()
                .url(SUPABASE_URL +"/rest/v1/" + select)
                .get()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    public static String deletebyid(int id) throws IOException {
        Request request = new Request.Builder()
                .url(SUPABASE_URL + id + id)
                .delete()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Prefer", "return=representation")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
