package com.example.appointmentsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    EditText edtSignUpEmail, edtSignUpPassword, edtSignUpDoctorId,edtSignUpUsername;
    RadioButton rbtnDoctorS, rbtnPatientS;
    TextView txtSignUpLogin;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DatabaseReference myRef;
    String doctoroOrPatient="";
    Users neededData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpPassword=findViewById(R.id.edtSignUpPassword);
        rbtnDoctorS = findViewById(R.id.rbtnDoctorS);
        rbtnPatientS = findViewById(R.id.rbtnPatientS);
        txtSignUpLogin = findViewById(R.id.txtSignUpLogin);
        edtSignUpUsername = findViewById(R.id.edtSignUpUsername);
        edtSignUpDoctorId = findViewById(R.id.editSignUpDoctorId);
        txtSignUpLogin.setOnClickListener(this::onClick);

        edtSignUpPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    signUP();
                };
                return false;
            }
        });

        rbtnPatientS.setOnClickListener(this::onRadioButtonClicked);
        rbtnDoctorS.setOnClickListener(this::onRadioButtonClicked);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtSignUpLogin: loginTransition();
                break;
            case R.id.btnSignUp:break;
        }
    }

    public void loginTransition(){
        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    public void signUP(){
        ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Logging in....");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(edtSignUpEmail.getText().toString(),edtSignUpPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userId = user.getUid();
                    HashMap<String,String> profile = new HashMap<>();
                    profile.put("id",userId);
                    profile.put("username", edtSignUpUsername.getText().toString());
                    profile.put("accounttype",doctoroOrPatient);
                    profile.put("imagelink","default");
                    profile.put("email",edtSignUpEmail.getText().toString());
                    profile.put("gender","male");
                    profile.put("phone","");
                    profile.put("age","");
                    profile.put("department","");
                    profile.put("patients","0");
                    profile.put("preference","online");
                    if(doctoroOrPatient.equals("doctor")){
                        profile.put("doctorId",edtSignUpDoctorId.getText().toString());
                    }else{
                        profile.put("doctorId","12345");
                    }
//                    if(doctoroOrPatient.equals("doctor")){
//                        Date date = Calendar.getInstance().getTime();
//                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
//                        SimpleDateFormat df2 = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
//                        String formattedTime = df2.format(date);
//                        String formattedDate = df.format(date);
//                        myRef = FirebaseDatabase.getInstance().getReference("appointmentdata").child(userId);
//                        for(int i =0; i<12;i++){
//                            HashMap<String, Object> appointment = new HashMap<>();
//                            appointment.put("doctorId", userId);
//                            appointment.put("date",formattedDate);
//                            appointment.put("patientId","");
//                            appointment.put("mode","");
//                            appointment.put("confirmed","false");
//                            appointment.put("time", formattedTime);
//                        }
//                    }

                    myRef = FirebaseDatabase.getInstance().getReference("my_users").child(userId);

                    myRef.setValue(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FancyToast.makeText(getApplicationContext(),"Welcome",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.putExtra("accounttype",doctoroOrPatient);
                                startActivity(intent);
                                FancyToast.makeText(getApplicationContext(),"Please Update your profile info",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
                                finish();
                            }
                        }
                    });


                }else{
                    FancyToast.makeText(getApplicationContext(),task.getException().toString(),FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                }
                progressDialog.dismiss();
            }
        });


    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbtnDoctorS:
                if (checked){
                    edtSignUpDoctorId.setVisibility(View.VISIBLE);
                doctoroOrPatient="doctor"; }
                break;
            case R.id.rbtnPatientS:
                if (checked)
                {edtSignUpDoctorId.setVisibility(View.INVISIBLE);
                doctoroOrPatient="patient";}
                break;
        }
    }


}