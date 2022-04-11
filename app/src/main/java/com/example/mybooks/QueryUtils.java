package com.example.mybooks;

import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    public static ArrayList<Book> extractBooks(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpConnection(url);
        } catch (IOException e) {

        }

        ArrayList<Book> booklist = null;
        try {
            booklist = ExtractData(jsonResponse);
        } catch (IOException e) {

        }
        Log.v(LOG_TAG, "books extracted");
        return booklist;

    }

    public static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {

        }
        Log.v(LOG_TAG, "url created");
        return url;
    }

    public static String makeHttpConnection(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream input = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();


            if (urlConnection.getResponseCode() == 200) {
                input = urlConnection.getInputStream();
                jsonResponse = readInput(input);
            }
        } catch (IOException e) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (input != null) {
                input.close();
            }
        }
        Log.v(LOG_TAG, "connection made");
        return jsonResponse;

    }

    public static String readInput(InputStream input) throws IOException {
        StringBuilder build = new StringBuilder();
        InputStreamReader inputreader = new InputStreamReader(input, Charset.forName("UTF-8"));
        BufferedReader buff = new BufferedReader(inputreader);
        String line = buff.readLine();
        while (line != null) {
            build.append(line);
            line = buff.readLine();
        }
        Log.v(LOG_TAG, "read the input");
        return build.toString();

    }

    public static ArrayList<Book> ExtractData(String jsonResponse) throws IOException {

        ArrayList<Book> booklist = new ArrayList<>();
        InputStream input = null;
        if (jsonResponse.isEmpty()) {
            return null;
        }
        try {
            int ugi = 0;
            JSONObject obj = new JSONObject(jsonResponse);
            JSONArray items = obj.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {

                //get all the objects
                JSONObject info = items.getJSONObject(i);
                JSONObject vol = info.getJSONObject("volumeInfo");


                //get image of the book
                JSONObject image = new JSONObject();
                Drawable d = null;
                if (vol.has("imageLinks")) {

                    image = vol.getJSONObject("imageLinks");
                    URL url = createUrl(image.getString("thumbnail"));
                    input = (InputStream) url.getContent();
                    d = Drawable.createFromStream(input, "src");
                } else {
                    Log.v(LOG_TAG, "no image key value pair");
                }
                Log.v(LOG_TAG, "data extracted from json" + ugi++);


                //get title and authors name
                String title = null;
                String author = null;
                JSONArray authors = null;
                if (vol.has("title")) {

                    title = vol.getString("title");
                    authors = new JSONArray();
                }


                //if there is no key value pair of author then skip
                if (vol.has("authors")) {
                    authors = vol.getJSONArray("authors");
                    author = authors.getString(0);
                }

                String str = null;
                if (vol.has("canonicalVolumeLink")) {
                    str = vol.getString("canonicalVolumeLink");
                }
                booklist.add(new Book(title, author, d, str));

            }
            return booklist;
        } catch (JSONException e) {
            Log.v(LOG_TAG, "exception in json data");

        } catch (MalformedURLException e) {
            Log.v(LOG_TAG, "exception in json creating url");

        } catch (IOException e) {

        } finally {
            if (input != null) {
                input.close();
            }
        }
        return null;
    }

}
