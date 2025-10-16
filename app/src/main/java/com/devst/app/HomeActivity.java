package com.devst.app;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity {

    private String emailUsuario = "";
    private TextView tvBienvenida;

    private Button btnLinterna;
    private CameraManager camara;
    private String camaraID = null;
    private boolean luz = false;

    private final ActivityResultLauncher<Intent> editarPerfilLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String nombre = result.getData().getStringExtra("nombre_editado");
                    if (nombre != null) {
                        tvBienvenida.setText("Hola, " + nombre);
                    }
                }
            });

    private final ActivityResultLauncher<String> permisoCamaraLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) alternarluz();
                else Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            });

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        prefs = getSharedPreferences("configuracion_app", MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvBienvenida = findViewById(R.id.tvBienvenida);
        Button btnIrPerfil = findViewById(R.id.btnIrPerfil);
        Button btnAbrirWeb = findViewById(R.id.btnAbrirWeb);
        Button btnEnviarCorreo = findViewById(R.id.btnEnviarCorreo);
        Button btnCompartir = findViewById(R.id.btnCompartir);
        btnLinterna = findViewById(R.id.btnLinterna);
        Button btnCamara = findViewById(R.id.btnCamara);
        Button btnAjustes = findViewById(R.id.btnAbrirConfig);
        Button btnGaleria = findViewById(R.id.btnGaleria);
        Button btnContactos = findViewById(R.id.btnContactos);
        Button btnUbicacion = findViewById(R.id.btnUbicacion);
        Button btnLlamar = findViewById(R.id.btnLlamada);

        emailUsuario = getIntent().getStringExtra("email_usuario");
        if(emailUsuario == null) emailUsuario = "";
        tvBienvenida.setText("Bienvenido: " + emailUsuario);

        // PERFIL
        btnIrPerfil.setOnClickListener(v -> {
            Intent i = new Intent(HomeActivity.this, PerfilActivity.class);
            i.putExtra("email_usuario", emailUsuario);
            editarPerfilLauncher.launch(i);
            aplicarTransicion();
        });

        // WEB
        btnAbrirWeb.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.santotomas.cl");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
            aplicarTransicion();
        });

        // CORREO
        btnEnviarCorreo.setOnClickListener(v -> {
            Intent email = new Intent(Intent.ACTION_SENDTO);
            email.setData(Uri.parse("mailto:"));
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailUsuario});
            email.putExtra(Intent.EXTRA_SUBJECT, "Prueba desde la app");
            email.putExtra(Intent.EXTRA_TEXT, "Hola, esto es un intento de correo.");
            startActivity(Intent.createChooser(email, "Enviar correo con:"));
            aplicarTransicion();
        });

        // COMPARTIR
        btnCompartir.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Hola desde mi app Android 😎");
            startActivity(Intent.createChooser(share, "Compartir usando:"));
            aplicarTransicion();
        });

        // CONTACTOS
        btnContactos.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/"));
            startActivity(intent);
            aplicarTransicion();
        });

        // GALERIA
        btnGaleria.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivity(intent);
            aplicarTransicion();
        });

        // AJUSTES
        btnAjustes.setOnClickListener(v -> {
            startActivity(new Intent(this, AjustesActivity.class));
            aplicarTransicion();
        });

        // UBICACION
        btnUbicacion.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://maps.app.goo.gl/H2HU6FinFcqMour98");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
            aplicarTransicion();
        });

        // LLAMADA
        btnLlamar.setOnClickListener(v -> {
            String numero = "+56945341996";
            Uri uri = Uri.parse("tel:" + numero);
            startActivity(new Intent(Intent.ACTION_DIAL, uri));
            aplicarTransicion();
        });

        // CAMARA
        btnCamara.setOnClickListener(v -> {
            startActivity(new Intent(this, CamaraActivity.class));
            aplicarTransicion();
        });

        // LINTERNAS
        camara = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            for(String id : camara.getCameraIdList()){
                CameraCharacteristics cc = camara.getCameraCharacteristics(id);
                Boolean disponibleFlash = cc.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                Integer lensFacing = cc.get(CameraCharacteristics.LENS_FACING);
                if(Boolean.TRUE.equals(disponibleFlash) && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK){
                    camaraID = id;
                    break;
                }
            }
        } catch (CameraAccessException e){
            Toast.makeText(this,"No se puede acceder a la cámara", Toast.LENGTH_SHORT).show();
        }

        btnLinterna.setOnClickListener(v -> {
            if(camaraID == null){
                Toast.makeText(this,"Este dispositivo no tiene flash disponible", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean camGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
            if(camGranted) alternarluz();
            else permisoCamaraLauncher.launch(Manifest.permission.CAMERA);
        });
    }

    private void alternarluz(){
        try{
            luz = !luz;
            camara.setTorchMode(camaraID, luz);
            btnLinterna.setText(luz ? "Apagar Linterna" : "Encender Linterna");
        } catch(CameraAccessException e){
            Toast.makeText(this,"Error al controlar la linterna", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(camaraID != null && luz){
            try{
                camara.setTorchMode(camaraID,false);
                luz=false;
                if(btnLinterna != null) btnLinterna.setText("Encender Linterna");
            } catch(CameraAccessException ignored){}
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull android.view.Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_perfil){
            Intent i = new Intent(this, PerfilActivity.class);
            i.putExtra("email_usuario", emailUsuario);
            editarPerfilLauncher.launch(i);
            aplicarTransicion();
            return true;
        } else if(id == R.id.action_web){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com")));
            aplicarTransicion();
            return true;
        } else if(id == R.id.action_ajustes){
            startActivity(new Intent(this, AjustesActivity.class));
            aplicarTransicion();
            return true;
        } else if(id == R.id.action_salir){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Método para aplicar la transición guardada
    private void aplicarTransicion(){
        int trans = prefs.getInt("transicion",1);
        if(trans == 1){
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}