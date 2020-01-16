/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entidad.Cheques;

/**
 *
 * @author Edu
 */
public class ChequeNoCobrado {
    private Cheques cheque;
    private String nombreBanco;
    private String nombreCliente;
    private boolean chequeSeleccionado;

    public ChequeNoCobrado() {
    }
        
    public Cheques getCheque() {
        return cheque;
    }

    public void setCheque(Cheques cheque) {
        this.cheque = cheque;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public boolean isChequeSeleccionado() {
        return chequeSeleccionado;
    }

    public void setChequeSeleccionado(boolean chequeSeleccionado) {
        this.chequeSeleccionado = chequeSeleccionado;
    }
        
}
