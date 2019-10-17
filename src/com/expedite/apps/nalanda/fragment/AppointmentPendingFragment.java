package com.expedite.apps.nalanda.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.adapter.AppointmentListAdapter;
import com.expedite.apps.nalanda.constants.Constants;

import static com.expedite.apps.nalanda.activity.TakeAppointmentListActivity.mPendingAppointmentArrayList;

public class AppointmentPendingFragment extends Fragment {
    private View mParentView;
    private ProgressBar mProgressbar;
    private RecyclerView appointmentRecycleView;
    private AppointmentListAdapter mAppointmentAdapter;
    private String mUserId = "", mGroupId = "";


//    public static final LeavePendingFragment newInstance(ArrayList<LeaveListModel.LeaveHistory> mData) {
//        LeavePendingFragment f = new LeavePendingFragment();
//        Bundle localBundle = new Bundle(1);
//        localBundle.putSerializable("LeavePendingArray", mData);
//        f.setArguments(localBundle);
//        return f;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.leave_pending_fragment_layout, container, false);
        init();
        return mParentView;
    }


    private void init() {
        try {
            mProgressbar = (ProgressBar) mParentView.findViewById(R.id.progressbar);
            appointmentRecycleView = (RecyclerView) mParentView.findViewById(R.id.leaveRecycleView);
            appointmentRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            mAppointmentAdapter = new AppointmentListAdapter(getActivity(), mPendingAppointmentArrayList);
            appointmentRecycleView.setAdapter(mAppointmentAdapter);
            if (mPendingAppointmentArrayList != null && mPendingAppointmentArrayList.size() > 0) {
                ((TextView) mParentView.findViewById(R.id.txtNoRecordFound)).setVisibility(View.GONE);
                appointmentRecycleView.setVisibility(View.VISIBLE);
            } else {
                ((TextView) mParentView.findViewById(R.id.txtNoRecordFound)).setVisibility(View.VISIBLE);
                appointmentRecycleView.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            Constants.writelog("AppointmentPendingFragment", "onCreate:60" + ex.getMessage());
        }
    }
}
