package com.recoded.thenews;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.recoded.thenews.databinding.NewsItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wisam on Oct 28 17.
 */

class NewsArrayAdapter extends ArrayAdapter {

    List<News> newsList;

    public NewsArrayAdapter(Context ctx) {
        super(ctx, 0);
        newsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setNews(newsList.get(position), (position % 2 == 1));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, newsList.get(position).getUrl());
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    public void resetCollection(List<News> list) {
        clear();
        addAll(list);
        this.newsList = list;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        NewsItemBinding binding;

        public ViewHolder(View convertView) {
            binding = DataBindingUtil.bind(convertView);
        }

        public void setNews(News n, boolean odd) {
            binding.setNews(n);
            if (odd)
                binding.itemContainer.setBackground(getContext().getResources().getDrawable(R.drawable.list_state_bg_odd));
            else
                binding.itemContainer.setBackground(getContext().getResources().getDrawable(R.drawable.list_state_bg_ev));

        }
    }
}
