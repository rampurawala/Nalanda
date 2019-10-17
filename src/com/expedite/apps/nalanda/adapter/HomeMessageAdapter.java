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
import com.expedite.apps.nalanda.activity.MessagesExpandableListActivity;
import com.expedite.apps.nalanda.common.Common;

/**
 * Created by Jaydeep on 24/09/16.
 */
public class HomeMessageAdapter extends PagerAdapter {

    private String[] messages;
    private LayoutInflater inflater;
    private Context mContext;
    private String date = "";


    public HomeMessageAdapter(Context context, String[] messages) {
        this.mContext = context;
        this.messages = messages;
        inflater = LayoutInflater.from(context);


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return messages.length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View mView = inflater.inflate(R.layout.message_list_raw_layout, null);
        if (mView != null) {
            TextView mTxtMessage = (TextView) mView.findViewById(R.id.txtMessage);
            TextView mTxtDate = (TextView) mView.findViewById(R.id.txtDate);
            View ll_MainView = (View) mView.findViewById(R.id.ll_MainView);
            if (messages != null && messages[position] != null) {
                try {
                    String[] items = messages[position].split("##,@@");
                    if (items[0] != null && !items[0].isEmpty())
                        mTxtMessage.setText(Html.fromHtml(items[0]));

                    if (items != null && items.length > 1)
                        if (items[1] != null && !items[1].isEmpty())
                            mTxtDate.setText(((BaseActivity) mContext).convertDate(mContext, items[1]));
                } catch (Exception ex) {
                    Common.printStackTrace(ex);
                }
            }
            ll_MainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MessagesExpandableListActivity.class);
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
