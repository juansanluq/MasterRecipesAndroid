package com.example.masterrecipesandroid.Actividades;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.masterrecipesandroid.Login;
import com.example.masterrecipesandroid.MapFragment;
import com.example.masterrecipesandroid.Principal;
import com.example.masterrecipesandroid.R;
import com.squareup.picasso.Picasso;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsuarioDetalle extends AppCompatActivity {


    @BindView(R.id.header_cover_image)
    ImageView headerCoverImage;
    @BindView(R.id.user_profile_photo)
    CircleImageView userProfilePhoto;
    @BindView(R.id.user_profile_name)
    TextView userProfileName;
    @BindView(R.id.user_profile_short_bio)
    TextView userProfileShortBio;
    @BindView(R.id.profile_layout)
    RelativeLayout profileLayout;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.llamar)
    LinearLayout llamar;
    @BindView(R.id.sms)
    LinearLayout sms;
    @BindView(R.id.email)
    LinearLayout email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_detalle);
        ButterKnife.bind(this);

        Picasso.with(getApplicationContext()).load(MapFragment.usuarioSeleccionado.getFoto()).into(userProfilePhoto);
        userProfileName.setText(MapFragment.usuarioSeleccionado.getNombre());
        userProfileShortBio.setText(MapFragment.usuarioSeleccionado.getComentarios());
    }

    @OnClick({R.id.llamar, R.id.sms, R.id.email})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llamar:
                String telefono = "tel:" + MapFragment.usuarioSeleccionado.getNumeroTelefono();
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(telefono)));
                break;
            case R.id.sms:
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialogo_sms, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.editText);

                dialogBuilder.setTitle("Enviar SMS");
                dialogBuilder.setMessage("Introduce el texto a enviar");
                dialogBuilder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(MapFragment.usuarioSeleccionado.getNumeroTelefono(), null, edt.getText().toString() , null, null);
                        Toast.makeText(getApplicationContext(),"Mensaje enviado!",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
                break;
            case R.id.email:
                String[] TO = {MapFragment.usuarioSeleccionado.getEmail()};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");

                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Mensaje de " + Login.loggedUser.getUsername());
                startActivity(Intent.createChooser(emailIntent, "Enviar correo con: "));
                break;
        }
    }
}
