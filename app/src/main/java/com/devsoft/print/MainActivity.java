package com.devsoft.print;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.devsoft.print.db.BaseDeDatos;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

        TextView msj = findViewById(R.id.txtMsj);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = null;
        try {
            date1 = df.parse(baseDeDatos.getFechaInicio());
            Date date2 = new Date();
            long diff = TimeUnit.DAYS.convert((date2.getTime() - date1.getTime()), TimeUnit.MILLISECONDS);

            msj.setText("DÍAS DE PRUEBA: "+(31-diff)+" DÍAS");

            if(diff >= 20){
                msj.setText("HA CONCLUIDO EL PERIODO DE PRUEBAS");
                baseDeDatos.setCaducoPrueba();
                btnAddCliente.setBackgroundColor(Color.GRAY);
                btnCreateFactura.setBackgroundColor(Color.GRAY);
            }

            btnAddCliente.setEnabled(!baseDeDatos.getCaducoPrueba());
            btnCreateFactura.setEnabled(!baseDeDatos.getCaducoPrueba());

            if(baseDeDatos.getCaducoPrueba()){
                btnAddCliente.setBackgroundColor(Color.GRAY);
                btnCreateFactura.setBackgroundColor(Color.GRAY);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
