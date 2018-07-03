package br.com.hillbernate.dataHandlers;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.hillbernate.connection.ConnectionFactory;

public class DataTable<Objeto extends Object> {

	private HashMap<String, Method> getters = new HashMap<>();
	private HashMap<String, Method> setters = new HashMap<>();
	private ArrayList<String> fields = new ArrayList<>();

	private Connection conexao;
	private String tabela;

	private Class<Objeto> classe;

	
 	public DataTable(String tabela, Objeto obj) {

		this("main", tabela, obj);
	}

	@SuppressWarnings("unchecked")
	public DataTable(String banco, String tabela, Objeto obj) {

		conexao = new ConnectionFactory().getConnection(banco);
		this.tabela = tabela;
		
		classe = (Class<Objeto>) obj.getClass();
		
		configurarDados(classe);
	}

	
	public ArrayList<Objeto> select() {
		
		ArrayList<Objeto> objetos = new ArrayList<Objeto>();
		
		
		String sql = "select ";
		for (String field : fields) {
			
			sql += field + (fields.indexOf(field) < fields.size() - 1 ? ", " : " ");
		}
		sql += "from " + tabela;
		
		
		try {
			
			Statement st = conexao.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next()) {
				
				Objeto obj = null;
				
				try {
					
					obj = classe.newInstance();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
				for (String field : fields) {
					
					Object value = rs.getObject(field);
					
					try {
						
						setters.get(field).invoke(obj, value);
					} catch (Exception e) {
						
						e.printStackTrace();
						
						int lf = field.length();
						int lt = tabela.length();
						
						String f = field.substring(0, lf - lt);
							
						try {
							
							setters.get(f).invoke(obj, value);
						} catch (Exception ex) {
							
							ex.printStackTrace();
						}
					}
				}
				
				objetos.add(obj);
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return objetos;
	}
	
	
	public DataTable<Objeto> setConnection(Connection conexao) {
		
		this.conexao = conexao;
		
		return this;
	}
	
	
	private void configurarDados(Class<?> classe) {
		
		getGettersAndSetters(classe);
		
		getFieldsName(classe);
	}
	
	private void getGettersAndSetters(Class<?> classe) {
		
		for (Method m : classe.getMethods()) {
			
			if (isGetter(m)) {
				
				String field = m.getName().substring(3);
				
				getters.put(field.toLowerCase(), m);
			}
			
			if (isSetter(m)) {
				
				String field = m.getName().substring(3);
				
				setters.put(field.toLowerCase(), m);
			}
		}
	}
	
	private boolean isGetter(Method metodo) {
		
		String nome = metodo.getName();
		
		if (!nome.startsWith("get")) return false;
		if (metodo.getParameterTypes().length != 0) return false;
		if (void.class.equals(metodo.getReturnType())) return false;
		
		return true;
	}
	
	private boolean isSetter(Method metodo) {
		
		String nome = metodo.getName();
		
		if (!nome.startsWith("set")) return false;
		if (metodo.getParameterTypes().length == 0) return false;
		
		return true;
	}

	private void getFieldsName(Class<?> classe) {
		
		String sql = "select * from " + tabela + " limit 1";
		
		try {
		
			Statement st = conexao.createStatement();
			
			ResultSetMetaData rsmd = st.executeQuery(sql).getMetaData();
			
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				
				String field = rsmd.getColumnName(i);
				
				fields.add(field.toLowerCase());
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
}