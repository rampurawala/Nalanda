package com.expedite.apps.nalanda.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.activity.NoticeBoardActivity;

/**
 * Created by Jaydeep on 17/04/17.
 */
public class HomeNoticeBoardAdapter extends PagerAdapter {

    private String[] CircularName;
    private String[] date;
    private String[] groupName;
    private LayoutInflater inflater;
    private Context mContext;


    public HomeNoticeBoardAdapter(Context context, String[] CircularName,
                                  String[] date, String[] groupName) {
        this.mContext = context;
        this.CircularName = CircularName;
        this.date = date;
        this.groupName = groupName;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return CircularName.length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View mView = inflater.inflate(R.layout.noticeboard_list_raw_layout, null);

        if (mView != null) {
            TextView txtGroupName = (TextView) mView.findViewById(R.id.txtGroupName);
            TextView txtCircularName = (TextView) mView.findViewById(R.id.txtCircularName);
            TextView txtDate = (TextView) mView.findViewById(R.id.txtDate);
            View ll_MainView = (View) mView.findViewById(R.id.ll_MainView);

            if (groupName != null && groupName[position] != null && !groupName[position].isEmpty())
                txtGroupName.setText(Html.fromHtml("(" + groupName[position]+")"));
            if (CircularName != null && CircularName[position] != null && !CircularName[position].isEmpty())
                txtCircularName.setText(Html.fromHtml("<b>Circular Name: </b>" + CircularName[position]));
            if (date != null && date[position] != null && !date[position].isEmpty())
                txtDate.setText(((BaseActivity) mContext).convertDate(mContext, date[position]));

            ll_MainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NoticeBoardActivity.class);
                    intent.putExtra("IsFromHome", "IsFromHome");
                    mContext.startActivity(intent);
                    ((BaseActivity) mContext).onClickAnimation();

                }
            });

            view.addView(mView, 0);
        }
        return mView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
