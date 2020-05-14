 <?php
include 'conexion.php';
$fecha=$_POST["fecha"];
$hora=$_POST["hora"];
$direccion=$_POST['direccion']; //direccion1
$direccion2=$_POST['direccion2']; //direccion2
$refencia=$_POST['referencia'];
$correo=$_POST['correo'];
$dpreliminar="SELECT iddireccion from direccion where calle='$direccion'";
$Rdpreliminar= mysqli_query($conect,$dpreliminar) or die ("error al consultar los datos");
$iddireccionpreliminar=mysqli_fetch_array($Rdpreliminar);
$consultaridUsuario="SELECT idpersona from persona where correo='$correo'";
$RconsultaridUsuario= mysqli_query($conect,$consultaridUsuario) or die ("error al consultar los datos");
$idUsuario=mysqli_fetch_array($RconsultaridUsuario);
$registrarservicio= "INSERT INTO `servicios` (`idservicios`,`fecha`, `hora`,`referencia`,direccion2,persona_idpersona,status_idstatus,direccion_iddireccion)VALUES (NULL, '$fecha', '$hora','$refencia','$direccion2',$idUsuario[0],1,$iddireccionpreliminar[0])";
mysqli_query($conect,$registrarservicio) or die ("<div class='alert alert-dark' role='alert'>
    Ocupas de ingresar una direccion
    </div>"); 
$datosusuario="SELECT nombre FROM persona where idpersona=$idUsuario[0]";
$nombrepersona= mysqli_query($conect,$datosusuario) or die ("error al consultar nombre");
$Rnombrepersona=mysqli_fetch_array($nombrepersona);


			//Notificacion
			    $url = "https://fcm.googleapis.com/fcm/send";
    $token = "eRRaNXEcukQ:APA91bE6d6gcxmLkJG-HPhEUULCk7HJoCQquaSw5-g1fLhsFY4jE5b_2whHE-4-kIKLqROCxg-F_MtQQlqjd2w3xRfbV-KF4RMzpslGifk9--BwHJ8QxPNA9KPFSHNXu9zCFHVf2sN_G";
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



$to = "josehipolito.c.a@gmail.com";
    $subject = "TAXI SERVICIOS";
    $message = "
    <html>
    El cliente $Rnombrepersona[0]  ha solicitado un servicio para el dia de $fecha a las $hora
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

