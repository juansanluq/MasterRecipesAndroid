package com.example.masterrecipesandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.masterrecipesandroid.Adaptadores.RecetasAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class NuevaReceta extends AppCompatActivity {

    @BindView(R.id.imgv)
    ImageView imgv;
    @BindView(R.id.edtNombre)
    EditText edtNombre;
    @BindView(R.id.txvCategoria)
    TextView txvCategoria;
    @BindView(R.id.txvDificultad)
    TextView txvDificultad;
    @BindView(R.id.txvPDF)
    TextView txvPDF;
    Bitmap photo;
    File PDF;
    String extesionPDF;
    int Categoria;
    int Dificultad;

    int CAMERA_REQUEST = 0;
    int RESULT_LOAD_IMG = 1;
    int PICKFILE_REQUEST_CODE = 2;
    @BindView(R.id.btnGuardar)
    Button btnGuardar;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_receta);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.imgv, R.id.edtNombre, R.id.txvCategoria, R.id.txvDificultad, R.id.txvPDF})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgv:
                AlertDialog.Builder builder = new AlertDialog.Builder(NuevaReceta.this);
                builder.setTitle("Selecciona método de entrada")
                        .setItems(R.array.opcionesEntrada, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                }
                                if (which == 1) {
                                    if (checkPermission()) {
                                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                        intent.setType("image/*");
                                        startActivityForResult(intent, RESULT_LOAD_IMG);
                                    }
                                }
                            }
                        });
                builder.create().show();
                break;
            case R.id.edtNombre:
                break;
            case R.id.txvCategoria:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(NuevaReceta.this);
                builder1.setTitle("Selecciona la categoría de la receta")
                        .setItems(R.array.Categoria, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                txvCategoria.setText(RecetasAdapter.getCategoria(which));
                                Categoria = which;
                            }
                        });
                builder1.create().show();
                break;
            case R.id.txvDificultad:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(NuevaReceta.this);
                builder2.setTitle("Selecciona la dificultad de la receta")
                        .setItems(R.array.Dificultad, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                txvDificultad.setText(RecetasAdapter.getDificultad(which));
                                Dificultad = which;
                            }
                        });
                builder2.create().show();
                break;
            case R.id.txvPDF:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
                break;
        }
    }

    public boolean checkPermission() {
        /* Se compureba de que la SDK es superior a marshmallow, pues si es inferior no es necesario
         * pedir permisos */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                /* En caso de no haber cargado correctamente los permisos se avisa con
                 * un Toast y se piden */
                Toast.makeText(getApplicationContext(), "Error al cargar permisos", Toast.LENGTH_LONG).show();
                requestPermissions(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 100);
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imgv.setImageBitmap(photo);
        }
        if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imgv.setImageBitmap(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            try
            {
                Uri uri2 = data.getData();
                PDF = new File(uri2.getPath());
                txvPDF.setText(getFileName(uri2));
                extesionPDF = getMimeType(getApplicationContext(),uri2);
                String sd = "";
            }catch (RuntimeException e)
            {

            }
        }
    }

    @OnClick(R.id.btnGuardar)
    public void onViewClicked() {
        String mensaje = "";
        if(photo == null)
        {
            mensaje = mensaje + "Debes seleccionar una imagen";
        }
        else if (edtNombre.getText().toString().isEmpty() || txvCategoria.getText().toString().equals("Seleccionar categoria") || txvDificultad.getText().toString().equals("Seleccionar dificultad"))
        {
            mensaje = mensaje + "Debes rellenar todos los campos";
        }
        else if (PDF == null)
        {
            mensaje = mensaje + "Debes elegir un archivo PDF para subir";
        }
        else if(!extesionPDF.equals("pdf"))
        {
            mensaje = mensaje + "Solo se pueden seleccionar archivos PDF";
        }

        if (!mensaje.equals(""))
        {
            Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog = new ProgressDialog(NuevaReceta.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Registrando usuario...");
            progressDialog.show();
            new crear_receta().execute();
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    private class crear_receta extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String url = Login.base_url + "/api/1.0/recetas/crear";
            VolleyMultipartRequest sr = new VolleyMultipartRequest(Request.Method.POST, url,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Toast.makeText(getApplicationContext(),"Receta creada correctamente, ya puedes iniciar sesión",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // As of f605da3 the following should work
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    String creador = String.valueOf(Login.loggedUser.getId());
                    params.put("creador", String.valueOf(Login.loggedUser.getId()));
                    params.put("nombre", edtNombre.getText().toString());
                    params.put("categoria", String.valueOf(Categoria));
                    params.put("dificultad", String.valueOf(Dificultad));

                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("imagen", new DataPart(edtNombre.getText().toString().trim() + ".png", Registro.getFileDataFromDrawable(photo)));
                    params.put("pdf", new DataPart(edtNombre.getText().toString().trim() + ".pdf",convertFileToByteArray(PDF)));
                    return params;
                }

            };
            requestQueue.add(sr);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }
    }

    public static byte[] convertFileToByteArray(File f)
    {
        byte[] byteArray = null;
        try
        {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024*8];
            int bytesRead =0;

            while ((bytesRead = inputStream.read(b)) != -1)
            {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return byteArray;
    }
}
