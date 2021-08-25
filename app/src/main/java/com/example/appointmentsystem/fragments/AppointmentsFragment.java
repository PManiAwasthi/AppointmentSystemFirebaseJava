package com.example.appointmentsystem.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appointmentsystem.Adapters.AppointmentAdapter;
import com.example.appointmentsystem.Appointment;
import com.example.appointmentsystem.R;
import com.example.appointmentsystem.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView txtViewAppointmentsFragmentDoctorsHeading, txtViewAppointmentsFragmentPatientHeading,txtHidden;
    EditText edtAppointmentsFragmentDayCount;
    RecyclerView recyclerView;
    List<Appointment> mAppointments;
    AppointmentAdapter appointmentAdapter;
    Button btnAppointmentsFragmentGenerateCount;
    int dd,mm, yyyy, hr;

    String userId, accounttype;

    public AppointmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Appointments.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentsFragment newInstance(String param1, String param2) {
        AppointmentsFragment fragment = new AppointmentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointments_fragment, container, false);
        userId = getArguments().getString("userId");
        accounttype = getArguments().getString("accounttype");

        txtViewAppointmentsFragmentDoctorsHeading = view.findViewById(R.id.txtViewAppointmentsFragementDoctorHeading);
        txtViewAppointmentsFragmentPatientHeading = view.findViewById(R.id.txtViewAppointmnetsFragementPatientHeading);
        edtAppointmentsFragmentDayCount = view.findViewById(R.id.edtAppointmentsFragmentDayCount);
        btnAppointmentsFragmentGenerateCount = view.findViewById(R.id.btnAppointmentsFragementGenerateCount);
        txtHidden = view.findViewById(R.id.txtHidden);
        recyclerView = view.findViewById(R.id.recyclerViewAppointmentsFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAppointments = new ArrayList<>();
        readFromDatabase();
        if(accounttype.equals("doctor")){
            txtViewAppointmentsFragmentPatientHeading.setVisibility(View.INVISIBLE);
            txtViewAppointmentsFragmentDoctorsHeading.setVisibility(View.VISIBLE);
            btnAppointmentsFragmentGenerateCount.setVisibility(View.VISIBLE);
            edtAppointmentsFragmentDayCount.setVisibility(View.VISIBLE);
        }else{
            txtViewAppointmentsFragmentPatientHeading.setVisibility(View.VISIBLE);
            txtViewAppointmentsFragmentDoctorsHeading.setVisibility(View.INVISIBLE);
            btnAppointmentsFragmentGenerateCount.setVisibility(View.INVISIBLE);
            edtAppointmentsFragmentDayCount.setVisibility(View.INVISIBLE);
        }

        btnAppointmentsFragmentGenerateCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAppointments();
            }
        });

        return view;
    }

    public void readFromDatabase(){
        if(accounttype.equals("doctor")){
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("doctor_appointments").child(userId);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Appointment appointment = dataSnapshot.getValue(Appointment.class);
                        assert appointment != null;
                        mAppointments.add(appointment);
                    }
                    appointmentAdapter = new AppointmentAdapter(getContext(), mAppointments);
                    recyclerView.setAdapter(appointmentAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("active_appointments").child(userId);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Appointment appointment = dataSnapshot.getValue(Appointment.class);
                        assert appointment != null;
                        mAppointments.add(appointment);
                    }
                    appointmentAdapter = new AppointmentAdapter(getContext(), mAppointments);
                    recyclerView.setAdapter(appointmentAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public void generateAppointments(){
        deletePrevValues();
        Date now = Calendar.getInstance().getTime();
        HashMap<String, Object> appointmentsHashMap = new HashMap<>();
        dd=mm=yyyy=hr=0;
        int i = Integer.parseInt(edtAppointmentsFragmentDayCount.getText().toString());
        for(int k = 0; k< i;k++) {
            if(k==0) {
                dd = now.getDate();
                mm = now.getMonth()+1;
                yyyy = now.getYear()-100+2000;
                hr = 10;

            }else {
                if(dd==30 && mm != 12) {
                    dd = 1;
                    mm = mm+1;
                }else if(dd==30 && mm == 12) {
                    dd = 1;
                    mm = 1;
                    yyyy = yyyy+1;
                }else {
                    dd= dd+1;
                }


            }
            for(int j = 0; j<4; j++) {
                String Date,Time;
                if (j != 0) {
                    hr = hr + 2;
                }
                Date = date(dd,mm,yyyy);
                Time = time(hr);
                DatabaseReference myProfile = FirebaseDatabase.getInstance().getReference("my_users").child(userId);
                myProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users user = snapshot.getValue(Users.class);
                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("doctor_appointments").child(userId);
                        appointmentsHashMap.put("date",Date);
                        appointmentsHashMap.put("time",Time);
                        appointmentsHashMap.put("mode", user.getPreference());
                        appointmentsHashMap.put("doctorName", user.getUsername());
                        appointmentsHashMap.put("confirmed", "inactive");
                        appointmentsHashMap.put("doctorId", userId);
                        appointmentsHashMap.put("patientId","");
                        appointmentsHashMap.put("patientName","");
                        myRef.push().setValue(appointmentsHashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
            hr = 10;
        }
    }
    public static String date(int dd,int mm,int yyyy) {
        String dateV ="";
        dateV = Integer.toString(dd) +"/"+ Integer.toString(mm) +
                "/"+Integer.toString(yyyy);
        return dateV;

    }
    public static String time(int hr) {
        String timeV ="";
        if(hr<12) {
            timeV = Integer.toString(hr) + ":00:00am";
        }else {
            int newhr = hr-12;
            if(newhr == 0) {
                timeV = "1:00:00pm";
            }
            else {
                timeV = Integer.toString(hr-12) + ":00:00pm";
            }
        }
        return timeV;
    }
    public void deletePrevValues(){
        DatabaseReference newMyRef = FirebaseDatabase.getInstance().getReference("doctor_appointments").child(userId);
        newMyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    dataSnapshot.getRef().removeValue().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}