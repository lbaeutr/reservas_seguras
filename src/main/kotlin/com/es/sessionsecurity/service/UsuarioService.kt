package com.es.sessionsecurity.service

import com.es.sessionsecurity.error.exception.BadRequestException
import com.es.sessionsecurity.error.exception.NotFoundException
import com.es.sessionsecurity.model.Session
import com.es.sessionsecurity.model.Usuario
import com.es.sessionsecurity.repository.SessionRepository
import com.es.sessionsecurity.repository.UsuarioRepository
import com.es.sessionsecurity.util.CipherUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@Service
class UsuarioService {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository
    @Autowired
    private lateinit var sessionRepository: SessionRepository
    @Autowired
    private lateinit var cipherUtils: CipherUtils

    //Modificamos el método login en UsuarioService para usar cifrado simétrico en lugar de UUID:
    fun login(userLogin: Usuario) : String {

        // COMPROBACIÓN DE LOS CAMPOS DEL OBJETO USERLOGIN
        if(userLogin.password.isBlank() || userLogin.nombre.isBlank()) {
            throw BadRequestException("Los campos nombre y password son obligatorios")
        }

        // COMPROBAR CREDENCIALES
        // 1 Obtener el usuario de la base de datos
        var userBD: Usuario = usuarioRepository
            .findByNombre(userLogin.nombre)
            .orElseThrow{NotFoundException("El usuario proporcionado no existe en BDD")}

        // 2 Compruebo nombre y pass
//        if(userBD.password == userLogin.password) {
//            // 3 GENERAR EL TOKEN
//            var token: String = ""
//            token = UUID.randomUUID().toString()


        // Parte modificada
        if (userBD.password == userLogin.password) {
            val clavePublica = "clave_publica_que_no_cambia"
            val token = cipherUtils.encrypt(userBD.nombre, clavePublica)


            // 4 CREAR UNA SESSION
            val s: Session = Session(
                null,
                token,
                userBD,
                LocalDateTime.now().plusMinutes(3)
            )

            // 5 INSERTAMOS EN BDD
            sessionRepository.save(s)

            return token
        } else {
            // SI LA CONTRASEÑA NO COINCIDE, LANZAMOS EXCEPCIÓN
            throw NotFoundException("Las credenciales son incorrectas")
        }
    }



    // Comprobacion del cifrado de la contraseña al registrar un usuario
     // El metodo register en UsuarioService se modifica para cifrar la password antes de guardarla en la base de datos
    //  esto se hace con el metodo encrypt de la clase CipherUtils
    //  el metodo recibe un objeto Usuario y cifra la password con una clave publica que no cambia
    //  y luego guarda el usuario en la base de datos

    fun register(nuevoUsuario: Usuario) {
    // Cifrar la password antes de guardar
    val clavePublica = "clave_publica_que_no_cambia"
    nuevoUsuario.password = cipherUtils.encrypt(nuevoUsuario.password, clavePublica)
    usuarioRepository.save(nuevoUsuario)
}

}