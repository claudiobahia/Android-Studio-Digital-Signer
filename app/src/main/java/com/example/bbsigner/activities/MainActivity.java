package com.example.bbsigner.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bbsigner.R;

public class MainActivity extends AppCompatActivity {

    private Button mbtnNovaAssinatura;
    private Button mbtnIrListarAssunaturas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isStoragePermissionGranted();
        //Toast.makeText(getApplicationContext(),"Firebased", Toast.LENGTH_LONG).show();

        mbtnNovaAssinatura = findViewById(R.id.btnNovaAssinatura);
        mbtnIrListarAssunaturas = findViewById(R.id.btnIrListaAssinaturas);

        mbtnNovaAssinatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NovaAssinaturaActivity.class));
            }
        });

        mbtnIrListarAssunaturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ListaAssinaturasActivity.class));
            }
        });
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(this, "The app was not allowed to write to your storage. Hence," +
                    " it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
        }
    }
}
