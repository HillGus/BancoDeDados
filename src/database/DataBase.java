package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class DataBase {

	private Connection connect;
	private Statement statement;
	
	public static void main(String args[]) throws SQLException {
		
		DataBase bd = new DataBase("localhost", "atividade01", "root", "");
		
		DataTable produtos = bd.getTable("produtos");
		
		JFrame frame = new JFrame();
		frame.setSize(616, 339);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setLocationRelativeTo(null);
		
		JScrollPane pane = produtos.getModel().getScroll();
		
		pane.setBounds(25, 25, 550, 250);
		
		frame.add(pane);
		
		frame.setVisible(true);
	}
	
	
	public DataBase(String host, String banco, String user, String password) {
		
		try {
			
			//Inicia driver do java que conecta com o sql
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//Inicia conexão
			//O useTimezone[...] é para que não dê conflito com o banco dependendo do lugar
			this.connect = DriverManager.getConnection("jdbc:mysql://" + host + "/" + banco + "?useTimezone=true&serverTimezone=UTC", user, password);
		} catch (Exception e) {
			
			System.out.println("Impossível conectar com Banco De Dados em " + host + ".");
			e.printStackTrace();
		}
		
		try {
			
			//Cria o statement, que conecta com o banco e retorna os dados
			this.statement = this.connect.createStatement();
		} catch (Exception e) {
			
			System.out.println("Impossível criar conexão com o Banco De Dados em " + host + ".");
		}
	}
	
	
	public DataTable getTable(String tabela) {
		
		return new DataTable(tabela, this);
	}
	
	public Object execute(String command) {
		
		try {
			
			Object objeto = this.statement.executeUpdate(command);
			return objeto;
		} catch (SQLException e) {
			
			System.out.println("Erro ao executar comando.");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ResultSet select(String select) {
		
		try {
			
			return this.statement.executeQuery(select);
		} catch (SQLException e) {
			
			System.out.println("Erro ao selecionar dados.");
			e.printStackTrace();
			return null;
		}
	}
	
	public void insert(String insert) {
		
		try {
			
			this.statement.executeUpdate(insert);
		} catch (SQLException e) {
			
			System.out.println("Erro ao inserir dados.");
			e.printStackTrace();
		}
	}
}
