/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author lmore
 */
@Named
@SessionScoped
public class TablaCuentasBancarias {

    public TablaCuentasBancarias() {
    }

    private String codigoBanco;
    private String descBanco;
    private String nroCuentaCorriente;
    private boolean deletedProvisorio;

    /**
     * @return the codigoBanco
     */
    public String getCodigoBanco() {
        return codigoBanco;
    }

    /**
     * @param codigoBanco the codigoBanco to set
     */
    public void setCodigoBanco(String codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    /**
     * @return the descBanco
     */
    public String getDescBanco() {
        return descBanco;
    }

    /**
     * @param descBanco the descBanco to set
     */
    public void setDescBanco(String descBanco) {
        this.descBanco = descBanco;
    }

    /**
     * @return the nroCuentaCorriente
     */
    public String getNroCuentaCorriente() {
        return nroCuentaCorriente;
    }

    /**
     * @param nroCuentaCorriente the nroCuentaCorriente to set
     */
    public void setNroCuentaCorriente(String nroCuentaCorriente) {
        this.nroCuentaCorriente = nroCuentaCorriente;
    }

    /**
     * @return the deletedProvisorio
     */
    public boolean isDeletedProvisorio() {
        return deletedProvisorio;
    }

    /**
     * @param deletedProvisorio the deletedProvisorio to set
     */
    public void setDeletedProvisorio(boolean deletedProvisorio) {
        this.deletedProvisorio = deletedProvisorio;
    }

}
