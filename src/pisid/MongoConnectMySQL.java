package pisid;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoConnectMySQL extends MongotoMongo{
	static String db = "pisid";
	static String DBuser = "root";
	static String DBpass = "root";
	static Connection Connectionsql;
	
	
	
	
	
	
	public MongoConnectMySQL() {

	}
	
	
	static Timestamp timestamp(String datahora) {
        String T= datahora.replace('T', ' ');
        String Z= T.substring(0, T.length()-1);
        
        Timestamp timestamp= null;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Z);
            timestamp = new Timestamp(date.getTime());

            
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        return timestamp;
    }
	
	
	public void connect() {
		
		//LIGAÃ‡ÃƒO Ã€ MONGO CLOUD******
		
		
		String urlLocal = "localhost:3306";
		
		String database = "bd_g09";

		
	
        
		
	    try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Connectionsql = DriverManager.getConnection("jdbc:mysql://" + urlLocal +  "/" + database + "?useTimezone=true&serverTimezone=UTC", DBuser, DBpass);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(Connectionsql!=null){
			System.out.println("Ligacao estabelecida");
		}
		
	}
		
		
		
		public void insert(Document cursor_individual) { 		
		
		
		
			//ENQUANTO PODER CORRER, ELE CORRE
		    
		    	
		    	//System.out.println(cursor.next().toString());
		    	String dados = cursor_individual.toString();
		    	//System.out.println(dados);
		    	
		    	String[] parts = dados.split(",");
		    	String[] zona = parts[1].split("=");
		    	String[] sensor = parts[2].split("=");
		    	String[] hora = parts[3].split("=");
		    	
		    	
		    	Timestamp datahora=timestamp(hora[1]);
		    	
		    	String[] medicao = parts[4].split("=");
		    	String[] medicao2= medicao[1].split("}}", 2);
		        String auxleitura= medicao2[0];
		       		             
		    	String query = "INSERT INTO medicao (IDzona, Sensor, Hora, Leitura) VALUES ('" + zona[1] + "', '" + sensor[1] +"', '"+ datahora +"', '"+ Double.parseDouble(auxleitura) + "');";

		        System.out.println("---MYSQL---");
		        System.out.println(query);
		        try {
		            Connectionsql.createStatement().executeUpdate(query);
		        } catch (SQLException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }
		        
			
	
		
	}
		
}
	
	

