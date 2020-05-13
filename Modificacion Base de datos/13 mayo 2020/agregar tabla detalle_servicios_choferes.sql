-- Seleccionamos la base de datos
use `lavivesh_taxiserviciobd`;

-- Creamos la tabla
create table detalle_servicios_choferes(
servicios_idservicios int,
persona_idpersona int,
primary key (servicios_idservicios, persona_idpersona),
FOREIGN KEY (servicios_idservicios) REFERENCES servicios(idservicios),
FOREIGN KEY (persona_idpersona) REFERENCES persona(idpersona)
) ENGINE=InnoDB;




-- Mostramos solo los usuarios choferes
select * 
from persona P
where P.tipoUsuario_idtipoUsuario=3;
