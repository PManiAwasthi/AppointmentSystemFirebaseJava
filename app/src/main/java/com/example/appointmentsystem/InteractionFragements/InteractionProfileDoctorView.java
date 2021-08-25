package com.example.appointmentsystem.InteractionFragements;

import android.app.Fragment;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.appointmentsystem.R;
import com.example.appointmentsystem.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InteractionProfileDoctorView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InteractionProfileDoctorView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView txtInteractionPatientName, txtInteractionPatientAge, txtInteractionPatientGender, txtInteractionContactNumber;
    RadioButton rdbtnInteractionPatientOnline, rdbtnInteractionPatientOffline, rdbtnInteractionPatientBoth;
    String mainUserId, secondUserId, secondUserAccounttype;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InteractionProfileDoctorView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InteractionProfileDoctorView.
     */
    // TODO: Rename and change types and number of parameters
    public static InteractionProfileDoctorView newInstance(String param1, String param2) {
        InteractionProfileDoctorView fragment = new InteractionProfileDoctorView();
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
        View view = inflater.inflate(R.layout.fragment_interaction_profile_doctor_view, container, false);
        secondUserAccounttype = getArguments().getString("accounttypeseconduser");
        mainUserId = getArguments().getString("mainuserid");
        secondUserId = getArguments().getString("seconduserid");


        txtInteractionPatientName = view.findViewById(R.id.txtInteractionPatientName);
        txtInteractionPatientAge = view.findViewById(R.id.txtInteractionPatientAge);
        txtInteractionContactNumber = view.findViewById(R.id.txtInteractionPatientContactNumber);
        txtInteractionPatientGender = view.findViewById(R.id.txtInteractionPatientGender);
        rdbtnInteractionPatientOnline = view.findViewById(R.id.rdbtnInteractionPatientOnline);
        rdbtnInteractionPatientOffline = view.findViewById(R.id.rdbtnInteractionPatientOffline);
        rdbtnInteractionPatientBoth = view.findViewById(R.id.rdbtnInteractionPatientBoth);
        rdbtnInteractionPatientOnline.setEnabled(false);
        rdbtnInteractionPatientOffline.setEnabled(false);
        rdbtnInteractionPatientBoth.setEnabled(false);
        getValuesComponent();

        return view;
    }


    public void getValuesComponent(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("my_users").child(secondUserId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                txtInteractionPatientName.setText(user.getUsername());
                txtInteractionPatientGender.setText(user.getGender());
                txtInteractionContactNumber.setText(user.getPhone());
                txtInteractionPatientAge.setText(user.getAge());
                switch (user.getPreference()){
                    case "online":rdbtnInteractionPatientOnline.setChecked(true);
                    rdbtnInteractionPatientOffline.setChecked(false);
                    rdbtnInteractionPatientBoth.setChecked(false);
                    break;
                    case "offline":rdbtnInteractionPatientOnline.setChecked(false);
                    rdbtnInteractionPatientOffline.setChecked(true);
                    rdbtnInteractionPatientBoth.setChecked(false);
                    break;
                    case "both":rdbtnInteractionPatientOnline.setChecked(false);
                    rdbtnInteractionPatientOffline.setChecked(false);
                    rdbtnInteractionPatientBoth.setChecked(true);
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                FancyToast.makeText(getContext(), error.getMessage(),FancyToast.ERROR,FancyToast.LENGTH_LONG,false).show();
            }
        });

    }
}