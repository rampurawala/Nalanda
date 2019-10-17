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
import com.expedite.apps.nalanda.adapter.LeaveListAdapter;
import com.expedite.apps.nalanda.constants.Constants;

import static com.expedite.apps.nalanda.activity.LeaveListActivity.mRejectedLeaveArrayList;

public class LeaveRejectFragment extends Fragment {
    private View mParentView;
    private ProgressBar mProgressbar;
    private RecyclerView mLeaveRecycleView;
    private LeaveListAdapter mLeaveAdapter;
    private String mUserId = "", mGroupId = "";


    public static final LeaveRejectFragment newInstance(String paramString) {
        LeaveRejectFragment f = new LeaveRejectFragment();
        Bundle localBundle = new Bundle(1);
        localBundle.putString("Flag", paramString);
        f.setArguments(localBundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.leave_pending_fragment_layout, container, false);
        init();
        return mParentView;
    }


    private void init() {
        try {
            mProgressbar = (ProgressBar) mParentView.findViewById(R.id.progressbar);
            mLeaveRecycleView = (RecyclerView) mParentView.findViewById(R.id.leaveRecycleView);
            mLeaveRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            mLeaveAdapter = new LeaveListAdapter(getActivity(), mRejectedLeaveArrayList);
            mLeaveRecycleView.setAdapter(mLeaveAdapter);
            if (mRejectedLeaveArrayList != null && mRejectedLeaveArrayList.size() > 0) {
                ((TextView) mParentView.findViewById(R.id.txtNoRecordFound)).setVisibility(View.GONE);
                mLeaveRecycleView.setVisibility(View.VISIBLE);
            } else {
                ((TextView) mParentView.findViewById(R.id.txtNoRecordFound)).setVisibility(View.VISIBLE);
                mLeaveRecycleView.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            Constants.writelog("LeavePendingFragment", "onCreate:79" + ex.getMessage());
        }
    }
}
