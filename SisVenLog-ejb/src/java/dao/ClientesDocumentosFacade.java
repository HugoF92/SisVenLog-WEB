/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidad.Clientes;
import entidad.ClientesDocumentos;
import entidad.ClientesDocumentosPK;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author lmore
 */
@Stateless
public class ClientesDocumentosFacade extends AbstractFacade<ClientesDocumentos> {

    private static final Logger LOGGER = Logger.getLogger(ClientesDocumentosFacade.class.getName());

    @PersistenceContext(unitName = "SisVenLog-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientesDocumentosFacade() {
        super(ClientesDocumentos.class);
    }

    public List<ClientesDocumentos> buscarDocumentosCodCliente(Clientes cliente) {

        TypedQuery<ClientesDocumentos> consultaCD = em.createNamedQuery("ClientesDocumentos.findByCodCliente", ClientesDocumentos.class);
        consultaCD.setParameter("codCliente", cliente.getCodCliente());
//        List<ClientesDocumentos> lista = consultaCD.getResultList();
        List<ClientesDocumentos> resultado = consultaCD.getResultList();

        return resultado;

    }

    public List<TablaClientesDocumentos> buscarDocumentosRequeridos(String codCliente) {

        String sql = "SELECT  a.cod_cliente, a.fvencimiento, a.cdocum, b.xdesc, a.xobs, a.fpresentacion, a.cusuario_presentacion, \n"
                + "c.mobligatorio,  b.con_fec_vto, a.nsecuencia \n"
                + "FROM  clientes_documentos a, documentos_requeridos b, documentos_req_condiciones c, clientes d \n"
                + "  WHERE  a.cod_cliente = " + codCliente + "\n"
                + "    AND  d.cod_cliente = " + codCliente + "\n"
                + "    AND  d.mtipo_persona = c.mtipo_persona\n"
                + "    AND a.cdocum = c.cdocum\n"
                + "    AND  a.cdocum = b.cdocum";

        LOGGER.log(Level.INFO, "clientesDocumentosFacade: " + sql);

        Query q = getEntityManager().createNativeQuery(sql);

        List<Object[]> resultados = q.getResultList();
        List<TablaClientesDocumentos> resultado = new ArrayList<TablaClientesDocumentos>();
        DateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        for (Object[] elemento : resultados) {
            try {
                TablaClientesDocumentos tcd = new TablaClientesDocumentos();
                tcd.setCodigoCliente(elemento[0] == null ? ("0") : (elemento[0].toString()));
                tcd.setFvencimiento(elemento[1] == null ? null : format.parse(elemento[1].toString()));
                tcd.setcDocum(elemento[2] == null ? null : elemento[2].toString());
                tcd.setxDesc(elemento[3] == null ? null : elemento[3].toString());
                tcd.setxObs(elemento[4] == null ? null : elemento[4].toString());
                tcd.setfPresentacion(elemento[5] == null ? null : format.parse(elemento[5].toString()));
                tcd.setcUsuario_presentacion(elemento[6] == null ? null : elemento[6].toString());
                tcd.setmObligatorio(elemento[7] == null ? null : elemento[7].toString());
                tcd.setConFecVto(elemento[8] == null ? null : elemento[8].toString());
                tcd.setSecuencia(elemento[9] == null ? null : elemento[9].toString());
                tcd.setMarcado(tcd.getfPresentacion() == null ? false : true);

                resultado.add(tcd);
            } catch (ParseException ex) {
                Logger.getLogger(ClientesDocumentosFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return resultado;
    }

    public void updateDocumentosRequeridos(List<ClientesDocumentos> tablaClientesDocumentos) throws Exception {

        //em.getTransaction().begin();
        ClientesDocumentos clienteDocumento;

        for (ClientesDocumentos elemento : tablaClientesDocumentos) {

            Logger.getLogger(ClientesDocumentosFacade.class.getName()).log(Level.INFO, "secuencia: " + elemento.getCdocum(), "");
            Logger.getLogger(ClientesDocumentosFacade.class.getName()).log(Level.INFO, "nombredocumento: " + elemento.getNombreDocumento(), "");
            Logger.getLogger(ClientesDocumentosFacade.class.getName()).log(Level.INFO, "observacion: " + elemento.getXobs(), "");
            Logger.getLogger(ClientesDocumentosFacade.class.getName()).log(Level.INFO, "fvencimiento: " + elemento.getFvencimiento(), "");
            Logger.getLogger(ClientesDocumentosFacade.class.getName()).log(Level.INFO, "obligatorio: " + elemento.getObligatorio(), "");
            Logger.getLogger(ClientesDocumentosFacade.class.getName()).log(Level.INFO, "presentado: " + elemento.getPresentado(), "");
            Logger.getLogger(ClientesDocumentosFacade.class.getName()).log(Level.INFO, "fpresentacion: " + elemento.getfPresentacion(), "");
            Logger.getLogger(ClientesDocumentosFacade.class.getName()).log(Level.INFO, "upresentacion: " + elemento.getCusuarioPresentacion(), "");


            /*            clienteDocumento = new ClientesDocumentos();
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
            clienteDocumento.setCusuarioModifi(elemento.getcUsuario_presentacion()); */
            if (!elemento.getPresentado() && elemento.getObligatorio()) {

                throw new Exception("Documento obligatorio \n" + elemento.getNombreDocumento() + "\n" +  "no fue presentado, verifique");

            }
//            if (elemento.getPresentado()) {
            elemento.setCodEmpr(Short.valueOf("2"));
            em.merge(elemento);
//            }

        }

        //em.getTransaction().commit();
    }

}
