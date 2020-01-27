package com.devsoft.print;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devsoft.print.db.BaseDeDatos;
import com.devsoft.print.db.tablas.Clientes;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import java.util.HashMap;
import java.util.Map;

public class AddCliente extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private BaseDeDatos baseDeDatos;
    Spinner spDepartamento;
    Spinner spMunicipio;
    EditText txtNit, txtNrc, txtNombre, txtGiro;
    TextView txaDireccion;

    Button btnGuardar;

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.add_cliente);

        txtNit = findViewById(R.id.txtNit);
        txtNit.addTextChangedListener(new PatternedTextWatcher("####-######-###-#"));

        txtNrc = findViewById(R.id.txtNrc);
        txtNombre = findViewById(R.id.txtNombre);
        txtGiro = findViewById(R.id.txtGiro);
        txaDireccion = findViewById(R.id.txaDireccion);

        spDepartamento = findViewById(R.id.spDepartamento);
        spMunicipio = findViewById(R.id.spMunicipio);

        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,
                R.array.spinnerDepartamento, android.R.layout.simple_spinner_dropdown_item);
        spDepartamento.setAdapter(adapter1);
        spDepartamento.setOnItemSelectedListener(this);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(this);

        baseDeDatos = new BaseDeDatos(this);
    }

    @Override
    public void onClick(View v){
         if(v == btnGuardar){
             addCliente();
         }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spDepartamento.getSelectedItem().equals("AHUACHAPAN")) {
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.ahuachapan_array, android.R.layout.simple_spinner_dropdown_item);
            spMunicipio.setAdapter(adapter2);
        } else if(spDepartamento.getSelectedItem().equals("SANTA ANA")){
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.santa_ana_array, android.R.layout.simple_spinner_dropdown_item);
            spMunicipio.setAdapter(adapter2);
        } else if (spDepartamento.getSelectedItem().equals("SONSONATE")) {
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.sonsonate_array, android.R.layout.simple_spinner_dropdown_item);
            spMunicipio.setAdapter(adapter2);
        } else if(spDepartamento.getSelectedItem().equals("CHALATENANGO")){
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.chalatenango_array, android.R.layout.simple_spinner_dropdown_item);
            spMunicipio.setAdapter(adapter2);
        } else if (spDepartamento.getSelectedItem().equals("LA LIBERTAD")) {
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.la_libertad_array, android.R.layout.simple_spinner_dropdown_item);
            spMunicipio.setAdapter(adapter2);
        } else if(spDepartamento.getSelectedItem().equals("SAN SALVADOR")){
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.san_salvador_array, android.R.layout.simple_spinner_dropdown_item);
            spMunicipio.setAdapter(adapter2);
        } else if (spDepartamento.getSelectedItem().equals("CUSCATLAN")) {
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.cuscatlan_array, android.R.layout.simple_spinner_dropdown_item);
            spMunicipio.setAdapter(adapter2);
        } else if(spDepartamento.getSelectedItem().equals("LA PAZ")){
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.la_paz_array, android.R.layout.simple_spinner_dropdown_item);
            spMunicipio.setAdapter(adapter2);
        } else if(spDepartamento.getSelectedItem().equals("CABAÑAS")){
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.cabanhas_array, android.R.layout.simple_spinner_dropdown_item);
            spMunicipio.setAdapter(adapter2);
        } else if (spDepartamento.getSelectedItem().equals("SAN VICENTE")) {
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.san_vicente_array, android.R.layout.simple_spinner_dropdown_item);
            spMunicipio.setAdapter(adapter2);
        } else if(spDepartamento.getSelectedItem().equals("USULUTAN")){
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.usulutan_array, android.R.layout.simple_spinner_dropdown_item);
            spMunicipio.setAdapter(adapter2);
        } else if (spDepartamento.getSelectedItem().equals("SAN MIGUEL")) {
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.san_miguel_array, android.R.layout.simple_spinner_dropdown_item);
            spMunicipio.setAdapter(adapter2);
        } else if(spDepartamento.getSelectedItem().equals("MORAZAN")){
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.morazan_array, android.R.layout.simple_spinner_dropdown_item);
            spMunicipio.setAdapter(adapter2);
        } else if(spDepartamento.getSelectedItem().equals("LA UNION")){
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.la_union_array, android.R.layout.simple_spinner_dropdown_item);
            spMunicipio.setAdapter(adapter2);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void addCliente(){
        boolean error = false;

        final String nit = txtNit.getText().toString().trim();
        final String nrc = txtNrc.getText().toString().trim();
        final String nombre = txtNombre.getText().toString().trim();
        final String giro = txtGiro.getText().toString().trim();
        final String direccion = txaDireccion.getText().toString().trim();
        final String departamento = spDepartamento.getSelectedItem().toString().trim();
        final String municipio = spMunicipio.getSelectedItem().toString().trim();

        if(nit.length() == 0){
            txtNit.setError("El N.I.T. es requerido!");
            error = true;
        }
        if(nrc.length() == 0){
            txtNrc.setError("El N.C.R. es requerido!");
            error = true;
        }
        if(nombre.length() == 0){
            txtNombre.setError("El Nombre es requerido!");
            error = true;
        }
        if(giro.length() == 0){
            txtGiro.setError("El Giro es requerido!");
            error = true;
        }
        if(direccion.length() == 0){
            txaDireccion.setError("La Dirección es requerida!");
            error = true;
        }

        if(!error){
            final ProgressDialog loading = ProgressDialog.show(this,"Guardando Datos","Espere por favor");

            Clientes c = new Clientes();
            c.setNit(nit);
            c.setNcr(nrc);
            c.setGiro(giro);
            c.setNombre(nombre);
            c.setDireccion(direccion);
            c.setDepartamento(departamento);
            c.setMunicipio(municipio);
            //c.setFechaCreacion();
            baseDeDatos.guardarCliente(c);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbwrXbbZNnVabO1hTG1J7mFN5jqdhc1WtwaTZoWRHDjepVZrnTs/exec",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            loading.dismiss();
                            Toast.makeText(AddCliente.this,response,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parmas = new HashMap<>();

                    //here we pass params
                    parmas.put("action","addCliente");
                    parmas.put("nit",nit);
                    parmas.put("nrc",nrc);
                    parmas.put("giro",giro);
                    parmas.put("nombre",nombre);
                    parmas.put("direccion",direccion);
                    parmas.put("municipio",municipio);
                    parmas.put("departamento",departamento);

                    return parmas;
                }
            };

            int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

            RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(retryPolicy);

            RequestQueue queue = Volley.newRequestQueue(this);

            queue.add(stringRequest);
        }
    }
}
