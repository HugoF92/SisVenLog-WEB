package bean.buscadores;

import bean.CanalesBean;
import dao.BuscadorFacade;
import dto.buscadorDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class BuscadorBean {

    @EJB
    private BuscadorFacade buscadorFacade;

    private List<buscadorDto> listaResultado;
    private List<String> columnas;
    private List<String> filtros;
    private buscadorDto Resultado;

    private String filtro;
    private String titulo;
    private String sql;
    private String sqlOrderGroup;
    private String ventana;
    
    /*@ManagedProperty("#{canalesBean}")*/
    private CanalesBean canalesBean = new CanalesBean();

    public BuscadorBean() throws IOException {

    }

    public void instanciar() {
    listaResultado = new ArrayList<buscadorDto>();
    columnas = new ArrayList<String>();
    filtros = new ArrayList<String>();
    Resultado = new buscadorDto();
    
    filtro = "";
    titulo = "";
    sql = "";
    sqlOrderGroup = "";
    ventana = "";
    }
    
    
    public void buscar() {
        try {
            
            StringBuilder sqlFinal = new StringBuilder();
            
            sqlFinal.append(sql + "\n");
            sqlFinal.append("and ( \n");
            
            for (int i = 0; i < filtros.size(); i++) {
                if (i == 0) {
                    sqlFinal.append(" upper(" + filtros.get(i) + ") like upper('%"+filtro+"%') \n");
                }else{
                    sqlFinal.append(" or  upper(" + filtros.get(i) + ") like upper('%"+filtro+"%') \n");
                }
                
            }
            
            sqlFinal.append(" ) \n");
            
            

            /*String sql = "(UPPER(xnombre) like UPPER('%"+filtro+"%') or xruc like '%"+filtro+"%')";*/
            
            this.listaResultado = buscadorFacade.buscar(sqlFinal.toString());
            
            if (this.listaResultado.size() <= 0) {
                FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Atencion", "Datos no encontrados."));
            }else{
                return;
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Atencion", "Error durante la busqueda de datos."));
        }
    }
    
    public void onRowSelect(SelectEvent event) {

        
        if (this.Resultado != null) {

            if (this.Resultado.getDato1() != null) {
                
                if (this.ventana.equals("maCanales")) {
                    canalesBean.setResultadoProveedor(Resultado);
                    RequestContext.getCurrentInstance().update("agreCanalPnlProv");
                }
                RequestContext.getCurrentInstance().execute("PF('dlgBuscador').hide();");
            }

        }

    }

    //Getters & Setters
    public List<buscadorDto> getListaResultado() {
        return listaResultado;
    }

    public void setListaResultado(List<buscadorDto> listaResultado) {
        this.listaResultado = listaResultado;
    }

    public buscadorDto getResultado() {
        return Resultado;
    }

    public void setResultado(buscadorDto Resultado) {
        this.Resultado = Resultado;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getColumnas() {
        return columnas;
    }

    public void setColumnas(List<String> columnas) {
        this.columnas = columnas;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<String> getFiltros() {
        return filtros;
    }

    public void setFiltros(List<String> filtros) {
        this.filtros = filtros;
    }

    public String getSqlOrderGroup() {
        return sqlOrderGroup;
    }

    public void setSqlOrderGroup(String sqlOrderGroup) {
        this.sqlOrderGroup = sqlOrderGroup;
    }
    
    public String getVentana() {
        return ventana;
    }

    public void setVentana(String ventana) {
        this.ventana = ventana;
    }

    /*public CanalesBean getCanalesBean() {
        return canalesBean;
    }

    public void setCanalesBean(CanalesBean canalesBean) {
        this.canalesBean = canalesBean;
    }*/

}
