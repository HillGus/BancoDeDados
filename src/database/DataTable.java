package database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import tableModel.CustomTableModel;

public class DataTable {

	//Atributos
	private String nome;
	
	private DataBase bd;

	private CustomTableModel<TableObject> tableModel;
	
	private ArrayList<TableObject> objetos = new ArrayList<>();
	private ArrayList<String> atributos = new ArrayList<>();

	//Construtores
 	public DataTable(String nome, DataBase bd) {
		
		this.nome = nome;
		this.bd = bd;
		
		iniciar();
		
		tableModel = new CustomTableModel<>(objetos);
	}
	
	
 	//Métodos públicos
	public CustomTableModel<TableObject> getModel() {
		
		return tableModel;
	}

	public void reset() {
		
		this.objetos.clear();
		this.atributos.clear();
		
		iniciar();
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
		
		reset();
		
		atualizar();
	}
	
	public void select() {
		
		gerarObjetos(montarSelect());
	}
	
	public void select(String[] atributos) {
		
		atualizarAtributos(atributos);
		
		gerarObjetos(montarSelect());
		
		atualizar();
	}
	
	public void select(ArrayList<String> atributos) {
		
		//Transform o arraylist de atributos em um array para passar como parâmetro do método atualizarAtributos
		atualizarAtributos(atributos.toArray(new String[atributos.size()]));
		
		gerarObjetos(montarSelect());
		
		atualizar();
	}
	
	public ArrayList<TableObject> getObjetos() {
		
		return this.objetos;
	}
	
	
	//Métodos privados
	private void iniciar() {
		
		//Monta um select para pegar 1 "objeto" da tabela
		ResultSet resultado = this.bd.select("select * from " + this.nome + " limit 1");
		
		try {
			
			/*Obtém o Metadata do resultado
			Isso serve para pegar dados como quantidade de colunas, nomes das colunas, etc
			*/
			ResultSetMetaData metadata = resultado.getMetaData();

			int tamanho = metadata.getColumnCount();
			
			//Adiciona os nomes das colunas ao arraylist de atributos
			for (int i = 1; i <= tamanho; i++) {
				
				atributos.add(metadata.getColumnName(i));
			}
			
		} catch (SQLException e) {
			
			System.out.println("Erro ao obter informações da tabela.");
			e.printStackTrace();
		}
		
		select();
	}
	
	private void atualizar() {
		
		tableModel.setObjects(objetos);
	}
	
	private void atualizarAtributos(String[] atributos) {
		
		this.atributos.clear();
		
		for (String atributo : atributos) {
			
			this.atributos.add(atributo);
		}
	}
	
	private void gerarObjetos(String select) {
		
		this.objetos.clear();
		
		//Recebe os "objetos" da tabela de acordo com um select informado pelo usuário
		ResultSet resultado = this.bd.select(select);
		
		try {
			
			//Obtém o metadata do resultado
			ResultSetMetaData metadata = resultado.getMetaData();
			
			int tamanho = metadata.getColumnCount();
			
			//Enquanto houverem "objetos" resultantes do select roda o while
			while (resultado.next()) {
				
				//Cria um objeto de tabela
				TableObject obj = new TableObject();
				
				//Adiciona as informações ao objeto
				for (int i = 0; i < tamanho; i++) {
					
					String tituloColuna = metadata.getColumnName(i + 1);
					
					//Verifica se o nome da coluna está na lista de atributos
					//Ou se a lista de atributos está vazia, simbolizando que todos as colunas são colocadas
					if (inAtributos(tituloColuna) || this.atributos.isEmpty()) {
						
						//Adiciona a informação e o nome dela ao objeto
						obj.addInfo(atributos.get(i), resultado.getObject(atributos.get(i)));
					}
				}
				
				this.objetos.add(obj);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	private String montarSelect() {
		
		String select = "select ";
		
		for (String atributo : atributos) {
		
			select += atributo + (atributos.indexOf(atributo) != atributos.size() - 1? ", " : " ");
		}
		
		select += "from " + this.nome;
		
		return select;
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
				
				//O try catch existe para se caso o objeto passado seja nulo não dê erro
				try {
					//Verifica se o tipo do objeto passado é String
					//Caso o objeto seja String adiciona uma " antes do valor aparecer, para que o phpo entenda que é uma String
					insert += valor.getClass().equals(String.class) ? "\"" : "";
				} catch (Exception e) {}
				
				insert += valor;
				
				//O try catch existe para se caso o objeto passado seja nulo não dê erro
				try {
					//Verifica se o tipo do objeto passado é String
					//Caso o objeto seja String adiciona uma " antes do valor aparecer, para que o phpo entenda que é uma String
					insert += valor.getClass().equals(String.class) ? "\"" : "";
				} catch (Exception e) {}
				
				insert += (valor == conjuntoValores[conjuntoValores.length - 1] ? "" : ", ");
			}
			
			insert += ")" + (conjuntoValores == valores[valores.length - 1] ? "" : ", ");
		}
		
		return insert;
	}

	private boolean inAtributos(String atributo) {
		
		for (String at : atributos) {
			
			if (atributo.equals(at)) {
				
				return true;
			}
		}
		
		return false;
	}
}