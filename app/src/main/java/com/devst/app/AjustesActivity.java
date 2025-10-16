package com.devst.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.appbar.MaterialToolbar;

public class AjustesActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        preferences = getSharedPreferences("configuracion_app", MODE_PRIVATE);

        // Toolbar con flecha de regreso
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Switch de modo oscuro
        Switch switchModo = findViewById(R.id.switchModoOscuro);
        boolean modoOscuro = preferences.getBoolean("modo_oscuro", false);
        switchModo.setChecked(modoOscuro);
        AppCompatDelegate.setDefaultNightMode(
                modoOscuro ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        switchModo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("modo_oscuro", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
            Toast.makeText(this, "Reinicia la app para aplicar cambios completos", Toast.LENGTH_SHORT).show();
        });

        // Selector de transiciÃ³n
        RadioGroup radioGroup = findViewById(R.id.radioGroupTransiciones);
        int transicion = preferences.getInt("transicion", 1);
        if(transicion == 1) radioGroup.check(R.id.rbSlide);
        else radioGroup.check(R.id.rbFade);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int valor = (checkedId == R.id.rbSlide) ? 1 : 2;
            preferences.edit().putInt("transicion", valor).apply();
        });
    }
}