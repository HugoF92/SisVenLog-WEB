/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Clara
 */
public class LiVentasCab {
    private Short codVendedor;
    private String descripcionVendedor;
    private Short codRuta;
    private String descripcionRunta;
    private String codZona;
    private String descripcionZona;
    private Map<String, BigDecimal> montoPorMes;
    private List<Map<String, Object>> clientesPorMes;
    private List<Map<String, Object>> reportePorMes;

    public Short getCodVendedor() {
        return codVendedor;
    }

    public void setMontoPorMes(Map<String, BigDecimal> montoPorMes) {
        this.montoPorMes = montoPorMes;
    }

    public Map<String, BigDecimal> getMontoPorMes() {
        return montoPorMes;
    }

    public String getDescripcionVendedor() {
        return descripcionVendedor;
    }

    public Short getCodRuta() {
        return codRuta;
    }

    public String getDescripcionRunta() {
        return descripcionRunta;
    }

    public String getCodZona() {
        return codZona;
    }

    public String getDescripcionZona() {
        return descripcionZona;
    }


    public void setCodVendedor(Short codVendedor) {
        this.codVendedor = codVendedor;
    }

    public void setDescripcionVendedor(String descripcionVendedor) {
        this.descripcionVendedor = descripcionVendedor;
    }

    public void setCodRuta(Short codRuta) {
        this.codRuta = codRuta;
    }

    public void setDescripcionRunta(String descripcionRunta) {
        this.descripcionRunta = descripcionRunta;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public void setDescripcionZona(String descripcionZona) {
        this.descripcionZona = descripcionZona;
    }

    public List<Map<String, Object>> getClientesPorMes() {
        return clientesPorMes;
    }

    public void setClientesPorMes(List<Map<String, Object>> clientesPorMes) {
        this.clientesPorMes = clientesPorMes;
    }

    public List<Map<String, Object>> getReportePorMes() {
        return reportePorMes;
    }

    public void setReportePorMes(List<Map<String, Object>> reportePorMes) {
        this.reportePorMes = reportePorMes;
    }
    
}
