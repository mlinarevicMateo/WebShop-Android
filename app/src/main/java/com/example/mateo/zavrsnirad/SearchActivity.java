package com.example.mateo.zavrsnirad;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorAccent)));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("query", query);
            setTitle(getString(R.string.search_title_actionbar) + " " + query);
            Bundle bundle = new Bundle();
            Fragment fragment = new SearchFragment();
            bundle.putString("query", query);
            fragment.setArguments(bundle);

            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame_search, fragment).commit();
        }
    }
}
