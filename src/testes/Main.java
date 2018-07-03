package testes;

import br.com.hillbernate.connection.ConnectionFactory;
import br.com.hillbernate.dataHandlers.DataTable;
import br.com.hillbernate.dataHandlers.Database;

public class Main {

	public static void main(String args[]) {
		
		Database db = new ConnectionFactory().setConnection("jdbc:mysql://localhost:3306/teste", "root", "proway").getDatabase();
		
		DataTable<ClasseGenerica> dt = db.getTable("tabelateste", new ClasseGenerica());
		
		System.out.println(dt.select());
	}
}