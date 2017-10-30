package com.recoded.thenews;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.recoded.thenews.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List> {
    ActivityMainBinding binding;
    LoaderManager loaderManager;
    private String query = "iraq";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.articlesListView.setAdapter(new NewsArrayAdapter(this));

        binding.progressBar.setVisibility(View.VISIBLE);

        binding.articlesListView.setEmptyView(binding.emptyTv);

        loaderManager = getSupportLoaderManager();

        loaderManager.initLoader(1, null, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        }

        loaderManager.restartLoader(1, null, this);

    }


    @Override
    public Loader<List> onCreateLoader(int id, Bundle args) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.emptyTv.setVisibility(View.GONE);
        return new NewsLoader(this, QueryUtils.getUrl(QueryUtils.Ends.CONTENT) + "&q=" + query);
    }

    @Override
    public void onLoadFinished(Loader<List> loader, List data) {
        populateList(data);
    }

    private void populateList(List data) {

        binding.emptyTv.setVisibility(View.VISIBLE);
        ((NewsArrayAdapter) binding.articlesListView.getAdapter()).resetCollection(data);
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List> loader) {
        ((NewsArrayAdapter) binding.articlesListView.getAdapter()).clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        // Assumes current activity is the searchable activity
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.START));

        return true;
    }
}
