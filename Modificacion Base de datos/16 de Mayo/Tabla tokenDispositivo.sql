use lavivesh_taxiserviciobd;

-- Creamos la tabla
create table tokenDispositivo(
id_token_dispositivo int auto_increment,
token varchar(200),
persona_idpersona int,
PRIMARY KEY (id_token_dispositivo) ,
FOREIGN KEY (persona_idpersona) REFERENCES persona(idpersona)
) ENGINE=InnoDB;

