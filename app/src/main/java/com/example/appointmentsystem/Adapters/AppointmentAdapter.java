package com.example.appointmentsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointmentsystem.Appointment;
import com.example.appointmentsystem.R;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>{
    private Context context;
    private List<Appointment> mAppointments;

    public AppointmentAdapter() {
    }

    public AppointmentAdapter(Context context, List<Appointment> mAppointments) {
        this.context = context;
        this.mAppointments = mAppointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_recycler_view_main_activity, parent, false);
        return new AppointmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = mAppointments.get(position);
        holder.txtAppointmentItemPatientName.setText(appointment.getPatientName());
        holder.txtAppointmentItemDate.setText(appointment.getDate());
        holder.txtAppointmentItemMode.setText(appointment.getMode());
        holder.txtAppointmentItemStatus.setText(appointment.getConfirmed());
        holder.txtAppointmentItemTime.setText(appointment.getTime());
    }

    @Override
    public int getItemCount() {
        return mAppointments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtAppointmentItemPatientName, txtAppointmentItemMode,
                txtAppointmentItemDate, txtAppointmentItemStatus,
                txtAppointmentItemTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAppointmentItemPatientName = itemView.findViewById(R.id.txtAppointmentItemPatientName);
            txtAppointmentItemMode = itemView.findViewById(R.id.txtAppointmentItemMode);
            txtAppointmentItemDate = itemView.findViewById(R.id.txtAppointmentItemDate);
            txtAppointmentItemStatus = itemView.findViewById(R.id.txtAppointmentItemStatus);
            txtAppointmentItemTime = itemView.findViewById(R.id.txtAppointmentItemTime);
        }
    }
}
