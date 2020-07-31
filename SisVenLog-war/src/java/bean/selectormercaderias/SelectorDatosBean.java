/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.selectormercaderias;

import dao.LiPagaresFacade;
import entidad.TmpDatos;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Clara
 */
@ManagedBean
@SessionScoped
public class SelectorDatosBean {

    @EJB
    private LiPagaresFacade personalizedFacade;

    private List<TmpDatos> listaDatos = new ArrayList<TmpDatos>();

    private List<TmpDatos> listaDatosSeleccionadas = new ArrayList<TmpDatos>();

    List<TmpDatos> datosSource = new ArrayList<TmpDatos>();
    List<TmpDatos> datosSourceAux = new ArrayList<TmpDatos>();
    List<TmpDatos> datosTarget = new ArrayList<TmpDatos>();
    List<TmpDatos> datosTargetAux = new ArrayList<TmpDatos>();

    private DualListModel<TmpDatos> datos;

    public static boolean datosFiltrados = false;

    public static String sql = "";

    //ejemplo: tmp_clientes
    public static String tabla_temporal = "";

    //ejemplo (cod_clientes, xnombre)
    public static String campos_tabla_temporal = "";

    private String filtro = "";

    public SelectorDatosBean() {

    }

    //Operaciones
    //Instanciar objetos
    @PostConstruct
    public void instanciar() {

        this.listaDatos = new ArrayList<TmpDatos>();
        this.listaDatosSeleccionadas = new ArrayList<TmpDatos>();

        //Cities
        datosSource = new ArrayList<TmpDatos>();
        datosTarget = new ArrayList<TmpDatos>();

        //datosSource = personalizedFacade.getDatosSelctor("select * from tmp_datos order by codigo");
        datos = new DualListModel<TmpDatos>(datosSource, datosTarget);
    }

    public void cargarLista() {
        
        personalizedFacade.ejecutarSentenciaSQL("truncate  table " + tabla_temporal);
        
        datosFiltrados = false;

        this.listaDatos = new ArrayList<TmpDatos>();
        this.listaDatosSeleccionadas = new ArrayList<TmpDatos>();

        //Cities
        datosSource = new ArrayList<TmpDatos>();
        datosTarget = this.listaDatosSeleccionadas;

        datosSourceAux = personalizedFacade.getDatosSelctor(sql);
        //datosTarget = personalizedFacade.getDatosSelctor("select codigo, descripcion from tmp_datos order by codigo");

        datos = new DualListModel<TmpDatos>(datosSource, datosTarget);

        if (datosSource.size() >= 1000) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Atención", "Demasiados resultados, favor ingrese un filtro mas exacto!"));
            return;
        }
    }
    
    public void onTransfer(TransferEvent event) {
        datosTargetAux = datos.getTarget();
    } 

    public void filtrarDatos() {
        

        if (datos.getTarget().size() > 0) {
        //if (datosTargetAux.size() > 0) {
            datosTargetAux= datos.getTarget();
        }

        datosSource = new ArrayList<TmpDatos>();

        try {

            if (this.filtro.length() >= 3) {

                if (datosSourceAux.size() > 0) {

                    String auxFiltrar = "";

                    for (int i = 0; i < datosSourceAux.size(); i++) {

                        String dato = datosSourceAux.get(i).getCodigo() + "- " + datosSourceAux.get(i).getDescripcion();

                        int indexOf = dato.indexOf(this.filtro.toUpperCase());

                        if (indexOf >= 0) {
                            datosSource.add(datosSourceAux.get(i));
                        }

                    }

                }

                datos.setSource(datosSource);
                datosTarget = datosTargetAux;
                
                datos = new DualListModel<TmpDatos>(datosSource, datosTarget);

                //RequestContext.getCurrentInstance().update("pickListdatosPag");
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Atención", "Ingrese como minimo 3 caracteres"));
            }
        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al filtrar datos", e.getMessage()));
        }

    }

    public void ejecutar() {

        try {

            personalizedFacade.ejecutarSentenciaSQL("truncate  table " + tabla_temporal);

            if (datos.getTarget().size() > 0) {
                listaDatosSeleccionadas = datos.getTarget();
                //System.out.println(datos.getTarget().get(0).getDescripcion());
                for (int i = 0; i < listaDatosSeleccionadas.size(); i++) {

                    /*System.out.println("listaDatosSeleccionadas.get(i) : " + i + "----" + 
                            listaDatosSeleccionadas.get(i).getCodigo() + "---" + listaDatosSeleccionadas.get(i).getDescripcion());*/
                    personalizedFacade.ejecutarSentenciaSQL("INSERT INTO " + tabla_temporal + " (" + campos_tabla_temporal + " )\n"
                            + "								VALUES ('" + listaDatosSeleccionadas.get(i).getCodigo()
                            + "', '" + listaDatosSeleccionadas.get(i).getDescripcion() + "' )");
                }
            } else {
                personalizedFacade.ejecutarSentenciaSQL("insert into " + tabla_temporal + "\n"
                        + sql);
            }
            
            this.listaDatosSeleccionadas = new ArrayList<TmpDatos>();

            datosFiltrados = true;

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", e.getMessage()));
        }

    }

    //Getters & Setters
    public List<TmpDatos> getListaDatos() {
        return listaDatos;
    }

    public void setListaDatos(List<TmpDatos> listaDatos) {
        this.listaDatos = listaDatos;
    }

    public List<TmpDatos> getListaDatosSeleccionadas() {
        return listaDatosSeleccionadas;
    }

    public void setListaDatosSeleccionadas(List<TmpDatos> listaDatosSeleccionadas) {
        this.listaDatosSeleccionadas = listaDatosSeleccionadas;
    }

    public DualListModel<TmpDatos> getDatos() {
        return datos;
    }

    public void setDatos(DualListModel<TmpDatos> datos) {
        this.datos = datos;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

}
