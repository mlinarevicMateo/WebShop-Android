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

public class SearchFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View view = null;

    GridView gridview;
    String ArticlesSearchUrl;
    String query;
    RequestQueue requestQueue;
    ArrayList<Article> articles = new ArrayList<>();

    SwipeRefreshLayout swipeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ArticlesSearchUrl = getResources().getString(R.string.API_URL) + getResources().getString(R.string.ARTICLES_SEARCH_URL);

        if (isNetworkAvailable()) {
            view = inflater.inflate(R.layout.home_layout, container, false);
            setRetainInstance(true);
            requestQueue = Volley.newRequestQueue(getActivity());

            Bundle bundle = this.getArguments();

            if (bundle != null) {
                query = bundle.getString("query", "null");
            }
            ArticlesSearchUrl = ArticlesSearchUrl + query;

            searchArticles();
            swipeLayout = view.findViewById(R.id.swiperefresh);
            swipeLayout.setOnRefreshListener(SearchFragment.this);
            swipeLayout.setColorSchemeColors(Color.parseColor("#ff5722"), Color.parseColor("#30323a"));
        } else {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame, new NoInternet()).commit();
            getActivity().onBackPressed();
        }

        return view;
    }

    private void searchArticles() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, ArticlesSearchUrl, null, new Response.Listener<JSONArray>() {

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
            searchArticles();
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
