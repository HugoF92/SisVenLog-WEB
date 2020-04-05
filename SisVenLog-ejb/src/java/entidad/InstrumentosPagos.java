/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "instrumentos_pagos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstrumentosPagos.findAll", query = "SELECT i FROM InstrumentosPagos i")
    , @NamedQuery(name = "InstrumentosPagos.findByCodInstr", query = "SELECT i FROM InstrumentosPagos i WHERE i.codInstr = :codInstr")
    , @NamedQuery(name = "InstrumentosPagos.findByXdesc", query = "SELECT i FROM InstrumentosPagos i WHERE i.xdesc = :xdesc")
    , @NamedQuery(name = "InstrumentosPagos.findByFalta", query = "SELECT i FROM InstrumentosPagos i WHERE i.falta = :falta")
    , @NamedQuery(name = "InstrumentosPagos.findByCusuario", query = "SELECT i FROM InstrumentosPagos i WHERE i.cusuario = :cusuario")})
public class InstrumentosPagos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cod_instr")
    private String codInstr;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 10)
    @Column(name = "cusuario")
    private String cusuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instrumentosPagos")
    private Collection<PagosDet> pagosDetCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instrumentosPagos")
    private Collection<BoletasDet> boletasDetCollection;

    public InstrumentosPagos() {
    }

    public InstrumentosPagos(String codInstr) {
        this.codInstr = codInstr;
    }

    public String getCodInstr() {
        return codInstr;
    }

    public void setCodInstr(String codInstr) {
        this.codInstr = codInstr;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }

    public String getCusuario() {
        return cusuario;
    }

    public void setCusuario(String cusuario) {
        this.cusuario = cusuario;
    }

    @XmlTransient
    public Collection<PagosDet> getPagosDetCollection() {
        return pagosDetCollection;
    }

    public void setPagosDetCollection(Collection<PagosDet> pagosDetCollection) {
        this.pagosDetCollection = pagosDetCollection;
    }

    @XmlTransient
    public Collection<BoletasDet> getBoletasDetCollection() {
        return boletasDetCollection;
    }

    public void setBoletasDetCollection(Collection<BoletasDet> boletasDetCollection) {
        this.boletasDetCollection = boletasDetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codInstr != null ? codInstr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InstrumentosPagos)) {
            return false;
        }
        InstrumentosPagos other = (InstrumentosPagos) object;
        if ((this.codInstr == null && other.codInstr != null) || (this.codInstr != null && !this.codInstr.equals(other.codInstr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.InstrumentosPagos[ codInstr=" + codInstr + " ]";
    }
    
}
