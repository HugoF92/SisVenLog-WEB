/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Asus
 */
public class CantidadesOrdenCargaDto implements Serializable {
    private Integer cantExistenteOC;
    private Integer cantFechasDistintasOC;

    public CantidadesOrdenCargaDto() {
    }

    public CantidadesOrdenCargaDto(Integer cantExistenteOC, Integer cantFechasDistintasOC) {
        this.cantExistenteOC = cantExistenteOC;
        this.cantFechasDistintasOC = cantFechasDistintasOC;
    }

    public Integer getCantExistenteOC() {
        return cantExistenteOC;
    }

    public Integer getCantFechasDistintasOC() {
        return cantFechasDistintasOC;
    }

    public void setCantExistenteOC(Integer cantExistenteOC) {
        this.cantExistenteOC = cantExistenteOC;
    }

    public void setCantFechasDistintasOC(Integer cantFechasDistintasOC) {
        this.cantFechasDistintasOC = cantFechasDistintasOC;
    }
}
