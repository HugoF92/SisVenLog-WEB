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
public class PrecioDto {
    
    private short nRelacion;
    private short nRelaPack;
    private long iprecioCaja;
    private long iprecioUnidad;

    public short getnRelacion() {
        return nRelacion;
    }

    public void setnRelacion(short nRelacion) {
        this.nRelacion = nRelacion;
    }

    public short getnRelaPack() {
        return nRelaPack;
    }

    public void setnRelaPack(short nRelaPack) {
        this.nRelaPack = nRelaPack;
    }

    public long getIprecioCaja() {
        return iprecioCaja;
    }

    public void setIprecioCaja(long iprecioCaja) {
        this.iprecioCaja = iprecioCaja;
    }

    public long getIprecioUnidad() {
        return iprecioUnidad;
    }

    public void setIprecioUnidad(long iprecioUnidad) {
        this.iprecioUnidad = iprecioUnidad;
    }
    
}
