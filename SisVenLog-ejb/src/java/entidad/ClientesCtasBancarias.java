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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "clientes_ctas_bancarias")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClientesCtasBancarias.findAll", query = "SELECT c FROM ClientesCtasBancarias c")
    , @NamedQuery(name = "ClientesCtasBancarias.findByCodCliente", query = "SELECT c FROM ClientesCtasBancarias c WHERE c.clientesCtasBancariasPK.codCliente = :codCliente")
    , @NamedQuery(name = "ClientesCtasBancarias.findByXctaBancaria", query = "SELECT c FROM ClientesCtasBancarias c WHERE c.xctaBancaria = :xctaBancaria")
    , @NamedQuery(name = "ClientesCtasBancarias.findByFalta", query = "SELECT c FROM ClientesCtasBancarias c WHERE c.falta = :falta")
    , @NamedQuery(name = "ClientesCtasBancarias.findByCusuAlta", query = "SELECT c FROM ClientesCtasBancarias c WHERE c.cusuAlta = :cusuAlta")
    , @NamedQuery(name = "ClientesCtasBancarias.findByFultimModif", query = "SELECT c FROM ClientesCtasBancarias c WHERE c.fultimModif = :fultimModif")
    , @NamedQuery(name = "ClientesCtasBancarias.findByCusuUltimModif", query = "SELECT c FROM ClientesCtasBancarias c WHERE c.cusuUltimModif = :cusuUltimModif")})
public class ClientesCtasBancarias implements Serializable {

    @EmbeddedId
    protected ClientesCtasBancariasPK clientesCtasBancariasPK;
    @Size(max = 50)
    @Column(name = "xcta_bancaria")
    private String xctaBancaria;
    @Size(max = 50)
    @Column(name = "cusu_alta")
    private String cusuAlta;
    @Size(max = 50)
    @Column(name = "cusu_ultim_modif")
    private String cusuUltimModif;

//    private static final long serialVersionUID = 1L;
//    @Id
//    @Basic(optional = false)
//    @NotNull
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
    @Column(name = "falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta;
    @Column(name = "fultim_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fultimModif;
//    @JoinColumn(name = "cod_banco", referencedColumnName = "cod_banco")
//    @ManyToOne(optional = false)
//    private Bancos codBanco;
//    @JoinColumn(name = "cod_cliente", referencedColumnName = "cod_cliente")
//    @ManyToOne(optional = false)
//    private Clientes codCliente;

    public ClientesCtasBancarias() {
    }

//    public ClientesCtasBancarias(Long id) {
//        this.id = id;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getXctaBancaria() {
        return xctaBancaria;
    }

    public void setXctaBancaria(String xctaBancaria) {
        this.xctaBancaria = xctaBancaria;
    }

    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }

    public String getCusuAlta() {
        return cusuAlta;
    }

    public void setCusuAlta(String cusuAlta) {
        this.cusuAlta = cusuAlta;
    }

    public Date getFultimModif() {
        return fultimModif;
    }

    public void setFultimModif(Date fultimModif) {
        this.fultimModif = fultimModif;
    }

    public String getCusuUltimModif() {
        return cusuUltimModif;
    }

    public void setCusuUltimModif(String cusuUltimModif) {
        this.cusuUltimModif = cusuUltimModif;
    }

//    public Bancos getCodBanco() {
//        return codBanco;
//    }
//
//    public void setCodBanco(Bancos codBanco) {
//        this.codBanco = codBanco;
//    }

//    public Clientes getCodCliente() {
//        return codCliente;
//    }
//
//    public void setCodCliente(Clientes codCliente) {
//        this.codCliente = codCliente;
//    }

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }

//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof ClientesCtasBancarias)) {
//            return false;
//        }
//        ClientesCtasBancarias other = (ClientesCtasBancarias) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }

//    @Override
//    public String toString() {
//        return "entidad.ClientesCtasBancarias[ id=" + id + " ]";
//    }

    public ClientesCtasBancariasPK getClientesCtasBancariasPK() {
        return clientesCtasBancariasPK;
    }

    public void setClientesCtasBancariasPK(ClientesCtasBancariasPK clientesCtasBancariasPK) {
        this.clientesCtasBancariasPK = clientesCtasBancariasPK;
    }

    
    
}
