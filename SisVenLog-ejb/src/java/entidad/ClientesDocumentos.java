/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lmore
 */
@Entity
@Table(name = "clientes_documentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClientesDocumentos.findAll", query = "SELECT c FROM ClientesDocumentos c")
    , @NamedQuery(name = "ClientesDocumentos.findByCodCliente", query = "SELECT c FROM ClientesDocumentos c WHERE c.clientesDocumentosPK.codCliente = :codCliente")
    , @NamedQuery(name = "ClientesDocumentos.findByCdocum", query = "SELECT c FROM ClientesDocumentos c WHERE c.cdocum = :cdocum")
    , @NamedQuery(name = "ClientesDocumentos.findByXobs", query = "SELECT c FROM ClientesDocumentos c WHERE c.xobs = :xobs")
    , @NamedQuery(name = "ClientesDocumentos.findByFvencimiento", query = "SELECT c FROM ClientesDocumentos c WHERE c.fvencimiento = :fvencimiento")
    , @NamedQuery(name = "ClientesDocumentos.findByFalta", query = "SELECT c FROM ClientesDocumentos c WHERE c.falta = :falta")
    , @NamedQuery(name = "ClientesDocumentos.findByCusuario", query = "SELECT c FROM ClientesDocumentos c WHERE c.cusuario = :cusuario")
    , @NamedQuery(name = "ClientesDocumentos.findByFultimModif", query = "SELECT c FROM ClientesDocumentos c WHERE c.fultimModif = :fultimModif")
    , @NamedQuery(name = "ClientesDocumentos.findByCusuarioModifi", query = "SELECT c FROM ClientesDocumentos c WHERE c.cusuarioModifi = :cusuarioModifi")
    , @NamedQuery(name = "ClientesDocumentos.findByNsecuencia", query = "SELECT c FROM ClientesDocumentos c WHERE c.clientesDocumentosPK.nsecuencia = :nsecuencia")
    , @NamedQuery(name = "ClientesDocumentos.findByCusuarioPresentacion", query = "SELECT c FROM ClientesDocumentos c WHERE c.cusuarioPresentacion = :cusuarioPresentacion")
    , @NamedQuery(name = "ClientesDocumentos.findByCodEmpr", query = "SELECT c FROM ClientesDocumentos c WHERE c.codEmpr = :codEmpr")})
public class ClientesDocumentos implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "cdocum")
    private String cdocum;
    @Size(max = 200)
    @Column(name = "xobs")
    private String xobs;
    @Size(max = 50)
    @Column(name = "cusuario")
    private String cusuario;
    @Size(max = 50)
    @Column(name = "cusuario_modifi")
    private String cusuarioModifi;
    @Size(max = 50)
    @Column(name = "cusuario_presentacion")
    private String cusuarioPresentacion;
    @Transient
    private boolean presentado;

    @Transient
    private boolean obligatorio;
    @JoinColumn(name = "cod_cliente", referencedColumnName = "cod_cliente", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Clientes clientes;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ClientesDocumentosPK clientesDocumentosPK;
    @Column(name = "fvencimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fvencimiento;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
    @Column(name = "cod_empr")
    private Short codEmpr;

    @Column(name = "fpresentacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fPresentacion;
    
    @Transient
    private String nombreDocumento;
    
    public ClientesDocumentos() {
    }

    public ClientesDocumentos(ClientesDocumentosPK clientesDocumentosPK) {
        this.clientesDocumentosPK = clientesDocumentosPK;
    }

    public ClientesDocumentos(ClientesDocumentosPK clientesDocumentosPK, String cdocum) {
        this.clientesDocumentosPK = clientesDocumentosPK;
        this.cdocum = cdocum;
    }

    public ClientesDocumentos(long codCliente, short nsecuencia) {
        this.clientesDocumentosPK = new ClientesDocumentosPK(codCliente, nsecuencia);
    }

    public ClientesDocumentosPK getClientesDocumentosPK() {
        return clientesDocumentosPK;
    }

    public void setClientesDocumentosPK(ClientesDocumentosPK clientesDocumentosPK) {
        this.clientesDocumentosPK = clientesDocumentosPK;
    }

    public Date getFvencimiento() {
        return fvencimiento;
    }

    public void setFvencimiento(Date fvencimiento) {
        this.fvencimiento = fvencimiento;
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

    public String getCusuarioModifi() {
        return cusuarioModifi;
    }

    public void setCusuarioModifi(String cusuarioModifi) {
        this.cusuarioModifi = cusuarioModifi;
    }

    public String getCusuarioPresentacion() {
        return cusuarioPresentacion;
    }

    public void setCusuarioPresentacion(String cusuarioPresentacion) {
        this.cusuarioPresentacion = cusuarioPresentacion;
    }

    public Short getCodEmpr() {
        return codEmpr;
    }

    public void setCodEmpr(Short codEmpr) {
        this.codEmpr = codEmpr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientesDocumentosPK != null ? clientesDocumentosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientesDocumentos)) {
            return false;
        }
        ClientesDocumentos other = (ClientesDocumentos) object;
        if ((this.clientesDocumentosPK == null && other.clientesDocumentosPK != null) || (this.clientesDocumentosPK != null && !this.clientesDocumentosPK.equals(other.clientesDocumentosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ClientesDocumentos[ clientesDocumentosPK=" + clientesDocumentosPK + " ]";
    }

    /**
     * @return the fPresentacion
     */
    public Date getfPresentacion() {
        return fPresentacion;
    }

    /**
     * @param fPresentacion the fPresentacion to set
     */
    public void setfPresentacion(Date fPresentacion) {
        this.fPresentacion = fPresentacion;
    }


//    public String getCusuarioModifi() {
//        return cusuarioModifi;
//    }
//
//    public void setCusuarioModifi(String cusuarioModifi) {
//        this.cusuarioModifi = cusuarioModifi;
//    }
//
//    public String getCusuarioPresentacion() {
//        return cusuarioPresentacion;
//    }
//
//    public void setCusuarioPresentacion(String cusuarioPresentacion) {
//        this.cusuarioPresentacion = cusuarioPresentacion;
//    }



    public Clientes getClientes() {
        return clientes;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }



    public boolean getPresentado() {
        return presentado;
    }

    public void setPresentado(boolean presentado) {
        this.presentado = presentado;
    }

    public boolean getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public String getCdocum() {
        return cdocum;
    }

    public void setCdocum(String cdocum) {
        this.cdocum = cdocum;
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

    /**
     * @return the nombreDocumento
     */
    public String getNombreDocumento() {
        return nombreDocumento;
    }

    /**
     * @param nombreDocumento the nombreDocumento to set
     */
    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }



}
