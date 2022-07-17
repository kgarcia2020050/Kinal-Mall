/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.report;

import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.kennethgarcia.db.Conexion;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 17/07/2021
 * @time 09:41:08
 */
public class GenerarReporte {

    private static GenerarReporte instancia;

    private GenerarReporte() {

    }
    
    public static GenerarReporte getInstance() {
        if (instancia == null) {
            instancia = new GenerarReporte();
        }
        return instancia;
    }
    

    public void mostrarReporte(String nombreReporte, Map parametros, String titulo) {
        try {
           parametros.put("LOGO_HEADER", getClass().getResourceAsStream("/org/kennethgarcia/resources/image/sin fondo.png"));
            
           parametros.put("LOGO_FOOTER", getClass().getResourceAsStream("/org/kennethgarcia/resources/image/Free_Sample_By_Wix (2).jpg")    );
           
           InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);
            
            JasperReport report = (JasperReport) JRLoader.loadObject(reporte);
            
            JasperPrint print = JasperFillManager.fillReport(report, parametros,Conexion.getInstance().getConexion());
            
            JasperViewer visor=new JasperViewer(print,false);
            
            visor.setTitle(titulo);
            visor.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
