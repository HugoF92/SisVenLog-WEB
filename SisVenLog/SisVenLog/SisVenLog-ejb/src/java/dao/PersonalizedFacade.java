package dao;

import dto.AuxiliarImpresionMasivaDto;
import dto.AuxiliarImpresionRemisionDto;
import dto.AuxiliarImpresionServiciosDto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.*;

@Stateless
public class PersonalizedFacade {

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public PersonalizedFacade() {
    }

    public List<AuxiliarImpresionMasivaDto> listarSecuenciasFacturas(Integer desde, Integer hasta) {
        List<AuxiliarImpresionMasivaDto> respuesta = new ArrayList<AuxiliarImpresionMasivaDto>();

        try {

            Query q = getEntityManager().createNativeQuery("SELECT distinct(f.nrofact), f.xfactura, f.mestado\n"
                    + "FROM facturas f, facturas_det  d,  empleados e, mercaderias m , zonas z, depositos p left outer join conductores c\n"
                    + "     ON p.cod_conductor = c.cod_conductor LEFT OUTER JOIN  transportistas t\n"
                    + "     ON p.cod_transp = t.cod_transp\n"
                    + "	 WHERE  f.cod_empr = 2\n"
                    + "	  AND d.cod_empr = 2\n"
                    + "	  AND f.ffactur = d.ffactur\n"
                    + "	  AND f.cod_empr = d.cod_empr\n"
                    + "	  AND f.nrofact = d.nrofact\n"
                    + "	  AND d.cod_merca = m.cod_merca\n"
                    + "	  AND m.cod_empr = d.cod_empr\n"
                    + "	  AND f.cod_depo = p.cod_depo\n"
                    + "	  AND f.cod_zona = z.cod_zona\n"
                    + "	  AND f.cod_empr = p.cod_empr\n"
                    + "	  AND f.cod_vendedor = e.cod_empleado\n"
                    + "	  AND f.ctipo_docum = d.ctipo_docum\n"
                    + "	  AND f.cod_empr = e.cod_empr\n"
                    + "	  and f.ctipo_docum in ('FCO','FCR')\n"
                    + "	  AND f.nrofact between " + desde + " and " + hasta + "\n"
                    //+ "	  AND f.mestado = 'A'\n"
                    + "	  ORDER BY f.nrofact");

            System.out.println(q.toString());

            List<Object[]> resultList = q.getResultList();

            if (resultList.size() <= 0) {
                respuesta = new ArrayList<AuxiliarImpresionMasivaDto>();
                return respuesta;
            } else {
                for (Object[] obj : resultList) {

                    AuxiliarImpresionMasivaDto aux = new AuxiliarImpresionMasivaDto();

                    aux.setSecuencia(new Integer(obj[0].toString()));
                    aux.setFactura(obj[1].toString());
                    aux.setEstado(obj[2].toString());

                    respuesta.add(aux);

                }
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al listar secuencias."));
        }

        return respuesta;

    }

    public List<AuxiliarImpresionServiciosDto> listarSecuenciasFacturasServicios(Integer desde, Integer hasta) {
        List<AuxiliarImpresionServiciosDto> respuesta = new ArrayList<AuxiliarImpresionServiciosDto>();

        try {

            Query q = getEntityManager().createNativeQuery("SELECT distinct(f.nrofact), f.xfactura\n"
                    + "FROM facturas f, facturas_ser  d,  servicios s\n"
                    + " WHERE  f.cod_empr = 2\n"
                    + "  AND d.cod_empr = 2\n"
                    + "  AND d.ctipo_docum = f.ctipo_docum\n"
                    + " AND f.ffactur = d.ffactur\n"
                    + " AND f.cod_empr = d.cod_empr\n"
                    + " AND f.nrofact = d.nrofact\n"
                    + " AND d.cod_servicio = s.cod_servicio\n"
                    + " AND f.ctipo_docum IN ('FCS','FCP')\n"
                    + " AND f.nrofact between " + desde + " and " + hasta + "\n"
                    + " AND f.mestado = 'A'\n"
                    + " order by f.nrofact asc");

            System.out.println(q.toString());

            List<Object[]> resultList = q.getResultList();

            if (resultList.size() <= 0) {
                respuesta = new ArrayList<AuxiliarImpresionServiciosDto>();
                return respuesta;
            } else {
                for (Object[] obj : resultList) {

                    AuxiliarImpresionServiciosDto aux = new AuxiliarImpresionServiciosDto();

                    aux.setSecuencia(new Integer(obj[0].toString()));
                    aux.setFactura(obj[1].toString());

                    respuesta.add(aux);

                }
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al listar secuencias."));
        }

        return respuesta;

    }

    public List<AuxiliarImpresionRemisionDto> listarSecuenciasRemision(Integer desde, Integer hasta) {
        List<AuxiliarImpresionRemisionDto> respuesta = new ArrayList<AuxiliarImpresionRemisionDto>();

        try {

            Query q = getEntityManager().createNativeQuery("SELECT distinct(e.nro_remision), e.xnro_remision \n"
                    + "FROM         remisiones e INNER JOIN\n"
                    + "     remisiones_det d ON e.nro_remision = d.nro_remision AND e.cod_empr = d.cod_empr LEFT OUTER JOIN\n"
                    + "    vw_fact_remisiones v2 ON e.nro_remision = v2.nro_remision AND v2.ctipo_docum = 'FCR' AND e.cod_empr = v2.cod_empr LEFT OUTER JOIN\n"
                    + "     vw_fact_remisiones v ON e.nro_remision = v.nro_remision AND v.ctipo_docum in ('CPV','FCO') AND e.cod_empr = v.cod_empr INNER JOIN\n"
                    + "     depositos d3 ON e.cod_depo = d3.cod_depo AND e.cod_empr = d3.cod_empr INNER JOIN\n"
                    + "     empleados p ON e.cod_entregador = p.cod_empleado AND e.cod_empr = p.cod_empr INNER JOIN\n"
                    + "    conductores c ON e.cod_conductor = c.cod_conductor INNER JOIN\n"
                    + "     transportistas t ON e.cod_transp = t.cod_transp  INNER JOIN\n"
                    + "    mercaderias m ON d.cod_merca = m.cod_merca INNER JOIN\n"
                    + "    empresas e2 ON e2.cod_empr = e.cod_empr\n"
                    + "    WHERE e.cod_empr = 2\n"
                    + "    AND e.cod_empr = 2\n"
                    + "  AND e.nro_remision >= "+desde+"\n"
                    + " AND e.nro_remision <= "+hasta+"\n"
                    + " and e.mestado ='A'\n"
                    + " ORDER BY e.nro_remision");

            System.out.println(q.toString());

            List<Object[]> resultList = q.getResultList();

            if (resultList.size() <= 0) {
                respuesta = new ArrayList<AuxiliarImpresionRemisionDto>();
                return respuesta;
            } else {
                for (Object[] obj : resultList) {

                    AuxiliarImpresionRemisionDto aux = new AuxiliarImpresionRemisionDto();

                    aux.setSecuencia(new Integer(obj[0].toString()));
                    aux.setFactura(obj[1].toString());

                    respuesta.add(aux);

                }
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Error al listar secuencias."));
        }

        return respuesta;
    }
    
    public void ejecutarSentenciaSQL(String sql){
     
        System.out.println(sql);
        Query q = getEntityManager().createNativeQuery(sql);
        
        q.executeUpdate();
    }

}
