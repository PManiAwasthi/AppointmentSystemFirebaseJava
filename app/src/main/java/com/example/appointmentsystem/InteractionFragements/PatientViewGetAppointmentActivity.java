package com.example.appointmentsystem.InteractionFragements;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.appointmentsystem.Adapters.PatientViewAppointmentAdapter;
import com.example.appointmentsystem.Appointment;
import com.example.appointmentsystem.R;
import com.google.android.gms.common.annotation.KeepForSdkWithFieldsAndMethods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class PatientViewGetAppointmentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Appointment> mAppointments;
    List<String> mAppointmentIds;
    PatientViewAppointmentAdapter patientViewAppointmentAdapter;
    String secondUserAccountType, mainUserId, secondUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_get_appointment);

        Bundle bundle = getIntent().getExtras();
        secondUserAccountType = bundle.getString("accounttypeseconduser");
        mainUserId = bundle.getString("mainuserid");
        secondUserId = bundle.getString("seconduserid");
        recyclerView = findViewById(R.id.recyclerViewGetAppoitmentsPatientView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAppointments = new ArrayList<>();
        mAppointmentIds = new ArrayList<>();
        readDate();

    }

    public void readDate(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("doctor_appointments").child(secondUserId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mAppointments.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                    String appointmentId = dataSnapshot.getKey();
                    assert appointment != null;
                    assert appointmentId != null;
                    mAppointments.add(appointment);
                    mAppointmentIds.add(appointmentId);

                }
                patientViewAppointmentAdapter = new PatientViewAppointmentAdapter(getApplicationContext(), mAppointments, mAppointmentIds);
                recyclerView.setAdapter(patientViewAppointmentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}