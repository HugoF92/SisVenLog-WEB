/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author admin
 */
@Embeddable
public class EnviosPK implements Serializable {

    @Basic(optional = false)
    @NotNull(message="Se debe especificar el codigo empresa.")
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull(message="Se debe especificar el nro de envio.")
    @Column(name = "nro_envio")
    private long nroEnvio;

    public EnviosPK() {
    }

    public EnviosPK(short codEmpr, long nroEnvio) {
        this.codEmpr = codEmpr;
        this.nroEnvio = nroEnvio;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public long getNroEnvio() {
        return nroEnvio;
    }

    public void setNroEnvio(long nroEnvio) {
        this.nroEnvio = nroEnvio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpr;
        hash += (int) nroEnvio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnviosPK)) {
            return false;
        }
        EnviosPK other = (EnviosPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if (this.nroEnvio != other.nroEnvio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.EnviosPK[ codEmpr=" + codEmpr + ", nroEnvio=" + nroEnvio + " ]";
    }
    
}
