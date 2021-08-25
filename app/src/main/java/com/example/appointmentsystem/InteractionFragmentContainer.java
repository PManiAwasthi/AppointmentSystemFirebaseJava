package com.example.appointmentsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.appointmentsystem.InteractionFragements.InteractionDocumentFragment;
import com.example.appointmentsystem.InteractionFragements.InteractionProfileDoctorView;
import com.example.appointmentsystem.InteractionFragements.InteractionProfilePatientView;
import com.example.appointmentsystem.InteractionFragements.PrescriptionViewerFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.Serializable;

public class InteractionFragmentContainer extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Intent intent;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interaction_fragment_container);
        settingValues();




    }

    public void settingValues(){
        intent = getIntent();
        String userId = intent.getStringExtra("userId");



        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("my_users").child(userId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users secondUser = snapshot.getValue(Users.class);
                String mainUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                bundle.putSerializable("accounttypeseconduser", secondUser.getAccounttype());
                bundle.putString("mainuserid",mainUserId);
                bundle.putString("seconduserid",secondUser.getId());


                PrescriptionViewerFragment prescriptionViewerFragment = new PrescriptionViewerFragment();
                InteractionDocumentFragment interactionDocumentFragment = new InteractionDocumentFragment();

                prescriptionViewerFragment.setArguments(bundle);
                interactionDocumentFragment.setArguments(bundle);
                tabLayout = findViewById(R.id.tabLayoutInteractionFragment);
                viewPager = findViewById(R.id.viewPagerInteractionFragment);

                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                if(secondUser.getAccounttype().equals("doctor")){
                    InteractionProfilePatientView interactionProfilePatientView = new InteractionProfilePatientView();
                    interactionProfilePatientView.setArguments(bundle);
                    viewPagerAdapter.addFragment(interactionProfilePatientView,"Profile");
                }else{
                    InteractionProfileDoctorView interactionProfileDoctorView = new InteractionProfileDoctorView();
                    interactionProfileDoctorView.setArguments(bundle);
                    viewPagerAdapter.addFragment(interactionProfileDoctorView,"Profile");
                }

                viewPagerAdapter.addFragment(prescriptionViewerFragment,"Prescriptions");
                viewPagerAdapter.addFragment(interactionDocumentFragment,"Reports");
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}