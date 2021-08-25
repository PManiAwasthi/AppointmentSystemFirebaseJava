package com.example.appointmentsystem.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointmentsystem.Appointment;
import com.example.appointmentsystem.R;
import com.example.appointmentsystem.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class PatientViewAppointmentAdapter extends RecyclerView.Adapter<PatientViewAppointmentAdapter.ViewHolder>{
    Context context;
    List<Appointment> mAppointments;
    List<String> mAppointmentIds;

    public PatientViewAppointmentAdapter() {
    }

    public PatientViewAppointmentAdapter(Context context, List<Appointment> mAppointments, List<String> mAppointmentIds) {

        this.context = context;
        this.mAppointments = mAppointments;
        this.mAppointmentIds = mAppointmentIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_patient_view_get_appointment_box, parent, false);
        return new PatientViewAppointmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = mAppointments.get(position);
        String appointmentID = mAppointmentIds.get(position);
        holder.txtPatientViewGetAppointmentBoxDate.setText(appointment.getDate());
        holder.txtPatientViewGetAppointmentBoxTime.setText(appointment.getTime());
        if(appointment.getConfirmed().equals("confirmed")){
            holder.constraintLayout.setBackgroundColor(Color.parseColor("#AEAEAE"));
        }else{
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAppointment(appointment, appointmentID);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mAppointments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtPatientViewGetAppointmentBoxDate,txtPatientViewGetAppointmentBoxTime;
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPatientViewGetAppointmentBoxDate = itemView.findViewById(R.id.txtPatientViewGetAppointmentBoxDate);
            txtPatientViewGetAppointmentBoxTime = itemView.findViewById(R.id.txtPatientViewGetAppointmentBoxTime);
            constraintLayout = itemView.findViewById(R.id.constraintLayoutPatientView);
        }
    }

    public void setAppointment(Appointment appointment, String appointmentID){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String doctorId = appointment.getDoctorId();
        DatabaseReference myProfile = FirebaseDatabase.getInstance().getReference("my_users").child(userId);
        myProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                String userName = user.getUsername();
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("doctor_appointments").child(doctorId).child(appointmentID);
                HashMap<String, Object> newValues = new HashMap<>();
                newValues.put("date",appointment.getDate());
                newValues.put("time",appointment.getTime());
                newValues.put("mode", appointment.getMode());
                newValues.put("doctorName", appointment.getDoctorName());
                newValues.put("confirmed", "confirmed");
                newValues.put("doctorId", doctorId);
                newValues.put("patientId",userId);
                newValues.put("patientName",userName);
                myRef.updateChildren(newValues);

                DatabaseReference myActiveAppointments = FirebaseDatabase.getInstance().getReference("active_appointments").child(userId);
                myActiveAppointments.push().setValue((newValues));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
