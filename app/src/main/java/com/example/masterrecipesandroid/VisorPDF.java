package com.example.masterrecipesandroid;

import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.example.masterrecipesandroid.Adaptadores.RecetasAdapter;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class VisorPDF extends AppCompatActivity {


    private PDFView pdfView;
    private Receta recetaSeleccionada;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_pdf);
        recetaSeleccionada = RecetasAdapter.recetaVisor;

        pdfView = findViewById(R.id.pdfView);

        final String path = Environment.getExternalStorageDirectory().toString();
        PRDownloader.initialize(getApplicationContext());

        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);

        int downloadId = PRDownloader.download(recetaSeleccionada.getPdf(), path, "receta2.pdf")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                })
                .start(new OnDownloadListener() {

                    @Override
                    public void onDownloadComplete() {
                        file = new File(path + "/" + "receta2.pdf");
                        pdfView.fromFile(file) //FROM FILE
                                .enableSwipe(true)
                                .enableDoubletap(true)
                                .enableAntialiasing(true)
                                .load();
                        //file.delete();
                    }

                    @Override
                    public void onError(Error error) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        file.delete();
    }
}
