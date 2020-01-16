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
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
@Embeddable
public class EnviosDetPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "cod_merca")
    private String codMerca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_envio")
    private long nroEnvio;

    public EnviosDetPK() {
    }

    public EnviosDetPK(short codEmpr, String codMerca, long nroEnvio) {
        this.codEmpr = codEmpr;
        this.codMerca = codMerca;
        this.nroEnvio = nroEnvio;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
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
        hash += (codMerca != null ? codMerca.hashCode() : 0);
        hash += (int) nroEnvio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnviosDetPK)) {
            return false;
        }
        EnviosDetPK other = (EnviosDetPK) object;
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if ((this.codMerca == null && other.codMerca != null) || (this.codMerca != null && !this.codMerca.equals(other.codMerca))) {
            return false;
        }
        if (this.nroEnvio != other.nroEnvio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.EnviosDetPK[ codEmpr=" + codEmpr + ", codMerca=" + codMerca + ", nroEnvio=" + nroEnvio + " ]";
    }
    
}
