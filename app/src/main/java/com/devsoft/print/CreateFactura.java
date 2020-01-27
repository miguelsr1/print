package com.devsoft.print;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devsoft.print.db.BaseDeDatos;
import com.devsoft.print.db.tablas.Clientes;
import com.devsoft.print.util.Herramientas;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateFactura extends AppCompatActivity implements View.OnClickListener {

    private BaseDeDatos baseDeDatos;
    List<Clientes> lst = new ArrayList();

    String strMonto, strMontoIva, strMontoTotal;
    String nit="";

    EditText txtNit, txtMonto, txtConcepto, txaNombreCliente;
    TextView txtIva, txtMontoTotal;
    Button btnBuscar;
    Button btnGenerarFactura;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat df = new DecimalFormat("#,##0.00");

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.create_factura);

        txtConcepto = findViewById(R.id.txaConcepto);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(this);

        btnGenerarFactura = findViewById(R.id.btnCrearPdf);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        btnGenerarFactura.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                boolean error = false;
                                if(lst.isEmpty()) {
                                    error = true;
                                    Toast.makeText(CreateFactura.this,"Debe de ingresar un N.I.T. registrado ",Toast.LENGTH_LONG).show();
                                }
                                if(txtConcepto.getText().toString().isEmpty()){
                                    error = true;
                                    txtConcepto.setError("El concepto es requerido!");
                                }
                                if(txtMonto.getText().toString().isEmpty()){
                                    error = true;
                                    txtMonto.setError("El monto es requerido!");
                                }
                                if(!error) {
                                    crearFacturaPDF(Common.getAppPath(CreateFactura.this) + "test_factura.pdf");
                                }
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();

        txaNombreCliente = findViewById(R.id.txaNombreCliente);

        txtNit = findViewById(R.id.txtFindNit);
        txtNit.addTextChangedListener(new PatternedTextWatcher("####-######-###-#"));

        txtMonto = findViewById(R.id.txtMonto);
        txtIva = findViewById(R.id.txtIva);
        txtMontoTotal = findViewById(R.id.txtMontoTotal);

        txtMonto.addTextChangedListener(new TextWatcher() {
            // the user's changes are saved here
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                Double monto = Double.parseDouble(txtMonto.getText().toString());
                Double montoIva = monto * 0.13;
                txtIva.setText(df.format(montoIva));
                txtMontoTotal.setText(df.format((monto + montoIva)));

                strMonto = txtMonto.getText().toString();
                strMontoIva = df.format(montoIva);
                strMontoTotal = df.format((monto + montoIva));
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });

        baseDeDatos = new BaseDeDatos(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnBuscar){
            lst.clear();
            nit = txtNit.getText().toString();
            lst = baseDeDatos.getClienteByNit(nit);

            if(lst.isEmpty()){
                Toast.makeText(CreateFactura.this,"No se ha encontrado el cliente",Toast.LENGTH_LONG).show();
            }else{
                txaNombreCliente.setText(lst.get(0).getNombre());
            }
        }

    }


    private void crearFacturaPDF(String path) {
        if(new File(path).exists()){
            new File(path).delete();
        }

        try{
            Font smallfont = new Font(Font.FontFamily.HELVETICA, 9);

            Document document = new Document();
            document.setMargins(40,40,141.735f,141.735f);

            //save
            PdfWriter.getInstance(document, new FileOutputStream(path));
            //open to write
            document.open();

            //setting
            document.setPageSize(PageSize.LETTER);
            document.addCreationDate();
            document.addAuthor("devsoft");
            document.addCreator("Desarrollo PC");

            //Font Setting
            BaseColor colorAccent = new BaseColor(0, 153, 204, 255);
            float fontSize = 20.0f;
            float valieFontSize = 26.0f;
            Font verysmallfont = new Font(Font.FontFamily.HELVETICA, 8);

            //Custom font
            BaseFont fontName = BaseFont.createFont("assets/fonts/BemboStd.otf", "UTF-8", BaseFont.EMBEDDED);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(90f);

            table.setTotalWidth(new float[]{ 45,160,40,55 });

            table.addCell(new PdfPCell(new Phrase("Cliente",smallfont)));
            table.addCell(new PdfPCell(new Phrase(lst.get(0).getNombre().toUpperCase(), smallfont)));
            table.addCell(new PdfPCell(new Phrase("Fecha",smallfont)));
            table.addCell(new PdfPCell(new Phrase(sdf.format(new Date()), smallfont)));
            table.addCell(new PdfPCell(new Phrase("Dirección",smallfont)));
            table.addCell(new PdfPCell(new Phrase(lst.get(0).getDireccion().toUpperCase()+", "+lst.get(0).getMunicipio().toUpperCase(), smallfont)));
            table.addCell(new PdfPCell(new Phrase("N.C.R.",smallfont)));
            table.addCell(new PdfPCell(new Phrase(lst.get(0).getNcr().toUpperCase(), smallfont)));
            table.addCell(new PdfPCell(new Phrase("Departamento",smallfont)));
            table.addCell(new PdfPCell(new Phrase(lst.get(0).getDepartamento().toUpperCase(), smallfont)));
            table.addCell(new PdfPCell(new Phrase("N.I.T.",smallfont)));
            table.addCell(new PdfPCell(new Phrase(lst.get(0).getNit().toUpperCase(), smallfont)));
            table.addCell(new PdfPCell(new Phrase("Giro",smallfont)));
            table.addCell(new PdfPCell(new Phrase(lst.get(0).getGiro().toUpperCase(), smallfont)));
            table.addCell(new PdfPCell(new Phrase("CON/PAGO",smallfont)));
            table.addCell("");

            document.add(table);

            addNewItem(document, " ", 0, smallfont);
            addNewItem(document, " ", 0, smallfont);

            PdfPTable detalle = new PdfPTable(5);
            detalle.setWidthPercentage(90f);
            detalle.setTotalWidth(new float[]{ 15,150,33,40,33 });

            addCelda(detalle, "CAN", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, smallfont, false, false);
            addCelda(detalle, "DETALLE", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, smallfont, false, false);
            addCelda(detalle, "OPERACIÓN NO SUJETA", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, verysmallfont, false, false);
            addCelda(detalle, "OPERACIÓN EXENTA", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, verysmallfont, false, false);
            addCelda(detalle, "OPERACIÓN GRAVADA", Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, verysmallfont, false, false);

            addCelda(detalle,"",Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, smallfont, false, true);
            addCelda(detalle,"",Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, smallfont, false, true);
            addCelda(detalle,"",Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, smallfont, false, true);
            addCelda(detalle,"",Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, smallfont, false, true);
            addCelda(detalle,strMonto,Element.ALIGN_TOP, Element.ALIGN_RIGHT, smallfont, false, true);

            PdfPCell cel = new PdfPCell(new Phrase("TOTAL DE IMPORTE EN LETRAS",smallfont));
            cel.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cel.setHorizontalAlignment(Element.ALIGN_CENTER);
            cel.setColspan(3);
            cel.setNoWrap(false);
            detalle.addCell(cel);

            addCelda(detalle,"SUMA",Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, smallfont, false, false);
            addCelda(detalle,strMonto,Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, smallfont, false, false);

            PdfPCell cel2 = new PdfPCell(new Phrase("CANTIDAD EN LETRAS",smallfont));
            cel2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cel2.setColspan(3);
            cel2.setRowspan(3);
            cel2.setNoWrap(false);
            detalle.addCell(cel2);
            addCelda(detalle,"IVA 13%",Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, smallfont, false, false);
            addCelda(detalle,strMontoIva,Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, smallfont, false, false);
            addCelda(detalle,"SUB-TOTAL",Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, smallfont, false, false);
            addCelda(detalle,strMontoTotal,Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, smallfont, false, false);
            addCelda(detalle,"V.EXENTA",Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, smallfont, false, false);
            addCelda(detalle,"-",Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, smallfont, false, false);

            PdfPCell cel3 = new PdfPCell(new Phrase("RECIBI DE: "+lst.get(0).getNombre().toUpperCase()+", LA CANTIDAD DE: "+ Herramientas.aLetras(new BigDecimal(strMontoTotal))+" EN CONCEPTO DE " +txtConcepto.getText().toString().toUpperCase(),smallfont));
            cel3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cel3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cel3.setColspan(3);
            cel3.setRowspan(3);
            cel3.setNoWrap(false);
            detalle.addCell(cel3);

            addCelda(detalle,"V.NO SUJETA",Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, verysmallfont, false, false);
            addCelda(detalle,"-",Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, smallfont, false, false);
            addCelda(detalle,"TOTAL",Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, smallfont, false, false);
            addCelda(detalle,"-",Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, smallfont, false, false);
            addCelda(detalle,"IVA RETENIDO",Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, verysmallfont, false, false);
            addCelda(detalle,"-",Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, smallfont, false, false);

            addCeldaColspan(detalle,"",Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, smallfont, false, 2);
            addCeldaColspan(detalle,"TOTAL A PAGAR",Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, smallfont, false, 2);
            addCelda(detalle,strMontoTotal,Element.ALIGN_MIDDLE, Element.ALIGN_RIGHT, smallfont, false, false);

            document.add(detalle);

            document.close();

            Toast.makeText(this, "Se ha creado la factura", Toast.LENGTH_SHORT).show();

            printPDF();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printPDF() {
        PrintManager pm = (PrintManager)getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter pda = new PdfDocumentAdapter(CreateFactura.this, Common.getAppPath(CreateFactura.this) + "test_factura.pdf");
            pm.print("Document", pda, new PrintAttributes.Builder().build());
        }catch(Exception ex){
            Log.e("devsoft", ""+ex.getMessage());
        }
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }

    private void addCelda(PdfPTable tabla, String texto, int verticalAlignment, int horizontalAlignment, Font font, boolean noWrap, boolean detalle){
        PdfPCell celda = new PdfPCell(new Phrase(texto,font));
        celda.setVerticalAlignment(verticalAlignment);
        celda.setHorizontalAlignment(horizontalAlignment);
        celda.setNoWrap(noWrap);
        if(detalle) {
            celda.setFixedHeight(340f);
        }
        tabla.addCell(celda);
    }

    private void addCeldaColspan(PdfPTable tabla, String texto, int verticalAlignment, int horizontalAlignment, Font font, boolean noWrap, int colspan){
        PdfPCell celda = new PdfPCell(new Phrase(texto,font));
        celda.setVerticalAlignment(verticalAlignment);
        celda.setHorizontalAlignment(horizontalAlignment);
        celda.setNoWrap(noWrap);
        celda.setColspan(colspan);
        tabla.addCell(celda);
    }
}
