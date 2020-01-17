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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "pagos_det")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PagosDet.findAll", query = "SELECT p FROM PagosDet p")
    , @NamedQuery(name = "PagosDet.findByCodEmpr", query = "SELECT p FROM PagosDet p WHERE p.pagosDetPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "PagosDet.findByNroPago", query = "SELECT p FROM PagosDet p WHERE p.pagosDetPK.nroPago = :nroPago")
    , @NamedQuery(name = "PagosDet.findByCodInstr", query = "SELECT p FROM PagosDet p WHERE p.pagosDetPK.codInstr = :codInstr")
    , @NamedQuery(name = "PagosDet.findByCodBanco", query = "SELECT p FROM PagosDet p WHERE p.codBanco = :codBanco")
    , @NamedQuery(name = "PagosDet.findByNroCheque", query = "SELECT p FROM PagosDet p WHERE p.nroCheque = :nroCheque")
    , @NamedQuery(name = "PagosDet.findByIpagado", query = "SELECT p FROM PagosDet p WHERE p.ipagado = :ipagado")
    , @NamedQuery(name = "PagosDet.findByKdocumentos", query = "SELECT p FROM PagosDet p WHERE p.kdocumentos = :kdocumentos")})
public class PagosDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PagosDetPK pagosDetPK;
    @Column(name = "cod_banco")
    private Short codBanco;
    @Size(max = 12)
    @Column(name = "nro_cheque")
    private String nroCheque;
    @Column(name = "ipagado")
    private Long ipagado;
    @Column(name = "kdocumentos")
    private Short kdocumentos;
    @JoinColumn(name = "cod_instr", referencedColumnName = "cod_instr", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private InstrumentosPagos instrumentosPagos;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "nro_pago", referencedColumnName = "nro_pago", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Pagos pagos;

    public PagosDet() {
    }

    public PagosDet(PagosDetPK pagosDetPK) {
        this.pagosDetPK = pagosDetPK;
    }

    public PagosDet(short codEmpr, short nroPago, String codInstr) {
        this.pagosDetPK = new PagosDetPK(codEmpr, nroPago, codInstr);
    }

    public PagosDetPK getPagosDetPK() {
        return pagosDetPK;
    }

    public void setPagosDetPK(PagosDetPK pagosDetPK) {
        this.pagosDetPK = pagosDetPK;
    }

    public Short getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(Short codBanco) {
        this.codBanco = codBanco;
    }

    public String getNroCheque() {
        return nroCheque;
    }

    public void setNroCheque(String nroCheque) {
        this.nroCheque = nroCheque;
    }

    public Long getIpagado() {
        return ipagado;
    }

    public void setIpagado(Long ipagado) {
        this.ipagado = ipagado;
    }

    public Short getKdocumentos() {
        return kdocumentos;
    }

    public void setKdocumentos(Short kdocumentos) {
        this.kdocumentos = kdocumentos;
    }

    public InstrumentosPagos getInstrumentosPagos() {
        return instrumentosPagos;
    }

    public void setInstrumentosPagos(InstrumentosPagos instrumentosPagos) {
        this.instrumentosPagos = instrumentosPagos;
    }

    public Pagos getPagos() {
        return pagos;
    }

    public void setPagos(Pagos pagos) {
        this.pagos = pagos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pagosDetPK != null ? pagosDetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagosDet)) {
            return false;
        }
        PagosDet other = (PagosDet) object;
        if ((this.pagosDetPK == null && other.pagosDetPK != null) || (this.pagosDetPK != null && !this.pagosDetPK.equals(other.pagosDetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.PagosDet[ pagosDetPK=" + pagosDetPK + " ]";
    }
    
}
