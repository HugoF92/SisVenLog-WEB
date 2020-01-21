package dao;

import dto.AuxiliarImpresionMasivaDto;
import dto.AuxiliarImpresionRemisionDto;
import dto.AuxiliarImpresionServiciosDto;
import dto.LiMercaSinDto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.*;

@Stateless
public class ExcelFacade {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ExcelFacade() {
    }

    public List<LiMercaSinDto> listarLiMercaSin(String sql) {
        List<LiMercaSinDto> respuesta = new ArrayList<LiMercaSinDto>();

        try {
            
            System.out.println(sql);

            Query q = getEntityManager().createNativeQuery(sql);


            List<Object[]> resultList = q.getResultList();

            if (resultList.size() <= 0) {
                respuesta = new ArrayList<LiMercaSinDto>();
                return respuesta;
            } else {
                for (Object[] obj : resultList) {

                    LiMercaSinDto aux = new LiMercaSinDto();

                    aux.setCodMerca(obj[0].toString());
                    aux.setCodBarra(obj[1].toString());
                    aux.setXdesc(obj[2].toString());
                    aux.setCajas(obj[4].toString());
                    aux.setUnidades(obj[5].toString());

                    respuesta.add(aux);

                }
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al listar secuencias."));
        }

        return respuesta;

    }
    
    public List<Object[]> listarParaExcel(String sql) {
        List<Object[]> respuesta = new ArrayList<Object[]>();

        try {
            
            System.out.println(sql);

            Query q = getEntityManager().createNativeQuery(sql);
            
            respuesta = q.getResultList();

        } catch (Exception e) {
            System.out.println("-ListaExcel: "+e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al listar secuencias."));
        }

        return respuesta;

    }
    
    public Boolean ejecutarQuery(String sql){
        try{
            System.out.println(sql);
//            Query q = getEntityManager().createNamedQuery(sql);
            Query q = getEntityManager().createNativeQuery(sql);
            q.executeUpdate();
            return true;
        }catch(Exception e){
            System.out.println("-Query: "+e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error de sql."));
            return false;
        }
    }
}
