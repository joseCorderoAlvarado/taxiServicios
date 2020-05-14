<?php
include 'conexion.php';
$correo=$_POST['correo'];
$respuesta = array();
$respuesta['servicios'] = array();
$consultarservicios="select 
if ((select nombre from persona as P2 where P2.idpersona=S.persona_idpersona)  is null
or (select nombre from persona as P2 where P2.idpersona=S.persona_idpersona)='', 
'Nombre no registrado',(select nombre from persona as P2 where P2.idpersona=S.persona_idpersona)) as cliente, -- fin columna
S.fecha, S.hora,
if (D.calle IS NULL or D.calle = '', 'No especificado', D.calle) as recoger,
if (S.direccion2 is null or S.direccion2 = '', 'No especificado', S.direccion2) as llevar,
if ((select telefono from persona as P3 where P3.idpersona=S.persona_idpersona) is null
or (select telefono from persona as P3 where P3.idpersona=S.persona_idpersona) ='',
'Teléfono no registrado',(select telefono from persona as P3 where P3.idpersona=S.persona_idpersona)) as telefono -- fin columna
from servicios S
inner join descripcion DS
on DS.servicios_idservicios=S.idservicios
inner join detalle_taxis_choferes DTC
on DS.notaxi = DTC.taxis_notaxi
inner join direccion D
on D.iddireccion=S.direccion_iddireccion
inner join persona P
on DTC.persona_idpersona=P.idpersona
where S.status_idstatus=4
and P.correo='$correo'
ORDER BY S.idservicios DESC";
//echo $consultarservicios;
$Rconsultarservicios= mysqli_query($conect,$consultarservicios) or die ("error al consultar los datos");
  while($fila=mysqli_fetch_array($Rconsultarservicios))
       {
       $tmp = array();
       $tmp['cliente'] = $fila[0];
       $tmp['fecha'] = $fila[1];
       $tmp['hora'] = $fila[2];
       $tmp['recoger'] = $fila[3];
       $tmp['llevar']=$fila[4];
	   $tmp['telefono']=$fila[5];
       array_push($respuesta['servicios'], $tmp);
       }
       header('Content-Type: application/json');
//Escuchando el resultado de json
echo json_encode($respuesta); 
?>