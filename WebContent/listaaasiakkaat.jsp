<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<title>Asiakastaulu</title>
</head>
<body>

<table id="lista">

	<thead>
		<tr>
			<th colspan="4" class="oikealle"><span id="uusiAsiakas">Lisää uusi asiakas</span></th>
		</tr>
		<tr>
			<th class="oikealle">Hakusana:</th>
			<th colspan="3"><input type="text" id="hakusana"></th>
			<th><input type="button" value="hae" id="hakunappi"></th>
		</tr>
		<tr>
			<th>Id</th>
			<th>Etunimi</th>
			<th>Sukunimi</th>
			<th>Puh.</th>
			<th>Email</th>
		
		</tr>
	
	</thead>
	<tbody>
	</tbody>
</table>
<script>

	$(document).ready(function() {
		
		$("#uusiAsiakas").click(function(){
			document.location="lisaaasiakas.jsp";
		});
		
		$.ajax({
			url:"asiakkaat",
			type:"GET", dataType:"json",
			success:function(result) {
			console.log(result);
			$.each(result.asiakkaat, function(i, field){  
	        	var htmlStr;
	        	htmlStr+="<tr>";
	        	htmlStr+="<td>"+field.asiakas_id+"</td>";
	        	htmlStr+="<td>"+field.etunimi+"</td>";
	        	htmlStr+="<td>"+field.sukunimi+"</td>";
	        	htmlStr+="<td>"+field.puhelin+"</td>";
	        	htmlStr+="<td>"+field.sposti+"</td>";
	        	htmlStr+="<td><span class='poista' onclick=poista('"+field.asiakas_id+"')>Poista</span></td>";
	        	htmlStr+="</tr>";
	        	$("#lista tbody").append(htmlStr);
			});
		}});
	});
	function poista(asiakas_id){
		if(confirm("Poista asiakas " + asiakas_id +"?")){
			$.ajax({url:"asiakkaat/"+asiakas_id, type:"DELETE", dataType:"json", success:function(result) { //result on joko {"response:1"} tai {"response:0"}
		        if(result.response==0){
		        	$("#ilmo").html("Asiakkaan poisto epäonnistui.");
		        }else if(result.response==1){
		        	$("#rivi_"+asiakas_id).css("background-color", "red"); //Värjätään poistetun asiakkaan rivi
		        	alert("Asiakkaan " + asiakas_id +" poisto onnistui.");
					       	
				}
		    }});
		}
	}

</script>
</body>
</html>