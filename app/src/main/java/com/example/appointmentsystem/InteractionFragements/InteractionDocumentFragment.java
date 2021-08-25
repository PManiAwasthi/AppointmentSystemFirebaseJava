package com.example.appointmentsystem.InteractionFragements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

import com.example.appointmentsystem.Adapters.DocumentAdapter;
import com.example.appointmentsystem.DocumentsInformation;
import com.example.appointmentsystem.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InteractionDocumentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InteractionDocumentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button btnInteractionFragmentUploadButton,btnInteractionFragmentDownloadDocument;
    Uri imageUri = null;
    String mainUserId, secondUserId, secondUserAccounttype;
    EditText edtInteractionDocumentNameOfReport;
    RecyclerView recyclerView;
    List<DocumentsInformation> mDocumentInfo;
    DocumentAdapter documentAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InteractionDocumentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InteractionDocumentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InteractionDocumentFragment newInstance(String param1, String param2) {
        InteractionDocumentFragment fragment = new InteractionDocumentFragment();
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
        View view = inflater.inflate(R.layout.fragment_interaction_document, container, false);
        secondUserAccounttype = getArguments().getString("accounttypeseconduser");
        mainUserId = getArguments().getString("mainuserid");
        secondUserId = getArguments().getString("seconduserid");
        btnInteractionFragmentUploadButton = view.findViewById(R.id.btnInteractionFragmentUploadButton);
        edtInteractionDocumentNameOfReport = view.findViewById(R.id.edtInteractionDocumentNameOfReport);
        recyclerView = view.findViewById(R.id.recyclerViewInteractionDocument);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDocumentInfo = new ArrayList<>();
        btnInteractionFragmentUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("application/pdf");
                startActivityForResult(galleryIntent,1 );
            }
        });
        readDataFromDatabase();
        return view;
    }
    ProgressDialog dialog;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Date date = Calendar.getInstance().getTime();;
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(date);
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("uploading");
            dialog.show();
            imageUri = data.getData();
            final String timestamp = ""+ System.currentTimeMillis();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            final String messagePushID = timestamp+formattedDate;
            final StorageReference filepath = storageReference.child(messagePushID + "." + "pdf");
            filepath.putFile(imageUri).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        // After uploading is done it progress
                        // dialog box will be dismissed
                        dialog.dismiss();
                        Uri uri = task.getResult();
                        String myurl;
                        Date date = Calendar.getInstance().getTime();;
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(date);
                        myurl = uri.toString();
                        if(secondUserAccounttype.equals("doctor")){
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("documents").child(mainUserId).child(secondUserId);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("imageUri",myurl);
                            hashMap.put("documentName",edtInteractionDocumentNameOfReport.getText().toString());
                            hashMap.put("createdAt",formattedDate);
                            myRef.push().setValue(hashMap);
                        }else if(secondUserAccounttype.equals("patient")){
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("documents").child(secondUserId).child(mainUserId);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("imageUri",myurl);
                            hashMap.put("documentName",edtInteractionDocumentNameOfReport.getText().toString());
                            hashMap.put("createdAt",formattedDate);
                            myRef.push().setValue(hashMap);
                        }
                        Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "UploadedFailed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }


    public void readDataFromDatabase(){
        if(secondUserAccounttype.equals("doctor")){
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("documents").child(mainUserId).child(secondUserId);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mDocumentInfo.clear();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        DocumentsInformation documentsInformation = dataSnapshot.getValue(DocumentsInformation.class);
                        assert documentsInformation != null;
                        mDocumentInfo.add(documentsInformation);
                    }
                    documentAdapter = new DocumentAdapter(getContext(), mDocumentInfo);
                    recyclerView.setAdapter(documentAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(secondUserAccounttype.equals("patient")){
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("documents").child(secondUserId).child(mainUserId);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mDocumentInfo.clear();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        DocumentsInformation documentsInformation = dataSnapshot.getValue(DocumentsInformation.class);
                        assert documentsInformation != null;
                        mDocumentInfo.add(documentsInformation);
                    }
                    documentAdapter = new DocumentAdapter(getContext(), mDocumentInfo);
                    recyclerView.setAdapter(documentAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


}