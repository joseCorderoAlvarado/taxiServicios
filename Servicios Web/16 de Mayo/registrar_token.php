<?php
    include 'conexion.php';
    $token=$_POST["tokenDispositivo"];
	 $correo=$_POST["correo"];
	
  


    $cosultarExistenciaToken="SELECT persona_idpersona from tokenDispositivo inner join persona P 
	on tokenDispositivo.persona_idpersona=P.idpersona
	where P.correo='$correo'";
	//echo $cosultarExistenciaToken;
    $respuestaConsulta= mysqli_query($conect,$cosultarExistenciaToken) or die ("error al consultar los datos");
	$tablaConsulta=mysqli_fetch_array($respuestaConsulta);

	
	
      
      //$contador = mysqli_num_rows($respuestaConsulta);
      
    echo $contador;
		
      if($contador <1) {
		//  	  echo "NO tiene";
		
		   $consultarIdPersona="SELECT idpersona from persona P 
		 where P.correo='$correo'";
		  // echo $consultarIdPersona;
		       $respuestaConsulta= mysqli_query($conect,$consultarIdPersona) or die ("error al consultar los datos");
				$tablaConsulta=mysqli_fetch_array($respuestaConsulta);
				
		$idPersona=$tablaConsulta[0];
		   
		  
		   $insertarToken="insert into tokenDispositivo(id_token_dispositivo,token,persona_idpersona) values (null,'$token',$idPersona)";
		  // echo $insertarToken;
		    $respuestaConsulta= mysqli_query($conect,$insertarToken) or die ("error al consultar los datos");
	  }
	  else{
		$idPersona=$tablaConsulta[0];
		 $actualizarTokenDispositivo="update tokenDispositivo set token='$token' where persona_idpersona='$idPersona'";
		 $respuestaConsulta= mysqli_query($conect,$actualizarTokenDispositivo) or die ("error al consultar los datos");
		  
		  
		  
		  
	  }
	
	
    
	

			
			
?>