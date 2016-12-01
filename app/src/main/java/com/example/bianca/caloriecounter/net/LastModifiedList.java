package com.example.bianca.caloriecounter.net;

import java.util.List;

/**
 * Created by bianca on 01.12.2016.
 */
public class LastModifiedList<T> {
    private String lastModified;
    private List<T> list;

    public LastModifiedList(String lastModified, List<T> list) {
        lastModified = lastModified;
        list = list;
    }

    public String getLastModified() {
        return lastModified;
    }

    public List<T> getList() {
        return list;
    }

    public int size() {
        if (list != null)
            return list.size();
        return 0;
    }
}
