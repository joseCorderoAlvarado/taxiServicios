<?php
include 'conexion.php';
$correo=$_POST['correo'];
$respuesta = array();
$respuesta['servicios'] = array();
$consultarservicios="select 
if ((select nombre from persona as P2 where P2.idpersona=S.persona_idpersona) is null 
or (select nombre from persona as P2 where P2.idpersona=S.persona_idpersona)='',
'Nombre no registrado', (select nombre from persona as P2 where P2.idpersona=S.persona_idpersona))
as cliente, -- fin columna
if (D.evaluacion is null or D.evaluacion='', 'Sin asignar', D.evaluacion)as evaluacion,
if (D.nota is null or D.nota='', 'Sin comentarios', D.nota) as nota,
S.fecha, S.hora
from servicios S 
inner join descripcion D
on D.servicios_idservicios=S.idservicios
inner join detalle_taxis_choferes DTC
on D.notaxi = DTC.taxis_notaxi
inner join persona P
on DTC.persona_idpersona=P.idpersona
where S.status_idstatus=3
and P.correo='$correo'
ORDER BY S.idservicios DESC";
//echo $consultarservicios;
$Rconsultarservicios= mysqli_query($conect,$consultarservicios) or die ("error al consultar los datos");
  while($fila=mysqli_fetch_array($Rconsultarservicios))
       {
       $tmp = array();
       $tmp['cliente'] = $fila[0];
       $tmp['evaluacion'] = $fila[1];
       $tmp['nota'] = $fila[2];
       $tmp['fecha'] = $fila[3];
       $tmp['hora']=$fila[4];
       array_push($respuesta['servicios'], $tmp);
       }
       header('Content-Type: application/json');
//Escuchando el resultado de json
echo json_encode($respuesta); 
?>