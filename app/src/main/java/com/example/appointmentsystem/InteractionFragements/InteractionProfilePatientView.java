package com.example.appointmentsystem.InteractionFragements;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
 * Use the {@link InteractionProfilePatientView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InteractionProfilePatientView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView txtInteractionDoctorName, txtInteractionDoctorDepartment, txtInteractionDoctorNumPatient,
            txtInteractionDoctorGender,txtInteractionDoctorContactNumber;

    RadioButton rdbtnInteractionDoctorOnline, rdbtnInteractionDoctorOffline,rdbtnInteractionDoctorBoth;
    ImageView imageViewInteractionDoctor;
    Button btnInteractionFragmentPatientViewGetAppointment;

    String mainUserId,secondUserId,secondUserAccountType;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InteractionProfilePatientView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InteractionProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static InteractionProfilePatientView newInstance(String param1, String param2) {
        InteractionProfilePatientView fragment = new InteractionProfilePatientView();
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
        View view = inflater.inflate(R.layout.fragment_interaction_profile_patient_view, container, false);
        secondUserAccountType = getArguments().getString("accounttypeseconduser");
        mainUserId = getArguments().getString("mainuserid");
        secondUserId = getArguments().getString("seconduserid");
        txtInteractionDoctorName = view.findViewById(R.id.txtInteractionDoctorName);
        txtInteractionDoctorDepartment = view.findViewById(R.id.txtInteractionDoctorDepartment);
        txtInteractionDoctorGender = view.findViewById(R.id.txtInteractionDoctorGender);
        txtInteractionDoctorContactNumber =view.findViewById(R.id.txtInteractionDoctorContactNumber);
        txtInteractionDoctorNumPatient = view.findViewById(R.id.txtInteractionDoctorNumPatient);
        rdbtnInteractionDoctorOnline = view.findViewById(R.id.rdbtnInteractionDoctorOnline);
        rdbtnInteractionDoctorOffline = view.findViewById(R.id.rdbtnInteractionDoctorOffline);
        rdbtnInteractionDoctorBoth= view.findViewById(R.id.rdbtnInteractionDoctorBoth);
        imageViewInteractionDoctor = view.findViewById(R.id.imageViewInteractionDoctor);
        imageViewInteractionDoctor.setImageResource(R.mipmap.ic_person_round);
        btnInteractionFragmentPatientViewGetAppointment = view.findViewById(R.id.btnInteractionFragmentPatientViewGetAppointment);
        btnInteractionFragmentPatientViewGetAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("accounttypeseconduser",secondUserAccountType);
                bundle.putString("mainuserid",mainUserId);
                bundle.putString("seconduserid", secondUserId);
                Intent intent = new Intent(getContext(), PatientViewGetAppointmentActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        rdbtnInteractionDoctorBoth.setEnabled(false);
        rdbtnInteractionDoctorOffline.setEnabled(false);
        rdbtnInteractionDoctorOnline.setEnabled(false);
        getValues();

        //Toast.makeText(getContext(),userId,Toast.LENGTH_LONG).show();
        return view;
    }

    public void getValues(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("my_users").child(secondUserId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users temp = snapshot.getValue(Users.class);

                txtInteractionDoctorName.setText(temp.getUsername());
                txtInteractionDoctorGender.setText(temp.getGender());
                txtInteractionDoctorDepartment.setText(temp.getDepartment());
                txtInteractionDoctorContactNumber.setText(temp.getPhone());
                txtInteractionDoctorNumPatient.setText(temp.getPatient());
                switch (temp.getPreference()){
                    case "online":rdbtnInteractionDoctorOnline.setChecked(true);
                        rdbtnInteractionDoctorOffline.setChecked(false);
                        rdbtnInteractionDoctorBoth.setChecked(false);
                        break;
                    case "offline":rdbtnInteractionDoctorOnline.setChecked(false);
                        rdbtnInteractionDoctorOffline.setChecked(true);
                        rdbtnInteractionDoctorBoth.setChecked(false);
                        break;
                    case "both":rdbtnInteractionDoctorOnline.setChecked(false);
                        rdbtnInteractionDoctorOffline.setChecked(false);
                        rdbtnInteractionDoctorBoth.setChecked(true);
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