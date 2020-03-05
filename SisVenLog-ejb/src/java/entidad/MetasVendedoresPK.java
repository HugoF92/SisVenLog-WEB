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
public class MetasVendedoresPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empleado")
    private short codEmpleado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_empr")
    private short codEmpr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cod_zona")
    private String codZona;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_sublinea")
    private short codSublinea;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nmes")
    private short nmes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nanno")
    private short nanno;

    public MetasVendedoresPK() {
    }

    public MetasVendedoresPK(short codEmpleado, short codEmpr, String codZona, short codSublinea, short nmes, short nanno) {
        this.codEmpleado = codEmpleado;
        this.codEmpr = codEmpr;
        this.codZona = codZona;
        this.codSublinea = codSublinea;
        this.nmes = nmes;
        this.nanno = nanno;
    }

    public short getCodEmpleado() {
        return codEmpleado;
    }

    public void setCodEmpleado(short codEmpleado) {
        this.codEmpleado = codEmpleado;
    }

    public short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(short codEmpr) {
        this.codEmpr = codEmpr;
    }

    public String getCodZona() {
        return codZona;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public short getCodSublinea() {
        return codSublinea;
    }

    public void setCodSublinea(short codSublinea) {
        this.codSublinea = codSublinea;
    }

    public short getNmes() {
        return nmes;
    }

    public void setNmes(short nmes) {
        this.nmes = nmes;
    }

    public short getNanno() {
        return nanno;
    }

    public void setNanno(short nanno) {
        this.nanno = nanno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEmpleado;
        hash += (int) codEmpr;
        hash += (codZona != null ? codZona.hashCode() : 0);
        hash += (int) codSublinea;
        hash += (int) nmes;
        hash += (int) nanno;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetasVendedoresPK)) {
            return false;
        }
        MetasVendedoresPK other = (MetasVendedoresPK) object;
        if (this.codEmpleado != other.codEmpleado) {
            return false;
        }
        if (this.codEmpr != other.codEmpr) {
            return false;
        }
        if ((this.codZona == null && other.codZona != null) || (this.codZona != null && !this.codZona.equals(other.codZona))) {
            return false;
        }
        if (this.codSublinea != other.codSublinea) {
            return false;
        }
        if (this.nmes != other.nmes) {
            return false;
        }
        if (this.nanno != other.nanno) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.MetasVendedoresPK[ codEmpleado=" + codEmpleado + ", codEmpr=" + codEmpr + ", codZona=" + codZona + ", codSublinea=" + codSublinea + ", nmes=" + nmes + ", nanno=" + nanno + " ]";
    }
    
}
