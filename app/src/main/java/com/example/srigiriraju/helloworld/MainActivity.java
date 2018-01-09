package com.example.srigiriraju.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public TextView mTextView = (TextView) findViewById(R.id.text);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getMainContent();
    }

    public void getMainContent() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://localhost:5000/content/test-uuid";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mTextView.setText("Response is : " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTextView.setText("Error");
                    }
                });
        queue.add(stringRequest);
    }
}