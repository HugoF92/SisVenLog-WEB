package util;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class conectar_BD {

    public Connection conVenlog;
    public Connection conIntercambio;
    public Connection conDatamex;
    public Statement sentencia;
    public ResultSet rs;
    public PreparedStatement ps;

    public void conectarBD() {
    }
    
    public boolean conectar(String user, String password) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //this.con = DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS;instance=SQLEXPRESS;databaseName=VenlogDB;user=sa;password=root;");
            this.conVenlog = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=VenlogDB",user,password);
            //this.conVenlog = DriverManager.getConnection("jdbc:sqlserver://192.168.0.9;databaseName=VenlogDB",user,password);
            return true;
            //System.out.println("Conectado con exito a AdminIntercambioAJ!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
