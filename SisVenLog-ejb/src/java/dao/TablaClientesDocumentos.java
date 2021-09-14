/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Date;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author lmore
 */
@Named
@SessionScoped
public class TablaClientesDocumentos {

    TablaClientesDocumentos() {
    }

    private String codigoCliente;
    private Date fvencimiento;
    private String cDocum;
    private String xDesc;
    private String xObs;
    private Date fPresentacion;
    private String cUsuario_presentacion;
    private String mObligatorio;
    private String conFecVto;
    private String secuencia;
    private boolean marcado;

    /**
     * @return the codigoCliente
     */
    public String getCodigoCliente() {
        return codigoCliente;
    }

    /**
     * @param codigoCliente the codigoCliente to set
     */
    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    /**
     * @return the fvencimiento
     */
    public Date getFvencimiento() {
        return fvencimiento;
    }

    /**
     * @param fvencimiento the fvencimiento to set
     */
    public void setFvencimiento(Date fvencimiento) {
        this.fvencimiento = fvencimiento;
    }

    /**
     * @return the cDocum
     */
    public String getcDocum() {
        return cDocum;
    }

    /**
     * @param cDocum the cDocum to set
     */
    public void setcDocum(String cDocum) {
        this.cDocum = cDocum;
    }

    /**
     * @return the xDesc
     */
    public String getxDesc() {
        return xDesc;
    }

    /**
     * @param xDesc the xDesc to set
     */
    public void setxDesc(String xDesc) {
        this.xDesc = xDesc;
    }

    /**
     * @return the xObs
     */
    public String getxObs() {
        return xObs;
    }

    /**
     * @param xObs the xObs to set
     */
    public void setxObs(String xObs) {
        this.xObs = xObs;
    }

    /**
     * @return the fPresentacion
     */
    public Date getfPresentacion() {
        return fPresentacion;
    }

    /**
     * @param fPresentacion the fPresentacion to set
     */
    public void setfPresentacion(Date fPresentacion) {
        this.fPresentacion = fPresentacion;
    }

    /**
     * @return the cUsuario_presentacion
     */
    public String getcUsuario_presentacion() {
        return cUsuario_presentacion;
    }

    /**
     * @param cUsuario_presentacion the cUsuario_presentacion to set
     */
    public void setcUsuario_presentacion(String cUsuario_presentacion) {
        this.cUsuario_presentacion = cUsuario_presentacion;
    }

    /**
     * @return the mObligatorio
     */
    public String getmObligatorio() {
        return mObligatorio;
    }

    /**
     * @param mObligatorio the mObligatorio to set
     */
    public void setmObligatorio(String mObligatorio) {
        this.mObligatorio = mObligatorio;
    }

    /**
     * @return the conFecVto
     */
    public String getConFecVto() {
        return conFecVto;
    }

    /**
     * @param conFecVto the conFecVto to set
     */
    public void setConFecVto(String conFecVto) {
        this.conFecVto = conFecVto;
    }

    /**
     * @return the marcado
     */
    public boolean isMarcado() {
        return marcado; 
    }

    /**
     * @param marcado the marcado to set
     */
    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    /**
     * @return the secuencia
     */
    public String getSecuencia() {
        return secuencia;
    }

    /**
     * @param secuencia the secuencia to set
     */
    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }



}

