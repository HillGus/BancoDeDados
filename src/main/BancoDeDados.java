package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import tableModel.CustomTableModel;

public class BancoDeDados{

	private CustomTableModel<ObjetoGenerico> tableModel = new CustomTableModel<>();
	private Connection connect;
	private Statement statement;
	
	
	public static void main(String args[]) throws SQLException {
		
		BancoDeDados bd = new BancoDeDados("jdbc:mysql://localhost:3306/atividade01", "root", "");
		
		JFrame frame = new JFrame();
		frame.setSize(616, 339);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setLocationRelativeTo(null);
		
		JScrollPane tabela = bd.select("produtos", new String[][] {{"idProduto",   "Código"}, 
																   {"nomeProduto", "Nome"}});
	
		tabela.setBounds(25, 25, 550, 250);
		
		frame.add(tabela);
		
		frame.setVisible(true);
	}
	
	
	public BancoDeDados(String url, String user, String password) {
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connect = DriverManager.getConnection(url + "?useTimezone=true&serverTimezone=UTC", user, password);
		} catch (Exception e) {
			
			System.out.println("Impossível conectar com Banco De Dados em " + url + ".");
			e.printStackTrace();
		}
		
		try {
			
			this.statement = this.connect.createStatement();
		} catch (Exception e) {
			
			System.out.println("Impossível criar conexão com o Banco De Dados em " + url + ".");
		}
	}
	
	
	public JScrollPane select(String tabela, String[][] atributos) throws SQLException {
		
		String select = montarSelect(tabela, atributos);
		
		ResultSet resultado = null;
		
		try {
			
			resultado = this.statement.executeQuery(select);
		} catch (SQLException e) {
			
			System.out.println("Erro ao selecionar dados.");
			e.printStackTrace();
		}
		
		ArrayList<ObjetoGenerico> objetos = getObjetos(resultado, atributos);
		
		tableModel.setObjects(objetos);
		
		return tableModel.getScroll();
	}
	
	
	private String montarSelect(String tabela, String[][] atributos) {
		
		String select = "select ";
		
		for (String[] linha : atributos) {
			
			select += linha[0] + " as " + linha[1] + 
					  (atributos[atributos.length - 1] == linha ? " " : ", ");
		}
			
		select += "from " + tabela;
		
		return select;
	}

	private ArrayList<ObjetoGenerico> getObjetos(ResultSet resultado, String[][] atributos) throws SQLException {
		
		ArrayList<ObjetoGenerico> objetos = new ArrayList<>();
		
		int tamanho = atributos.length;
		
		while (resultado.next()) {
			
			ObjetoGenerico obj = new ObjetoGenerico(tamanho);
			
			for (int i = 0; i < tamanho; i++) {
				
				String tituloColuna = atributos[i][1];
				
				obj.addInfo(tituloColuna, resultado.getObject(tituloColuna));
			}
			
			objetos.add(obj);
		}
		
		return objetos;
	}
}
