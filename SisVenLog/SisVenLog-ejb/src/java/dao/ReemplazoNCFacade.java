/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.CantidadesOrdenCargaDto;
import entidad.NotasVentas;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import static util.DateUtil.dateToString;

/**
 *
 * @author Asus
 */
@Stateless
public class ReemplazoNCFacade extends AbstractFacade<NotasVentas>{
    
    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public ReemplazoNCFacade() {
        super(NotasVentas.class);
    }

    public CantidadesOrdenCargaDto buscarCantidadesNC(Long nroInicial, Long nroFinal) throws Exception {
        Query q = em.createNativeQuery("SELECT count(*) cant_exist, count(distinct fdocum) as cant_fechas \n"
                + "FROM notas_ventas\n"
                + "WHERE nro_nota BETWEEN "+ nroInicial.toString() +" and "+ nroFinal.toString() +"\n"
                + "AND cod_empr = 2 ");

        System.out.println(q.toString());

        List<Object[]> resultados = q.getResultList();
        
        CantidadesOrdenCargaDto cantidadesOrdenCargaDto = new CantidadesOrdenCargaDto(null, null);
        
        for(Object[] resultado: resultados){
            cantidadesOrdenCargaDto.setCantExistenteOC(Integer.valueOf(resultado[0].toString()));
            cantidadesOrdenCargaDto.setCantFechasDistintasOC(Integer.valueOf(resultado[1].toString()));
        }
        
        return cantidadesOrdenCargaDto;
    }

    public Date buscarFechaNC(Long nroInicial, Long nroFinal) throws Exception { 
        Query q = em.createNativeQuery("SELECT distinct fdocum as fdocum \n"
                + "FROM notas_ventas \n"
                + "WHERE nro_nota \n"
                + "BETWEEN " + nroInicial.toString() + " and " + nroFinal.toString() + " \n"
                + "AND cod_empr = 2");

        System.out.println(q.toString());

        Object resultado = q.getSingleResult();
        
        Date fenvio = null;
        
        if (resultado != null) {
            Timestamp timeStamp_2 = (Timestamp) resultado;
            java.util.Date dateResult_2 = new Date(timeStamp_2.getTime());
            fenvio = dateResult_2;
        }
        
        return fenvio;
    }
    
    public Integer buscarCantidadesNuevoNC(Long nuevoNroInicial, Integer cantidadExistente) throws Exception { 
        Query q = em.createNativeQuery("SELECT count(*) cant_exist \n"
                + "FROM notas_ventas\n"
                + "WHERE nro_nota BETWEEN "+ nuevoNroInicial.toString() +" and "+ (nuevoNroInicial + cantidadExistente - 1) +"\n"
                + "AND cod_empr = 2 ");

        System.out.println(q.toString());
        
        Object resultado = q.getSingleResult();

        return (Integer) ( (resultado == null) ? 0 :  resultado);
    }
    
    public void procesarReemplazoNC(Long nroOriginalNC, Long nuevoNroNC, Date fechaNuevoNC, Integer cantidadNC) throws Exception {
        for (int i = 0; i < cantidadNC; i++) {
            String query = " UPDATE notas_ventas \n"
                    + "SET nro_nota = " + (nuevoNroNC + i) + ",  fdocum = '" + dateToString(fechaNuevoNC) + "' \n"
                    + "WHERE cod_empr = 2 AND nro_nota = " + (nroOriginalNC + i)+ " \n";

            Query q = em.createNativeQuery(query);

            q.executeUpdate();
        }
    }
    
    public void procesarMovimientosMercaderiasNC(Long nroOriginalNC, Long nuevoNroNC, Date fechaOriginalNC, Date fechaNuevoNC, Integer cantidadNC) throws Exception {
        for (int i = 0; i < cantidadNC; i++) {
            if (fechaOriginalNC != fechaNuevoNC) {
                
                String query = " UPDATE movimientos_merca \n"
                        + "SET ndocum = " + (nuevoNroNC + i) + ",  fmovim = '" + dateToString(fechaNuevoNC) + "' \n"
                        + "WHERE cod_empr = 2 \n"
                        + "AND fmovim = " + (nroOriginalNC + i) + " \n"
                        + "AND ctipo_docum = 'EN'";

                Query q = em.createNativeQuery(query);

                q.executeUpdate();
                
            } else {
                
                String query = " UPDATE movimientos_merca \n"
                        + "SET ndocum = " + (nuevoNroNC + i) + " \n"
                        + "WHERE cod_empr = 2 \n"
                        + "AND fdocum = '" + dateToString(fechaOriginalNC) + "' \n"
                        + "AND ndocum = " + (nroOriginalNC + i) + " \n"
                        + "AND ctipo_docum = 'EN' ";

                Query q = em.createNativeQuery(query);

                q.executeUpdate();
            }
        }
    }
}
