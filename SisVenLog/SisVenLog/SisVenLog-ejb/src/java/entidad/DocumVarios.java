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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name = "docum_varios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumVarios.findAll", query = "SELECT d FROM DocumVarios d")
    , @NamedQuery(name = "DocumVarios.findByCodEmpr", query = "SELECT d FROM DocumVarios d WHERE d.documVariosPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "DocumVarios.findByCtipoDocum", query = "SELECT d FROM DocumVarios d WHERE d.documVariosPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "DocumVarios.findByNdocum", query = "SELECT d FROM DocumVarios d WHERE d.documVariosPK.ndocum = :ndocum")
    , @NamedQuery(name = "DocumVarios.findByXnombre", query = "SELECT d FROM DocumVarios d WHERE d.xnombre = :xnombre")
    , @NamedQuery(name = "DocumVarios.findByFdocum", query = "SELECT d FROM DocumVarios d WHERE d.fdocum = :fdocum")
    , @NamedQuery(name = "DocumVarios.findByMestado", query = "SELECT d FROM DocumVarios d WHERE d.mestado = :mestado")
    , @NamedQuery(name = "DocumVarios.findByXobs", query = "SELECT d FROM DocumVarios d WHERE d.xobs = :xobs")
    , @NamedQuery(name = "DocumVarios.findByFalta", query = "SELECT d FROM DocumVarios d WHERE d.falta = :falta")
    , @NamedQuery(name = "DocumVarios.findByCusuario", query = "SELECT d FROM DocumVarios d WHERE d.cusuario = :cusuario")
    , @NamedQuery(name = "DocumVarios.findByFultimModif", query = "SELECT d FROM DocumVarios d WHERE d.fultimModif = :fultimModif")
    , @NamedQuery(name = "DocumVarios.findByCusuarioModif", query = "SELECT d FROM DocumVarios d WHERE d.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "DocumVarios.findByFanul", query = "SELECT d FROM DocumVarios d WHERE d.fanul = :fanul")
    , @NamedQuery(name = "DocumVarios.findByCusuarioAnul", query = "SELECT d FROM DocumVarios d WHERE d.cusuarioAnul = :cusuarioAnul")
    , @NamedQuery(name = "DocumVarios.findByNroPedido", query = "SELECT d FROM DocumVarios d WHERE d.nroPedido = :nroPedido")
    , @NamedQuery(name = "DocumVarios.findByCusuarioAutoriz", query = "SELECT d FROM DocumVarios d WHERE d.cusuarioAutoriz = :cusuarioAutoriz")})
public class DocumVarios implements Serializable {

    @Size(max = 50)
    @Column(name = "xnombre")
    private String xnombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fdocum")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fdocum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mestado")
    private Character mestado;
    @Size(max = 250)
    @Column(name = "xobs")
    private String xobs;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Size(max = 30)
    @Column(name = "cusuario_anul")
    private String cusuarioAnul;
    @Size(max = 30)
    @Column(name = "cusuario_autoriz")
    private String cusuarioAutoriz;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documVarios")
    private Collection<DocumVariosDet> documVariosDetCollection;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DocumVariosPK documVariosPK;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Column(name = "fanul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fanul;
    @Column(name = "nro_pedido")
    private Long nroPedido;
    @JoinColumn(name = "cod_cliente", referencedColumnName = "cod_cliente")
    @ManyToOne
    private Clientes codCliente;
    @JoinColumns({
        @JoinColumn(name = "cod_empr", referencedColumnName = "cod_empr", insertable = false, updatable = false)
        , @JoinColumn(name = "cod_depo", referencedColumnName = "cod_depo")})
    @ManyToOne(optional = false)
    private Depositos depositos;

    public DocumVarios() {
    }

    public DocumVarios(DocumVariosPK documVariosPK) {
        this.documVariosPK = documVariosPK;
    }

    public DocumVarios(DocumVariosPK documVariosPK, Date fdocum, Character mestado) {
        this.documVariosPK = documVariosPK;
        this.fdocum = fdocum;
        this.mestado = mestado;
    }

    public DocumVarios(short codEmpr, String ctipoDocum, int ndocum) {
        this.documVariosPK = new DocumVariosPK(codEmpr, ctipoDocum, ndocum);
    }

    public DocumVariosPK getDocumVariosPK() {
        return documVariosPK;
    }

    public void setDocumVariosPK(DocumVariosPK documVariosPK) {
        this.documVariosPK = documVariosPK;
    }


    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }


    public Date getFultimModif() {
        return fultimModif;
    }

    public void setFultimModif(Date fultimModif) {
        this.fultimModif = fultimModif;
    }

    public String getCusuarioModif() {
        return cusuarioModif;
    }

    public void setCusuarioModif(String cusuarioModif) {
        this.cusuarioModif = cusuarioModif;
    }

    public Date getFanul() {
        return fanul;
    }

    public void setFanul(Date fanul) {
        this.fanul = fanul;
    }

    public String getCusuarioAnul() {
        return cusuarioAnul;
    }

    public void setCusuarioAnul(String cusuarioAnul) {
        this.cusuarioAnul = cusuarioAnul;
    }

    public Long getNroPedido() {
        return nroPedido;
    }

    public void setNroPedido(Long nroPedido) {
        this.nroPedido = nroPedido;
    }

    public String getCusuarioAutoriz() {
        return cusuarioAutoriz;
    }

    public void setCusuarioAutoriz(String cusuarioAutoriz) {
        this.cusuarioAutoriz = cusuarioAutoriz;
    }

    public Clientes getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Clientes codCliente) {
        this.codCliente = codCliente;
    }

    public Depositos getDepositos() {
        return depositos;
    }

    public void setDepositos(Depositos depositos) {
        this.depositos = depositos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documVariosPK != null ? documVariosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumVarios)) {
            return false;
        }
        DocumVarios other = (DocumVarios) object;
        if ((this.documVariosPK == null && other.documVariosPK != null) || (this.documVariosPK != null && !this.documVariosPK.equals(other.documVariosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.DocumVarios[ documVariosPK=" + documVariosPK + " ]";
    }

    public void setDocumVariosPK(TiposDocumentos tiposDocumentos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getXnombre() {
        return xnombre;
    }

    public void setXnombre(String xnombre) {
        this.xnombre = xnombre;
    }

    public Date getFdocum() {
        return fdocum;
    }

    public void setFdocum(Date fdocum) {
        this.fdocum = fdocum;
    }

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
    }

    public String getXobs() {
        return xobs;
    }

    public void setXobs(String xobs) {
        this.xobs = xobs;
    }

    public String getCusuario() {
        return cusuario;
    }

    public void setCusuario(String cusuario) {
        this.cusuario = cusuario;
    }

    @XmlTransient
    public Collection<DocumVariosDet> getDocumVariosDetCollection() {
        return documVariosDetCollection;
    }

    public void setDocumVariosDetCollection(Collection<DocumVariosDet> documVariosDetCollection) {
        this.documVariosDetCollection = documVariosDetCollection;
    }
    
}
