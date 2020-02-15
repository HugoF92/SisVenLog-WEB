/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Envios;
import dto.CantidadesOrdenCargaDto;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;
import java.text.DateFormat;
import javax.ejb.Stateless;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author WORK
 */
@Stateless
public class ReemplazoOCFacade extends AbstractFacade<Envios> {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReemplazoOCFacade() {
        super(Envios.class);
    }

    public CantidadesOrdenCargaDto buscarCantidadesOC(Long nroInicial, Long nroFinal) throws Exception { 
        Query q = em.createNativeQuery("SELECT count(*) cant_exist, count(distinct fenvio) as cant_fechas \n"
                + "FROM envios\n"
                + "WHERE nro_envio BETWEEN "+ nroInicial.toString() +" and "+ nroFinal.toString() +"\n"
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
    
    public Date buscarFechaOC(Long nroInicial, Long nroFinal) throws Exception { 
        Query q = em.createNativeQuery("SELECT distinct fenvio as fenvio \n"
                + "FROM envios \n"
                + "WHERE nro_envio \n"
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
    
    public Integer buscarCantidadesNuevoOC(Long nuevoNroInicial, Integer cantidadExistente) throws Exception { 
        Query q = em.createNativeQuery("SELECT count(*) cant_exist \n"
                + "FROM envios\n"
                + "WHERE nro_envio BETWEEN "+ nuevoNroInicial.toString() +" and "+ (nuevoNroInicial + cantidadExistente - 1) +"\n"
                + "AND cod_empr = 2 ");

        System.out.println(q.toString());
        
        Object resultado = q.getSingleResult();

        return (Integer) ( (resultado == null) ? 0 :  resultado);
    }
    
    public void procesarEnviosOC(Long nroEnvioOriginalOC, Long nuevoNroOC, Date fechaNuevoOC, Integer cantidadOC) throws Exception {
        for (int i = 0; i < cantidadOC; i++) {
            String query = " UPDATE envios \n"
                    + "SET nro_envio = " + (nuevoNroOC + i) + ",  fenvio = '" + dateToString(fechaNuevoOC) + "' \n"
                    + "WHERE cod_empr = 2 AND nro_envio = " + (nroEnvioOriginalOC + i)+ " \n";

            Query q = em.createNativeQuery(query);

            q.executeUpdate();
        }
    }
    
    public void procesarMovimientosMercaderiasOC(Long nroEnvioOriginalOC, Long nuevoNroOC, Date fechaEnvioOriginalOC, Date fechaNuevoOC, Integer cantidadOC) throws Exception {
        for (int i = 0; i < cantidadOC; i++) {
            if (fechaEnvioOriginalOC != fechaNuevoOC) {
                
                String query = " UPDATE movimientos_merca \n"
                        + "SET ndocum = " + (nuevoNroOC + i) + ",  fmovim = '" + dateToString(fechaNuevoOC) + "' \n"
                        + "WHERE cod_empr = 2 \n"
                        + "AND fmovim = " + (nroEnvioOriginalOC + i) + " \n"
                        + "AND ctipo_docum = 'EN'";

                Query q = em.createNativeQuery(query);

                q.executeUpdate();
                
            } else {
                
                String query = " UPDATE movimientos_merca \n"
                        + "SET ndocum = " + (nuevoNroOC + i) + " \n"
                        + "WHERE cod_empr = 2 \n"
                        + "AND fdocum = '" + dateToString(fechaEnvioOriginalOC) + "' \n"
                        + "AND ndocum = " + (nroEnvioOriginalOC + i) + " \n"
                        + "AND ctipo_docum = 'EN' ";

                Query q = em.createNativeQuery(query);

                q.executeUpdate();
            }
        }
    }
    
    private String dateToString(Date fecha) {

        String resultado = "";

        try {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            resultado = dateFormat.format(fecha);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al convertir fecha"));
        }

        return resultado;
    }
}