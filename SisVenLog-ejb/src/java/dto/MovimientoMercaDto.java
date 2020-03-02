/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author dadob
 */
public class MovimientoMercaDto {
    
    private String codMerca;
    private Long cantCajas;
    private Long cantUnid;

    public String getCodMerca() {
        return codMerca;
    }

    public void setCodMerca(String codMerca) {
        this.codMerca = codMerca;
    }

    public Long getCantCajas() {
        return cantCajas;
    }

    public void setCantCajas(Long cantCajas) {
        this.cantCajas = cantCajas;
    }

    public Long getCantUnid() {
        return cantUnid;
    }

    public void setCantUnid(Long cantUnid) {
        this.cantUnid = cantUnid;
    }
    
    
}
