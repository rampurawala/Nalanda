package com.expedite.apps.nalanda.fragment;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.expedite.apps.nalanda.R;

import com.expedite.apps.nalanda.adapter.AdapterPracticeTestFragment;
import com.expedite.apps.nalanda.constants.Constants;
import com.expedite.apps.nalanda.model.AppService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PracticeTestFragment extends Fragment {
    RecyclerView rc_fragPracticeTest;
    String flag, jsonstr;
    List<AppService.ListArray> listTest;
    LinearLayout empty_practiceTest;


    public PracticeTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_practice_test, container, false);
        try {
            rc_fragPracticeTest = v.findViewById(R.id.rc_fragPracticeTest);
            empty_practiceTest = v.findViewById(R.id.empty_practiceTest);
            rc_fragPracticeTest.setHasFixedSize(true);
            rc_fragPracticeTest.setLayoutManager(new LinearLayoutManager(this.getActivity()));

            readBundle(getArguments());
            if (flag.equals("0"))
                getActivity().setTitle("Completed - Practice Test");
            if (flag.equals("1"))
                getActivity().setTitle("Pending - Practice Test");
            Gson gson = new Gson();
            Type type = new TypeToken<List<AppService.ListArray>>() {
            }.getType();
            listTest = gson.fromJson(jsonstr, type);
            if (listTest == null || listTest.isEmpty() || listTest.size() <= 0) {
                empty_practiceTest.setVisibility(View.VISIBLE);
                rc_fragPracticeTest.setVisibility(View.GONE);
            } else {
                empty_practiceTest.setVisibility(View.GONE);
                rc_fragPracticeTest.setVisibility(View.VISIBLE);
                AdapterPracticeTestFragment listAdapter = new AdapterPracticeTestFragment(getActivity(), listTest, Integer.parseInt(flag));
                rc_fragPracticeTest.setAdapter(listAdapter);


            }
        }catch (Exception e) {
                Constants.writelog("PracticeTestFragment", "onCreate 70:" + e.getMessage());
            }
            return v;
        }

        private void readBundle(Bundle bundle) {
            if (bundle != null) {
                flag = bundle.getString("flag");
                jsonstr = bundle.getString("testList");
            }
        }
    }
