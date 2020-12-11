package com.mycompany.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Musica {

    private String nomeDaMusica;
    private String generoDaMusica;
    private int idMusica;
    private int nota;
    
    public Musica() {
        this.nomeDaMusica = nomeDaMusica;
        this.idMusica = idMusica;
        this.nota = nota;
        this.generoDaMusica = generoDaMusica;       
    }    
    // Metodo insercao de musica e relaciona com 1 ou 2 generos
    public void inserir(String nomeMusica, int gen, int gen_1) {
        ArrayList<String> generos_disponiveis = new ArrayList();        
//1: Definir o comando SQL
        String sql = "INSERT INTO musica(Nome) VALUES (?)";
//2: Abrir uma conexão
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection c = factory.obterConexao()) {
//3: Pré compila o comando
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//4: Preenche os dados faltantes
            ps.setString(1, nomeMusica);
//5: Executa o comando
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int lastInsertId = rs.getInt(1);
                idMusica = lastInsertId;              
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
//1: Definir o comando SQL
        String sql_getgen = "SELECT * FROM genero ORDER BY Id";
//2: Abrir uma conexão
        ConnectionFactory factory_2 = new ConnectionFactory();
        try (Connection c = factory.obterConexao()) {
//3: Pré compila o comando
            PreparedStatement ps = c.prepareStatement(sql_getgen);
//4: Executa o comando e guarda o resultado em um ResultSet
            ResultSet rs = ps.executeQuery();
//5: itera sobre o resultado e salva em array            
            while (rs.next()) {
                String Nome = rs.getString("Nome");
                int Id = rs.getInt("Id");
                String looper_generos = String.format("%d:  %s", Id, Nome);
                generos_disponiveis.add(looper_generos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Aqui inserir algo que o usuario possa selecionar os generos da musica
        //int generoSelecionado1 = Integer.parseInt(JOptionPane.showInputDialog("Selecione um dos seguintes generos: " + generos_disponiveis));             
        String sqlRelacionarMusGen = "INSERT INTO registromusicagenero (MusicaId, GeneroId) VALUES (?,?) ";
        try (Connection c = factory.obterConexao()){
            //3: Pré compila o comando
            PreparedStatement ps = c.prepareStatement(sqlRelacionarMusGen);
            //4: Preenche os dados faltantes
            ps.setString(1, Integer.toString(idMusica));
            ps.setString(2, Integer.toString(gen));
            //5: Executa o comando
            ps.execute();
        }
        catch (Exception e){
        e.printStackTrace();
        }
        //int generoSelecionado2 = Integer.parseInt(JOptionPane.showInputDialog("Deseja selecionar mais algum genero? " + generos_disponiveis));
        if (gen_1 != 0){
            String sqlRelacionarMusGen2 = "INSERT INTO registromusicagenero (MusicaId, GeneroId) VALUES (?,?)";
            try (Connection c = factory.obterConexao()){
            //3: Pré compila o comando
            PreparedStatement ps = c.prepareStatement(sqlRelacionarMusGen2);
            //4: Preenche os dados faltantes
            ps.setString(1, Integer.toString(idMusica));
            ps.setString(2, Integer.toString(gen_1));
            //5: Executa o comando
            ps.execute();       
        }
        catch (Exception e){
        e.printStackTrace();
        }               
        }
    }
}
