package com.es.sessionsecurity.service

import com.es.sessionsecurity.error.exception.BadRequestException
import com.es.sessionsecurity.model.Session
import com.es.sessionsecurity.repository.SessionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SessionService {

    @Autowired
    private lateinit var sessionRepository: SessionRepository

    fun checkToken(token: String): Boolean {

        // 0 Comprobamos que el token no sea nulo
        if (token == null) throw BadRequestException("Token invalido")

        //1ero vamos a obtener la session asociada al toke
        val s: Session = sessionRepository
            .findByToken(token)
            .orElseThrow { RuntimeException("Token invalido") }

        // Por ultimo, comprobamos si la fecha de expiración es mayor que la fecha actual

        return  s.fechaExp.isAfter(LocalDateTime.now()) // Si la fecha de expiración es mayor que la fecha actual, devolvemos true


    }

}