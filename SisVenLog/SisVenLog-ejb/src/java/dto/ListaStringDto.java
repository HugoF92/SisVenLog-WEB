/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.List;

/**
 *
 * @author jvera
 */
public class ListaStringDto {
    private List<Object[]> lista;
    private String sql;

    public ListaStringDto() {
    }

    public ListaStringDto(List<Object[]> lista, String sql) {
        this.lista = lista;
        this.sql = sql;
    }

    public List<Object[]> getLista() {
        return lista;
    }

    public String getSql() {
        return sql;
    }

    public void setLista(List<Object[]> lista) {
        this.lista = lista;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
