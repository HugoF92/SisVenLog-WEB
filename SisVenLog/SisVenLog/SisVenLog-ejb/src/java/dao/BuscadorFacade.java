package dao;

import dto.AuxiliarImpresionMasivaDto;
import dto.AuxiliarImpresionRemisionDto;
import dto.AuxiliarImpresionServiciosDto;
import dto.buscadorDto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.*;

@Stateless
public class BuscadorFacade {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public BuscadorFacade() {
    }

    public List<buscadorDto> buscar(String sql) {
        List<buscadorDto> respuesta = new ArrayList<buscadorDto>();

        try {

            Query q = getEntityManager().createNativeQuery(sql);

            System.out.println(q.toString());

            List<Object[]> resultList = q.getResultList();

            if (resultList.size() <= 0) {
                respuesta = new ArrayList<buscadorDto>();
                return respuesta;
            } else {
                for (Object[] obj : resultList) {

                    buscadorDto aux = new buscadorDto();

                    aux.setDato1(obj[0].toString());
                    aux.setDato2(obj[2].toString());
                    aux.setDato3(obj[1].toString());

                    respuesta.add(aux);

                }
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al listar secuencias."));
        }

        return respuesta;

    }

}
