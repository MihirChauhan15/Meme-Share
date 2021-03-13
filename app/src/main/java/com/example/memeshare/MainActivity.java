package com.example.memeshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;

import static com.example.memeshare.R.id.*;

public class MainActivity extends AppCompatActivity {
    String currUrl=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadMeme();
        Button nextButton =(Button)findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMeme();
                //Toast.makeText(getApplicationContext(),"mihir",Toast.LENGTH_SHORT).show();
            }
        });

        Button shareButton =(Button)findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"Hey check out this cool meme...."+currUrl);
                Intent chooser = Intent.createChooser(intent,"Send this meme via");
                startActivity(chooser);
            }
        });
    }
    private void loadMeme(){
        String url ="https://meme-api.herokuapp.com/gimme";

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            currUrl =response.getString("url");

                            Glide.with(getApplicationContext()).load(currUrl).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                                    .into((ImageView) findViewById(imageView));

                        }
                        catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),""+error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });


        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


}