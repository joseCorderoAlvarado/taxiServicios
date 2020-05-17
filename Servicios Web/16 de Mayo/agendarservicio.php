<?php
    include 'conexion.php';
    $fecha=$_POST["fecha"];
	
    
    $hora=$_POST["hora"];

	$direccion=$_POST["direccion"];

    $direccion2=$_POST['direccion2'];
    $d1=$_POST['d1'];
    $d2=$_POST['d2'];
    $refencia=$_POST['referencia'];
    $correo=$_POST['correo'];
    $consultaridUsuario="SELECT idpersona from persona where correo='$correo'";
    $RconsultaridUsuario= mysqli_query($conect,$consultaridUsuario) or die ("error al consultar los datos");
    $idUsuario=mysqli_fetch_array($RconsultaridUsuario);
      $dpreliminar="SELECT iddireccion from direccion where calle='$direccion'";
    $Rdpreliminar= mysqli_query($conect,$dpreliminar) or die ("error al consultar los datos");
    $iddireccionpreliminar=mysqli_fetch_array($Rdpreliminar);
	
$registrardireccion = "INSERT INTO `direccion` (`iddireccion`, `calle`, `numero`, `colonia`) VALUES (NULL, '$d1', ' ', '$d2')";
        mysqli_query($conect,$registrardireccion) or die ("error al registrar");
        $consultariddireccion="SELECT MAX(iddireccion) AS iddireccion FROM direccion";
        $Rconsultariddireccion= mysqli_query($conect,$consultariddireccion) or die ("error al consultar los datos");
        $iddireccion=mysqli_fetch_array($Rconsultariddireccion);
        $registrarservicio= "INSERT INTO `servicios` (`idservicios`, `fecha`, `hora`,`referencia`,
        `direccion2`,`persona_idpersona`, `status_idstatus`,`direccion_iddireccion`) VALUES
         (NULL, '$fecha', '$hora','$refencia','$d2' ,$idUsuario[0],1,$iddireccion[0])";
        mysqli_query($conect,$registrarservicio) or die ("error al registrarservicio");
        $datosusuario="SELECT nombre FROM persona where idpersona=$idUsuario[0]";
            $nombrepersona= mysqli_query($conect,$datosusuario) or die ("error al consultar nombre");
            $Rnombrepersona=mysqli_fetch_array($nombrepersona);
			
			
					//Notificaciones
					
					
				$consultarToken="SELECT token from tokenDispositivo inner join persona P 
	on tokenDispositivo.persona_idpersona=P.idpersona
	where P.tipoUsuario_idtipoUsuario=1";
	
	// echo $consultarToken;
	
    $respuestaConsulta = mysqli_query($conect,$consultarToken) or die ("error al consultar los datos");
   
	
	while($tablaConsulta = mysqli_fetch_array($respuestaConsulta)) {
	    
		$tokenMandar=$tablaConsulta[0];
//echo $tokenMandar;

		
		
					
			    $url = "https://fcm.googleapis.com/fcm/send";
    $token = $tokenMandar;
    $serverKey = "AAAAXUTjKWQ:APA91bGG-gEZS6I0cpp6_bXVPtGhMTULeQworcUNugZyUb1-lJksLcC9fRlBGHcB1d6RXWEbvWQhd78GXg1oQacRChlpdxg9tqxZr5u1PAj3637l1aNfrqiZ5UsY9stIC1ygxtfcwcXN";
    $title = "Servicio Nuevo";
    $body = "$Rnombrepersona[0] ha solicitado un servicio para el dia de $fecha a las $hora";
    $notification = array('title' =>$title , 'body' => $body, 'sound' => 'default', 'badge' => '1');
    $arrayToSend = array('to' => $token, 'notification' => $notification,'priority'=>'high');
    $json = json_encode($arrayToSend);
    $headers = array();
    $headers[] = 'Content-Type: application/json';
    $headers[] = 'Authorization: key='. $serverKey;
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST,"POST");
    curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
    curl_setopt($ch, CURLOPT_HTTPHEADER,$headers);
    //Send the request
    $response = curl_exec($ch);
    //Close request
    if ($response === FALSE) {
    die('FCM Send Error: ' . curl_error($ch));
    }
    curl_close($ch);
	
			}//fin while
			
			
			//Correo
            $to = "josehipolito.c.a@gmail.com";
            $subject = "TAXI SERVICIOS";
            $message = "
            <html>
            El cliente $Rnombrepersona[0] ha solicitado un servicio para el dia de $fecha a las $hora
            desde la pagina de www.pidetaxiservicios.com
            <img src='https://pidetaxiservicios.com/images/logo.jpg'>
            </html>";
            $headers  = 'MIME-Version: 1.0' . "\r\n";
            $headers .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
            $headers .= "From: $correo";
            mail($to, $subject, $message, $headers);
            $para="josehipolito.c.a@gmail.com";
            mail($para, $subject, $message, $headers);
?>