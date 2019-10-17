package com.expedite.apps.nalanda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.expedite.apps.nalanda.R;


public class AccountRemoveListAdapter extends BaseAdapter {

    private Context context;
    private String[] names;

    public AccountRemoveListAdapter(Context context, String[] names) {
        this.context = context;
        this.names = names;
    }

    public int getCount() {
        return names.length;
    }

    public Object getItem(int arg) {
        return arg;
    }

    public long getItemId(int arg) {
        return 0;
    }

    public String getItenName(int arg) {
        return names[arg];
    }

    public View getView(int position, View mView, ViewGroup mViewgroups) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_item_list, mViewgroups, false);
        TextView txtname = (TextView) view.findViewById(R.id.txtmessages);
        ImageView imgMore = (ImageView) view.findViewById(R.id.imgMore);
       // imgMore.setVisibility(View.GONE);

        if (names[position] != null && names[position].length() > 0)
            txtname.setText(names[position]);

        return view;
    }

}
