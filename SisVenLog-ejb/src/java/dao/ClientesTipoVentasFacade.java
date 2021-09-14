/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Clientes;
import entidad.ClientesDocumentos;
import entidad.ClientesDocumentosPK;
import entidad.TiposVentas;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author lmore
 */
@Stateless
public class ClientesTipoVentasFacade extends AbstractFacade<TiposVentas> {

    private static final Logger LOGGER = Logger.getLogger(ClientesTipoVentasFacade.class.getName());

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientesTipoVentasFacade() {
        super(TiposVentas.class);
    }

    public void mergeClientesTipoVenta(String [] tipoVentaSeleccionado, Clientes clienteGuardar, ArrayList<TiposVentas> tiposVentas) {

        
        //se borra por si se eliminan elementos
        clienteGuardar.setTiposVentasCollection(new ArrayList<TiposVentas>());
        for (TiposVentas elemento : tiposVentas) {

            for (int i=0; i< tipoVentaSeleccionado.length; i++){
            
                    if (tipoVentaSeleccionado[i].compareTo(String.valueOf(elemento.getTiposVentasPK().getCtipoVta()))==0) // son iguales, guardar 
                    {
                        clienteGuardar.getTiposVentasCollection().add(elemento);
                        break;
                    }
            
            }
            
        }

    }

    public void updateDocumentosRequeridos(List<TablaClientesDocumentos> tablaClientesDocumentos) {

        //em.getTransaction().begin();
        ClientesDocumentos clienteDocumento;

        for (TablaClientesDocumentos elemento : tablaClientesDocumentos) {

            clienteDocumento = new ClientesDocumentos();
            ClientesDocumentosPK clave = new ClientesDocumentosPK();

            clave.setCodCliente(Long.valueOf(clave.getCodCliente()));
            clave.setNsecuencia(Short.valueOf(elemento.getSecuencia()));
            clienteDocumento.setClientesDocumentosPK(clave);
            clienteDocumento.setCodEmpr(Short.valueOf("2")); //en durango

            clienteDocumento.setCdocum(elemento.getcDocum());

            clienteDocumento.setXobs(elemento.getxObs());
            clienteDocumento.setFvencimiento(elemento.getFvencimiento());
            clienteDocumento.setfPresentacion(elemento.getfPresentacion());
            clienteDocumento.setCusuarioPresentacion(elemento.getcUsuario_presentacion());
            clienteDocumento.setCusuarioModifi(elemento.getcUsuario_presentacion());

            em.merge(clienteDocumento);

        }

        //em.getTransaction().commit();
    }

}
