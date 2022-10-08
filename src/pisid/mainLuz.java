package pisid;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.nin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

public class mainLuz extends MongotoMongo{
	
	public static List<String> zonas;
	public static List<Document> cursor_simultaneo;
	
	
	
	public static void main(String[] args) throws SQLException, InterruptedException {
		
		
		
		zonas = new ArrayList<>();
		cursor_simultaneo = new ArrayList<>();
		new MongotoMongo().connect();
		
		MongoConnectMySQL mongoToMysql= new MongoConnectMySQL();
		mongoToMysql.connect();
		String lastDate="";
		
		alertas alertas = new alertas();
		 
		
		 
		while(true) {
			
			String DBCloud = "sid2022";
	        String DBuserCloud = "aluno";
	        String DBpassCloud = "aluno";
			
			Connection connCloud = DriverManager.getConnection("jdbc:mysql://194.210.86.10/" + DBCloud + "?useTimezone=true&serverTimezone=UTC", DBuserCloud, DBpassCloud);
			limites(connCloud);
			
			
			for(int i =1; i< n_zonas_luz+1; i++) {
				
			
			Document cursor=null;
			 Document cursor2=null;
			 Document cursor_verify=null;
			 Document cursor_verify2=null;
			 
			 long current=0;
			
			 double mn=0;
			 double mx=0;
			
			
				
		
				 if (lastDate.equals("")) {	
					 
					
				
					 String patternStr = "L";
					 Pattern pattern = Pattern.compile(patternStr);
					 
					 Collection<String> colection_zonas = zonas;
					 
					 
					 
					 Iterable<String> iterador_zonas = colection_zonas;
					 
					 
					 
					 Bson andComparison = and(Filters.regex("Sensor", pattern), nin("Zona",iterador_zonas));
					 
           
           
           
           while(cursor==null) {
        	   
        	   cursor = t1.find(andComparison).sort(new BasicDBObject("Data", -1)).first();
        	   
        	   current = System.currentTimeMillis();
           }
           
           
           
           String docStr1 = cursor.toString();
			String leitura1 = filterLeitura(docStr1);
			String zonaa = filterZona(docStr1);
			String sensor =filterSensor(docStr1);
			String s1 = zonaa.replace("Z", "");
			String s2 = sensor.replace("L", ""); 
			
			if(s1.equals(s2)) {
			if(zonaa.equals("Z1")) {
				mn = MnL1;
				mx = MxL1;
			} else if (zonaa.equals("Z2")){
				mn = MnL2;
				mx = MxL2;
				
			}
			
			if(Double.parseDouble(leitura1)>  mn && Double.parseDouble(leitura1)< mx ) {
				
				String leitura2 = filterZona(docStr1);
				
				if(total_zonas_luz.contains(leitura2)) {
				zonas.add(leitura2);
				cursor_verify = cursor;
				cursor_simultaneo.add(cursor_verify);
				}
				
				
				

			}
			}
           
           
           
           
				 } else {
					 
					 
					 String patternStr = "L";
					 Pattern pattern = Pattern.compile(patternStr);
					 
					 
					 Collection<String> colection_zonas = zonas;
					 
					 Iterable<String> iterador_zonas = colection_zonas;
					
					 
			           
			           
			           Bson andComparison = and (Filters.regex("Sensor", pattern), gt("Data", lastDate),nin("Zona",iterador_zonas));
			           while(cursor==null) {
			           cursor = t1.find(andComparison).sort(new BasicDBObject("Data", -1)).first();
			           current = System.currentTimeMillis();
			           }
			           
			           
			           String docStr1 = cursor.toString();
						String leitura1 = filterLeitura(docStr1);
						String zonaa = filterZona(docStr1);
						String sensor =filterSensor(docStr1);
						String s1 = zonaa.replace("Z", "");
						String s2 = sensor.replace("L", ""); 
						
						if(s1.equals(s2)) {
						 
						
						
						if(zonaa.equals("Z1")) {
							mn = MnL1;
							mx = MxL1;
						} else if (zonaa.equals("Z2")){
							mn = MnL2;
							mx = MxL2;
							
						}

						if(Double.parseDouble(leitura1)>mn && Double.parseDouble(leitura1)<mx ) {
							
							String leitura2 = filterZona(docStr1);
							if(total_zonas_luz.contains(leitura2)) {
							if(!zonas.contains(leitura2)) {
								zonas.add(leitura2);
								cursor_verify = cursor;
								cursor_simultaneo.add(cursor_verify);
								
								
							}
							}
							

						}
				 }
			           
			      
			           
					 
					 
				 }
			}
			
			zonas.clear();
				 
			String after_date= "0000-01-01T00:00:00Z";
			 Instant after_value= Instant.parse(after_date);
			 
			 if(cursor_simultaneo.size() > 0) {
			 after_date=(String) cursor_simultaneo.get(0).get("Data");
			 after_value= Instant.parse(after_date);
			 }
				 
				 
       
			for (Document c : cursor_simultaneo)	{ 
           if(c !=null ) {
        	   
        	   
        	   //lastDate = (String) c.get("Data");
        	   
        	   String select_date = (String) c.get("Data");
        	   Instant select_value = Instant.parse(select_date);
        	   
        	   
        	   if(select_value.isAfter(after_value)) {
        		   after_date = select_date;
        	   }
        		
        	   lastDate = after_date;
        	   
        	   System.out.println(lastDate);
        	   
        	  
           luz.insertOne(c);
           System.out.println(c);
           
           String docStr10 = c.toString();
           String peek_leitura = filterLeitura(docStr10);
           String peek_data= filterData_Hora_Temp(docStr10);
         
           
           Bson comp = and( eq("Data", peek_data),eq("Medicao", peek_leitura)); ;
           
           
           Document cursor_individual = luz.find(comp).sort(new BasicDBObject("Data", -1)).first();
           mongoToMysql.insert(cursor_individual);
           long duration = System.currentTimeMillis();
           
           String docStr1 = cursor_individual.toString();
           String hora = filterData_Hora_HumeLuz(docStr1);
           String zona = filterZona(docStr1);
           alertas.verificar_medicao_alerta(zona, hora);
          
           
           }
			}
           
           
           System.out.println("-------------------------//------------------------------------//--------------------------------//-----------------------");
           
           
           Thread.sleep(1500);
           
		

			cursor_simultaneo.clear();
		}

}
}
