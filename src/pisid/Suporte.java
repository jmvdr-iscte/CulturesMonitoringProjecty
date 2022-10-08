package pisid;

import java.sql.Connection;


import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Suporte {
	
	public static double MxH1;
	public static double MxH2;
	public static double MxT1;
	public static double MxT2;
	public static double MxL1;
	public static double MxL2;
	
	public static double MnH1;
	public static double MnH2;
	public static double MnT1;
	public static double MnT2;
	public static double MnL1;
	public static double MnL2;
	
	

	
	
	
	public static void limites(Connection conn) throws SQLException {

        String query1 = "select * from sensor,zona";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query1);
        while (rs.next()) {
            int sensor = rs.getInt("IDsensor");
            String tipo = rs.getString("tipo");
            double max = rs.getDouble("limitesuperior");
            double min = rs.getDouble("limiteinferior");

            if (sensor == 1 && tipo.equals("H")) {
                MxH1 = max;
                MnH1 = min;
               
            } else if (sensor == 2 && tipo.equals("H")) {
                MxH2 = max;
                MnH2 = min;
            } else if (sensor == 1 && tipo.equals("L")) {
                MxL1 = max;
                MnL1 = min;

            } else if (sensor == 2 && tipo.equals("L")) {
                MxL1 = max;
                MnL2 = min;

            } else if (sensor == 1 && tipo.equals("T")) {
                MxT1 = max;
                MnT1 = min;

            } else if (sensor == 2 && tipo.equals("T")) {
                MxT2 = max;
                MnT2 = min;

            }
        }
        
        

    }
	
	

	public static String filterZona(String s) {

		s = s.substring(s.indexOf(",") + 1);
		s = s.substring(s.indexOf("=") + 1);
		s = s.substring(0, s.indexOf(","));

		return s;
	}

	public static String filterSensor(String s) {

		s = s.substring(s.indexOf(",") + 1);
		s = s.substring(s.indexOf("=") + 1);
		s = s.substring(s.indexOf(",") + 1);
		s = s.substring(s.indexOf("=") + 1);
		s = s.substring(0, s.indexOf(","));

		return s;
	}

	public static String filterData_Hora_Temp(String s) {

		s = s.substring(s.indexOf(",") + 1);
		s = s.substring(s.indexOf(",") + 1);
		s = s.substring(s.indexOf(",") + 1);
		s = s.substring(s.indexOf("=") + 1);
		s = s.substring(0, s.indexOf(","));

		

		return s;
	}

	public static String filterData_Hora_HumeLuz(String s) {

		s = s.substring(s.indexOf(",") + 1);
		s = s.substring(s.indexOf(",") + 1);
		s = s.substring(s.indexOf(",") + 1);
		s = s.substring(s.indexOf("=") + 1);
		s = s.substring(0, s.indexOf(","));

		String s2 = "T";
		String s3 = "Z";
		String s4 = s.replace(s2, " ");
		String s5 = s4.replace(s3, "");

		return s5;
	}

	public static String filterLeitura(String s1) {

		String s= s1.replace('T',' ');
		s = s.substring(s.indexOf(",") + 1);
		s = s.substring(s.indexOf(",") + 1);
		s = s.substring(s.indexOf(",") + 1);
		s = s.substring(s.indexOf(",") + 1);
		s = s.substring(s.indexOf("=") + 1);
		s = s.substring(s.indexOf(" ") + 1);
		s = s.substring(0, s.indexOf("}"));

		return s;
	}
	
	

	

	public static boolean errorControl(String sensor, String leitura) {

		if (Double.parseDouble(leitura) < 100.0 && Double.parseDouble(leitura) > 0) {
			return true;
		}else {
			return false;
		}
	
	

}
}
