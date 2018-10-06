package com.example.mateo.zavrsnirad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mlinar on 5/13/2018.
 */

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View view = null;

    GridView gridview;
    String ArticlesUrl;
    String CategoryUrl;

    RequestQueue requestQueue;
    ArrayList<Article> articles = new ArrayList<>();

    SwipeRefreshLayout swipeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ArticlesUrl = getResources().getString(R.string.API_URL) + getResources().getString(R.string.ARTICLES_URL);
        CategoryUrl = getResources().getString(R.string.API_URL) + getResources().getString(R.string.ARTICLES_BY_CATEGORY_URL);

        if (isNetworkAvailable()) {
            view = inflater.inflate(R.layout.home_layout, container, false);
            setRetainInstance(true);
            requestQueue = Volley.newRequestQueue(getActivity());
            loadJSON();
            swipeLayout = view.findViewById(R.id.swiperefresh);
            swipeLayout.setOnRefreshListener(HomeFragment.this);
            swipeLayout.setColorSchemeColors(Color.parseColor("#ff5722"), Color.parseColor("#30323a"));

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //consuming the touch
                    //letting the touch propagate
                    return swipeLayout.isRefreshing();
                }
            });

        } else {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame, new NoInternet()).commit();
            getActivity().onBackPressed();
        }

        return view;
    }

    private void loadJSON() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, ArticlesUrl, null, new Response.Listener<JSONArray>() {

            // Ukoliko smo dobili odgovor od servera pozvati će se onResponse metoda
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = (JSONObject) response.get(i);

                        String name = object.getString("name");
                        String price = object.getString("cijena");
                        String quantity = object.getString("kolicina");
                        String description = object.getString("opis");
                        String image = object.getString("slika");
                        String category = object.getString("category_name");
                        Log.d("name", description);
                        articles.add(new Article(name, price, quantity, description, image, category));
                    } catch (JSONException e) {
                        Log.d("ERROR", e.getMessage());
                    }
                }
                gridview = view.findViewById(R.id.customgrid);
                gridview.setAdapter(new CustomAdapter(view.getContext(), articles));

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        Intent i = new Intent(getActivity(), SingleArticleActivity.class);
                        i.putExtra("Article", articles.get(position));
                        startActivity(i);

                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JSON ERROR", "Greska: " + error);
            }
        });

        requestQueue.add(jsonArrayRequest);
    }


    @Override
    public void onRefresh() {
        articles.clear();
        if (isNetworkAvailable()) {
            loadJSON();
        } else {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame, new NoInternet()).commit();
        }
        swipeLayout.setRefreshing(false);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
