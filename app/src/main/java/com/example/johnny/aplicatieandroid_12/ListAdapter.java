package com.example.johnny.aplicatieandroid_12;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Johnny on 07-Mar-18.
 */

public class ListAdapter extends SimpleAdapter{


    public ListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }


}
