package com.example.appointmentsystem.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointmentsystem.R;
import com.example.appointmentsystem.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText edtUserProfileUsername, edtUserProfileEmail, edtUserProfilePhone,edtUserProfileAge,
    edtUserProfileDepartment, edtUserProfileNumberOfPatients;
    public static Users profile = new Users();

    RadioButton rdbtnUserProfileMale, rdbtnUserProfileFemale, rdbtnUserProfileOthers,rdbtnUserProfileOnline,
            rdbtnUserProfileOffline, rdbtnUserProfileBoth;

    ImageView imageViewProfileView;

    Button btnUserProfileSave;

    TextView txtViewUserProfileEdit, txtUserProfileNumberOfPatients;
    String userId, accounttype;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userId = getArguments().getString("userId");
        accounttype = getArguments().getString("accounttype");
        edtUserProfileAge = view.findViewById(R.id.edtUserProfileAge);
        edtUserProfileDepartment = view.findViewById(R.id.edtUserProfileDepartment);
        edtUserProfileEmail = view.findViewById(R.id.edtUserProfileEmail);
        edtUserProfileNumberOfPatients = view.findViewById(R.id.edtUserProfileNumberOfPatients);
        edtUserProfilePhone = view.findViewById(R.id.edtUserProfilePhone);
        edtUserProfileUsername = view.findViewById(R.id.edtUserProfileUsername);

        rdbtnUserProfileMale = view.findViewById(R.id.rdbtnUserProfileMale);
        rdbtnUserProfileFemale = view.findViewById(R.id.rdbtnUserProfileFemale);
        rdbtnUserProfileOthers = view.findViewById(R.id.rdbtnUserProfileOthers);
        rdbtnUserProfileOnline = view.findViewById(R.id.rdbtnUserProfileOnline);
        rdbtnUserProfileOffline = view.findViewById(R.id.rdbtnUserProfileOffline);
        rdbtnUserProfileBoth = view.findViewById(R.id.rdbtnUserProfileBoth);

        txtViewUserProfileEdit = view.findViewById(R.id.txtViewUserProfileEdit);

        btnUserProfileSave = view.findViewById(R.id.btnUserProfileSave);
        txtUserProfileNumberOfPatients = view.findViewById(R.id.txtUserProfileNumberOfPatients);

        imageViewProfileView = view.findViewById(R.id.imageViewUserProfile);
        if(accounttype.equals("doctor")){
            edtUserProfileDepartment.setVisibility(View.VISIBLE);
            edtUserProfileNumberOfPatients.setVisibility(View.VISIBLE);
            txtUserProfileNumberOfPatients.setVisibility(View.VISIBLE);
        }else if(accounttype.equals("patient")){
            edtUserProfileDepartment.setVisibility(View.INVISIBLE);
            edtUserProfileNumberOfPatients.setVisibility(View.INVISIBLE);
            txtUserProfileNumberOfPatients.setVisibility(View.INVISIBLE);
        }

        setElementsDisabled();

        imageViewProfileView.setImageResource(R.mipmap.ic_person);
        btnUserProfileSave.setOnClickListener(this::onClick);
        txtViewUserProfileEdit.setOnClickListener(this::onClick);
        saveProfile(userId);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.txtViewUserProfileEdit:setElementsEnabled();
                break;
            case R.id.btnUserProfileSave:setElementsDisabled();
            setValues();
                break;
        }
    }

    public void setElementsEnabled(){
        edtUserProfileAge.setInputType(InputType.TYPE_CLASS_TEXT);
        edtUserProfileDepartment.setInputType(InputType.TYPE_CLASS_TEXT);
        edtUserProfilePhone.setInputType(InputType.TYPE_CLASS_TEXT);
        edtUserProfileUsername.setInputType(InputType.TYPE_CLASS_TEXT);
        btnUserProfileSave.setEnabled(true);
        rdbtnUserProfileOthers.setEnabled(true);
        rdbtnUserProfileMale.setEnabled(true);
        rdbtnUserProfileFemale.setEnabled(true);
        rdbtnUserProfileOnline.setEnabled(true);
        rdbtnUserProfileOffline.setEnabled(true);
        rdbtnUserProfileBoth.setEnabled(true);
        FancyToast.makeText(getContext(), "Now you can change the details", FancyToast.INFO,FancyToast.LENGTH_LONG,false).show();

    }

    public void setElementsDisabled(){
        edtUserProfileAge.setInputType(InputType.TYPE_NULL);
        edtUserProfileDepartment.setInputType(InputType.TYPE_NULL);
        edtUserProfileEmail.setInputType(InputType.TYPE_NULL);
        edtUserProfileNumberOfPatients.setInputType(InputType.TYPE_NULL);
        edtUserProfilePhone.setInputType(InputType.TYPE_NULL);
        edtUserProfileUsername.setInputType(InputType.TYPE_NULL);
        rdbtnUserProfileOnline.setEnabled(false);
        rdbtnUserProfileOffline.setEnabled(false);
        rdbtnUserProfileBoth.setEnabled(false);
        rdbtnUserProfileOthers.setEnabled(false);
        rdbtnUserProfileMale.setEnabled(false);
        rdbtnUserProfileFemale.setEnabled(false);
        btnUserProfileSave.setEnabled(false);

    }

    public void saveProfile(String userId){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("my_users").child(userId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    profile = snapshot.getValue(Users.class);
                    edtUserProfileAge.setText(profile.getAge());
                    edtUserProfileEmail.setText(profile.getEmail());
                    edtUserProfilePhone.setText(profile.getPhone());
                    edtUserProfileDepartment.setText(profile.getDepartment());
                    edtUserProfileNumberOfPatients.setText(profile.getPatient());
                    edtUserProfileUsername.setText(profile.getUsername());
                    if(profile.getImagelink().equals("default")){

                    }else{
                        
                    }
                    switch (profile.getGender()){
                        case "male":rdbtnUserProfileMale.setChecked(true);
                        rdbtnUserProfileFemale.setChecked(false);
                        rdbtnUserProfileOthers.setChecked(false);
                        break;
                        case "female":rdbtnUserProfileMale.setChecked(false);
                            rdbtnUserProfileFemale.setChecked(true);
                            rdbtnUserProfileOthers.setChecked(false);
                            break;
                        case "other":rdbtnUserProfileMale.setChecked(false);
                            rdbtnUserProfileFemale.setChecked(false);
                            rdbtnUserProfileOthers.setChecked(true);
                            break;
                    }
                    switch (profile.getPreference()){
                        case "online":rdbtnUserProfileOnline.setChecked(true);
                            rdbtnUserProfileOffline.setChecked(false);
                            rdbtnUserProfileBoth.setChecked(false);
                            break;
                        case "offline":rdbtnUserProfileOnline.setChecked(false);
                            rdbtnUserProfileOffline.setChecked(true);
                            rdbtnUserProfileBoth.setChecked(false);
                            break;
                        case "both":rdbtnUserProfileOnline.setChecked(false);
                            rdbtnUserProfileOffline.setChecked(false);
                            rdbtnUserProfileBoth.setChecked(true);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void setValues(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("my_users").child(userId);
        HashMap<String, Object> profile = new HashMap<>();
        profile.put("username", edtUserProfileUsername.getText().toString());
        if(rdbtnUserProfileMale.isChecked()){
            profile.put("gender","male");
        }else if(rdbtnUserProfileFemale.isChecked()){
            profile.put("gender","female");
        }else if(rdbtnUserProfileOthers.isChecked()){
            profile.put("gender","others");
        }
        if(rdbtnUserProfileOnline.isChecked()){
            profile.put("preference","online");
        }else if(rdbtnUserProfileOffline.isChecked()){
            profile.put("preference","offline");
        }else if(rdbtnUserProfileBoth.isChecked()){
            profile.put("preference","both");
        }

        profile.put("phone",edtUserProfilePhone.getText().toString());
        profile.put("age",edtUserProfileAge.getText().toString());
        profile.put("department",edtUserProfileDepartment.getText().toString());
        myRef.updateChildren(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FancyToast.makeText(getContext(),"Values saved successfully",FancyToast.SUCCESS,FancyToast.LENGTH_LONG,true).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FancyToast.makeText(getContext(),e.getMessage(),FancyToast.ERROR,FancyToast.LENGTH_LONG,true).show();
            }
        });


    }
}