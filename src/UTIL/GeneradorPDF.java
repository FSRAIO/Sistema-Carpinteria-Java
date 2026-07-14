package UTIL;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class GeneradorPDF {

    // --- PALETA DE COLORES Y FUENTES ---
    // Marrón elegante (Rojo 139, Verde 69, Azul 19)
    BaseColor COLOR_PRIMARIO = new BaseColor(139, 69, 19); 
    BaseColor COLOR_FONDO_CLARO = new BaseColor(245, 245, 245); // Gris muy suavecito

    // Fuentes limpias (Helvetica es más moderna que Times)
    Font FONT_TITULO = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, COLOR_PRIMARIO);
    Font FONT_SUBTITULO = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    Font FONT_BOLD = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.DARK_GRAY);
    Font FONT_BLANCA = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);

    public void generarCotizacion(int idProyecto) {
        Document documento = new Document();
        // Márgenes más estrechos para aprovechar la hoja
        documento.setMargins(36, 36, 36, 36); 

        try {
            String nombreArchivo = "Cotizacion_" + idProyecto + ".pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));

            documento.open();

            // ==========================================
            // 1. ENCABEZADO (LOGO + DATOS EMPRESA)
            // ==========================================
            PdfPTable tablaHeader = new PdfPTable(2);
            tablaHeader.setWidthPercentage(100);
            tablaHeader.setWidths(new float[]{1, 2}); // Columna derecha más ancha

            // --- A) LOGO ---
            PdfPCell celdaLogo = new PdfPCell();
            celdaLogo.setBorder(0);
            try {
                Image imagen = Image.getInstance("logo.png"); 
                imagen.scaleToFit(120, 100); // Un poco más grande
                celdaLogo.addElement(imagen);
            } catch (Exception e) {
                celdaLogo.addElement(new Phrase("EBANIX SAC", FONT_TITULO));
            }
            tablaHeader.addCell(celdaLogo);

            // --- B) DATOS EMPRESA (Alineados a la derecha) ---
            PdfPCell celdaEmpresa = new PdfPCell();
            celdaEmpresa.setBorder(0);
            celdaEmpresa.setHorizontalAlignment(Element.ALIGN_RIGHT);
            
            // Usamos párrafos para controlar la alineación derecha
            Paragraph pEmpresa = new Paragraph("CARPINTERÍA EBANIX S.A.C.", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK));
            pEmpresa.setAlignment(Element.ALIGN_RIGHT);
            celdaEmpresa.addElement(pEmpresa);
            
            Paragraph pRuc = new Paragraph("RUC: 20123456789", FONT_NORMAL);
            pRuc.setAlignment(Element.ALIGN_RIGHT);
            celdaEmpresa.addElement(pRuc);
            
            Paragraph pDir = new Paragraph("Av. Los Cedros 123, Lima", FONT_NORMAL);
            pDir.setAlignment(Element.ALIGN_RIGHT);
            celdaEmpresa.addElement(pDir);
            
            Paragraph pTel = new Paragraph("Cel: 999 156 675", FONT_NORMAL);
            pTel.setAlignment(Element.ALIGN_RIGHT);
            celdaEmpresa.addElement(pTel);

            tablaHeader.addCell(celdaEmpresa);
            documento.add(tablaHeader);

            // LÍNEA SEPARADORA ELEGANTE
            PdfPTable linea = new PdfPTable(1);
            linea.setWidthPercentage(100);
            PdfPCell celdaLinea = new PdfPCell(new Phrase(" "));
            celdaLinea.setBackgroundColor(COLOR_PRIMARIO); // Línea marrón
            celdaLinea.setBorder(0);
            celdaLinea.setFixedHeight(3f); // Grosor de 3 puntos
            linea.addCell(celdaLinea);
            documento.add(linea);
            
            documento.add(new Paragraph(" ")); // Espacio

            // ==========================================
            // 2. CAJA DE DATOS DEL CLIENTE (TIPO FACTURA)
            // ==========================================
            DbBean db = new DbBean();
            Connection cn = db.connect();
            
            String sqlCabecera = "SELECT p.nombreProyecto, p.descripcion, c.nombre, c.telefono " +
                                 "FROM Proyectos p INNER JOIN Clientes c ON p.idCliente = c.idCliente " +
                                 "WHERE p.idProyecto = ?";
            PreparedStatement pst = cn.prepareStatement(sqlCabecera);
            pst.setInt(1, idProyecto);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                // Creamos una tabla para organizar los datos (Grid 2 columnas)
                PdfPTable tablaCliente = new PdfPTable(2);
                tablaCliente.setWidthPercentage(100);
                tablaCliente.setWidths(new float[]{1, 1}); // 50% y 50%
                tablaCliente.setSpacingAfter(10);

                // COLUMNA IZQUIERDA: CLIENTE
                PdfPCell cIzq = new PdfPCell();
                cIzq.setBorder(Rectangle.BOX); // Marco alrededor
                cIzq.setBorderColor(BaseColor.LIGHT_GRAY);
                cIzq.setPadding(10);
                
                cIzq.addElement(new Phrase("CLIENTE:", FONT_BOLD));
                cIzq.addElement(new Phrase(rs.getString("nombre").toUpperCase(), FONT_NORMAL));
                cIzq.addElement(new Phrase("Tel: " + rs.getString("telefono"), FONT_NORMAL));
                tablaCliente.addCell(cIzq);

                // COLUMNA DERECHA: DETALLES COTIZACIÓN
                PdfPCell cDer = new PdfPCell();
                cDer.setBorder(Rectangle.BOX);
                cDer.setBorderColor(BaseColor.LIGHT_GRAY);
                cDer.setPadding(10);
                
                String fechaHoy = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                cDer.addElement(new Phrase("NRO COTIZACIÓN: 000-" + idProyecto, FONT_BOLD));
                cDer.addElement(new Phrase("FECHA: " + fechaHoy, FONT_NORMAL));
                cDer.addElement(new Phrase("PROYECTO: " + rs.getString("nombreProyecto"), FONT_NORMAL));
                tablaCliente.addCell(cDer);

                documento.add(tablaCliente);
                
                // Descripción del trabajo (fuera de la caja)
                Paragraph pDesc = new Paragraph("Descripción del trabajo:", FONT_BOLD);
                pDesc.setSpacingAfter(5);
                documento.add(pDesc);
                Paragraph pDescTexto = new Paragraph(rs.getString("descripcion"), FONT_NORMAL);
                pDescTexto.setSpacingAfter(15);
                documento.add(pDescTexto);
            }

            // ==========================================
            // 3. TABLA DE PRODUCTOS (Estilo Cebra)
            // ==========================================
            PdfPTable tablaProd = new PdfPTable(4);
            tablaProd.setWidthPercentage(100);
            tablaProd.setWidths(new float[]{4, 1, 1.5f, 1.5f}); // Anchos ajustados
            
            // HEADER
            tablaProd.addCell(crearHeader("DESCRIPCIÓN / MATERIAL"));
            tablaProd.addCell(crearHeader("CANT."));
            tablaProd.addCell(crearHeader("P. UNIT"));
            tablaProd.addCell(crearHeader("TOTAL"));

            String sqlDetalle = "SELECT m.nombre, d.cantidad, d.precioMomentaneo, d.subtotal " +
                                "FROM Detalle_Presupuesto d " +
                                "INNER JOIN Materiales_Ref m ON d.idMaterial = m.idMaterial " +
                                "WHERE d.idProyecto = ?";
            PreparedStatement pstDet = cn.prepareStatement(sqlDetalle);
            pstDet.setInt(1, idProyecto);
            ResultSet rsDet = pstDet.executeQuery();
            
            boolean gris = false; // Para alternar colores (Efecto Cebra)
            while (rsDet.next()) {
                BaseColor colorFondo = gris ? COLOR_FONDO_CLARO : BaseColor.WHITE;
                
                tablaProd.addCell(crearCeldaData(rsDet.getString("nombre"), Element.ALIGN_LEFT, colorFondo));
                tablaProd.addCell(crearCeldaData(rsDet.getString("cantidad"), Element.ALIGN_CENTER, colorFondo));
                tablaProd.addCell(crearCeldaData("S/. " + rsDet.getString("precioMomentaneo"), Element.ALIGN_RIGHT, colorFondo));
                tablaProd.addCell(crearCeldaData("S/. " + rsDet.getString("subtotal"), Element.ALIGN_RIGHT, colorFondo));
                
                gris = !gris; // Cambiamos el color para la siguiente vuelta
            }
            
            // Rellenar con filas vacías si son pocas (para que se vea estético)
            // (Opcional, lo omito por simplicidad, pero da buen toque)
            
            documento.add(tablaProd);

            // ==========================================
            // 4. TOTALES (DISEÑO BLOQUE)
            // ==========================================
            String sqlTotales = "SELECT precioVentaFinal, montoAdelanto, saldoPendiente FROM Proyectos WHERE idProyecto = ?";
            PreparedStatement pstTot = cn.prepareStatement(sqlTotales);
            pstTot.setInt(1, idProyecto);
            ResultSet rsTot = pstTot.executeQuery();
            
            if (rsTot.next()) {
                documento.add(new Paragraph(" "));
                
                PdfPTable tablaTotales = new PdfPTable(2);
                tablaTotales.setWidthPercentage(40); // 40% del ancho de hoja
                tablaTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);
                
                // Total
                tablaTotales.addCell(crearCeldaTotal("TOTAL:", false));
                tablaTotales.addCell(crearCeldaTotal("S/. " + rsTot.getDouble("precioVentaFinal"), false));
                
                // Adelanto
                tablaTotales.addCell(crearCeldaTotal("Adelanto:", false));
                tablaTotales.addCell(crearCeldaTotal("S/. " + rsTot.getDouble("montoAdelanto"), false));
                
                // SALDO PENDIENTE (RECUADRO CON COLOR)
                PdfPCell celdaSaldoLbl = new PdfPCell(new Phrase("SALDO PENDIENTE:", FONT_BLANCA));
                celdaSaldoLbl.setBackgroundColor(COLOR_PRIMARIO); // Fondo marrón
                celdaSaldoLbl.setPadding(8);
                tablaTotales.addCell(celdaSaldoLbl);
                
                PdfPCell celdaSaldoVal = new PdfPCell(new Phrase("S/. " + rsTot.getDouble("saldoPendiente"), FONT_BLANCA));
                celdaSaldoVal.setBackgroundColor(COLOR_PRIMARIO); // Fondo marrón
                celdaSaldoVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celdaSaldoVal.setPadding(8);
                tablaTotales.addCell(celdaSaldoVal);

                documento.add(tablaTotales);
            }
            
            // ==========================================
            // 5. PIE DE PÁGINA PROFESIONAL
            // ==========================================
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph(" "));
            
            PdfPTable footer = new PdfPTable(1);
            footer.setWidthPercentage(100);
            
            PdfPCell cFooter = new PdfPCell(new Phrase("Condiciones de Pago: 50% Adelanto - 50% Contraentrega | Validez: 15 Días\n"
                    + "Cuentas Bancarias: BCP 191-xxxxxxxx-0-xx | Interbank 200-xxxxxxxx", 
                    new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.GRAY)));
            
            cFooter.setBorder(Rectangle.TOP); // Solo línea arriba
            cFooter.setBorderColor(BaseColor.LIGHT_GRAY);
            cFooter.setHorizontalAlignment(Element.ALIGN_CENTER);
            cFooter.setPaddingTop(10);
            footer.addCell(cFooter);
            
            documento.add(footer);

            documento.close();
            JOptionPane.showMessageDialog(null, "✅ Cotización Generada con Éxito");
            
            try {
                java.awt.Desktop.getDesktop().open(new java.io.File(nombreArchivo));
            } catch (Exception ex) {}

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error PDF: " + e.getMessage());
        } finally {
             // db.desconectar(); // Descomenta si usas el desconectar
        }
    }
    
    // --- MÉTODOS AUXILIARES PARA LIMPIAR EL CÓDIGO ---

    private PdfPCell crearHeader(String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FONT_BLANCA));
        celda.setBackgroundColor(COLOR_PRIMARIO);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(8); // Más espacio vertical
        celda.setBorderColor(BaseColor.WHITE); // Separadores blancos
        return celda;
    }
    
    private PdfPCell crearCeldaData(String texto, int alineacion, BaseColor colorFondo) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FONT_NORMAL));
        celda.setBackgroundColor(colorFondo);
        celda.setHorizontalAlignment(alineacion);
        celda.setPadding(6);
        celda.setBorderColor(BaseColor.LIGHT_GRAY);
        return celda;
    }
    
    private PdfPCell crearCeldaTotal(String texto, boolean fondoColor) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FONT_BOLD));
        celda.setBorder(0);
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setPadding(4);
        return celda;
    }
}