package com.expedite.apps.nalanda.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.activity.PracticeTestQAActivity;
import com.expedite.apps.nalanda.activity.PracticeTestResultActivity;
import com.expedite.apps.nalanda.constants.Constants;
import com.expedite.apps.nalanda.model.AppService;


import java.util.List;

public class AdapterPracticeTestFragment extends RecyclerView.Adapter<AdapterPracticeTestFragment.ItemViewHolder> {
    Context context;
    List<AppService.ListArray> itemModels;
    int flag;
    String msg = "";

    public AdapterPracticeTestFragment(Context context, List<AppService.ListArray> itemModels, int flag) {
        this.context = context;
        this.itemModels = itemModels;
        this.flag = flag;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_practice_test_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        try {
            holder.testName.setVisibility(View.GONE);
            holder.msg.setVisibility(View.GONE);
            holder.cardTest.setVisibility(View.GONE);
            /* holder.date.setText(itemModels.getDate());*/
            /*holder.img.setImageResource(itemModal.getImage());*/
            if (flag == 1 && (itemModels.get(position).getThird().equals("1") || itemModels.get(position).getThird().equals("2"))) {
                holder.cardTest.setVisibility(View.VISIBLE);
                holder.testName.setVisibility(View.VISIBLE);
                holder.testName.setText(itemModels.get(position).getSecond());
                /* holder.msgLayout.setVisibility(View.VISIBLE);*/
//            holder.marksLayout.setVisibility(View.VISIBLE);
           /*  String[] parts = itemModels.get(position).getFourth().split(",");
            if (parts.length > 0) {
                holder.marks.setVisibility(View.VISIBLE);
                holder.outOf.setText(parts[1]);
                holder.get.setText(parts[0]);
            }else {
                holder.marks.setVisibility(View.GONE);
                holder.msg_marks.setText(itemModels.get(position).getFourth());
            }
         holder.msg.setText(itemModels.get(position).getFourth());*/
                if (itemModels.get(position).getFourth().isEmpty() && itemModels.get(position).getFifth().isEmpty()) {
                    holder.msg.setVisibility(View.GONE);
                } else {
                    if (itemModels.get(position).getFourth()!=null && !itemModels.get(position).getFourth().isEmpty()) {
                        msg = itemModels.get(position).getFourth() + "  ";
                    }
                    if (itemModels.get(position).getFifth()!=null && !itemModels.get(position).getFifth().isEmpty()) {
                        msg += itemModels.get(position).getFifth();
                    }
                    holder.msg.setVisibility(View.VISIBLE);
                    holder.msg.setText(msg);
                }

            }
            if (flag == 0 && itemModels.get(position).getThird().equals("0")) {
                holder.cardTest.setVisibility(View.VISIBLE);
                holder.testName.setVisibility(View.VISIBLE);
                holder.testName.setText(itemModels.get(position).getSecond());
           /* holder.msgLayout.setVisibility(View.GONE);
            holder.marksLayout.setVisibility(View.VISIBLE);
            if (itemModels.get(position).getFourth().isEmpty()) {
                holder.marksLayout.setVisibility(View.GONE);
            } else {
                String[] parts = itemModels.get(position).getFourth().split(",");
                if (parts.length > 1) {
                    holder.marks.setVisibility(View.VISIBLE);
                    holder.outOf.setText(parts[1]);
                    holder.get.setText(parts[0]);
                } else {
                    holder.marks.setVisibility(View.GONE);
                    holder.msg_marks.setText(itemModels.get(position).getFourth());
                }
            }
            if (itemModels.get(position).getFourth().isEmpty())
                holder.marks.setVisibility(View.GONE);
            if (!itemModels.get(position).getFifth().isEmpty())
                holder.msg_marks.setText(itemModels.get(position).getFifth());*/
                if (itemModels.get(position).getFourth().isEmpty() && itemModels.get(position).getFifth().isEmpty()) {
                    holder.msg.setVisibility(View.GONE);
                } else {
                    if (itemModels.get(position).getFourth()!=null && !itemModels.get(position).getFourth().isEmpty()) {
                        msg = itemModels.get(position).getFourth() + "  ";
                    }
                    if (itemModels.get(position).getFifth()!=null && !itemModels.get(position).getFifth().isEmpty()) {
                        msg += itemModels.get(position).getFifth();
                    }
                    holder.msg.setVisibility(View.VISIBLE);
                    holder.msg.setText(msg);
                }

            }

            //  holder.date.setText(itemModels.get(position).getMdate());
            // Picasso.with((context)).load(itemModels.get(position).getUProfilePic()).placeholder(R.drawable.person_default_theme).into(holder.img);

            holder.cardTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (flag != 0 && !itemModels.get(position).getThird().equals("0")) {
                        Intent i = new Intent(context, PracticeTestQAActivity.class);
                        i.putExtra("testId", itemModels.get(position).getFirst());
                        i.putExtra("testName",itemModels.get(position).getSecond());
                        i.putExtra("flag", "1");
                        context.startActivity(i);
                    }else {
                        Intent i=new Intent(context, PracticeTestResultActivity.class);
                        i.putExtra("testId", itemModels.get(position).getFirst());
                        i.putExtra("testName",itemModels.get(position).getSecond());
                        i.putExtra("flag", "0");
                        context.startActivity(i);
                    }

                    /* int message_id=Integer.parseInt(itemModels.get(position).getMessageMasterId());*//*
                i.putExtra("senderName", itemModels.get(position).getUName());
                i.putExtra("assignmentTitle", itemModels.get(position).getMessageTitle());
                i.putExtra("profilePic", itemModels.get(position).getUProfilePic());
                i.putExtra("message_id", itemModels.get(position).getMessageMasterId());
                context.startActivity(i);*/
                    //Toast.makeText(context, itemModal.getName(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception ex){
            Constants.writelog("AdapterPracticeTest", "onBindViewHolder 135:" + ex.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (itemModels == null)
            return 0;
        else return itemModels.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView /*get, outOf,*/ msg, testName/*, msg_marks*/;
        LinearLayout /*marksLayout, msgLayout,*/ cardTest/*, marks*/;

        public ItemViewHolder(View itemView) {
            super(itemView);
            //  get = itemView.findViewById(R.id.get);
            //  outOf = itemView.findViewById(R.id.outOf);
            //  marksLayout = itemView.findViewById(R.id.marksLayout);
            msg = itemView.findViewById(R.id.msg);
          //  msgLayout = itemView.findViewById(R.id.msgLayout);
            testName = itemView.findViewById(R.id.testName);
            // msg_marks = itemView.findViewById(R.id.msg_marks);
            cardTest = itemView.findViewById(R.id.cardTest);
            // marks = itemView.findViewById(R.id.marks);
        }


    }

}
