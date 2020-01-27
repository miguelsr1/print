package com.devsoft.print;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.devsoft.print.db.BaseDeDatos;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BaseDeDatos baseDeDatos;
    Button btnAddCliente, btnCreateFactura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddCliente = findViewById(R.id.btnAddCliente);
        btnAddCliente.setOnClickListener(this);

        btnCreateFactura = findViewById(R.id.btnCreateFactura);
        btnCreateFactura.setOnClickListener(this);

        baseDeDatos = new BaseDeDatos(this);
    }

    @Override
    public void onClick(View v){
        if(v == btnAddCliente){
            Intent intent = new Intent(getApplicationContext(), AddCliente.class);
            startActivity(intent);
        }

        if(v == btnCreateFactura){
            Intent intent = new Intent(getApplicationContext(), CreateFactura.class);
            startActivity(intent);
        }
    }
}
