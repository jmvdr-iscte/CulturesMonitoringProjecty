package pisid;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;

import static com.mongodb.client.model.Filters.*;




public class MongotoMongo extends Suporte{
	
	static MongoCollection<Document> temperatura;
	static MongoCollection<Document> luz;
	static MongoCollection<Document> humidade;
	
	static MongoCollection<Document> t1;
	static MongoDatabase dbLocal;
	
	public final static int n_zonas_temperatura=2;
	public static List<String> total_zonas_temperatura;
	
	public final static int n_zonas_luz=2;
	public static List<String> total_zonas_luz;
	
	public final static int n_zonas_humidade=2;
	public static List<String> total_zonas_humidade;
	

	public MongotoMongo() {
		
		
	}
	
	
	public void connect() throws SQLException {
		
		String url = "mongodb://aluno:aluno@194.210.86.10:27017/?authSource=admin";

		String DBCloud = "sid2022";
        String DBuserCloud = "aluno";
        String DBpassCloud = "aluno";
		
		
		MongoClient cloudMongoClient = new MongoClient(new MongoClientURI(url));
	    MongoDatabase cloudMongoDatabase = cloudMongoClient.getDatabase("sid2022");
	    t1 = cloudMongoDatabase.getCollection("medicoes2022");
	    
	    
	    
	    MongoClient mongoLocal = new MongoClient("localhost", 27017);
        MongoDatabase dbLocal = mongoLocal.getDatabase("bd_g09");
        temperatura = dbLocal.getCollection("temperatura");
        luz = dbLocal.getCollection("luz");
        humidade = dbLocal.getCollection("humidade");
	    
	    
    	
	   
	    
		
		total_zonas_temperatura= new ArrayList<>();
		for(int i=1; i< n_zonas_temperatura+1; i++)
			total_zonas_temperatura.add("Z"+i);
		
		
	
		total_zonas_luz= new ArrayList<>();
		for(int i=1; i< n_zonas_luz+1; i++)
		total_zonas_luz.add("Z"+i);
	
		

		total_zonas_humidade= new ArrayList<>();
		for(int i=1; i< n_zonas_humidade+1; i++)
		total_zonas_humidade.add("Z"+i);

	

	
	}
	
	

         
            
        
		
        
        
        
        
        
        
       


	}
	

