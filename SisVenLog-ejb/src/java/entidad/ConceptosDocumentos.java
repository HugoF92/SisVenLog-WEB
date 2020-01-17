/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "conceptos_documentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConceptosDocumentos.findAll", query = "SELECT c FROM ConceptosDocumentos c")
    , @NamedQuery(name = "ConceptosDocumentos.findByCtipoDocum", query = "SELECT c FROM ConceptosDocumentos c WHERE c.conceptosDocumentosPK.ctipoDocum = :ctipoDocum")
    , @NamedQuery(name = "ConceptosDocumentos.findByCconc", query = "SELECT c FROM ConceptosDocumentos c WHERE c.conceptosDocumentosPK.cconc = :cconc")
    , @NamedQuery(name = "ConceptosDocumentos.findByXdesc", query = "SELECT c FROM ConceptosDocumentos c WHERE c.xdesc = :xdesc")
    , @NamedQuery(name = "ConceptosDocumentos.findByMafectaStock", query = "SELECT c FROM ConceptosDocumentos c WHERE c.mafectaStock = :mafectaStock")
    , @NamedQuery(name = "ConceptosDocumentos.findByCusuario", query = "SELECT c FROM ConceptosDocumentos c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "ConceptosDocumentos.findByFalta", query = "SELECT c FROM ConceptosDocumentos c WHERE c.falta = :falta")
    , @NamedQuery(name = "ConceptosDocumentos.findByFultimModif", query = "SELECT c FROM ConceptosDocumentos c WHERE c.fultimModif = :fultimModif")
    , @NamedQuery(name = "ConceptosDocumentos.findByCusuarioModif", query = "SELECT c FROM ConceptosDocumentos c WHERE c.cusuarioModif = :cusuarioModif")
    , @NamedQuery(name = "ConceptosDocumentos.findByKdiasdif", query = "SELECT c FROM ConceptosDocumentos c WHERE c.kdiasdif = :kdiasdif")
    , @NamedQuery(name = "ConceptosDocumentos.findByXctacble10", query = "SELECT c FROM ConceptosDocumentos c WHERE c.xctacble10 = :xctacble10")
    , @NamedQuery(name = "ConceptosDocumentos.findByXctacble5", query = "SELECT c FROM ConceptosDocumentos c WHERE c.xctacble5 = :xctacble5")
    , @NamedQuery(name = "ConceptosDocumentos.findByXctacblex", query = "SELECT c FROM ConceptosDocumentos c WHERE c.xctacblex = :xctacblex")})
public class ConceptosDocumentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ConceptosDocumentosPK conceptosDocumentosPK;
    @Size(max = 50)
    @Column(name = "xdesc")
    private String xdesc;
    @Column(name = "mafecta_stock")
    private Character mafectaStock;
    @Size(max = 30)
    @Column(name = "cusuario")
    private String cusuario;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Size(max = 30)
    @Column(name = "cusuario_modif")
    private String cusuarioModif;
    @Column(name = "kdiasdif")
    private Short kdiasdif;
    @Size(max = 20)
    @Column(name = "xctacble10")
    private String xctacble10;
    @Size(max = 20)
    @Column(name = "xctacble5")
    private String xctacble5;
    @Size(max = 20)
    @Column(name = "xctacblex")
    private String xctacblex;

    public ConceptosDocumentos() {
    }

    public ConceptosDocumentos(ConceptosDocumentosPK conceptosDocumentosPK) {
        this.conceptosDocumentosPK = conceptosDocumentosPK;
    }

    public ConceptosDocumentos(String ctipoDocum, String cconc) {
        this.conceptosDocumentosPK = new ConceptosDocumentosPK(ctipoDocum, cconc);
    }

    public ConceptosDocumentosPK getConceptosDocumentosPK() {
        return conceptosDocumentosPK;
    }

    public void setConceptosDocumentosPK(ConceptosDocumentosPK conceptosDocumentosPK) {
        this.conceptosDocumentosPK = conceptosDocumentosPK;
    }

    public String getXdesc() {
        return xdesc;
    }

    public void setXdesc(String xdesc) {
        this.xdesc = xdesc;
    }

    public Character getMafectaStock() {
        return mafectaStock;
    }

    public void setMafectaStock(Character mafectaStock) {
        this.mafectaStock = mafectaStock;
    }

    public String getCusuario() {
        return cusuario;
    }

    public void setCusuario(String cusuario) {
        this.cusuario = cusuario;
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

    public Short getKdiasdif() {
        return kdiasdif;
    }

    public void setKdiasdif(Short kdiasdif) {
        this.kdiasdif = kdiasdif;
    }

    public String getXctacble10() {
        return xctacble10;
    }

    public void setXctacble10(String xctacble10) {
        this.xctacble10 = xctacble10;
    }

    public String getXctacble5() {
        return xctacble5;
    }

    public void setXctacble5(String xctacble5) {
        this.xctacble5 = xctacble5;
    }

    public String getXctacblex() {
        return xctacblex;
    }

    public void setXctacblex(String xctacblex) {
        this.xctacblex = xctacblex;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (conceptosDocumentosPK != null ? conceptosDocumentosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConceptosDocumentos)) {
            return false;
        }
        ConceptosDocumentos other = (ConceptosDocumentos) object;
        if ((this.conceptosDocumentosPK == null && other.conceptosDocumentosPK != null) || (this.conceptosDocumentosPK != null && !this.conceptosDocumentosPK.equals(other.conceptosDocumentosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ConceptosDocumentos[ conceptosDocumentosPK=" + conceptosDocumentosPK + " ]";
    }
    
}
