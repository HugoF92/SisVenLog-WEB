/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entidad.Proveedores;
import entidad.TiposDocumentos;
import java.util.Date;

/**
 *
 * @author dadob
 */
public class DocumentoCompraDto {
    
    private TiposDocumentos tipoDocumento;
    private Date fechaDocumento;
    private long nroDcumento;
    private long montoDocumento;
    private long montoAPagarDocumento;
    private long saldoDocumento;
    private long nroRecibo;
    private Date fechaRecibo;
    private Proveedores proveedor;
    private String observacion;
    private String canalCompra;
    private Date fechaVencimiento;
    private String comCtipoDocum;
    private Date comFfactur;
    private long comNrofact;
    private String canalCompraDet;
    private Date fechaVencDet;
    
    public TiposDocumentos getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TiposDocumentos tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Date getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(Date fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public long getNroDcumento() {
        return nroDcumento;
    }

    public void setNroDcumento(long nroDcumento) {
        this.nroDcumento = nroDcumento;
    }

    public long getMontoDocumento() {
        return montoDocumento;
    }

    public void setMontoDocumento(long montoDocumento) {
        this.montoDocumento = montoDocumento;
    }

    public long getMontoAPagarDocumento() {
        return montoAPagarDocumento;
    }

    public void setMontoAPagarDocumento(long montoAPagarDocumento) {
        this.montoAPagarDocumento = montoAPagarDocumento;
    }

    public long getSaldoDocumento() {
        return saldoDocumento;
    }

    public void setSaldoDocumento(long saldoDocumento) {
        this.saldoDocumento = saldoDocumento;
    }

    public long getNroRecibo() {
        return nroRecibo;
    }

    public void setNroRecibo(long nroRecibo) {
        this.nroRecibo = nroRecibo;
    }

    public Date getFechaRecibo() {
        return fechaRecibo;
    }

    public void setFechaRecibo(Date fechaRecibo) {
        this.fechaRecibo = fechaRecibo;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getCanalCompra() {
        return canalCompra;
    }

    public void setCanalCompra(String canalCompra) {
        this.canalCompra = canalCompra;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getComCtipoDocum() {
        return comCtipoDocum;
    }

    public void setComCtipoDocum(String comCtipoDocum) {
        this.comCtipoDocum = comCtipoDocum;
    }

    public Date getComFfactur() {
        return comFfactur;
    }

    public void setComFfactur(Date comFfactur) {
        this.comFfactur = comFfactur;
    }

    public long getComNrofact() {
        return comNrofact;
    }

    public void setComNrofact(long comNrofact) {
        this.comNrofact = comNrofact;
    }

    public String getCanalCompraDet() {
        return canalCompraDet;
    }

    public void setCanalCompraDet(String canalCompraDet) {
        this.canalCompraDet = canalCompraDet;
    }

    public Date getFechaVencDet() {
        return fechaVencDet;
    }

    public void setFechaVencDet(Date fechaVencDet) {
        this.fechaVencDet = fechaVencDet;
    }
        
}
