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
    
    public void venderProduto(ProdutosDTO p){
        String jQuery = "UPDATE produtos SET status = ? WHERE id = ?";
        try{
            if(conectar()){
             PreparedStatement st = conn.prepareStatement(jQuery);
             
             prep.setString(1, p.getStatus());
             prep.setInt(2, p.getId());
             
             prep.executeUpdate();
             JOptionPane.showMessageDialog(null, "Produto vendido!");
            }
        
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Erro ao atuzalizar "+ ex.getMessage());
        }finally {
            desconectar();
        }
        
    }
    
    
        
}

