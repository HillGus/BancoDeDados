package main;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import tableModel.CustomTableModel;

public class DataTable {

	private String nome;
	
	private BancoDeDados bd;
	private ArrayList<TableObject> objetos = new ArrayList<>();

	private CustomTableModel<TableObject> tabela;
	
 	public DataTable(String nome, BancoDeDados bd) {
		
		this.nome = nome;
		this.bd = bd;
		
		gerarObjetos();
		
		tabela = new CustomTableModel<>(objetos);
	}
	
	
	public CustomTableModel<TableObject> getModel() {
		
		return tabela;
	}
	
	public void insert(Object[] valores) {
		
		insert(new Object[][] {valores});
	}
		
	public void insert(Object[][] valores) {
		
		insert(null, valores);
	}
	
	public void insert(String[] atributos, Object[][] valores) {
		
		String insert = montarInsert(atributos, valores);
		
		this.bd.insert(insert);
		
		gerarObjetos();
		
		tabela.setObjects(objetos);
	}
	
	public ArrayList<TableObject> getObjetos() {
		
		return this.objetos;
	}

	
	private void gerarObjetos() {
		
		this.objetos.clear();
		
		ResultSet resultado = this.bd.select("select * from " + this.nome);
		
		try {
			
			ResultSetMetaData metadata = resultado.getMetaData();
			

			int tamanho = metadata.getColumnCount();
			String[] atributos = new String[tamanho];
			
			for (int i = 0; i < tamanho; i++) {
				
				atributos[i] = metadata.getColumnName(i + 1);
			}
			
			while (resultado.next()) {
				
				TableObject obj = new TableObject(tamanho);
				
				for (int i = 0; i < tamanho; i++) {
					
					obj.addInfo(atributos[i], resultado.getObject(atributos[i]));
				}
				
				this.objetos.add(obj);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	private String montarInsert(String[] atributos, Object[][] valores) {
		
		String insert = "insert into " + this.nome;
		
		if (atributos != null) {
			
			insert += " (";
			
			for (String atributo : atributos) {
				
				insert += atributo + (atributo == atributos[atributos.length - 1] ? ")" : ", ");
			}
		}
		
		insert += " values ";
		
		for (Object[] conjuntoValores : valores) {
			
			insert += "(";
			
			for (Object valor : conjuntoValores) {
				
				try {
					insert += valor.getClass().equals(String.class) ? "\"" : "";
				} catch (Exception e) {}
				
				insert += valor;
				
				try {
					insert += valor.getClass() == "".getClass() ? "\"" : "";
				} catch (Exception e) {}
				
				insert += (valor == conjuntoValores[conjuntoValores.length - 1] ? "" : ", ");
			}
			
			insert += ")" + (conjuntoValores == valores[valores.length - 1] ? "" : ", ");
		}
		
		return insert;
	}
}