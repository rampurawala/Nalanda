package com.expedite.apps.nalanda.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.MyApplication;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.common.Datastorage;
import com.expedite.apps.nalanda.constants.Constants;
import com.expedite.apps.nalanda.model.GetGuardianHostModal;
import com.expedite.apps.nalanda.model.GetTimeModal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakeAppointmentActivity extends BaseActivity {
    ImageView /*timePicker,*/ datePicker;
    TextView dateSelected, /*timeSelected,*/
            txtTitle;
    String mDay, mMonth, mYear, str = "";
    EditText input_mobileNo, input_name, visitPurpose;
    Spinner spnGuardian, spnHost, spnSelectTime;
    RelativeLayout relative_progress;
    Button btn_takeAppointment;
    ScrollView scroll_takeAppointment;
    String selectedGuardianId, selectedGuardianName, selectedHostId, selectedHostName, selectedHostEmail, selectedHostMobile, selectedTimeId, selectedTimeName;
    int selectedHostIndex = 0, selectedGuardianIndex = 0, selectedTimeIndex = 0;
    private String SchoolId, StudentId, Year_Id;
    private List<String> mSpnGuardianNameArray = new ArrayList<>(), mSpnGuardianIdArray = new ArrayList<>();
    private List<String> mSpnTimeNameArray = new ArrayList<>(), mSpnTimeIdArray = new ArrayList<>();
    private List<String> mSpnHostNameArray = new ArrayList<>(), mSpnHostIdArray = new ArrayList<>(), mSpnHostMobileArray = new ArrayList<>(), mSpnHostEmailArray = new ArrayList<>();
    ArrayAdapter<String> hostAdapter, guardianAdapter, timeAdapter;
    Common common;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_appointment);
        // timePicker = findViewById(R.id.timePicker);
        datePicker = findViewById(R.id.datePicker);
        dateSelected = findViewById(R.id.dateSelected);
        // timeSelected = findViewById(R.id.timeSelected);
        input_mobileNo = findViewById(R.id.input_mobileNo);
        input_name = findViewById(R.id.input_name);
        relative_progress = findViewById(R.id.relative_progress);
        spnGuardian = findViewById(R.id.spnGuardian);
        spnSelectTime = findViewById(R.id.spnSelectTime);
        scroll_takeAppointment = findViewById(R.id.scroll_takeAppointment);
        visitPurpose = findViewById(R.id.visitPurpose);
        btn_takeAppointment = findViewById(R.id.btn_takeAppointment);
        spnHost = findViewById(R.id.spnHost);
        txtTitle = findViewById(R.id.txtTitle);
        common = new Common(this);
       /* db = new DatabaseHandler(TakeAppointmentActivity.this);
        SchoolId = Datastorage.GetSchoolId(TakeAppointmentActivity.this);
        StudentId = Datastorage.GetStudentId(TakeAppointmentActivity.this);
        String defacnt = db.GetDefaultAcademicYearAccount(Integer.parseInt(SchoolId), Integer.parseInt(StudentId));
        if (defacnt != null && defacnt.length() > 0) {
            String[] splterstr = defacnt.split(",");
            Year_Id = splterstr[1];
        } else {
            Year_Id = Datastorage.GetCurrentYearId(TakeAppointmentActivity.this);
        }*/
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("Take Appointment");
        }
        mSpnGuardianNameArray.add("---- Select Guardian ----");
        mSpnGuardianIdArray.add("0");
        mSpnHostNameArray.add("---- Select Host ----");
        mSpnHostIdArray.add("0");
        mSpnTimeNameArray.add("---- Select Time ----");
        mSpnTimeIdArray.add("0");
        mSpnHostEmailArray.add("0");
        mSpnHostMobileArray.add("0");

        getGuardianHostList();

        dateSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog;

                datePickerDialog = new DatePickerDialog(TakeAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //    date_dob.setText(dayOfMonth + "/" + (month +1) + "/" + year);
                        dateSelected.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        String parts[] = dateSelected.getText().toString().split("/");

                        if (parts[0].length() == 1)
                            mDay = "0" + parts[0];
                        else
                            mDay = parts[0];
                        if (parts[1].length() == 1)
                            mMonth = "0" + parts[1];
                        else
                            mMonth = parts[1];

                        mYear = parts[2];
                        str = mYear + "-" + mMonth + "-" + mDay;
                        if (selectedHostIndex != 0) {
                            getTime();
                        }

                    }
                }, year, month, day);

                datePickerDialog.setTitle("Select Date");
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

       /* timeSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cla = Calendar.getInstance();
                int hour = cla.get(Calendar.HOUR_OF_DAY);
                int min = cla.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(TakeAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mhours = hourOfDay + "";
                        mminutes = minute + "";
                        timeSelected.setText(hourOfDay + ":" + minute);
                    }
                }, hour, min, true);
                timePickerDialog.setTitle("Select time");
                timePickerDialog.show();

            }
        });*/

        btn_takeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PhoneNumberValidation() && !input_name.getText().toString().isEmpty())
                    setBookAppointment();
                else {
                    input_name.setError("Please Enter Name");
                    Constants.requestFocus(TakeAppointmentActivity.this, input_name);
                }
            }
        });


        spnHost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedHostIndex = 0;
                    if (position != selectedHostIndex) {
                        selectedHostIndex = position;
                        selectedHostId = mSpnHostIdArray.get(position);
                        selectedHostName = mSpnHostNameArray.get(position);
                        selectedHostEmail = mSpnHostEmailArray.get(position);
                        selectedHostMobile = mSpnHostMobileArray.get(position);
                        if (str != "") {
                            getTime();
                        }
                    }
                } catch (Exception ex) {
                    Constants.writelog("spnHost:OnCreate 159", ex.getMessage());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnSelectTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedTimeIndex = 0;
                    if (position != selectedTimeIndex) {
                        selectedTimeIndex = position;
                        selectedTimeId = mSpnTimeIdArray.get(position);
                        selectedTimeName = mSpnTimeNameArray.get(position);
                    }
                } catch (Exception ex) {
                    Constants.writelog("spnSelectTime:OnCreate 224", ex.getMessage());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnGuardian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedGuardianIndex = 0;
                    if (position != selectedGuardianIndex) {
                        selectedGuardianIndex = position;
                        selectedGuardianId = mSpnGuardianIdArray.get(position);
                        selectedGuardianName = mSpnGuardianNameArray.get(position);
                    }
                } catch (Exception ex) {
                    Constants.writelog("spnGuardian:OnCreate 180", ex.getMessage());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        hostAdapter = new ArrayAdapter<String>(TakeAppointmentActivity.this,
                R.layout.school_spinner_item, mSpnHostNameArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(getResources().getColor(R.color.viewname));
                ((TextView) v).setTextSize(15);
                return v;
            }
        };
        spnHost.setAdapter(hostAdapter);

        guardianAdapter = new ArrayAdapter<String>(TakeAppointmentActivity.this,
                R.layout.school_spinner_item, mSpnGuardianNameArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(getResources().getColor(R.color.viewname));
                ((TextView) v).setTextSize(15);
                return v;
            }
        };
        spnGuardian.setAdapter(guardianAdapter);

        timeAdapter = new ArrayAdapter<String>(TakeAppointmentActivity.this,
                R.layout.school_spinner_item, mSpnTimeNameArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(getResources().getColor(R.color.viewname));
                ((TextView) v).setTextSize(15);
                return v;
            }
        };
        spnSelectTime.setAdapter(timeAdapter);

    }

    private void getGuardianHostList() {
        relative_progress.setVisibility(View.VISIBLE);
        scroll_takeAppointment.setVisibility(View.GONE);
        try {
            String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            Call<GetGuardianHostModal> call = ((MyApplication) getApplicationContext()).getRetroFitAppointment()
                    .GetGuardianHostList("", mSchoolId, /*mStudentId*/"47", mYearId, Datastorage.GetPhoneNumber(getApplicationContext()), Constants.PLATFORM, common.getSession(Constants.APPname), Datastorage.GetAppVersion(TakeAppointmentActivity.this));
            call.enqueue(new Callback<GetGuardianHostModal>() {
                @Override
                public void onResponse(Call<GetGuardianHostModal> call, Response<GetGuardianHostModal> response) {
                    try {
                        relative_progress.setVisibility(View.GONE);
                        scroll_takeAppointment.setVisibility(View.VISIBLE);

                        GetGuardianHostModal tmp = response.body();
                        if (tmp != null && tmp.getResponse() != null && !tmp.getResponse().isEmpty() &&
                                tmp.getResult().equalsIgnoreCase("1")) {
                            List<GetGuardianHostModal.Gethostlist> hostLists = tmp.getGethostlist();
                            List<GetGuardianHostModal.Appointmenttypelist> appointmentTypeLists = tmp.getAppointmenttypelist();
                            if (hostLists != null && hostLists.size() > 0) {
                                for (int i = 0; i < hostLists.size(); i++) {
                                    mSpnHostIdArray.add(hostLists.get(i).getTypeId());
                                    mSpnHostNameArray.add(hostLists.get(i).getTypeName());
                                    mSpnHostEmailArray.add(hostLists.get(i).getEmail());
                                    mSpnHostMobileArray.add(hostLists.get(i).getMobileno());
                                }
                            }
                            if (appointmentTypeLists != null && appointmentTypeLists.size() > 0) {
                                for (int i = 0; i < appointmentTypeLists.size(); i++) {
                                    mSpnGuardianIdArray.add(appointmentTypeLists.get(i).getTypeid());
                                    mSpnGuardianNameArray.add(appointmentTypeLists.get(i).getTypename());
                                }
                            }

                        }
                        // mProgressBar.setVisibility(View.GONE);
                    } catch (Exception ex) {
                        relative_progress.setVisibility(View.GONE);
                        scroll_takeAppointment.setVisibility(View.VISIBLE);
                        //     mProgressBar.setVisibility(View.GONE);
                        Constants.writelog("TakeAppointmentActivity", "Ex 250 :" + ex.getMessage());
                    } finally {
                        relative_progress.setVisibility(View.GONE);
                        scroll_takeAppointment.setVisibility(View.VISIBLE);
                        //   mProgressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GetGuardianHostModal> call, Throwable t) {
                    relative_progress.setVisibility(View.GONE);
                    scroll_takeAppointment.setVisibility(View.VISIBLE);
                    Constants.writelog("TakeAppointmentActivity", "failure()258:" + t.getMessage());
                    //  mProgressBar.setVisibility(View.GONE);
                }
            });
        } catch (Exception ex) {
            relative_progress.setVisibility(View.GONE);
            scroll_takeAppointment.setVisibility(View.VISIBLE);
            //   mProgressBar.setVisibility(View.GONE);
            Constants.writelog("TakeAppointmentActivity", "getGuardianHostList:Error:264:" + ex.getMessage());
        }
    }

    private void getTime() {
        relative_progress.setVisibility(View.VISIBLE);
        scroll_takeAppointment.setVisibility(View.GONE);
        try {

            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            ////GetGroupListFromHost?hostid=1&requesteddate=24/07/2019&year_id=&mobileno=&platform=&appName=&appVersion=
            Call<GetTimeModal> call = ((MyApplication) getApplicationContext()).getRetroFitAppointment()
                    .GetTimeList(selectedHostId, str, mYearId, Datastorage.GetPhoneNumber(getApplicationContext()), Constants.PLATFORM, common.getSession(Constants.APPname), Datastorage.GetAppVersion(TakeAppointmentActivity.this));
            call.enqueue(new Callback<GetTimeModal>() {
                @Override
                public void onResponse(Call<GetTimeModal> call, Response<GetTimeModal> response) {
                    try {
                        relative_progress.setVisibility(View.GONE);
                        scroll_takeAppointment.setVisibility(View.VISIBLE);

                        GetTimeModal tmp = response.body();
                        if (tmp != null && tmp.getResponse() != null && !tmp.getResponse().isEmpty() &&
                                tmp.getResponse().equalsIgnoreCase("1")) {
                            List<GetTimeModal.Getgrouplist> timeLists = tmp.getGetgrouplist();
                            timeAdapter.clear();
                            if (timeLists != null && timeLists.size() > 0) {
                                for (int i = 0; i < timeLists.size(); i++) {
                                    mSpnTimeIdArray.add(timeLists.get(i).getId());
                                    mSpnTimeNameArray.add(timeLists.get(i).getValue());
                                }
                                //timeAdapter.clear();
                                //timeAdapter.addAll(mSpnTimeNameArray);
                                timeAdapter.notifyDataSetChanged();
                            }
                        }
                        // mProgressBar.setVisibility(View.GONE);
                    } catch (Exception ex) {
                        relative_progress.setVisibility(View.GONE);
                        scroll_takeAppointment.setVisibility(View.VISIBLE);
                        //     mProgressBar.setVisibility(View.GONE);
                        Constants.writelog("TakeAppointmentActivity", "Ex 250 :" + ex.getMessage());
                    } finally {
                        relative_progress.setVisibility(View.GONE);
                        scroll_takeAppointment.setVisibility(View.VISIBLE);
                        //   mProgressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GetTimeModal> call, Throwable t) {
                    relative_progress.setVisibility(View.GONE);
                    scroll_takeAppointment.setVisibility(View.VISIBLE);
                    Constants.writelog("TakeAppointmentActivity", "failure()258:" + t.getMessage());
                    //  mProgressBar.setVisibility(View.GONE);
                }
            });
        } catch (Exception ex) {
            relative_progress.setVisibility(View.GONE);
            scroll_takeAppointment.setVisibility(View.VISIBLE);
            //   mProgressBar.setVisibility(View.GONE);
            Constants.writelog("TakeAppointmentActivity", "getGuardianHostList:Error:264:" + ex.getMessage());
        }
    }

    private void setBookAppointment() {
        relative_progress.setVisibility(View.VISIBLE);
        try {
            String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            Call<GetGuardianHostModal> call = ((MyApplication) getApplicationContext()).getRetroFitAppointment()
                    .SetBookAppointment("", mSchoolId, mStudentId, input_mobileNo.getText().toString(), input_name.getText().toString(), selectedTimeId, "", selectedHostMobile, selectedHostEmail, selectedHostName, selectedHostId, str, visitPurpose.getText().toString(), mYearId, Datastorage.GetPhoneNumber(getApplicationContext()), Constants.PLATFORM, common.getSession(Constants.APPname), Datastorage.GetAppVersion(TakeAppointmentActivity.this));
            call.enqueue(new Callback<GetGuardianHostModal>() {
                @Override
                public void onResponse(Call<GetGuardianHostModal> call, Response<GetGuardianHostModal> response) {
                    try {
                        relative_progress.setVisibility(View.GONE);
                        scroll_takeAppointment.setVisibility(View.VISIBLE);

                        GetGuardianHostModal tmp = response.body();
                        if (tmp != null && tmp.getResponse() != null && !tmp.getResponse().isEmpty() /*&&
                                tmp.getResponse().equalsIgnoreCase("1")*/) {
                            Toast.makeText(TakeAppointmentActivity.this, tmp.getResult(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(TakeAppointmentActivity.this, TakeAppointmentListActivity.class);
                            startActivity(i);
                            TakeAppointmentActivity.this.finish();

                        }
                        // mProgressBar.setVisibility(View.GONE);
                    } catch (Exception ex) {
                        relative_progress.setVisibility(View.GONE);
                        scroll_takeAppointment.setVisibility(View.VISIBLE);
                        //     mProgressBar.setVisibility(View.GONE);
                        Constants.writelog("TakeAppointmentActivity", "Ex 250 :" + ex.getMessage());
                    } finally {
                        relative_progress.setVisibility(View.GONE);
                        scroll_takeAppointment.setVisibility(View.VISIBLE);
                        //   mProgressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GetGuardianHostModal> call, Throwable t) {
                    relative_progress.setVisibility(View.GONE);
                    scroll_takeAppointment.setVisibility(View.VISIBLE);
                    Constants.writelog("TakeAppointmentActivity", "failure()258:" + t.getMessage());
                    //  mProgressBar.setVisibility(View.GONE);
                }
            });
        } catch (Exception ex) {
            relative_progress.setVisibility(View.GONE);
            scroll_takeAppointment.setVisibility(View.VISIBLE);
            //   mProgressBar.setVisibility(View.GONE);
            Constants.writelog("TakeAppointmentActivity", "getGuardianHostList:Error:264:" + ex.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                TakeAppointmentActivity.this.finish();
                Common.onBackClickAnimation(TakeAppointmentActivity.this);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean PhoneNumberValidation() {
        boolean isvalidate = true;
        if (input_mobileNo.getText().toString().trim().length() <= 0) {
            input_mobileNo.setError("Please Enter Mobile Number");
            Constants.requestFocus(TakeAppointmentActivity.this, input_mobileNo);
            isvalidate = false;
        } else if (input_mobileNo.getText().toString().length() < 10) {
            input_mobileNo.setError("Please Enter Valid Mobile Number");
            Constants.requestFocus(TakeAppointmentActivity.this, input_mobileNo);
            isvalidate = false;
            try {
                int number = Integer.parseInt(input_mobileNo.getText().toString().trim());
            } catch (Exception ex) {
                input_mobileNo.setError("Please Enter Valid Mobile Number");
                Constants.requestFocus(TakeAppointmentActivity.this, input_mobileNo);
                isvalidate = false;
            }
        }

        return isvalidate;
    }
}
