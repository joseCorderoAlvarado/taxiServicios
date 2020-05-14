 <?php
include 'conexion.php';
 $nombre=$_POST['nombre'];
 $telefono=$_POST['telefono'];
 $correo=$_POST['correo'];

$actualizardatos ="UPDATE persona SET persona.nombre='$nombre',persona.telefono='$telefono' WHERE correo='$correo'";

 
mysqli_query($conect,$actualizardatos) or die ("error al actualizar cita");
 
 ?>