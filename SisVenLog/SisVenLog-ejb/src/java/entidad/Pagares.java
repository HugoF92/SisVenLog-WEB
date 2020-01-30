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
@Table(name = "pagares")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pagares.findAll", query = "SELECT p FROM Pagares p")
    , @NamedQuery(name = "Pagares.findByCodEmpr", query = "SELECT p FROM Pagares p WHERE p.pagaresPK.codEmpr = :codEmpr")
    , @NamedQuery(name = "Pagares.findByNpagare", query = "SELECT p FROM Pagares p WHERE p.pagaresPK.npagare = :npagare")
    , @NamedQuery(name = "Pagares.findByFvenc", query = "SELECT p FROM Pagares p WHERE p.fvenc = :fvenc")
    , @NamedQuery(name = "Pagares.findByIpagare", query = "SELECT p FROM Pagares p WHERE p.ipagare = :ipagare")
    , @NamedQuery(name = "Pagares.findByFemision", query = "SELECT p FROM Pagares p WHERE p.femision = :femision")
    , @NamedQuery(name = "Pagares.findByCodEntregador", query = "SELECT p FROM Pagares p WHERE p.codEntregador = :codEntregador")
    , @NamedQuery(name = "Pagares.findByCodZona", query = "SELECT p FROM Pagares p WHERE p.codZona = :codZona")
    , @NamedQuery(name = "Pagares.findByMestado", query = "SELECT p FROM Pagares p WHERE p.mestado = :mestado")
    , @NamedQuery(name = "Pagares.findByFalta", query = "SELECT p FROM Pagares p WHERE p.falta = :falta")
    , @NamedQuery(name = "Pagares.findByCusuario", query = "SELECT p FROM Pagares p WHERE p.cusuario = :cusuario")
    , @NamedQuery(name = "Pagares.findByFultimModif", query = "SELECT p FROM Pagares p WHERE p.fultimModif = :fultimModif")
    , @NamedQuery(name = "Pagares.findByCusuarioModif", query = "SELECT p FROM Pagares p WHERE p.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "Pagares.findByFcobro", query = "SELECT p FROM Pagares p WHERE p.fcobro = :fcobro")})
public class Pagares implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PagaresPK pagaresPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fvenc")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fvenc;
    @Column(name = "ipagare")
    private Long ipagare;
    @Column(name = "femision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date femision;
    @Column(name = "cod_entregador")
    private Integer codEntregador;
    @Size(max = 3)
    @Column(name = "cod_zona")
    private String codZona;
    @Column(name = "mestado")
    private Character mestado;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "fcobro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcobro;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pagares")
    private Collection<PagaresDet> pagaresDetCollection;
    @JoinColumn(name = "cod_cliente", referencedColumnName = "cod_cliente")
    @ManyToOne
    private Clientes codCliente;

    public Pagares() {
    }

    public Pagares(PagaresPK pagaresPK) {
        this.pagaresPK = pagaresPK;
    }

    public Pagares(PagaresPK pagaresPK, Date fvenc) {
        this.pagaresPK = pagaresPK;
        this.fvenc = fvenc;
    }

    public Pagares(short codEmpr, long npagare) {
        this.pagaresPK = new PagaresPK(codEmpr, npagare);
    }

    public PagaresPK getPagaresPK() {
        return pagaresPK;
    }

    public void setPagaresPK(PagaresPK pagaresPK) {
        this.pagaresPK = pagaresPK;
    }

    public Date getFvenc() {
        return fvenc;
    }

    public void setFvenc(Date fvenc) {
        this.fvenc = fvenc;
    }

    public Long getIpagare() {
        return ipagare;
    }

    public void setIpagare(Long ipagare) {
        this.ipagare = ipagare;
    }

    public Date getFemision() {
        return femision;
    }

    public void setFemision(Date femision) {
        this.femision = femision;
    }

    public Integer getCodEntregador() {
        return codEntregador;
    }

    public void setCodEntregador(Integer codEntregador) {
        this.codEntregador = codEntregador;
    }

    public String getCodZona() {
        return codZona;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public Character getMestado() {
        return mestado;
    }

    public void setMestado(Character mestado) {
        this.mestado = mestado;
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

    public Date getFcobro() {
        return fcobro;
    }

    public void setFcobro(Date fcobro) {
        this.fcobro = fcobro;
    }

    @XmlTransient
    public Collection<PagaresDet> getPagaresDetCollection() {
        return pagaresDetCollection;
    }

    public void setPagaresDetCollection(Collection<PagaresDet> pagaresDetCollection) {
        this.pagaresDetCollection = pagaresDetCollection;
    }

    public Clientes getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Clientes codCliente) {
        this.codCliente = codCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pagaresPK != null ? pagaresPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pagares)) {
            return false;
        }
        Pagares other = (Pagares) object;
        if ((this.pagaresPK == null && other.pagaresPK != null) || (this.pagaresPK != null && !this.pagaresPK.equals(other.pagaresPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Pagares[ pagaresPK=" + pagaresPK + " ]";
    }
    
}
