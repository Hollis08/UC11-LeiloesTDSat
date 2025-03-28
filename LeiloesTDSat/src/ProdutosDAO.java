/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProdutosDAO {
    
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    private String url = "jdbc:mysql://localhost:3306/uc11?useSSL=false";
    private String user = "root";
    private String password = "Coldmaster1@";
    
    public boolean conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            return true;
        } catch(ClassNotFoundException | SQLException ex){
            System.out.println("Falha na conexão. Erro: " + ex.getMessage());
            return false;
        }
    }
    
    public void desconectar(){
        try{
            conn.close();
        } catch (SQLException ex){
            System.out.println("Erro ao desconectar "+ ex.getMessage());
        }
    }
    
    public int cadastrarProduto (ProdutosDTO p){
        int status;
        if(this.conectar()){
            try {
            prep = conn.prepareStatement("INSERT INTO produtos VALUES(?,?,?,?) ");
            prep.setInt(1, p.getId());
            prep.setString(2, p.getNome());
            prep.setInt(3, p.getValor());
            prep.setString(4, p.getStatus());
            
            status = prep.executeUpdate();
            
            return status;
            
        } catch (SQLException ex){
            System.out.println("Erro ao cadastrar "+ ex.getMessage());
            return ex.getErrorCode();
        }
            
        }else {
            System.out.println("A conexão com o banco de dados não foi estabelecida.");
            return 10;
            }
    }
    
    public ArrayList<ProdutosDTO> listarProdutos(){
        String jpqlQuery = "SELECT id, nome, valor, status FROM produtos";
        try{
            if(conectar()){
                PreparedStatement st = conn.prepareStatement(jpqlQuery);
                resultset = st.executeQuery();
                
                while(resultset.next()){
                    ProdutosDTO p = new ProdutosDTO();
                    p.setId(resultset.getInt("id"));
                    p.setNome(resultset.getString("nome"));
                    p.setValor(resultset.getInt("valor"));
                    p.setStatus(resultset.getString("status"));
                    listagem.add(p);
                }
                resultset.close();
                st.close();
            }
            
        }catch(Exception e) {
            System.err.println("Erro ao listar os produtos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return listagem;
    }
    
    public void venderProduto(String id){
        String jQuery = "UPDATE produtos SET status = 'Vendido' WHERE id = ? ";
        try{
            if(conectar()){
             prep = conn.prepareStatement(jQuery);
             
             prep.setInt(1, Integer.parseInt(id));
             
             prep.executeUpdate();
             JOptionPane.showMessageDialog(null, "Produto vendido!");
            }
        
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Erro ao atuzalizar "+ ex.getMessage());
        }finally {
            desconectar();
        }
        
    }
    
    public ArrayList<ProdutosDTO> listarProdutosVendidos (){
        String jQuery = "SELECT id, nome, valor, status FROM produtos p WHERE status LIKE 'Vendido' ";
        
        try{
            if(conectar()){
                prep = conn.prepareStatement(jQuery);
                
                resultset = prep.executeQuery();
                ArrayList<ProdutosDTO> lista = new ArrayList();
                while(resultset.next()){
                    ProdutosDTO p = new ProdutosDTO();
                    p.setId(resultset.getInt("id"));
                    p.setNome(resultset.getString("nome"));
                    p.setStatus(resultset.getString("status"));
                    p.setValor(resultset.getInt("valor"));
                    lista.add(p);
                }
                return lista;
            }else {
                JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados.");
                return null;
            }
            
        }catch(Exception ex){
            System.out.println("Erro ao consultar produto."+ ex.getMessage());
            return null;
        }finally {
            desconectar();
        }
    }
    
}

