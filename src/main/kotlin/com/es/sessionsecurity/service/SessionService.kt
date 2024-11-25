package com.es.sessionsecurity.service

import com.es.sessionsecurity.error.exception.BadRequestException
import com.es.sessionsecurity.model.Session
import com.es.sessionsecurity.model.Usuario
import com.es.sessionsecurity.repository.SessionRepository
import com.es.sessionsecurity.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SessionService {

    @Autowired
    private lateinit var sessionRepository: SessionRepository

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository

//    fun checkToken(token: String): Boolean {
//
//        // 0 Comprobamos que el token no sea nulo
//        if (token == null) throw BadRequestException("Token invalido")
//
//        //1ero vamos a obtener la session asociada al toke
//        val s: Session = sessionRepository
//            .findByToken(token)
//            .orElseThrow { RuntimeException("Token invalido") }
//
//        // Por ultimo, comprobamos si la fecha de expiración es mayor que la fecha actual
//
//        return  s.fechaExp.isAfter(LocalDateTime.now()) // Si la fecha de expiración es mayor que la fecha actual, devolvemos true
//

    //   }

    /*
        checkToken: Verifica si un token de sesión es válido.
        Comprueba que el token no esté vacío.
        Busca la sesión asociada al token en el repositorio.
        Verifica si la fecha de expiración de la sesión es posterior a la fecha actual.
    */
    fun checkToken(token: String): Boolean {
        if (token.isBlank()) throw BadRequestException("Token invalido")

        val session = sessionRepository.findByToken(token)
            .orElseThrow { RuntimeException("Token invalido") }

        return session.fechaExp.isAfter(LocalDateTime.now())
    }


    /*
        getUsuarioFromToken: Obtiene el usuario asociado a un token de sesión.
        Busca la sesión asociada al token en el repositorio.
        Devuelve el usuario asociado a la sesión.

     */
    fun getUsuarioFromToken(token: String): Usuario {
        val session = sessionRepository.findByToken(token)
            .orElseThrow { BadRequestException("Token invalido") }

        return session.usuario
    }


}