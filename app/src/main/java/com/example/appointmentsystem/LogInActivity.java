package com.example.appointmentsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{
    EditText edtLoginEmail, edtLoginPassword, edtDoctorId;
    RadioButton rbtnDoctor, rbtnPatient;
    RadioGroup radioGroupLogin;
    TextView txtRegister;
    Button btnLogin;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Users neededData = new Users();
    String doctorOrPatient,userId;
    private DatabaseReference myRef;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null){
            mAuth.signOut();
            //getData(mUser.getUid());
//            goToTest(mUser);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtLoginEmail = findViewById(R.id.edtLogInEmail);
        edtLoginPassword = findViewById(R.id.edtLogInPassword);
        edtDoctorId = findViewById(R.id.edtDoctorId);
        txtRegister = findViewById(R.id.txtRegister);
        btnLogin = findViewById(R.id.btnLogIn);
        rbtnDoctor = (RadioButton)findViewById(R.id.rbtnDoctor);
        rbtnPatient = (RadioButton)findViewById(R.id.rbtnPatient);
        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    login();
                };
                return false;
            }
        });
        btnLogin.setOnClickListener(this::onClick);
        rbtnDoctor.setOnClickListener(this::onRadioButtonClicked);
        rbtnPatient.setOnClickListener(this::onRadioButtonClicked);

        txtRegister.setOnClickListener(this::onClick);

    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbtnDoctor:
                if (checked) {
                    edtDoctorId.setVisibility(View.VISIBLE);
                    doctorOrPatient="doctor";
                }
                    break;
            case R.id.rbtnPatient:
                if (checked) {
                    edtDoctorId.setVisibility(View.INVISIBLE);
                    doctorOrPatient="patient";
                }
                    break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtRegister:signUpTransition();
                break;
            case R.id.btnLogIn:login();
            break;
        }
    }
    public void signUpTransition(){
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
    public void login(){
        ProgressDialog progressDialog = new ProgressDialog(LogInActivity.this);
        progressDialog.setMessage("Logging In...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(edtLoginEmail.getText().toString(),edtLoginPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser myUser = mAuth.getCurrentUser();
                    String userid2 = myUser.getUid();
                    if(doctorOrPatient.equals("doctor")){
                        myRef = FirebaseDatabase.getInstance().getReference("my_users").child(userid2);
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Users user = snapshot.getValue(Users.class);
                                if(user.getDoctorId().equals(edtDoctorId.getText().toString())){
                                    goToTest(doctorOrPatient);
                                    FancyToast.makeText(getApplicationContext(),"Welcome",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                                }else{
                                    FancyToast.makeText(getApplicationContext(),"Doctor Id not found please enter again",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                                    edtDoctorId.setText("");

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else{
                        goToTest(doctorOrPatient);
                        //FancyToast.makeText(getApplicationContext(),"Welcome",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                    }



                }else{
                    Toast.makeText(LogInActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }

        });

    }
    public void goToTest(String accounttype){
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        Log.d("new",accounttype);
        intent.putExtra("accounttype", accounttype);
        startActivity(intent);
        finish();
    }

}