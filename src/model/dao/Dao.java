package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Asiakas;

public class Dao { 
	private Connection con=null;
	private ResultSet rs = null;
	private PreparedStatement stmtPrep=null; 
	private String sql;
	private String db ="Myynti.sqlite";
	
	private Connection yhdista(){
    	Connection con = null;    	
    	String path = System.getProperty("catalina.base");    	
    	path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/"); //Eclipsessa
    	//path += "/webapps/"; //Tuotannossa. Laita tietokanta webapps-kansioon
    	String url = "jdbc:sqlite:"+path+db;    	
    	try {	       
    		Class.forName("org.sqlite.JDBC");
	        con = DriverManager.getConnection(url);	
	        System.out.println("Yhteys avattu.");
	     }catch (Exception e){	
	    	 System.out.println("Yhteyden avaus epäonnistui.");
	        e.printStackTrace();	         
	     }
	     return con;
	}
	
	public boolean lisaaAsiakas(Asiakas asiakas){
		boolean paluuArvo=true;
		sql="INSERT INTO asiakas VALUES(?,?,?,?,?)";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setInt(1, asiakas.getAsiakas_id());
			stmtPrep.setString(2, asiakas.getEtunimi());
			stmtPrep.setString(3, asiakas.getSukunimi());
			stmtPrep.setInt(4, asiakas.getPuhelin());
			stmtPrep.setString(5, asiakas.getSposti());
			stmtPrep.executeUpdate();
	        con.close();
		} catch (SQLException e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}
	
	public ArrayList<Asiakas> listaaKaikki(){
		ArrayList<Asiakas> asiakkaat = new ArrayList<Asiakas>();
		sql = "SELECT * FROM asiakas";       
		try {
			con=yhdista();
			if(con!=null){ //jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);        		
        		rs = stmtPrep.executeQuery();   
				if(rs!=null){ //jos kysely onnistui
					//con.close();					
					while(rs.next()){
						Asiakas asiakas = new Asiakas();
						asiakas.setAsiakas_id(rs.getInt(1));
						asiakas.setEtunimi(rs.getString(2));
						asiakas.setSukunimi(rs.getString(3));	
						asiakas.setPuhelin(rs.getInt(4));	
						asiakas.setSposti(rs.getString(5));
						asiakkaat.add(asiakas);
					}					
				}				
			}	
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return asiakkaat;
	}
	
	public ArrayList<Asiakas> listaaKaikki(String hakusana){
		ArrayList<Asiakas> asiakkaat = new ArrayList<Asiakas>();
		sql = "SELECT * FROM asiakas WHERE asiakas_id LIKE ? or etunimi LIKE ? or sukunimi LIKE ?";       
		try {
			con=yhdista();
			if(con!=null){ //jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);  
				stmtPrep.setString(1, "%" + hakusana + "%"); //!!
				stmtPrep.setString(2, "%" + hakusana + "%");   
				stmtPrep.setString(3, "%" + hakusana + "%");   
        		rs = stmtPrep.executeQuery();   
				if(rs!=null){ //jos kysely onnistui							
					while(rs.next()){
						Asiakas asiakas = new Asiakas();
						asiakas.setAsiakas_id(rs.getInt(1));
						asiakas.setEtunimi(rs.getString(2));
						asiakas.setSukunimi(rs.getString(3));	
						asiakas.setPuhelin(rs.getInt(4));	
						asiakas.setSposti(rs.getString(5));
						asiakkaat.add(asiakas);
					}					
				}				
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return asiakkaat;
	}
	
	public Asiakas etsiAsiakas(int asiakas_id){
		Asiakas asiakas = null;
		sql = "SELECT * FROM asiakas WHERE asiakas_id=?";       
		try {
			con=yhdista();
			if(con!=null){ 
				stmtPrep = con.prepareStatement(sql); 
				stmtPrep.setInt(1, asiakas_id);
        		rs = stmtPrep.executeQuery();  
        		if(rs.isBeforeFirst()){ 
        			rs.next();
        			asiakas = new Asiakas();        			
        			asiakas.setAsiakas_id(rs.getInt(1));
					asiakas.setEtunimi(rs.getString(2));
					asiakas.setSukunimi(rs.getString(3));	
					asiakas.setPuhelin(rs.getInt(4));	
					asiakas.setSposti(rs.getString(5));
					  			      			
				}        		
			}	
			con.close();  
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return asiakas;		
	}
	
	public boolean muutaAsiakas(Asiakas asiakas, int asiakas_id){
		boolean paluuArvo=true;
		sql="UPDATE asiakas SET asiakas_id=?, etunimi=?, sukunimi=?, puhelin=?, sposti=?, WHERE asiakas_id=?";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setInt(1, asiakas.getAsiakas_id());
			stmtPrep.setString(2, asiakas.getEtunimi());
			stmtPrep.setString(3, asiakas.getSukunimi());
			stmtPrep.setInt(4, asiakas.getPuhelin());
			stmtPrep.setString(5, asiakas.getSposti());
			stmtPrep.setInt(5, asiakas_id);
			stmtPrep.executeUpdate();
	        con.close();
		} catch (SQLException e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}
	
	
	
	public boolean poistaAsiakas(int asiakas_id){ //Oikeassa elämässä tiedot ensisijaisesti merkitään poistetuksi.
		boolean paluuArvo=true;
		sql="DELETE FROM asiakas WHERE asiakas_id=?";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setInt(1, asiakas_id);			
			stmtPrep.executeUpdate();
	        con.close();
		} catch (SQLException e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}	
	
	public String kirjaudu(String uid, String pwd, String sessionId){
		String paluuArvo="";
		sql="SELECT * FROM kayttajat WHERE email=? AND pwd=?";			
		try {
			con=yhdista();
			if(con!=null){ 
				stmtPrep = con.prepareStatement(sql); 
				stmtPrep.setString(1, uid);
				stmtPrep.setString(2, pwd);
        		rs = stmtPrep.executeQuery();  
        		if(rs.isBeforeFirst()){ //jos kysely tuotti dataa, eli käyttäjä löytyi         			
        			rs.next();
        			paluuArvo=rs.getString(2)+" "+rs.getString(3);	
        			//Lisätään istunnon id tietokantaan
        			sql="INSERT INTO Istunnot(sessionId) VALUES(?)";
        			stmtPrep=con.prepareStatement(sql); 
        			stmtPrep.setString(1, sessionId);			
        			stmtPrep.executeUpdate();        			       			
				}        		
			}	
			con.close();
			suljeVanhatIstunnot();
		} catch (Exception e) {
			e.printStackTrace();
		}				
		return paluuArvo;
	}	
	
	public boolean etsiIstunto(String sessionId){
		boolean paluu=false;
		sql = "SELECT * FROM istunnot WHERE sessionId=?";       
		try {
			con=yhdista();
			if(con!=null){ 
				stmtPrep = con.prepareStatement(sql); 
				stmtPrep.setString(1, sessionId);
        		rs = stmtPrep.executeQuery();  
        		if(rs.isBeforeFirst()){ //jos kysely tuotti dataa, eli rekNo on käytössä
        			paluu=true;        			       			
				}        		
			}	
			con.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return paluu;		
	}
	
	public void suljeIstunto(String sessionId){		
		sql = "DELETE FROM istunnot WHERE sessionId=?";       
		try {
			con=yhdista();
			if(con!=null){ 
				stmtPrep = con.prepareStatement(sql); 
				stmtPrep.setString(1, sessionId);
        		stmtPrep.executeUpdate();        				
			}	
			con.close(); 			
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}
	
	private void suljeVanhatIstunnot(){		
		sql = "DELETE from istunnot WHERE date(timestamp) < date('now','-1 days')"; //Poistetaan kaikki eilistä vanhemmat istunnot      
		try {
			con=yhdista();
			if(con!=null){ 
				stmtPrep = con.prepareStatement(sql);				
        		stmtPrep.executeUpdate();        				
			}	
			con.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}
	
}
