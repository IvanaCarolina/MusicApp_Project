/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class Genero {

    private int idGenero;
    private String nomeDoGenero;

    public Genero() {
        this.idGenero = idGenero;
        this.nomeDoGenero = nomeDoGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public void cadatroDeGenero(String nomeGenero) {
        
//1: Definir o comando SQL
        String sql = "INSERT INTO genero(Nome) VALUES (?)";
//2: Abrir uma conexão
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection c = factory.obterConexao()) {
//3: Pré compila o comando
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//4: Preenche os dados faltantes
            ps.setString(1, nomeGenero);

//5: Executa o comando
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int lastInsertId = rs.getInt(1);
//                System.out.println(lastInsertId);
                idGenero = lastInsertId;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"Adicionado");
    }


}