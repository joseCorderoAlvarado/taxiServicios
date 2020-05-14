
<?php
       include 'conexion.php';
       $correoE=$_POST['correo'];
       $respuesta = array();
       $respuesta['datos'] = array();
       $consultardatos="SELECT nombre,telefono from persona WHERE correo='$correoE'";
$Rconsultardatos= mysqli_query($conect,$consultardatos) or die ("error al consultar los datos");
       while($fila=mysqli_fetch_array($Rconsultardatos))
       {
       $tmp = array();
       $tmp['nombre'] = $fila[0];
       $tmp['telefono'] = $fila[1];
       array_push($respuesta['datos'], $tmp);
       }
       header('Content-Type: application/json');
//Escuchando el resultado de json
echo json_encode($respuesta); 
?>