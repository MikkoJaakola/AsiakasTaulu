package control;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import model.Asiakas;
import model.dao.Dao;


@WebServlet("/asiakkaat")
public class asiakkaat extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public asiakkaat() {
        super();
        System.out.println("asiakkaat.asiakkaat()");
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("asiakkaat.doGet()");
		String pathInfo = request.getPathInfo();	//haetaan kutsun polkutiedot			
		System.out.println("polku: "+pathInfo);		
		Dao dao = new Dao();
		 
		ArrayList<Asiakas> asiakkaat;
		String strJSON="";
		if(pathInfo==null) { //Haetaan kaikki asiakkaat
			asiakkaat = dao.listaaKaikki();
			strJSON = new JSONObject().put("asiakkaat", asiakkaat).toString();	
		}else if(pathInfo.indexOf("haeyksi")!=-1) {		//polussa on sana "haeyksi", eli haetaan yhden asiakkaan tiedot
			String asiakas_id = pathInfo.replace("/haeyksi/", ""); //poistetaan polusta "/haeyksi/", j‰ljelle j‰‰ id	
			int i=Integer.parseInt(asiakas_id);
			Asiakas asiakas = dao.etsiAsiakas(i);
			JSONObject JSON = new JSONObject();
			JSON.put("etunimi", asiakas.getEtunimi());
			JSON.put("sukunimi", asiakas.getSukunimi());
			JSON.put("puhelin", asiakas.getPuhelin());
			JSON.put("sposti", asiakas.getSposti());
			JSON.put("asiakas_id", asiakas.getAsiakas_id());	
			strJSON = JSON.toString();		
		}else{ //Haetaan hakusanan mukaiset autot
			String hakusana = pathInfo.replace("/", "");
			asiakkaat = dao.listaaKaikki(hakusana);
			strJSON = new JSONObject().put("asiakkaat", asiakkaat).toString();	
		}	
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(strJSON);
		
		
		/*
		System.out.println("asiakkaat.doGet()");
		Dao dao = new Dao();
		ArrayList<Asiakas> asiakkaat = dao.listaaKaikki();
		System.out.println(asiakkaat);
		String strJSON = new JSONObject().put("asiakkaat", asiakkaat).toString(); 
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(strJSON);
		*/
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("asiakkaat.doPost()");
		JSONObject jsonObj = new JsonStrToObj().convert(request);
		Asiakas asiakas = new Asiakas();
		asiakas.setAsiakas_id(jsonObj.getInt("asiakas_id"));
		asiakas.setEtunimi(jsonObj.getString("etunimi"));
		asiakas.setSukunimi(jsonObj.getString("sukunimi"));
		asiakas.setPuhelin(jsonObj.getInt("puhelin"));
		asiakas.setSposti(jsonObj.getString("sposti"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.lisaaAsiakas(asiakas)){ //metodi palauttaa true/false
			out.println("{\"response\":1}");  
		}else{
			out.println("{\"response\":0}");  
		}
	}

	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doPut()");
		JSONObject jsonObj = new JsonStrToObj().convert(request); //Muutetaan kutsun mukana tuleva json-string json-objektiksi			
		int vanhaid = jsonObj.getInt("vanhaid");
		Asiakas asiakas = new Asiakas();
		asiakas.setAsiakas_id(jsonObj.getInt("asiakas_id"));
		asiakas.setEtunimi(jsonObj.getString("etunimi"));
		asiakas.setSukunimi(jsonObj.getString("sukunimi"));
		asiakas.setPuhelin(jsonObj.getInt("puhelin"));
		asiakas.setSposti(jsonObj.getString("sposti"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.muutaAsiakas(asiakas, vanhaid)){ //metodi palauttaa true/false
			out.println("{\"response\":1}");  //Auton muuttaminen onnistui {"response":1}
		}else{
			out.println("{\"response\":0}");  //Auton muuttaminen ep‰onnistui {"response":0}
		}		
	}
	

	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("asiakkaat.doDelete()");
		
		String pathInfo = request.getPathInfo();			
		System.out.println("polku: "+pathInfo);
		
		String poistettavaId = pathInfo.replace("/", "");
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();	
		int i=Integer.parseInt(poistettavaId); 
		
		if(dao.poistaAsiakas(i)){ 
			out.println("{\"response\":1}");  
		}else{
			out.println("{\"response\":0}");  
		}	
	}

}
