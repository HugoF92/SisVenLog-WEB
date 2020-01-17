package bean;

import dao.MarcasComercialesFacade;
import dao.MercaderiasFacade;
import dao.ProveedoresFacade;
import dao.SublineasFacade;
import entidad.MarcasComerciales;
import entidad.Mercaderias;
import entidad.MercaderiasPK;
import entidad.Proveedores;
import entidad.Sublineas;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.apache.commons.codec.binary.Base64;
import org.primefaces.context.RequestContext;

@Named(value = "mercaderiasBean")
@ViewScoped
public class MercaderiasBean implements Serializable {

    @EJB
    private MercaderiasFacade mercaderiaFacade;
    @Inject
    private ProveedoresFacade proveedorFacade;
    @Inject
    private SublineasFacade sublineasFacade;
    @Inject
    private MarcasComercialesFacade marcacomercialFacade;

    private Mercaderias selected;
    private List<Mercaderias> listaMercaderias;
    private List<Proveedores> listaProveedores;
    private List<Sublineas> listaSublineas;
    private List<MarcasComerciales> listaMarcascomerciasles;

    private boolean editar;
    private String imgBase64;

    @PostConstruct
    public void init() {
        listar();
    }

    public void listar() {

        listaProveedores = proveedorFacade.listarPrveedores();
        listaSublineas = sublineasFacade.listarSublineas();
        listaMarcascomerciasles = marcacomercialFacade.listarMarcascomerciales();

        listaMercaderias = mercaderiaFacade.listarMercaderias();
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public List<Sublineas> getListaSublineas() {
        return listaSublineas;
    }

    public void setListaSublineas(List<Sublineas> listaSublineas) {
        this.listaSublineas = listaSublineas;
    }

    public List<MarcasComerciales> getListaMarcascomerciasles() {
        return listaMarcascomerciasles;
    }

    public void setListaMarcascomerciasles(List<MarcasComerciales> listaMarcascomerciasles) {
        this.listaMarcascomerciasles = listaMarcascomerciasles;
    }

    public List<Proveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public Mercaderias getSelected() {
        return selected;
    }

    public void setSelected(Mercaderias selected) {
        this.selected = selected;
    }

    public List<Mercaderias> getListaMercaderias() {
        return listaMercaderias;
    }

    public void setListaMercaderias(List<Mercaderias> listaMercaderias) {
        this.listaMercaderias = listaMercaderias;
    }

    public void preparedCreate() {
        selected = new Mercaderias();
        selected.setCodProveed(new Proveedores());
        selected.setCodSublinea(new Sublineas());
        selected.setMercaderiasPK(new MercaderiasPK());
        selected.setMestado('A');
        selected.setCmarca(new MarcasComerciales());
        editar = false;
        imgBase64 = "";
    }

    public void preparedEdit() {
        editar = true;
        if (selected.getXfoto() == null || selected.getXfoto() == "") {
            RequestContext.getCurrentInstance().execute("$('#blah').attr('src', 'http://placehold.it/180')");
            RequestContext.getCurrentInstance().execute("document.getElementById('idFoto').value = ''");
            imgBase64 = "";
        } else {
            String foto = "data:image/png;base64," + encodeFileToBase64Binary(selected.getXfoto());
            RequestContext.getCurrentInstance().execute("$('#blah').attr('src', '" + foto + "')");
            RequestContext.getCurrentInstance().execute("document.getElementById('idFoto').value = '" + foto + "'");
            imgBase64 = foto;
        }
    }

    public void guardar() {
        if (!editar) {
            saveNew();
        } else {
            save();
        }

        listar();
        imgBase64 = "";
    }

    public void saveNew() {
        System.out.println("Crear");

        String dir = "C://mercaderias/" + this.selected.getMercaderiasPK().getCodMerca() + ".png";
        if (this.imgBase64.length() == 0) {
            selected.setXfoto("");
        } else {
            selected.setXfoto(dir);
        }

        selected.getMercaderiasPK().setCodEmpr(new Short("2"));
        selected.setFalta(new Date());

        int valor = mercaderiaFacade.insertarMercaderias(selected);

        if (valor == 1) {
            System.out.println("Creado con exito");
            if (this.imgBase64.length() != 0) {
                convertirImagen(dir);
            }

            RequestContext.getCurrentInstance().execute("PF('dlgNuevEditMercaderia').hide();");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Creado con éxito."));
        } else {
            System.out.println("Creado con error");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        }

        selected = null;
        editar = false;
    }

    public void save() {
        System.out.println("Editar");
        selected.setFultimModif(new Date());

        String dir = "C://mercaderias/" + this.selected.getMercaderiasPK().getCodMerca() + ".png";
        if (this.imgBase64.length() == 0) {
            selected.setXfoto("");
        } else {
            selected.setXfoto(dir);
        }

        int valor = mercaderiaFacade.editarMercaderias(selected);
        if (valor == 1) {
            System.out.println("Editado con exito");
            if (this.imgBase64.length() != 0) {
                convertirImagen(dir);
            }

            RequestContext.getCurrentInstance().execute("PF('dlgNuevEditMercaderia').hide();");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Editado con éxito."));
        } else {
            System.out.println("Edutadi con error");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        }
        selected = null;
        editar = false;
    }

    public void borrar() {
        try {
            mercaderiaFacade.remove(selected);
            selected = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Borrado con éxito."));
            listar();
            RequestContext.getCurrentInstance().execute("PF('dlgInacMercaderias').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atencion", "Ocurrió un error."));
        } finally {
            editar = false;
        }
    }

    public void convertirImagen(String dir) {
        FileOutputStream fos = null;
        try {
            File directorio = new File("C://mercaderias");
            if (!directorio.exists()) {
                if (directorio.mkdirs()) {
                    System.out.println("Directorio creado");
                } else {
                    System.out.println("Error al crear directorio");
                }
            }
            byte dearr[] = Base64.decodeBase64(imgBase64.substring("data:image/png;base64,".length()));
            fos = new FileOutputStream(new File(dir));
            fos.write(dearr);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
            }
        }
    }

    private static String encodeFileToBase64Binary(String fileName) {
        String base64File = "";
        File file = new File(fileName);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a file from file system
            byte fileData[] = new byte[(int) file.length()];
            imageInFile.read(fileData);
            base64File = Base64.encodeBase64String(fileData);
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the file " + ioe);
        }
        return base64File;
    }

    public static void main(String[] args) {
        System.out.println("valorr : " + encodeFileToBase64Binary("C://mercaderias//2.png"));
    }
}
