package com.example.appointmentsystem.InteractionFragements;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appointmentsystem.Adapters.MedicineAdapter;
import com.example.appointmentsystem.Medicines;
import com.example.appointmentsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrescriptionViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrescriptionViewerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    List<Medicines> mMedicines;
    MedicineAdapter medicineAdapter;
    List<String> mMedicineIds;

    TextView txtPrescriptionViewerHeading, txtPrescriptionViewerPatientHeading;
    EditText edtPrescriptionViewerDoctorMedicineName, edtPrescriptionViewerDoctorMedicineDosage;
    Button btnAdd;
    String mainUserId, secondUserId, secondUserAccountType;

    public PrescriptionViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrescriptionViewerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrescriptionViewerFragment newInstance(String param1, String param2) {
        PrescriptionViewerFragment fragment = new PrescriptionViewerFragment();
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
        View view = inflater.inflate(R.layout.fragment_prescription_viewer, container, false);
        secondUserAccountType = getArguments().getString("accounttypeseconduser");
        mainUserId = getArguments().getString("mainuserid");
        secondUserId = getArguments().getString("seconduserid");

        edtPrescriptionViewerDoctorMedicineName = view.findViewById(R.id.edtPresciptionViewerDoctorMedicineName);
        edtPrescriptionViewerDoctorMedicineDosage = view.findViewById(R.id.edtPresciptionViewerDoctorDosage);
        btnAdd = view.findViewById(R.id.btnPrescriptionViewerDoctorAdd);
        txtPrescriptionViewerHeading = view.findViewById(R.id.txtPrescriptionViewerHeading);
        txtPrescriptionViewerPatientHeading = view.findViewById(R.id.txtPrescriptionViewerPatientHeading);

        if(secondUserAccountType.equals("patient")){
            edtPrescriptionViewerDoctorMedicineDosage.setVisibility(View.VISIBLE);
            edtPrescriptionViewerDoctorMedicineName.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.VISIBLE);
            txtPrescriptionViewerHeading.setVisibility(View.VISIBLE);
            txtPrescriptionViewerPatientHeading.setVisibility(View.INVISIBLE);
        }else if(secondUserAccountType.equals("doctor")){
            edtPrescriptionViewerDoctorMedicineDosage.setVisibility(View.INVISIBLE);
            edtPrescriptionViewerDoctorMedicineName.setVisibility(View.INVISIBLE);
            btnAdd.setVisibility(View.INVISIBLE);
            txtPrescriptionViewerHeading.setVisibility(View.INVISIBLE);
            txtPrescriptionViewerPatientHeading.setVisibility(View.VISIBLE);

        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMedicine();
            }
        });

        recyclerView = view.findViewById(R.id.recyclerViewPrescriptionViewerDoctor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMedicines = new ArrayList<>();
        mMedicineIds = new ArrayList<>();
        readMedicines();
        return view;
    }

    void addMedicine(){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(date);
        //String toWrite = edtPrescriptionViewerDoctorMedicineName.getText().toString() + " x " + edtPrescriptionViewerDoctorMedicineDosage.getText().toString();
        if(secondUserAccountType.equals("patient")){
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("prescription").child(mainUserId).child(secondUserId);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("medicinename",edtPrescriptionViewerDoctorMedicineName.getText().toString());
            hashMap.put("medicinedosage",edtPrescriptionViewerDoctorMedicineDosage.getText().toString());
            hashMap.put("date",formattedDate);
            myRef.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        FancyToast.makeText(getContext(),"Value added",FancyToast.SUCCESS,FancyToast.LENGTH_LONG,false).show();
                    }else{
                        FancyToast.makeText(getContext(),task.getException().toString(),FancyToast.SUCCESS,FancyToast.LENGTH_LONG,false).show();
                    }
                }
            });
        }
    }

    void readMedicines(){
        if(secondUserAccountType.equals("patient")){

            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("prescription").child(mainUserId).child(secondUserId);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mMedicines.clear();
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Medicines medicines = dataSnapshot.getValue(Medicines.class);
                        assert medicines!=null;
                        String id = dataSnapshot.getKey();
                        mMedicineIds.add(id);
                        mMedicines.add(medicines);

                    }
                    medicineAdapter = new MedicineAdapter(getContext(),mMedicines,mMedicineIds, mainUserId,secondUserAccountType,secondUserId);
                    recyclerView.setAdapter(medicineAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    FancyToast.makeText(getContext(),error.getMessage(),FancyToast.ERROR, FancyToast.LENGTH_LONG,false).show();
                }
            });
        }else if(secondUserAccountType.equals("doctor")){

                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("prescription").child(secondUserId).child(mainUserId);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mMedicines.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            Medicines medicines = dataSnapshot.getValue(Medicines.class);
                            assert medicines!=null;
                            String id = dataSnapshot.getKey();
                            mMedicineIds.add(id);
                            mMedicines.add(medicines);

                        }
                        Collections.reverse(mMedicines);
                        medicineAdapter = new MedicineAdapter(getContext(),mMedicines, mMedicineIds, mainUserId,secondUserAccountType,secondUserId);
                        recyclerView.setAdapter(medicineAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        FancyToast.makeText(getContext(),error.getMessage(),FancyToast.ERROR, FancyToast.LENGTH_LONG,false).show();
                    }
                });
            }

    }
}