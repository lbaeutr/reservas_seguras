package com.es.sessionsecurity.service

import com.es.sessionsecurity.model.Reserva
import com.es.sessionsecurity.repository.ReservaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReservaService {

    @Autowired
    private lateinit var reservaRepository: ReservaRepository

    // Metodo para obtener todas las reservas por nombre de usuario
    fun  getReservasByNombreUsuario(nombre: String): List<Reserva> {
        return reservaRepository.findByUsuario_Nombre(nombre)
    }

    //Metodo para insertar una nueva reserva
    fun insertarReserva(nuevaReserva: Reserva): Reserva {
        return reservaRepository.save(nuevaReserva)
    }
}