package com.recoded.thenews;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wisam on Oct 28 17.
 */

class NewsLoader extends AsyncTaskLoader<List> {

    private static final String LOG_TAG = NewsLoader.class.getName();
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return new ArrayList<>();
        }
        return QueryUtils.parseContent(QueryUtils.fetch(mUrl));
    }
}
