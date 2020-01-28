package com.devsoft.print.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.devsoft.print.db.tablas.Clientes;
import com.devsoft.print.db.tablas.Version;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by misanchez on 10-23-17.
 */

public class BaseDeDatos extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DB_NAME = "clientes.db";
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public BaseDeDatos(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Clientes.SQL_CREATE_ENTRIES);
        db.execSQL(Version.SQL_CREATE_ENTRIES);

        db.execSQL("insert into version (id_version, fecha_inicio) values (1,"+sdf.format(new Date())+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Clientes.TABLA_NAME);
        onCreate(db);
    }

    public void guardarCliente(Clientes cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Clientes.ID_NAME , cliente.getIdCliente());
            values.put(Clientes.COL_NIT, cliente.getNit());
            values.put(Clientes.COL_NCR, cliente.getNcr());
            values.put(Clientes.COL_GIRO, cliente.getGiro());
            values.put(Clientes.COL_NOMBRE, cliente.getNombre());
            values.put(Clientes.COL_DIRECCION, cliente.getDireccion());
            values.put(Clientes.COL_MUNICIPIO, cliente.getMunicipio());
            values.put(Clientes.COL_DEPARTAMENTO, cliente.getDepartamento());
            db.insert(Clientes.TABLA_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }


    public List<Clientes> getClienteByNit(String nit) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Clientes> lstRespuesta = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE nit = '"+ nit+"'" ;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            //lstPreguntas.add(new Pregunta(cursor.getInt(0), cursor.getInt(1), cursor.getString(2)));
            while (cursor.isAfterLast() == false) {
                Clientes c = new Clientes();
                c.setIdCliente(cursor.getInt(0));
                c.setNit(cursor.getString(1));
                c.setNcr(cursor.getString(2));
                c.setGiro(cursor.getString(3));
                c.setNombre(cursor.getString(4));
                c.setDireccion(cursor.getString(5));
                c.setMunicipio(cursor.getString(6));
                c.setDepartamento(cursor.getString(7));
                lstRespuesta.add(c);

                cursor.moveToNext();
            }
        }
        return lstRespuesta;
    }


    public Boolean getPruebaInicio() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM version WHERE id_version = 1 and mostrar_msj_inicio = 1";
        Cursor cursor = db.rawQuery(sql, null);
        int result = cursor.getCount();

        if (result>1) {
            sql = "UPDATE version SET mostrar_msj_inicio = 0 WHERE id_version = 1";
            db.execSQL(sql);
            return true;
        }else{
            return false;
        }
    }
}
