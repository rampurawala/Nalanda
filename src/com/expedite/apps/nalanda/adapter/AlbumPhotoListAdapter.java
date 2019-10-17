package com.expedite.apps.nalanda.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.activity.AlbumWiseDetailActivityNew;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.constants.Constants;


/**
 * Created by Jaydeep on 24-04-17.
 */
public class AlbumPhotoListAdapter extends RecyclerView.Adapter {
    private String[] filePath;
    private String[] albumnamne;
    private String[] albumtime;
    private Integer[] albumId;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int lastPosition = -1;

    public AlbumPhotoListAdapter(Context context, String[] filePath, String[] alname,
                                 String[] albumtimes, Integer[] albumIds) {
        this.filePath = filePath;
        albumnamne = alname;
        albumId = albumIds;
        albumtime = albumtimes;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.album_photos_item_row_new, parent, false);
        return new CustomViewHolder(view);


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return filePath.length;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CustomViewHolder) {
            if (filePath != null && filePath[position] != null) {
                String[] splterstr = filePath[position].split("@@@###");
                String filepath = splterstr[0];
                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeFile(filepath);
                    ((CustomViewHolder) holder).imgPhotos.setImageBitmap(bmp);
                } catch (Exception ex) {
                    Common.printStackTrace(ex);
                }
            }
            if (albumnamne != null && albumnamne[position] != null)
                ((CustomViewHolder) holder).txtTitle.setText(albumnamne[position]);

            ((CustomViewHolder) holder).ll_mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (albumId != null)
                        try {
                            String id = albumId[position].toString();
                            Intent intent = new Intent(mContext, AlbumWiseDetailActivityNew.class);
                            intent.putExtra("ALbUMID", id);
                            mContext.startActivity(intent);
                            ((BaseActivity) mContext).onClickAnimation();
                        } catch (Exception err) {
                            Constants.writelog("HomePhotoListAdapter", err.getMessage());
                        }
                }
            });

            Animation animation = AnimationUtils.loadAnimation(mContext,
                    (position > lastPosition) ? R.anim.slide_in_right
                            : R.anim.slide_in_left);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }

    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhotos, imgLogo;
        private TextView txtTitle;
        private View ll_mainLayout;

        public CustomViewHolder(View view) {
            super(view);
            imgPhotos = (ImageView) view.findViewById(R.id.imgPhotos);
            imgLogo = (ImageView) view.findViewById(R.id.imgLogo);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            ll_mainLayout = (View) view.findViewById(R.id.ll_mainLayout);
        }
    }

}



