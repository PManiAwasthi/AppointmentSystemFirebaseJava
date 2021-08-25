package com.example.appointmentsystem.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointmentsystem.Medicines;
import com.example.appointmentsystem.R;
import com.example.appointmentsystem.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    private Context context;
    private List<Medicines> mMedicines;
    private List<String> mMedicineIds;
    private String mainUserId, secondUserId, secondUserAccountType;


    public MedicineAdapter() {
    }

    public MedicineAdapter(Context context, List<Medicines> mMedicines, List<String> mMedicineIds,
                           String mainUserId,String secondUserAccountType, String secondUserId) {
        this.context = context;
        this.mMedicines = mMedicines;
        this.mMedicineIds = mMedicineIds;
        this.mainUserId = mainUserId;
        this.secondUserAccountType = secondUserAccountType;
        this.secondUserId = secondUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.medicine_info,parent,false);
        return new MedicineAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicines medicines = mMedicines.get(position);
        holder.txtMedicineInfoDate.setText(medicines.getDate());
        holder.txtMedicineInfoName.setText(medicines.getMedicinename());
        holder.txtMedicineInfoDosage.setText(medicines.getMedicinedosage() + " times");
        holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String id = mMedicineIds.get(position);
                holder.itemView.setBackgroundColor(Color.parseColor("#AEAEAE"));
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Delete entry");
                alertDialog.setMessage("Are you sure you want to delete this entry?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(secondUserAccountType.equals("doctor")){
                            deleteRecord(mainUserId, secondUserId, id);
                        }else{
                            deleteRecord(secondUserId, mainUserId, id);
                        }
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                });
                alertDialog.show();


                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMedicines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtMedicineInfoDate, txtMedicineInfoName, txtMedicineInfoDosage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMedicineInfoDate = itemView.findViewById(R.id.txtMedicineInfoDate);
            txtMedicineInfoName = itemView.findViewById(R.id.txtMedicineInfoName);
            txtMedicineInfoDosage = itemView.findViewById(R.id.txtMedicineInfoDosage);

        }
    }

    public void deleteRecord(String userId1, String userId2, String toDelete){
        Log.d("newworld",userId1);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("prescription").child(userId2).child(userId1).child(toDelete);
        myRef.removeValue();

    }
}
