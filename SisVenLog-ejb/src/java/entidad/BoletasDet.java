/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "boletas_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BoletasDet.findAll", query = "SELECT b FROM BoletasDet b")
    , @NamedQuery(name = "BoletasDet.findByCodEmpr", query = "SELECT b FROM BoletasDet b WHERE b.boletasDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "BoletasDet.findByNroBoleta", query = "SELECT b FROM BoletasDet b WHERE b.boletasDetPK.nroBoleta = :nroBoleta")
    , @NamedQuery(name = "BoletasDet.findByCodInstr", query = "SELECT b FROM BoletasDet b WHERE b.boletasDetPK.codInstr = :codInstr")
    , @NamedQuery(name = "BoletasDet.findByCodBanco", query = "SELECT b FROM BoletasDet b WHERE b.boletasDetPK.codBanco = :codBanco")
    , @NamedQuery(name = "BoletasDet.findByNroCheque", query = "SELECT b FROM BoletasDet b WHERE b.boletasDetPK.nroCheque = :nroCheque")
    , @NamedQuery(name = "BoletasDet.findByIdepositado", query = "SELECT b FROM BoletasDet b WHERE b.idepositado = :idepositado")})
public class BoletasDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BoletasDetPK boletasDetPK;
    @Column(name = "idepositado")
    private Long idepositado;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_boleta", referencedColumnName = "nro_boleta", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private BoletasDepositos boletasDepositos;
    @JoinColumn(name = "cod_instr", referencedColumnName = "cod_instr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private InstrumentosPagos instrumentosPagos;

    public BoletasDet() {
    }

    public BoletasDet(BoletasDetPK boletasDetPK) {
        this.boletasDetPK = boletasDetPK;
    }

    public BoletasDet(short codEmpr, long nroBoleta, String codInstr, short codBanco, String nroCheque) {
        this.boletasDetPK = new BoletasDetPK(codEmpr, nroBoleta, codInstr, codBanco, nroCheque);
    }

    public BoletasDetPK getBoletasDetPK() {
        return boletasDetPK;
    }

    public void setBoletasDetPK(BoletasDetPK boletasDetPK) {
        this.boletasDetPK = boletasDetPK;
    }

    public Long getIdepositado() {
        return idepositado;
    }

    public void setIdepositado(Long idepositado) {
        this.idepositado = idepositado;
    }

    public BoletasDepositos getBoletasDepositos() {
        return boletasDepositos;
    }

    public void setBoletasDepositos(BoletasDepositos boletasDepositos) {
        this.boletasDepositos = boletasDepositos;
    }

    public InstrumentosPagos getInstrumentosPagos() {
        return instrumentosPagos;
    }

    public void setInstrumentosPagos(InstrumentosPagos instrumentosPagos) {
        this.instrumentosPagos = instrumentosPagos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (boletasDetPK != null ? boletasDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BoletasDet)) {
            return false;
        }
        BoletasDet other = (BoletasDet) object;
        if ((this.boletasDetPK == null && other.boletasDetPK != null) || (this.boletasDetPK != null && !this.boletasDetPK.equals(other.boletasDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.BoletasDet[ boletasDetPK=" + boletasDetPK + " ]";
    }
    
}
