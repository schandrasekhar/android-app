package com.example.srigiriraju.helloworld;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srigiriraju on 1/12/18.
 */

public class Main2Activity extends AppCompatActivity {
    final String Tag = "TimelineActivity";
    public RequestQueue requestQueue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getMainContent();
    }

    public void getMainContent() {
        RequestQueue queue = getRequestQueue();
        //final String url = "http://172.142.5.128:5000/content/timeline-uuid";
        final String url = "http://192.168.0.12:5000/content/timeline-uuid";
        final List<Object> listObjects = new ArrayList<Object>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                listObjects.add(jsonObject);
                                updateTimelineActivityView(listObjects);
                            } catch (JSONException e) {
                                String errMsg = e.getMessage();
                                if (errMsg == null || errMsg.isEmpty()) {
                                    errMsg = "Error while parsing JSON";
                                }
                                Log.e(Tag, errMsg);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public  void onErrorResponse(VolleyError error) {
                String errMsg = error.getMessage();
                if (errMsg == null || errMsg.isEmpty()) {
                    errMsg = "Error while getting data for url: " + url;
                }
                Log.e(Tag, errMsg);
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void updateTimelineActivityView(List contentArr) {
        for (int i = 0; i < contentArr.size(); i++) {
            Object content = contentArr.get(i);
            JSONObject obj = (JSONObject) content;
            renderMainActivity(obj);
        }
    }

    public void renderMainActivity(JSONObject content) {
        try {
            String url = (String) content.get("imageUrl");
            String event = (String) content.get("event");
            String headline = (String) content.get("headline");
            String provider = (String) content.get("provider");
            Long publishedDate = (Long) content.get("publishedDate");
            Integer views = (Integer) content.get("views");
            String body = (String) content.get("body");

            //todo should check this >> https://developer.android.com/guide/topics/ui/layout/listview.html#Example
            //todo should check this >> https://developer.android.com/training/material/lists-cards.html#RecyclerView
        } catch (JSONException e) {

        }
    }

    public void renderImageFromUrl(String url) {
        NetworkImageView networkImageView = findViewById(R.id.main_activity_image);
        ImageLoader imageLoader = getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                //to something
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                //to something
            }
        });
        networkImageView.setImageUrl(url, imageLoader);
    }

    public void renderEventName(String event) {
        TextView textView = findViewById(R.id.main_activity_event);
        textView.setText(event);
    }

    public void renderProviderName(String provider) {
        TextView textView = findViewById(R.id.main_activity_provider);
        textView.setText(provider);
    }

    public void renderPublishedDate(Long publishedDate) {
        TextView textView = findViewById(R.id.main_activity_publishedDate);
        textView.setText(publishedDate.toString());
    }

    public void renderPublishedViews(Integer views) {
        TextView textView = findViewById(R.id.main_activity_views);
        String viewStr = views.toString() + " viewing";
        textView.setText(viewStr);
    }

    public void renderPublishedBody(String body) {
        TextView textView = findViewById(R.id.main_activity_body);
        textView.setText(body);
    }

    public void renderHeadline(String headline) {
        TextView textView = findViewById(R.id.main_activity_headline);
        textView.setText(headline);
    }

    public ImageLoader getImageLoader() {
        RequestQueue queue = getRequestQueue();
        ImageLoader imageLoader = new ImageLoader(queue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);
                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
        return imageLoader;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }


}
