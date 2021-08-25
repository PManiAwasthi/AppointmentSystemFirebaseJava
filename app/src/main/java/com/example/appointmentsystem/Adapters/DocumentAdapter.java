package com.example.appointmentsystem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointmentsystem.DocumentsInformation;
import com.example.appointmentsystem.PdfViewer;
import com.example.appointmentsystem.R;

import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder>{
    private Context context;
    private List<DocumentsInformation> mDocumentsInformation;
    public DocumentAdapter(Context context, List<DocumentsInformation> mDocumentsInformation) {
        this.context = context;
        this.mDocumentsInformation = mDocumentsInformation;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.document_info, parent, false);

        return new DocumentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DocumentsInformation documentsInformation = mDocumentsInformation.get(position);
        holder.textViewInteractionDocumentFragmentDocumentName.setText(documentsInformation.getDocumentName());
        holder.textViewInteractionDocumentFragmentDocumentDate.setText(documentsInformation.getCreatedAt());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent (context, PdfViewer.class);
//                intent.putExtra("uri", documentsInformation.getImageUri());
//                context.startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("application/pdf");
                intent.setData(Uri.parse(documentsInformation.getImageUri()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDocumentsInformation.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewInteractionDocumentFragmentDocumentName, textViewInteractionDocumentFragmentDocumentDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewInteractionDocumentFragmentDocumentDate = itemView.findViewById(R.id.textViewInteractionDocumentFragmentDocumentDate);
            textViewInteractionDocumentFragmentDocumentName = itemView.findViewById(R.id.textViewInteractionDocumentFragmentDocumentName);

        }
    }
}
