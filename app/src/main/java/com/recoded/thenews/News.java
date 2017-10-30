package com.recoded.thenews;

import android.net.Uri;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wisam on Oct 28 17.
 */

public class News {
    private String title = null, author = null;
    private Uri url = null;
    private String sectionName = null;
    private Date publicationDate = null;

    public News(String title, String author, String url) {
        this.title = title;
        this.author = author;
        this.url = Uri.parse(url);
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Uri getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getPublicationDate() {
        DateFormat df = new SimpleDateFormat("MMM dd, YYYY  DDDD", Locale.US);

        return publicationDate == null ? "" : df.format(publicationDate);
    }

    public String getPublicationTime() {
        DateFormat df = new SimpleDateFormat("hh:mm aa", Locale.US);

        return publicationDate == null ? "" : df.format(publicationDate);
    }

    public void setPublicationDate(String publicationDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            this.publicationDate = sdf.parse(publicationDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getAuthor() {
        return author;
    }
}
