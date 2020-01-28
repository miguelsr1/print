package com.devsoft.print.db.tablas;

public class Version {

    public static String TABLA_NAME = "version";
    public static String ID_NAME="id_version";
    public static String COL_MSJ_INICIO = "mostrar_msj_inicio";
    public static String COL_FECHA_INICIO="fecha_inicio";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLA_NAME + " (" +
                    "   " + ID_NAME + " INTEGER PRIMARY KEY," +
                    "   " + COL_MSJ_INICIO + " INTEGER, "+
                    "   " + COL_FECHA_INICIO + " TEXT)";

    public Integer idVersion;
    public String fechaInicio;
    public Integer mostrarMsjInicio;

    public Version(Integer idVersion) {
        this.idVersion = idVersion;
    }

    public Integer getMostrarMsjInicio() {
        return mostrarMsjInicio;
    }

    public void setMostrarMsjInicio(Integer mostrarMsjInicio) {
        this.mostrarMsjInicio = mostrarMsjInicio;
    }

    public Integer getIdVersion() {
        return idVersion;
    }

    public void setIdVersion(Integer idVersion) {
        this.idVersion = idVersion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
}
