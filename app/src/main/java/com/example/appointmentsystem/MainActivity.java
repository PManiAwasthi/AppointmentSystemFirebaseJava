package com.example.appointmentsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.appointmentsystem.Adapters.UserAdapter;
import com.example.appointmentsystem.fragments.AppointedDoctors;
import com.example.appointmentsystem.fragments.AppointmentsFragment;
import com.example.appointmentsystem.fragments.DoctorsList;
import com.example.appointmentsystem.fragments.Profile;
import com.example.appointmentsystem.fragments.UserList;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Button btnLogOut;
    TabLayout tabLayout;
    ViewPager viewPager;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Bundle bundle = getIntent().getExtras();
        String accountType = bundle.getString("accounttype");
        Bundle bundle2 = new Bundle();
        bundle2.putString("userId",userId);
        bundle2.putString("accounttype",accountType);
        tabLayout = findViewById(R.id.tablayoutMainlayout);
        viewPager = findViewById(R.id.viewPagerMainLayout);
        AppointedDoctors appointedDoctors = new AppointedDoctors();
        AppointmentsFragment appointmentsFragment = new AppointmentsFragment();
        Profile profile = new Profile();
        appointedDoctors.setArguments(bundle2);
        appointmentsFragment.setArguments(bundle2);
        profile.setArguments(bundle2);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(appointedDoctors, "appointed doctors");
        viewPagerAdapter.addFragment(appointmentsFragment, "Appointments");
        if(accountType.equals("patient")){
            viewPagerAdapter.addFragment(new DoctorsList(), "doctors list");
        }else{
                viewPagerAdapter.addFragment(new UserList(), "user list");

        }


        viewPagerAdapter.addFragment(profile, "profile");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}