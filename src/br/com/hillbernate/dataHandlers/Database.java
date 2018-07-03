package br.com.hillbernate.dataHandlers;

import java.sql.Connection;

public class Database {

	
	private Connection conexao;
	
	
	public Database(Connection conexao) {
		
		this.conexao = conexao;
	}
	
	
	public <T extends Object> DataTable<T> getTable(String tabela, T obj) {
		
		return new DataTable<T>(tabela, obj);
	}
}
