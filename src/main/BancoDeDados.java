package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import tableModel.CustomTableModel;

public class BancoDeDados{

	private CustomTableModel<TableObject> tableModel = new CustomTableModel<>();
	private Connection connect;
	private Statement statement;
	
	public static void main(String args[]) throws SQLException {
		
		BancoDeDados bd = new BancoDeDados("localhost", "atividade03", "root", "");
		
		DataTable produtos = bd.getTable("produtos");
		DataTable marcas = bd.getTable("marcas");
		
		JFrame frame = new JFrame();
		frame.setSize(616, 614);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setLocationRelativeTo(null);
		
		JScrollPane pane = produtos.getModel().getScroll();
		
		pane.setBounds(25, 25, 550, 250);
		
		JScrollPane pane2 = marcas.getModel().getScroll();
		
		pane2.setBounds(25, 300, 550, 250);
		
		frame.add(pane);
		frame.add(pane2);
		
		frame.setVisible(true);
	}
	
	
	public BancoDeDados(String host, String banco, String user, String password) {
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connect = DriverManager.getConnection("jdbc:mysql://" + host + "/" + banco + "?useTimezone=true&serverTimezone=UTC", user, password);
		} catch (Exception e) {
			
			System.out.println("Impossível conectar com Banco De Dados em " + host + ".");
			e.printStackTrace();
		}
		
		try {
			
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
			
			Object objeto = this.statement.executeQuery(command);
			return objeto;
		} catch (SQLException e) {
			
			System.out.println("Erro ao executar comando.");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ResultSet select(String tabela, String[] atributos) {
		
		String select = montarSelect(tabela, atributos);
		
		ResultSet resultado = null;
		
		try {
			
			return this.statement.executeQuery(select);
		} catch (SQLException e) {
			
			System.out.println("Erro ao selecionar dados.");
			e.printStackTrace();
			return null;
		}
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
	
	
	private String montarSelect(String tabela, String[] atributos) {
		
		String select = "select ";
		
		for (String atributo : atributos) {
			
			String info = atributo;
			
			select += info + (atributo == atributos[atributos.length - 1] ? " " : ", ");
		}
			
		select += "from " + tabela;
		
		return select;
	}
}
