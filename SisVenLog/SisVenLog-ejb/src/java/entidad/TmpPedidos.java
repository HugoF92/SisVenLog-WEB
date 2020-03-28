package entidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Nico
 */
//@Entity
@Table(name = "tmp_pedidos")
public class TmpPedidos implements Serializable {

    @Column(name = "facnro", length = 9)
    private String facnro;
    @Column(name = "cod_vendedor")
    private Short codVendedor;
    @Column(name = "vencod" ,length = 24)
    private String vencod;
    @Column(name = "clicod" , length = 10)
    private String clicod;
    @Column(name = "facfecha" , length = 10)
    private String facfecha;
    @Column(name = "factipovta")
    private String factipovta;
    @Column(name = "forpago" , length = 4)
    private String forpago;
    @Column(name = "artcod" , length = 8)
    private String artcod;
    @Column(name = "familia" , length = 20)
    private String familia;
    @Column(name = "detunixcaja")
    private BigDecimal detunixcaja;
    @Column(name = "detcancajas")
    private BigDecimal detcancajas;
    @Column(name = "detcanunid")
    private BigDecimal detcanunid;
    @Column(name = "detcajbonif")
    private BigDecimal detcajbonif;
    @Column(name = "detunibonif")
    private BigDecimal detunibonif;
    @Column(name = "detprecio")
    private BigDecimal detprecio;
    @Column(name = "detdescto")
    private BigDecimal detdescto;
    @Column(name = "detdesctot")
    private BigDecimal detdesctot;
    @Column(name = "detmontoneto")
    private BigDecimal detmontoneto;
    @Column(name = "detmontoiva")
    private BigDecimal detmontoiva;
    @Column(name = "detmontotot")
    private BigDecimal detmontotot;
    @Column(name = "detfecha" , length = 10)
    private String detfecha;
    @Column(name = "dethora" , length = 8)
    private String dethora;
    @Column(name = "facdescapl")
    private BigDecimal facdescapl;
    @Column(name = "facdesctot")
    private BigDecimal facdesctot;
    @Column(name = "facneto")
    private BigDecimal facneto;
    @Column(name = "faciva")
    private BigDecimal faciva;
    @Column(name = "factotal")
    private BigDecimal factotal;
    @Column(name = "factipfac" , length = 8)
    private String factipfac;
    @Column(name = "fechagraba" , length = 10)
    private String fechagraba;    
    @Column(name = "estado" )
    private String estado;   
    @Column(name = "msg_error" , length = 80)
    private String msgError; 
    @Column(name = "fec_proc" )
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecProc; 
    @Column(name = "codnuevo")
    private Integer codnuevo;     
    @Column(name = "nroped")
    private Integer nroped;     
    @Column(name = "cod_canal" , length = 2)
    private String codCanal;     
    @Column(name = "nro_promo" )
    private BigDecimal nroPromo;     
    @Column(name = "nplazo_cheque" )
    private BigDecimal nplazoCheque;     
    @Column(name = "falta" )
    @Temporal(TemporalType.TIMESTAMP)
    private Date falta; 
    
    public TmpPedidos() {
    }

    public String getFacnro() {
        return facnro;
    }

    public void setFacnro(String facnro) {
        this.facnro = facnro;
    }

    public Short getCodVendedor() {
        return codVendedor;
    }

    public void setCodVendedor(Short codVendedor) {
        this.codVendedor = codVendedor;
    }

    public String getVencod() {
        return vencod;
    }

    public void setVencod(String vencod) {
        this.vencod = vencod;
    }

    public String getClicod() {
        return clicod;
    }

    public void setClicod(String clicod) {
        this.clicod = clicod;
    }

    public String getFacfecha() {
        return facfecha;
    }

    public void setFacfecha(String facfecha) {
        this.facfecha = facfecha;
    }

    public String getFactipovta() {
        return factipovta;
    }

    public void setFactipovta(String factipovta) {
        this.factipovta = factipovta;
    }

    public String getForpago() {
        return forpago;
    }

    public void setForpago(String forpago) {
        this.forpago = forpago;
    }

    public String getArtcod() {
        return artcod;
    }

    public void setArtcod(String artcod) {
        this.artcod = artcod;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public BigDecimal getDetunixcaja() {
        return detunixcaja;
    }

    public void setDetunixcaja(BigDecimal detunixcaja) {
        this.detunixcaja = detunixcaja;
    }

    public BigDecimal getDetcancajas() {
        return detcancajas;
    }

    public void setDetcancajas(BigDecimal detcancajas) {
        this.detcancajas = detcancajas;
    }

    public BigDecimal getDetcanunid() {
        return detcanunid;
    }

    public void setDetcanunid(BigDecimal detcanunid) {
        this.detcanunid = detcanunid;
    }

    public BigDecimal getDetcajbonif() {
        return detcajbonif;
    }

    public void setDetcajbonif(BigDecimal detcajbonif) {
        this.detcajbonif = detcajbonif;
    }

    public BigDecimal getDetunibonif() {
        return detunibonif;
    }

    public void setDetunibonif(BigDecimal detunibonif) {
        this.detunibonif = detunibonif;
    }

    public BigDecimal getDetprecio() {
        return detprecio;
    }

    public void setDetprecio(BigDecimal detprecio) {
        this.detprecio = detprecio;
    }

    public BigDecimal getDetdescto() {
        return detdescto;
    }

    public void setDetdescto(BigDecimal detdescto) {
        this.detdescto = detdescto;
    }

    public BigDecimal getDetdesctot() {
        return detdesctot;
    }

    public void setDetdesctot(BigDecimal detdesctot) {
        this.detdesctot = detdesctot;
    }

    public BigDecimal getDetmontoneto() {
        return detmontoneto;
    }

    public void setDetmontoneto(BigDecimal detmontoneto) {
        this.detmontoneto = detmontoneto;
    }

    public BigDecimal getDetmontoiva() {
        return detmontoiva;
    }

    public void setDetmontoiva(BigDecimal detmontoiva) {
        this.detmontoiva = detmontoiva;
    }

    public BigDecimal getDetmontotot() {
        return detmontotot;
    }

    public void setDetmontotot(BigDecimal detmontotot) {
        this.detmontotot = detmontotot;
    }

    public String getDetfecha() {
        return detfecha;
    }

    public void setDetfecha(String detfecha) {
        this.detfecha = detfecha;
    }

    public String getDethora() {
        return dethora;
    }

    public void setDethora(String dethora) {
        this.dethora = dethora;
    }

    public BigDecimal getFacdescapl() {
        return facdescapl;
    }

    public void setFacdescapl(BigDecimal facdescapl) {
        this.facdescapl = facdescapl;
    }

    public BigDecimal getFacdesctot() {
        return facdesctot;
    }

    public void setFacdesctot(BigDecimal facdesctot) {
        this.facdesctot = facdesctot;
    }

    public BigDecimal getFacneto() {
        return facneto;
    }

    public void setFacneto(BigDecimal facneto) {
        this.facneto = facneto;
    }

    public BigDecimal getFaciva() {
        return faciva;
    }

    public void setFaciva(BigDecimal faciva) {
        this.faciva = faciva;
    }

    public BigDecimal getFactotal() {
        return factotal;
    }

    public void setFactotal(BigDecimal factotal) {
        this.factotal = factotal;
    }

    public String getFactipfac() {
        return factipfac;
    }

    public void setFactipfac(String factipfac) {
        this.factipfac = factipfac;
    }

    public String getFechagraba() {
        return fechagraba;
    }

    public void setFechagraba(String fechagraba) {
        this.fechagraba = fechagraba;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getCodnuevo() {
        return codnuevo;
    }

    public void setCodnuevo(Integer codnuevo) {
        this.codnuevo = codnuevo;
    }

    public Integer getNroped() {
        return nroped;
    }

    public void setNroped(Integer nroped) {
        this.nroped = nroped;
    }

    public Date getFalta() {
        return falta;
    }

    public void setFalta(Date falta) {
        this.falta = falta;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public Date getFecProc() {
        return fecProc;
    }

    public void setFecProc(Date fecProc) {
        this.fecProc = fecProc;
    }

    public String getCodCanal() {
        return codCanal;
    }

    public void setCodCanal(String codCanal) {
        this.codCanal = codCanal;
    }

    public BigDecimal getNroPromo() {
        return nroPromo;
    }

    public void setNroPromo(BigDecimal nroPromo) {
        this.nroPromo = nroPromo;
    }

    public BigDecimal getNplazoCheque() {
        return nplazoCheque;
    }

    public void setNplazoCheque(BigDecimal nplazoCheque) {
        this.nplazoCheque = nplazoCheque;
    }

}
