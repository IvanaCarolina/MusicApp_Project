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
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.UUID;
import javax.swing.JOptionPane;

public class Usuario {

    private int idUsuario;
    private String nomeUsuario;
    private String senha;
    private String uuid;
    private boolean logado = true;

    public Usuario() {
        this.idUsuario = idUsuario;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.uuid = uuid;
        this.logado = logado;
    }

    //cadastra usr
    public boolean cadastro(String usuario, String senha) {
        //Declaracao de variaveis
        ArrayList<String> nomesCadastrados = new ArrayList();
        int counter_cadastro = 0;

        // Checar disponibilidade de nome de usuario 
        while (counter_cadastro == 0) {
            int flagNomeIgual = 0;
            ConnectionFactory factory = new ConnectionFactory();
            nomeUsuario = usuario;
            //1: Definir o comando SQL
            String sql_getusr = "SELECT nome FROM usuario";
            //2: Abrir uma conexão
            try (Connection c = factory.obterConexao()) {
                //3: Pré compila o comando
                PreparedStatement ps = c.prepareStatement(sql_getusr);
                //4: Executa o comando e guarda o resultado em um ResultSet
                ResultSet rs = ps.executeQuery();
                //5: itera sobre o resultado e salva em arraY
                while (rs.next()) {
                    String Nome = rs.getString("Nome");

                    for (int i = 0; i < nomesCadastrados.size(); i++) {
                        if (!nomesCadastrados.get(i).equals(Nome)) {
                            nomesCadastrados.add(Nome);
                        }
                    }
                    if (nomeUsuario.equals(Nome)) {
                        flagNomeIgual++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (flagNomeIgual == 0) {
                counter_cadastro++;
            } else {
                JOptionPane.showMessageDialog(null, "Usuario Invalido");
                counter_cadastro = 1;
                return false;
            }

        }

        //Definir senha e seguranca da senha, gerar uuid para o guid
        while (senha.length() < 12) {
            senha = JOptionPane.showInputDialog("Escolha uma senha segura, com no minimo 12 caracteres");
        }
        uuid = UUID.randomUUID().toString();
        //1: Definir o comando SQL
        String sqlInsertSenhaUuid = "INSERT INTO usuario(Nome, Senha, GuidUsuario) VALUES (?, ?, ?)";
        //2: Abrir uma conexão
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection c = factory.obterConexao()) {
            //3: Pré compila o comando
            PreparedStatement ps = c.prepareStatement(sqlInsertSenhaUuid, Statement.RETURN_GENERATED_KEYS);
            //4: Preenche os dados faltantes
            ps.setString(1, nomeUsuario);
            ps.setString(2, senha);
            ps.setString(3, uuid);

            //5: Executa o comando
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    //Verifica credenciais e loga caso exista
    public boolean login(String usuario, String senha){
        //Pede credenciais
        //Puxa dados e verifica se existe login/senha e se ambos conferem
        //1: Definir o comando SQL
        String sqlRetornarCredenciais = "SELECT * FROM usuario";
        //2: Abrir uma conexão
        ConnectionFactory factory = new ConnectionFactory();
        boolean existe = false;
        try (Connection c = factory.obterConexao()) {
            //3: Pré compila o comando
            PreparedStatement ps = c.prepareStatement(sqlRetornarCredenciais);
            //5: Executa o comando
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("Nome").equals(usuario) && rs.getString("Senha").equals(senha)) {
                    logado = true;
                    uuid = rs.getString("GuidUsuario");
                    existe = true;
                    String lastLogin = "INSERT INTO Gambiarra2(UltimoLogin) VALUES (?)";  
                    try (Connection b = factory.obterConexao()) {
//3: Pré compila o comando
                    PreparedStatement ps2 = b.prepareStatement(lastLogin, Statement.RETURN_GENERATED_KEYS);
//4: Preenche os dados faltantes
                    ps2.setString(1, uuid);
//5: Executa o comando
                    ps2.executeUpdate();
                    ResultSet rs2 = ps2.getGeneratedKeys();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        } 
        }
        catch (Exception e) {
            e.printStackTrace();
        } //checar uuid que aparece na hora de inserir na string
        return existe;
    }

    public void avaliarGenero(int genero, String uuid) {
        //vars
        ArrayList<String> generosDisponiveis = new ArrayList();
        //1: Definir o comando SQL
        String sql = "SELECT * FROM genero ORDER BY Id";
        //2: Abrir uma conexão
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection c = factory.obterConexao()) {
            //3: Pré compila o comando
            PreparedStatement ps = c.prepareStatement(sql);
            //5: Executa o comando
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nomeDoGenero = rs.getString("Nome");
                String idDoGenero = rs.getString("Id");
                generosDisponiveis.add(idDoGenero + " - " + nomeDoGenero);
            }
            System.out.println(generosDisponiveis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Integer generoASerAvaliado = Integer.parseInt(JOptionPane.showInputDialog("Qual genero deseja avaliar? "));
        while (genero != 0) {
            String gn = Integer.toString(genero);
            //int indiceArrayGen = generoASerAvaliado - 1;
            //String idGenero = generosDisponiveis.get(indiceArrayGen);
            //String[] substringIdGenero = idGenero.split("\\-");
            //String idGeneroASerAvaliado = (substringIdGenero[0]);
            //String guidSessaoAtual = uuid;
            String notaGeneroEscolhido = JOptionPane.showInputDialog("Digite sua nota de 1-5 ou 0 para cancelar");
            if (!"0".equals(notaGeneroEscolhido)) {
                //1: Definir o comando SQL
                String avaliarGenero = "INSERT INTO preferenciagenero(GuidUsuario, GeneroId, Ordem) VALUES (?, ?, ?)";
                //2: Abrir uma conexão
                try (Connection c = factory.obterConexao()) {
                    //3: Pré compila o comando
                    PreparedStatement ps = c.prepareStatement(avaliarGenero, Statement.RETURN_GENERATED_KEYS);
                    //4: Preenche os dados faltantes
                    ps.setString(1, uuid);
                    ps.setString(2, gn);
                    ps.setString(3, notaGeneroEscolhido);
                    //5: Executa o comando
                    ps.executeUpdate();
                    ResultSet rs = ps.getGeneratedKeys();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            genero = 0;
        }
    }

    public ArrayList <String> listarGenerosFavoritos(String uuid)  {
        //definir array
        ArrayList<String> nomeGenerosAvaliados = new ArrayList();
        //1: Definir o comando maldito de SQL
        String mostrarGenerosAvaliados = "select * from preferenciagenero as a join genero as b on a.GeneroId = b.Id where Ordem > 0 AND a.GuidUsuario = '%%%' order by a.Ordem DESC";
        //Tratamento de string
        String mostrarGenerosAvaliadosTratado = mostrarGenerosAvaliados.replaceAll("%%%", uuid);
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection c = factory.obterConexao()) {
            //3: Pré compila o comando
            PreparedStatement ps = c.prepareStatement(mostrarGenerosAvaliadosTratado);
            //5: Executa o comando        
            ResultSet rs = ps.executeQuery();
            //adiciona Generos numa lista
            while (rs.next()) {
                String nomeGenero = rs.getString("Nome");
                nomeGenerosAvaliados.add(nomeGenero);
            }   
            //JOptionPane.showMessageDialog(null, nomeGenerosAvaliados);
            //System.out.println(nomeGenerosAvaliados);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nomeGenerosAvaliados;
    }

    public ArrayList<String> listarGeneros() throws Exception {
        //definir array
        ArrayList<String> generos = new ArrayList();
        //1: Definir o comando maldito de SQL
        String mostrarGeneros = "select * from genero order by Id";
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection c = factory.obterConexao()) {
            //3: Pré compila o comando
            PreparedStatement ps = c.prepareStatement(mostrarGeneros);
            //5: Executa o comando        
            ResultSet rs = ps.executeQuery();
            //adiciona Generos numa lista
            while (rs.next()) {
                String generoId = rs.getString("Id");
                String nomeGenero = rs.getString("Nome");
                generos.add(generoId + " - " + nomeGenero);
            }
            return generos;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ArrayList<String> listarMusicas() throws Exception {
        ArrayList<String> musicas = new ArrayList();
        String mostrarMusicas = "select * from musica order by Id";
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection c = factory.obterConexao()) {
            PreparedStatement ps = c.prepareStatement(mostrarMusicas);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String musicaId = rs.getString("Id");
                String nomeMusica = rs.getString("Nome");
                musicas.add(nomeMusica);
            }
            return musicas;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void avaliarMusica(int musica, String uuid) {
        //Definir Arrays
        ArrayList<String> musicasDisponiveis = new ArrayList();
        //1: Definir o comando SQL
        String sqlPegarMusicas = "SELECT * FROM musica ORDER BY Id";
        //2: Abrir uma conexão
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection c = factory.obterConexao()) {
            //3: Pré compila o comando
            PreparedStatement ps = c.prepareStatement(sqlPegarMusicas);
            //5: Executa o comando
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nomeMusica = rs.getString("Nome");
                String idMusica = rs.getString("Id");
                musicasDisponiveis.add(idMusica + " - " + nomeMusica);
            }
            //System.out.println(musicasDisponiveis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Integer musicaASerAvaliada = Integer.parseInt(JOptionPane.showInputDialog("Qual musica deseja avaliar? "));
        while (musica != 0) {
            String ms = Integer.toString(musica);
            //int indiceArrayGen = musica - 1;
            //String idMusica = musicasDisponiveis.get(indiceArrayGen);
            //String[] substringIdMusica = idMusica.split("\\-");
            //String idMusicaASerAvaliada = (substringIdMusica[0]);
            String notaMusicaEscolhida = JOptionPane.showInputDialog("Digite sua nota de 1-5 ou 0 para cancelar");
            if (!"0".equals(notaMusicaEscolhida)) {
                //1: Definir o comando SQL
                String avaliarGenero = "INSERT INTO preferenciamusica(GuidUsuario, MusicaId, Nota) VALUES (?, ?, ?)";
                //2: Abrir uma conexão
                try (Connection c = factory.obterConexao()) {
                    //3: Pré compila o comando
                    PreparedStatement ps = c.prepareStatement(avaliarGenero, Statement.RETURN_GENERATED_KEYS);
                    //4: Preenche os dados faltantes
                    ps.setString(1, uuid);
                    ps.setString(2, ms);
                    ps.setString(3, notaMusicaEscolhida);
                    //5: Executa o comando
                    ps.executeUpdate();
                    ResultSet rs = ps.getGeneratedKeys();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            musica = 0;
        }
    }

    public void listarMusicasFavoritas() {
        //Arraylist
        ArrayList<String> musicasAvaliadas = new ArrayList();
        //Comando SQL
        String mostrarMusicasAvaliadas = "select * from preferenciamusica as a join musica as b on a.MusicaId = b.Id where a.GuidUsuario = '%%%' AND Nota > 0 order by a.Nota DESC";

        //Tratamento de string
        String comandoSqlFinalBuscarMusicasAvaliadas = mostrarMusicasAvaliadas.replaceAll("%%%", uuid);

        //Conexao
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection c = factory.obterConexao()) {

            //3: Pré compila o comando
            PreparedStatement ps = c.prepareStatement(comandoSqlFinalBuscarMusicasAvaliadas);

            //5: Executa o comando        
            ResultSet rs = ps.executeQuery();
            //adiciona Generos numa lista
            while (rs.next()) {
                String nomeGenero = rs.getString("Nome");
                musicasAvaliadas.add(nomeGenero);
            }
            System.out.println(musicasAvaliadas);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList <String> listarMusicasRecomendadas(String uuid) {
        //Cria um set para evitar duplicatas
        //JOptionPane.showMessageDialog(null,uuid);
        ArrayList <String> musicasRecomendadas= new ArrayList<>();
        ArrayList <String> musicasRecomendadas_1 = new ArrayList();
        //Comando SQL
        String mostrarMusicasRecomendadas = "select * from preferenciagenero as a left join registromusicagenero as b on a.GeneroId = b.GeneroId left join musica as c on b.MusicaId = c.Id left join preferenciamusica as d on b.MusicaId = d.MusicaId where a.GuidUsuario = '###' and nota is null or nota =0 order by Nota";
        //Tratamento de string
        String comandoSqlFinalBuscarMusicasRecomendadas = mostrarMusicasRecomendadas.replaceAll("###", uuid);
        //Conexao
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection c = factory.obterConexao()) {
            //3: Pré compila o comando
            PreparedStatement ps = c.prepareStatement(comandoSqlFinalBuscarMusicasRecomendadas);
            //5: Executa o comando        
            ResultSet rs = ps.executeQuery();
            //adiciona Generos numa lista
            while (rs.next()) {
                String nomeMusica = rs.getString("Nome");
                musicasRecomendadas.add(nomeMusica);
            }
            for(String element:musicasRecomendadas){
                if(!musicasRecomendadas_1.contains(element))
                    musicasRecomendadas_1.add(element);
            }
            //System.out.println(musicasRecomendadas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //JOptionPane.showMessageDialog(null, musicasRecomendadas_1);
        return musicasRecomendadas_1;
    }
      
    public String ultimoLogin() {

        String mostrarUsrLogado = "SELECT UltimoLogin FROM recomendacaodemusicas.gambiarra2 order BY Id desc limit 1";
        //Conexao
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection c = factory.obterConexao()) {
            //3: Pré compila o comando
            PreparedStatement ps = c.prepareStatement(mostrarUsrLogado);
            //5: Executa o comando        
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String ultimoLogin = rs.getString("UltimoLogin");
                return ultimoLogin;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "x";
    }
}
