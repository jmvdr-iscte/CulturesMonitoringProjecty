package pisid;

import java.security.PublicKey;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class alertas extends MongoConnectMySQL{

	static int id_medicao;
	static String id_zona;
	static String data;
	static double leitura;
	static String sensor;
	
	
	static int id_cultura;
	static int id_utilizador;
	static String nome_cultura;
	static double Max_AmareloLuz;
	static double Max_VermelhoLuz;
	static double Max_VermelhoTemperatura;
	static double Max_AmareloTemperatura;
	static double Max_AmareloHumidade;
	static double Max_VermelhoHumidade;
	static double Min_AmareloLuz;
	static double Min_VermelhoLuz;
	static double Min_VermelhoTemperatura;
	static double Min_AmareloTemperatura;
	static double Min_AmareloHumidade;
	static double Min_VermelhoHumidade;
	public static int periodicidadeAmarelo;
	public static int periodicidadeVermelho;
	public Map<Integer, Long> parametros_culturas;
	public Map<Integer, Long> parametros_culturas2;
	
	
	static boolean t_v_start_time_verify=false;
	static boolean t_a_start_time_verify=false;
	static boolean l_v_start_time_verify=false;
	static boolean l_a_start_time_verify=false;
	static boolean h_v_start_time_verify=false;
	static boolean h_a_start_time_verify=false;
	
	
	
	
	static long t_v_start_time=-1;
	static long t_a_start_time=-1;
	static long l_v_start_time=-1;
	static long l_a_start_time=-1;
	static long h_v_start_time=-1;
	static long h_a_start_time=-1;
	
	
	
	public alertas() throws SQLException {
		
		
	}
	
	public void createHashMap_parametrocultura() throws SQLException {

		String query4 = "SELECT * FROM parametrocultura, cultura, utilizador  WHERE cultura.IDCultura = parametrocultura.IDCultura AND cultura.IDUtilizador = utilizador.IDUtilizador";
			
		Statement st4 = Connectionsql.createStatement();

		ResultSet rs4 = st4.executeQuery(query4);
		
		parametros_culturas = new HashMap<>();
		
		while(rs4.next()) {
			int id_cultura2 = rs4.getInt("IDCultura");
			parametros_culturas.put(id_cultura2, (long)-1);
			
		}
		
		parametros_culturas2 = new HashMap<>();
		
		while(rs4.next()) {
			int id_cultura3 = rs4.getInt("IDCultura");
			parametros_culturas2.put(id_cultura3, (long)-1);
			
		}
		
		
	}
	
	public boolean same_zone(String zona, int id_culturaa) throws SQLException {
		String zona_id ="";
		String query_verify = "SELECT IDZona FROM cultura WHERE cultura.IDCultura="+ id_culturaa;
		
		Statement st7 = Connectionsql.createStatement();

		ResultSet rs7 = st7.executeQuery(query_verify);
		
		while (rs7.next()) {
			
			zona_id = rs7.getString("IDZona");
			
		}
		
		
		
		if(id_zona.equals(zona_id))
			return true;
		
		
		return false;
	}
	
	
	
	public void verificar_medicao_alerta(String zona, String hora) throws SQLException, InterruptedException {
		
		createHashMap_parametrocultura();
		
		String query = "SELECT * FROM medicao WHERE (medicao.IDZona ='"+zona+"' AND medicao.Hora ='"+hora+"')";
		
		
		Statement st1 = Connectionsql.createStatement();

		ResultSet rs1 = st1.executeQuery(query);
		
		
		while (rs1.next()) {
		id_medicao = rs1.getInt("IDMedicao");
		id_zona = rs1.getString("IDZona");
		data = rs1.getString("Hora");
		leitura = rs1.getDouble("Leitura");
		sensor = rs1.getString("Sensor");
		}
		
		
		String query1 = "SELECT * FROM parametrocultura, cultura, utilizador  WHERE cultura.IDCultura = parametrocultura.IDCultura AND cultura.IDUtilizador = utilizador.IDUtilizador";
			
		Statement st = Connectionsql.createStatement();

		ResultSet rs = st.executeQuery(query1);

		

		
		
		while (rs.next()) {
			
			id_cultura = rs.getInt("IDCultura");
			
			if(!parametros_culturas.containsKey(id_cultura)) {
				parametros_culturas.put(id_cultura,(long) -1);
			}
			
			if(!parametros_culturas2.containsKey(id_cultura)) {
				parametros_culturas2.put(id_cultura,(long) -1);
			}
		
			
			id_utilizador= rs.getInt("IDUtilizador");
			nome_cultura = rs.getString("NomeCultura");
			Min_AmareloLuz = rs.getDouble("Min_AmareloLuz");
			Max_AmareloLuz = rs.getDouble("Max_AmareloLuz");
			Min_VermelhoLuz = rs.getDouble("Min_VermelhoLuz");
			Max_VermelhoLuz = rs.getDouble("Max_VermelhoLuz");
			Min_AmareloTemperatura = rs.getDouble("Min_AmareloTemperatura");
			Max_AmareloTemperatura = rs.getDouble("Max_AmareloTemperatura");
			Min_VermelhoTemperatura = rs.getDouble("Min_VermelhoTemperatura");
			Max_VermelhoTemperatura = rs.getDouble("Max_VermelhoTemperatura");
			Min_AmareloHumidade = rs.getDouble("Min_AmareloHumidade");
			Max_AmareloHumidade = rs.getDouble("Max_AmareloHumidade");
			Min_VermelhoHumidade = rs.getDouble("Min_VermelhoHumidade");
			Max_VermelhoHumidade = rs.getDouble("Max_VermelhoHumidade");
			
			
			
			periodicidadeAmarelo = rs.getInt("PeriodicidadeAmarelo")*1000;
			periodicidadeVermelho = rs.getInt("PeriodicidadeVermelho")*1000;
			
			
			if (sensor.contains("L")) {
				if (Min_AmareloLuz!=0 && Max_AmareloLuz!=0 && Min_VermelhoLuz!=0 && Max_VermelhoLuz!=0) {
					if(sensor.equals("L1")) { 	
					
				if (leitura < Min_AmareloLuz || leitura > Max_AmareloLuz) {
					if( leitura < Min_VermelhoLuz || leitura > Max_VermelhoLuz) {
						
						
						if(parametros_culturas.get(id_cultura) == -1) {
						
						if(same_zone(id_zona, id_cultura)) {	
						String query_l_v = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
								+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'V', 'Parametros ultrapassaram o limite vermelho', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

						Connectionsql.createStatement().executeUpdate(query_l_v);
						System.out.println("Alerta vermelho no Sensor Luz na cultura: "+nome_cultura+"-------------------------------------------------------");
						parametros_culturas.put(id_cultura, System.currentTimeMillis());
						}
						
						} else {
							
							if(System.currentTimeMillis() - parametros_culturas.get(id_cultura) > periodicidadeVermelho) {
								
								if(same_zone(id_zona, id_cultura)) {
								String query_l_v = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
										+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'V', 'Parametros ultrapassaram o limite vermelho', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

								Connectionsql.createStatement().executeUpdate(query_l_v);
								System.out.println("Alerta vermelho no Sensor Luz na cultura: "+nome_cultura+"-------------------------------------------------------");
								parametros_culturas.put(id_cultura, System.currentTimeMillis());
								}
								
							}
							
						}
						
					} else {
						
						if(parametros_culturas.get(id_cultura) == -1) {
							
							if(same_zone(id_zona, id_cultura)) {
						
						String query_l_a = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
								+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'A', 'Parametros ultrapassaram o limite amarelo', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

						Connectionsql.createStatement().executeUpdate(query_l_a);
						System.out.println("Alerta amarelo no Sensor Luz: "+nome_cultura+"---------------------------------------------------------");
						parametros_culturas.put(id_cultura, System.currentTimeMillis());
							}
						
						} else {
							
							if(System.currentTimeMillis() - parametros_culturas.get(id_cultura) > periodicidadeAmarelo) {
								
								if(same_zone(id_zona, id_cultura)) {
								String query_l_a = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
										+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'A', 'Parametros ultrapassaram o limite amarelo', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

								Connectionsql.createStatement().executeUpdate(query_l_a);
								System.out.println("Alerta amarelo no Sensor Luz: "+nome_cultura+"---------------------------------------------------------");
								parametros_culturas.put(id_cultura, System.currentTimeMillis());
								}
								
							}
							
						}
						
						
					}
				}
				}
					if(sensor.equals("L2")) { 	
						
						if (leitura < Min_AmareloLuz || leitura > Max_AmareloLuz) {
							if( leitura < Min_VermelhoLuz || leitura > Max_VermelhoLuz) {
								
								
								if(parametros_culturas2.get(id_cultura) == -1) {
									if(same_zone(id_zona, id_cultura)) {
								
								String query_l_v = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
										+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'V', 'Parametros ultrapassaram o limite vermelho', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

								Connectionsql.createStatement().executeUpdate(query_l_v);
								System.out.println("Alerta vermelho no Sensor Luz na cultura: "+nome_cultura+"-------------------------------------------------------");
								parametros_culturas2.put(id_cultura, System.currentTimeMillis());
									}
								
								} else {
									
									if(System.currentTimeMillis() - parametros_culturas2.get(id_cultura) > periodicidadeVermelho) {
										if(same_zone(id_zona, id_cultura)) {
										String query_l_v = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
												+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'V', 'Parametros ultrapassaram o limite vermelho', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

										Connectionsql.createStatement().executeUpdate(query_l_v);
										System.out.println("Alerta vermelho no Sensor Luz na cultura: "+nome_cultura+"-------------------------------------------------------");
										parametros_culturas2.put(id_cultura, System.currentTimeMillis());
										}
										
									}
									
								}
								
							} else {
								
								if(parametros_culturas2.get(id_cultura) == -1) {
									if(same_zone(id_zona, id_cultura)) {
								
								String query_l_a = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
										+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'A', 'Parametros ultrapassaram o limite amarelo', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

								Connectionsql.createStatement().executeUpdate(query_l_a);
								System.out.println("Alerta amarelo no Sensor Luz: "+nome_cultura+"---------------------------------------------------------");
								parametros_culturas2.put(id_cultura, System.currentTimeMillis());
									}
								
								} else {
									
									if(System.currentTimeMillis() - parametros_culturas2.get(id_cultura) > periodicidadeAmarelo) {
										if(same_zone(id_zona, id_cultura)) {
										
										String query_l_a = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
												+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'A', 'Parametros ultrapassaram o limite amarelo', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

										Connectionsql.createStatement().executeUpdate(query_l_a);
										System.out.println("Alerta amarelo no Sensor Luz: "+nome_cultura+"---------------------------------------------------------");
										parametros_culturas2.put(id_cultura, System.currentTimeMillis());
										}
										
									}
									
								}
								
								
							}
						}
						}	
			}
			}
			
			
			
			
			if (sensor.contains("T")) {
				if (Min_AmareloTemperatura!=0 && Max_AmareloTemperatura!=0 && Min_VermelhoTemperatura!=0 && Max_VermelhoTemperatura!=0) {
				if(sensor.equals("T1")) {
					
				if (leitura < Min_AmareloTemperatura || leitura > Max_AmareloTemperatura) {
					if( leitura < Min_VermelhoTemperatura || leitura > Max_VermelhoTemperatura) {
						
						if(parametros_culturas.get(id_cultura) == -1) {
						
						
							
							
						if(same_zone(id_zona, id_cultura)) {
						
						
							
							
						String query_t_v = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
								+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', "+leitura+", 'V', 'Parametros ultrapassaram os limites vermelho', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

						Connectionsql.createStatement().executeUpdate(query_t_v);
						System.out.println("Alerta vermelho no Sensor Temperatura" +nome_cultura+"-----------------------------------------------");
						
						parametros_culturas.put(id_cultura, System.currentTimeMillis());
						}
						
						} else {
								
							if(System.currentTimeMillis() - parametros_culturas.get(id_cultura) > periodicidadeVermelho) {
								
								if(same_zone(id_zona, id_cultura)) {
								String query_t_v = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
										+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', "+leitura+", 'V', 'Parametros ultrapassaram os limites vermelho', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

								Connectionsql.createStatement().executeUpdate(query_t_v);
								System.out.println("Alerta vermelho no Sensor Temperatura na cultura: "+nome_cultura+"-------------------------------------");
								parametros_culturas.put(id_cultura,System.currentTimeMillis());
								}
							}
							
							
						}
						
						
						
					} else {
						
						if(parametros_culturas.get(id_cultura) == -1) {
							
							if(same_zone(id_zona, id_cultura)) {
						String query_t_a = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
								+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', "+leitura+", 'A', 'Parametros ultrapassaram os limite amarelo', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

						Connectionsql.createStatement().executeUpdate(query_t_a);
						System.out.println("Alerta amarelo no Sensor Temperatura na cultura: "+nome_cultura+"-----------------------------------------------------------------------");
						parametros_culturas.put(id_cultura,System.currentTimeMillis());
							}
						
						} else {
							if(System.currentTimeMillis() - parametros_culturas.get(id_cultura) > periodicidadeAmarelo) {
								
								if(same_zone(id_zona, id_cultura)) {
								String query_t_a = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
										+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', "+leitura+", 'A', 'Parametros ultrapassaram os limite amarelo', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

								Connectionsql.createStatement().executeUpdate(query_t_a);
								System.out.println("Alerta amarelo no Sensor Temperatura na cultura: "+nome_cultura+"--------------------------------------------------------------------");
								parametros_culturas.put(id_cultura,System.currentTimeMillis());
								}
								
							}
							
							
						}
						
					}
				}
				
			}  
				
				if(sensor.equals("T2")) {
					
					if (leitura < Min_AmareloTemperatura || leitura > Max_AmareloTemperatura) {
						if( leitura < Min_VermelhoTemperatura || leitura > Max_VermelhoTemperatura) {
							
							if(parametros_culturas2.get(id_cultura) == -1) {
								
								if(same_zone(id_zona, id_cultura)) {
								
							String query_t_v = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
									+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', "+leitura+", 'V', 'Parametros ultrapassaram os limites vermelho', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

							Connectionsql.createStatement().executeUpdate(query_t_v);
							System.out.println("Alerta vermelho no Sensor Temperatura" +nome_cultura+"-----------------------------------------------");
							
							parametros_culturas2.put(id_cultura, System.currentTimeMillis());
								}
							
							} else {
									
								if(System.currentTimeMillis() - parametros_culturas2.get(id_cultura) > periodicidadeVermelho) {
									
									if(same_zone(id_zona, id_cultura)) {
									
									String query_t_v = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
											+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', "+leitura+", 'V', 'Parametros ultrapassaram os limites vermelho', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

									Connectionsql.createStatement().executeUpdate(query_t_v);
									System.out.println("Alerta vermelho no Sensor Temperatura na cultura: "+nome_cultura+"-------------------------------------");
									parametros_culturas2.put(id_cultura,System.currentTimeMillis());
									
									}
								}
								
								
							}
							
							
							
						} else {
							
							if(parametros_culturas2.get(id_cultura)  == -1) {
								
								if(same_zone(id_zona, id_cultura)) {
							String query_t_a = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
									+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', "+leitura+", 'A', 'Parametros ultrapassaram os limite amarelo', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

							Connectionsql.createStatement().executeUpdate(query_t_a);
							System.out.println("Alerta amarelo no Sensor Temperatura na cultura: "+nome_cultura+"-----------------------------------------------------------------------");
							parametros_culturas2.put(id_cultura, System.currentTimeMillis());
							
								}
							
							} else {
								if(System.currentTimeMillis() - parametros_culturas2.get(id_cultura) > periodicidadeAmarelo) {
									
									if(same_zone(id_zona, id_cultura)) {
									String query_t_a = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
											+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', "+leitura+", 'A', 'Parametros ultrapassaram os limite amarelo', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

									Connectionsql.createStatement().executeUpdate(query_t_a);
									System.out.println("Alerta amarelo no Sensor Temperatura na cultura: "+nome_cultura+"--------------------------------------------------------------------");
									parametros_culturas2.put(id_cultura, System.currentTimeMillis());
									}
									
								}
								
								
							}
							
						}
					}
				}
				
				
				}
				
				
			}
			
			
			if (sensor.contains("H")) {
				if (Min_AmareloHumidade!=0 && Max_AmareloHumidade!=0 && Min_VermelhoHumidade!=0 && Max_VermelhoHumidade!=0) {
					
					if(sensor.contains("H1")) {
				
				if (leitura < Min_AmareloHumidade|| leitura > Max_AmareloHumidade) {
					if( leitura < Min_VermelhoHumidade || leitura > Max_VermelhoHumidade) {
						
						if(parametros_culturas.get(id_cultura) == -1) {
							
							if(same_zone(id_zona, id_cultura)) {
						
						String query_h_v = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
								+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'V', 'Parametros ultrapassaram o limite vermelho', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

						Connectionsql.createStatement().executeUpdate(query_h_v);
						System.out.println("Alerta vermelho no Sensor Humidade na cultura: "+nome_cultura+"--------------------------------------------------");
						parametros_culturas.put(id_cultura,System.currentTimeMillis());
							}
						
						} else {
							if(System.currentTimeMillis() - parametros_culturas.get(id_cultura) > periodicidadeVermelho) {
								if(same_zone(id_zona, id_cultura)) {
								String query_h_v = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
										+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'V', 'Parametros ultrapassaram o limite vermelho', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

								Connectionsql.createStatement().executeUpdate(query_h_v);
								System.out.println("Alerta vermelho no Sensor Humidade na cultura: "+nome_cultura+"--------------------------------------------------");
								parametros_culturas.put(id_cultura,System.currentTimeMillis());
								}
								
								
							}
							
						}
						
						
					} else {
						
						if(parametros_culturas.get(id_cultura) == -1) {
							if(same_zone(id_zona, id_cultura)) {
						String query_h_a = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
								+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'A', 'Parametros ultrapassaram o limite amarelo', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

						Connectionsql.createStatement().executeUpdate(query_h_a);
						System.out.println("Alerta amarelo no Sensor Humidade na cultura: " +nome_cultura+"--------------------------------------------------");
						parametros_culturas.put(id_cultura,System.currentTimeMillis());
							}
						
						} else {
							if(System.currentTimeMillis() - parametros_culturas.get(id_cultura) > periodicidadeAmarelo) {
								if(same_zone(id_zona, id_cultura)) {
								String query_h_a = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
										+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'A', 'Parametros ultrapassaram o limite amarelo', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

								Connectionsql.createStatement().executeUpdate(query_h_a);
								System.out.println("Alerta amarelo no Sensor Humidade na cultura: " +nome_cultura+"--------------------------------------------------");
								parametros_culturas.put(id_cultura,System.currentTimeMillis());
								}
								
							}
							
						}
						
						
					}
				}
				}
					
					if(sensor.contains("H2")) {
						
						if (leitura < Min_AmareloHumidade|| leitura > Max_AmareloHumidade) {
							if( leitura < Min_VermelhoHumidade || leitura > Max_VermelhoHumidade) {
								
								if(parametros_culturas2.get(id_cultura) == -1) {
									if(same_zone(id_zona, id_cultura)) {
								
								String query_h_v = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
										+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'V', 'Parametros ultrapassaram o limite vermelho', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

								Connectionsql.createStatement().executeUpdate(query_h_v);
								System.out.println("Alerta vermelho no Sensor Humidade na cultura: "+nome_cultura+"--------------------------------------------------");
								parametros_culturas2.put(id_cultura, System.currentTimeMillis());
									}
								
								} else {
									if(System.currentTimeMillis() - parametros_culturas2.get(id_cultura) > periodicidadeVermelho) {
										if(same_zone(id_zona, id_cultura)) {
										String query_h_v = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
												+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'V', 'Parametros ultrapassaram o limite vermelho', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

										Connectionsql.createStatement().executeUpdate(query_h_v);
										System.out.println("Alerta vermelho no Sensor Humidade na cultura: "+nome_cultura+"--------------------------------------------------");
										parametros_culturas2.put(id_cultura, System.currentTimeMillis());
										}
										
										
									}
									
								}
								
								
							} else {
								
								if(parametros_culturas2.get(id_cultura) == -1) {
									if(same_zone(id_zona, id_cultura)) {
								String query_h_a = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
										+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'A', 'Parametros ultrapassaram o limite amarelo', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

								Connectionsql.createStatement().executeUpdate(query_h_a);
								System.out.println("Alerta amarelo no Sensor Humidade na cultura: " +nome_cultura+"--------------------------------------------------");
								parametros_culturas2.put(id_cultura, System.currentTimeMillis());
									}
								
								} else {
									if(System.currentTimeMillis() - parametros_culturas2.get(id_cultura) > periodicidadeAmarelo) {
										if(same_zone(id_zona, id_cultura)) {
										
										String query_h_a = "INSERT INTO alerta (IDZona, Sensor, Hora, Leitura, TipoAlerta, Mensagem, NomeCultura, IDUtilizador,IDCultura, IDMedicao)"
												+ "VALUES ('"+id_zona+"', '"+sensor+"', '"+data+"', '"+leitura+"', 'A', 'Parametros ultrapassaram o limite amarelo', '"+nome_cultura+"', "+id_utilizador+", "+id_cultura+", "+id_medicao+")";

										Connectionsql.createStatement().executeUpdate(query_h_a);
										System.out.println("Alerta amarelo no Sensor Humidade na cultura: " +nome_cultura+"--------------------------------------------------");
										parametros_culturas2.put(id_cultura, System.currentTimeMillis());
										}
										
									}
									
								}
								
								
							}
						}
						}
					
			}
				
			}
		
			
			
			
			
		}
		
		
		
		
		
	}
}
