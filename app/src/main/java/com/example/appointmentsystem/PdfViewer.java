package com.example.appointmentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class PdfViewer extends AppCompatActivity {
    PDFView pdfViewer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        Bundle bundle = getIntent().getExtras();
        Uri myUri = Uri.parse(bundle.getString("uri"));
        pdfViewer = findViewById(R.id.pdfView);
        pdfViewer.fromUri(myUri)
                .defaultPage(0)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(2)
                .load();


    }
}