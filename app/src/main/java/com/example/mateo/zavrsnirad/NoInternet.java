package com.example.mateo.zavrsnirad;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mlinar on 5/15/2018.
 */

public class NoInternet extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View view;
    SwipeRefreshLayout swipeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.no_internet, container, false);

        swipeLayout = view.findViewById(R.id.no_internet);
        swipeLayout.setOnRefreshListener(NoInternet.this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    @Override
    public void onRefresh() {
        FragmentManager fm = getFragmentManager();
        if (isNetworkAvailable()) {
            fm.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
        } else {
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