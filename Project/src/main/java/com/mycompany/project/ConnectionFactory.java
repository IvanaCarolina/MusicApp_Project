/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.project;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author gabri
 */
public class ConnectionFactory {
    private String usuario = "root";
    private String senha = "Sql1234*";
    private String host = "localhost";
    private String porta = "3306";
    private String db = "recomendacaodemusicas?useTimezone=true&serverTimezone=UTC";
    
    public Connection obterConexao (){
        
        try{
            Connection c = DriverManager.getConnection(
                //jdbc:mysql://localhost:3306/usjt_db_pessoas    
                "jdbc:mysql://" + host + ":" + porta + "/" + db,
                usuario,
                senha
            );
            return c;
                                    
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }      
    }
    }