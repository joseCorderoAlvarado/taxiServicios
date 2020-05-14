-- Seleccionamos la base de datos
use `lavivesh_taxiserviciobd`;

-- Creamos la tabla
create table detalle_taxis_choferes(
taxis_notaxi int,
persona_idpersona int,
primary key (taxis_notaxi, persona_idpersona),
FOREIGN KEY (taxis_notaxi) REFERENCES taxis(notaxi),
FOREIGN KEY (persona_idpersona) REFERENCES persona(idpersona)
) ENGINE=InnoDB;


