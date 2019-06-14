package com.monitorfordata.m4d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewAdapter extends ArrayAdapter<DadosClass> {

    public interface Listener {
        void onGrab(int position, RelativeLayout row);
    }

    private final Listener listener;
    private final Map<DadosClass, Integer> mIdMap = new HashMap<>();

    ListViewAdapter(Context context, List<DadosClass> list, Listener listener) {
        super(context, 0, list);
        this.listener = listener;
        for (int i = 0; i < list.size(); ++i) {
            mIdMap.put(list.get(i), i);
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Context context = getContext();
        final DadosClass data = getItem(position);
        if(null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.linhas_listview, null);
        }

        final RelativeLayout row =  view.findViewById(
                R.id.lytPattern);

        View color = view.findViewById(R.id.lytPatternColorDraw);
        color.setBackgroundColor(data.cor);

        TextView textView = view.findViewById(R.id.textViewTitle);
        textView.setText(data.informacao);

        TextView timerange = view.findViewById(R.id.textViewTimeRange);
        timerange.setText(data.campo);

        view.findViewById(R.id.imageViewGrab)
            .setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    listener.onGrab(position, row);
                    return false;
                }
            });

        return view;
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return -1;
        }
        DadosClass item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }
}
