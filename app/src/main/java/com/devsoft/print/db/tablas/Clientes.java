package com.devsoft.print.db.tablas;

public class Clientes {
    public static String TABLA_NAME = "cliente";
    public static String ID_NAME="id_cliente";
    public static String COL_NIT ="nit";
    public static String COL_NCR ="ncr";
    public static String COL_GIRO ="giro";
    public static String COL_NOMBRE ="nombre";
    public static String COL_DIRECCION ="direccion";
    public static String COL_MUNICIPIO ="municipio";
    public static String COL_DEPARTAMENTO ="departamento";
    public static String COL_FECHA_CREACION ="fecha_creacion";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLA_NAME + " (" +
                    "   " + ID_NAME + " INTEGER PRIMARY KEY," +
                    "   " + COL_NIT + " TEXT, " +
                    "   " + COL_NCR + " TEXT, " +
                    "   " + COL_GIRO + " TEXT, " +
                    "   " + COL_NOMBRE + " TEXT, " +
                    "   " + COL_DIRECCION + " TEXT, " +
                    "   " + COL_MUNICIPIO + " TEXT, " +
                    "   " + COL_DEPARTAMENTO + " TEXT, " +
                    "   " + COL_FECHA_CREACION + " TEXT)";
    private Integer idCliente;
    private String nit;
    private String ncr;
    private String giro;
    private String nombre;
    private String direccion;
    private String municipio;
    private String departamento;
    private String fechaCreacion;

    public Clientes() {
        this.idCliente = idCliente;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNcr() {
        return ncr;
    }

    public void setNcr(String ncr) {
        this.ncr = ncr;
    }

    public String getGiro() {
        return giro;
    }

    public void setGiro(String giro) {
        this.giro = giro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public static String getSqlCreateEntries() {
        return SQL_CREATE_ENTRIES;
    }
}
