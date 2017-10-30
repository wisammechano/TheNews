package com.recoded.thenews;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wisam on Oct 28 17.
 */

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final String API_KEY = "api-key=test";
    private static final String API_BASE = "https://content.guardianapis.com/";
    private static final String E_CONTENT = "search?page-size=50&show-fields=byline,thumbnail&";
    private static final String E_TAGS = "tags?";
    private static final String E_SECTIONS = "sections?";
    private static final String E_EDITIONS = "editions?";

    public static List<News> parseContent(String data) {
        List<News> newsList = new ArrayList<>();
        try {
            JSONArray articles = (new JSONObject(data).getJSONObject("response").getJSONArray("results"));
            JSONObject item, fields;
            News news;
            for (int i = 0; i < articles.length(); i++) {
                item = articles.getJSONObject(i);
                fields = item.getJSONObject("fields");
                if (fields.has("byline")) {
                    news = new News(item.getString("webTitle"), fields.getString("byline"), item.getString("webUrl"));
                } else {
                    news = new News(item.getString("webTitle"), "No Author", item.getString("webUrl"));
                }
                news.setPublicationDate(item.getString("webPublicationDate"));
                news.setSectionName(item.getString("sectionName"));
                newsList.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }

    public enum Ends {CONTENT, TAGS, SECTIONS, EDITIONS, BASE}

    ;

    public static String getUrl(Ends id) {
        String url = API_BASE;
        switch (id) {
            case CONTENT:
                url += E_CONTENT;
                break;
            case TAGS:
                url += E_TAGS;
                break;
            case SECTIONS:
                url += E_SECTIONS;
                break;
            case EDITIONS:
                url += E_EDITIONS;
                break;
            case BASE:
                return url;
        }
        url += API_KEY;
        return url;
    }

    public static String fetch(String mUrl) {
        URL url = createUrl(mUrl);

        String response = "";

        try {
            response = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        return response;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static URL createUrl(String mUrl) {
        URL url = null;
        try {
            url = new URL(mUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
}
