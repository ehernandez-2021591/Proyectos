Drop Database if exists DBSonricitas2021591;
Create database DBSonricitas2021591;

Use DBSonricitas2021591;

CREATE TABLE Pacientes(
   codigoPaciente int NOT NULL,
   nombrePaciente varchar(50) NOT NULL,
   apellidosPaciente varchar(50) NOT NULL,
   sexo char(1) NOT NULL,
   fechaNacimiento date NOT NULL,
   direccionPaciente varchar(100) NOT NULL,
   telefonoPersonal varchar(50) NOT NULL,
   fechaPrimeraVisita date DEFAULT NULL,
   PRIMARY KEY PK_codigoPaciente(codigoPaciente)
 );
 
 CREATE TABLE Especialidades(
   codigoEspecialidad int NOT NULL AUTO_INCREMENT,
   descripcion varchar(100) NOT NULL,
   PRIMARY KEY PK_codigoEspecialidad(codigoEspecialidad)
 );
 
 CREATE TABLE Medicamentos (
   codigoMedicamento int NOT NULL AUTO_INCREMENT,
   nombreMedicamento varchar(100) NOT NULL,
   PRIMARY KEY PK_codigoMedicamento(codigoMedicamento)
 );
 CREATE TABLE Doctores (
   numeroColegiado int NOT NULL,
   nombresDoctor varchar(50) NOT NULL,
   apellidosDoctor varchar(100) NOT NULL,
   telefonoContacto varchar(8) NOT NULL,
   codigoEspecialidad int NOT NULL,
   PRIMARY KEY PK_numeroColegiado(numeroColegiado),
   constraint FK_Doctores_Especialidades foreign key(codigoEspecialidad)
    REFERENCES Especialidades (codigoEspecialidad)
 );
 CREATE TABLE Recetas (
   codigoReceta int NOT NULL AUTO_INCREMENT,
   fechaReceta date NOT NULL,
   numeroColegiado int NOT NULL,
   PRIMARY KEY PK_codigoReceta(codigoReceta),
   constraint FK_Recetas_Doctores foreign key(numeroColegiado)
   REFERENCES Doctores (numeroColegiado)
 );
 
 CREATE TABLE Detallereceta (
   codigoDetalleReceta int NOT NULL AUTO_INCREMENT,
   dosis varchar(100) NOT NULL,
   codigoReceta int NOT NULL,
   codigoMedicamento int NOT NULL,
   PRIMARY KEY PK_co(codigoDetalleReceta),
   constraint FK_DetalleReceta_Recetas FOREIGN KEY (codigoReceta) references
   recetas (codigoReceta),
   constraint FK_DetalleReceta_Medicamentos  foreign key (codigoMedicamento) references 
   medicamentos (codigoMedicamento)
 ) ;
 
 CREATE TABLE Citas (
   codigoCita int NOT NULL AUTO_INCREMENT,
   fechaCita date NOT NULL,
   horaCita time NOT NULL,
   tratamiento varchar(150) NOT NULL,
   descripCondActual varchar(255) NOT NULL,
   codigoPaciente int NOT NULL,
   numeroColegiado int NOT NULL,
   PRIMARY KEY  PK_codigoCita(codigoCita),
   Constraint FK_Citas_Pacientes FOREIGN KEY (codigoPaciente) REFERENCES pacientes (codigoPaciente),
   Constraint FK_Citas_Doctores FOREIGN KEY (numeroColegiado) REFERENCES doctores (numeroColegiado)
   ); 
 
 /*-------------------------------Procedimientos Almacenados-------------------------*/
 /*-------------------------------Pacientes----------------------------*/
/*------------------------------Agregar Pacientes----------------------------*/

	Delimiter $$
		Create Procedure sp_AgregarPaciente(in codigoPaciente int,in nombrePaciente Varchar(100),
        in apellidosPaciente Varchar(100),in sexo char,in fechaNacimiento date,
        in direccionPaciente Varchar(100),in telefonoPersonal Varchar(10), in fechaPrimeraVisita date)
			Begin
				Insert into Pacientes(codigoPaciente,nombrePaciente,apellidosPaciente,sexo,
                fechaNacimiento,direccionPaciente,telefonoPersonal,fechaPrimeraVisita)
                Values (codigoPaciente,nombrePaciente,apellidosPaciente,sexo,
                fechaNacimiento,direccionPaciente,telefonoPersonal,fechaPrimeraVisita);
                End $$
    Delimiter ;
	Call sp_AgregarPaciente(1,'Eduardo Andre','Hernandez Carranza','M','2004-11-17','Zona 6','45098721',
    '2004-11-17');
    /*------------------------------Listar Pacientes----------------------------*/
    Delimiter $$
		Create Procedure sp_ListarPacientes()
        Begin
			Select
            P.codigoPaciente,
            P.nombrePaciente,
            P.apellidosPaciente,
            P.sexo,
            P.fechaNacimiento,
            P.direccionPaciente,
            P.telefonoPersonal,
            P.fechaPrimeraVisita
            From Pacientes P;
            End $$
    Delimiter ;
    
    Call sp_ListarPacientes();
  /*------------------------------Buscar Pacientes----------------------------*/
   Delimiter $$
		Create Procedure sp_BuscarPacientes(in codPaciente int)
        Begin
			Select
            P.codigoPaciente,
            P.nombrePaciente,
            P.apellidosPaciente,
            P.sexo,
            P.fechaNacimiento,
            P.direccionPaciente,
            P.telefonoPersonal,
            P.fechaPrimeraVisita
            From Pacientes P
				where codigoPaciente=codPaciente;
            End $$
    Delimiter ;
    
    Call sp_BuscarPacientes(3);
  
  /*------------------------------Eliminar Pacientes----------------------------*/
   Delimiter $$
		Create Procedure sp_EliminarPacientes(in codPaciente int)
        Begin
			delete From Pacientes
				where codigoPaciente=codPaciente;
            End $$
    Delimiter ;
    
    Call sp_EliminarPacientes(1);
  
  /*------------------------------Editar Pacientes----------------------------*/
 
 Delimiter $$
	Create Procedure sp_EditarPaciente(in codPaciente int,in nomPaciente Varchar(100),
    in apePaciente Varchar(100),in sex char,in fecNacimiento date,
        in dirPaciente Varchar(100),in telPersonal Varchar(10), in fechaPV date)
        Begin 
        Update Pacientes P
        SET
            P.nombrePaciente=nomPaciente,
            P.apellidosPaciente=apePaciente,
            P.sexo=sex,
            P.fechaNacimiento=fecNacimiento,
            P.direccionPaciente=dirPaciente,
            P.telefonoPersonal=telPersonal,
            P.fechaPrimeraVisita=fechaPV
            WHERE codigoPaciente=codPaciente;
        End $$
        Delimiter ;
       Call sp_EditarPaciente(1,'Estuardo Andre','Hernandez Carranza','M','2004-11-17','Zona 6','45098721',
    '2004-11-17'); 
    
    /*------------------------------Especialidades-------------------*/
	/*------------------------------Agregar Especialidades-------------------*/
    Delimiter $$
		Create Procedure sp_AgregarEspecialidad(in codigoEspecialidad int,in descripcion Varchar(100))
			Begin
				Insert into Especialidades( codigoEspecialidad , descripcion )
                Values ( codigoEspecialidad , descripcion );
                End $$
    Delimiter ;
	Call sp_AgregarEspecialidad(1,'Cirujano');
    /*------------------------------Listar Especialidades-------------------*/
    Delimiter $$
		Create Procedure sp_ListarEspecialidad()
        Begin
			Select
            E.codigoEspecialidad,
            E.descripcion
			From Especialidades E;
            End $$
    Delimiter ;
    
    Call sp_ListarEspecialidad();
    /*------------------------------BuscarEspecialidades-------------------*/
    Delimiter $$
		Create Procedure sp_BuscarEspecialidad(in codEspecialidad int)
        Begin
			Select
            E.codigoEspecialidad,
            E.descripcion
			From Especialidades E
            where codEspecialidad=codigoEspecialidad;
            End $$
    Delimiter ;
    
    Call sp_BuscarEspecialidad(1);
    /*------------------------------Eliminar Especialidades-------------------*/
     Delimiter $$
		Create Procedure sp_EliminarEspecialidad(in codEspecialidad int)
        Begin
			Delete
			From Especialidades 
            where codEspecialidad=codigoEspecialidad;
            End $$
    Delimiter ;
    
    Call sp_EliminarEspecialidad(1);
    /*------------------------------Editar Especialidades-------------------*/
	Delimiter $$
	Create Procedure sp_EditarEspecialidad(in codEspecialidad int,in descr Varchar(100))
        Begin 
        Update Especialidades E
        SET
            E.descripcion=descr;
        End $$
        Delimiter ;
       Call sp_EditarEspecialidad(1,'Cardiologo'); 
	/*-----------------------------Medicamentos--------------------------*/
	/*-----------------------------Agregar Medicamentos--------------------------*/
     Delimiter $$
		Create Procedure sp_AgregarMedicamento(in codigoMedicamento int,in nombreMedicamento Varchar(100))
			Begin
				Insert into Medicamentos( codigoMedicamento , nombreMedicamento)
                Values ( codigoMedicamento , nombreMedicamento);
                End $$
    Delimiter ;
	Call sp_AgregarMedicamento(1,'Ibuprofeno');
	/*-----------------------------Listar Medicamentos--------------------------*/
    Delimiter $$
		Create Procedure sp_ListarMedicamentos()
        Begin
			Select
            M.codigoMedicamento,
            M.nombreMedicamento
			From Medicamentos M;
            End $$
    Delimiter ;
    
    Call sp_ListarMedicamentos();
	/*-----------------------------Buscar Medicamentos--------------------------*/
    Delimiter $$
		Create Procedure sp_BuscarMedicamentos(in codMedicamento int)
        Begin
			Select
            M.codigoMedicamento,
            M.nombreMedicamento
			From Medicamentos M
            where codMedicamento=codigoMedicamento;
            End $$
    Delimiter ;
    
    Call sp_BuscarMedicamentos(1);
    /*-----------------------------EliminarMedicamentos--------------------------*/
 Delimiter $$
		Create Procedure sp_EliminarMedicamentos(in codMedicamento int)
        Begin
			Delete
			From Medicamentos 
            where codMedicamento=codigoMedicamento;
            End $$
    Delimiter ;
    
    Call sp_EliminarMedicamentos(1);
    /*----------------------------Doctores-------------------------------*/
	/*----------------------------Agregar Doctores-------------------------------*/
    Delimiter $$
		Create Procedure sp_AgregarDoctor(in numeroColegiado int,in nombresDoctor Varchar(100),
       in apellidosDoctor Varchar (100),in telefonoContacto Varchar (100),in codigoEspecialidad
       Varchar(100))
			Begin
				Insert into Doctores( numeroColegiado , nombresDoctor,apellidosDoctor,telefonoContacto,codigoEspecialidad)
                Values ( numeroColegiado , nombresDoctor,apellidosDoctor,telefonoContacto,codigoEspecialidad);
                End $$
    Delimiter ;
	Call sp_AgregarDoctor(1,'Roberto','Hernandez','23242464','1');
	/*----------------------------Listar Doctores-------------------------------*/
     Delimiter $$
		Create Procedure sp_ListarDoctores()
        Begin
			Select
            D.numeroColegiado,
            D.nombresDoctor,
            D.apellidosDoctor,
            D.telefonoContacto,
            D.codigoEspecialidad
			From Doctores D;
            End $$
    Delimiter ;
    
    Call sp_ListarDoctores();
    
	/*----------------------------Buscar Doctores-------------------------------*/ 
     Delimiter $$
		Create Procedure sp_BuscarDoctores(in numColegiado int)
        Begin
			Select
            D.numeroColegiado,
            D.nombresDoctor,
            D.apellidosDoctor,
            D.telefonoContacto,
            D.codigoEspecialidad
			From Doctores D
            where numColegiado=numeroColegiado;
            End $$
    Delimiter ;
    
    Call sp_BuscarDoctores(1);
    
	/*----------------------------Eliminar Doctores-------------------------------*/   
    Delimiter $$
		Create Procedure sp_EliminarDoctores(in numColegiado int)
        Begin
			Delete
			From Doctores 
            where numColegiado=numeroColegiado;
            End $$
    Delimiter ;
    
    Call sp_EliminarDoctores(1);
    
    /*----------------------------Recetas--------------------------*/
	/*----------------------------Agregar Recetas--------------------------*/
     Delimiter $$
		Create Procedure sp_AgregarReceta(in codigoReceta int,in fechaReceta date,
       in numeroColegiado int)
			Begin
				Insert into Recetas( codigoReceta , fechaReceta,numeroColegiado)
                Values ( codigoReceta , fechaReceta,numeroColegiado);
                End $$
    Delimiter ;
	Call sp_AgregarReceta(1,'2021-04-17',1);
    /*----------------------------Listar Recetas--------------------------*/
    Delimiter $$
		Create Procedure sp_ListarRecetas()
        Begin
			Select
            R.codigoReceta,
            R.fechaReceta,
            R.numeroColegiado
			From Recetas R;
            End $$
    Delimiter ;
    
    Call sp_ListarRecetas();
    /*----------------------------Buscar Recetas--------------------------*/
     Delimiter $$
		Create Procedure sp_BuscarRecetas(in codReceta int )
        Begin
			Select
            R.codigoReceta,
            R.fechaReceta,
            R.numeroColegiado
			From Recetas R
            where codReceta=codigoReceta;
            End $$
    Delimiter ;
    
    Call sp_BuscarRecetas(1);
    /*----------------------------Eliminar Recetas--------------------------*/
Delimiter $$
		Create Procedure sp_EliminarRecetas(in codReceta int )
        Begin
			Delete
			From Recetas 
            where codReceta=codigoReceta;
            End $$
    Delimiter ;
    
    Call sp_EliminarRecetas(1);
    /*---------------------------Detalle Receta----------------------------*/
    /*---------------------------Agregar Detalle Receta----------------------------*/
    Delimiter $$
		Create Procedure sp_AgregarDetalleReceta(in codigoDetalleReceta int,in dosis Varchar(100),
       in codigoReceta int,in codigoMedicamento int)
			Begin
				Insert into detalleReceta( codigoDetalleReceta , dosis,codigoReceta,codigoMedicamento)
                Values ( codigoDetalleReceta , dosis,codigoReceta,codigoMedicamento);
                End $$
    Delimiter ;
	Call sp_AgregarDetalleReceta(1,'dos',1,1);
    /*---------------------------Listar Detalle Receta----------------------------*/
    Delimiter $$
		Create Procedure sp_ListarDetalleRecetas()
        Begin
			Select
            D.codigoDetalleReceta,
            D.dosis,
            D.codigoReceta,
            D.codigoMedicamento
			From detalleReceta D;
            End $$
    Delimiter ;
    
    Call sp_ListarDetalleRecetas();

    /*---------------------------Buscar Detalle Receta----------------------------*/
     Delimiter $$
		Create Procedure sp_BuscarDetalleRecetas(in codDetalleReceta int)
        Begin
			Select
            D.codigoDetalleReceta,
            D.dosis,
            D.codigoReceta,
            D.codigoMedicamento
			From detalleReceta D
            where codDetalleReceta=codigoDetalleReceta;
            End $$
    Delimiter ;
    
    Call sp_BuscarDetalleRecetas(1);
    /*---------------------------Eliminar Detalle Receta----------------------------*/
    Delimiter $$
		Create Procedure sp_EliminarDetalleRecetas(in codDetalleReceta int)
        Begin
			Delete
			From detalleReceta 
            where codDetalleReceta=codigoDetalleReceta;
            End $$
    Delimiter ;
    
    Call sp_EliminarDetalleRecetas(1);
    
    /*-------------------------------Citas-----------------------------*/
    /*-------------------------------Agregar Citas-----------------------------*/
    Delimiter $$
		Create Procedure sp_AgregarCita(in codigoCita int,in fechaCita date,
       in horaCita time,in tratamiento varchar(150),in descripCondActual varchar(150),
       in codigoPaciente int ,in numeroColegiado int)
			Begin
				Insert into Citas( codigoCita , fechaCita,horaCita,tratamiento,descripCondActual
                ,codigoPaciente,numeroColegiado)
                Values ( codigoCita , fechaCita,horaCita,tratamiento,descripCondActual
                ,codigoPaciente,numeroColegiado);
                End $$
    Delimiter ;
	Call sp_AgregarCita(1,'2022-08-23','14:09:08','tres','saludable',1,1);
    /*------------------------------- Listar Citas-----------------------------*/
    Delimiter $$
		Create Procedure sp_ListarCitas()
        Begin
			Select
            C.codigoCita,
            C.fechaCita,
            C.horaCita,
            C.tratamiento,
            C.descripCondActual,
            C.codigoPaciente,
            C.numeroColegiado
			From Citas C;
            End $$
    Delimiter ;
    
    Call sp_ListarCitas();
    /*-------------------------------Buscar Citas-----------------------------*/
    Delimiter $$
		Create Procedure sp_BuscarCitas(in codCita int)
        Begin
			Select
            C.codigoCita,
            C.fechaCita,
            C.horaCita,
            C.tratamiento,
            C.descripCondActual,
            C.codigoPaciente,
            C.numeroColegiado
			From Citas C
            where codCita=codigoCita;
            End $$
    Delimiter ;
    
    Call sp_BuscarCitas(1);
    /*-------------------------------Eliminar Citas-----------------------------*/
  Delimiter $$
		Create Procedure sp_EliminarCitas(in codCita int)
        Begin
			Delete
			From Citas 
            where codCita=codigoCita;
            End $$
    Delimiter ;
    
    Call sp_EliminarCitas(1);
    