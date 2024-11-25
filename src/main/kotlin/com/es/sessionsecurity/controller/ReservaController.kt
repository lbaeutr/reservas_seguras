package com.es.sessionsecurity.controller

import com.es.sessionsecurity.error.exception.BadRequestException
import com.es.sessionsecurity.model.Reserva
import com.es.sessionsecurity.model.Rol
import com.es.sessionsecurity.service.ReservaService
import com.es.sessionsecurity.service.SessionService
import com.es.sessionsecurity.service.UsuarioService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.apache.coyote.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reservas")
class ReservaController {

    @Autowired
    private lateinit var reservaService: ReservaService

    @Autowired
    private lateinit var sessionService: SessionService

    @Autowired
    private lateinit var usuarioService: UsuarioService

    /*
    OBTENER TODAS LAS RESERVAS POR EL NOMBRE DE USUARIO DE UN CLIENTE
     */
    @GetMapping("/{nombre}")
    fun getByNombreUsuario(
        @PathVariable nombre: String,
        request: HttpServletRequest
    ) : ResponseEntity<List<Reserva>?> {

//        /*
//        COMPROBAR QUE LA PETICIÓN ESTÁ CORRECTAMENTE AUTORIZADA PARA REALIZAR ESTA OPERACIÓN
//         */
//
//        //1ero Extraemos la cookie
//        val cookie: Cookie? = request.cookies.find{ c: Cookie ? -> c?.name == "tokenSession"}
//        val token : String? = cookie?.value
//
//        //2do Comprobamos la validez dele token
//        if (sessionService.checkToken(token!!)){
//            //Realizar la consulta a la base de datos
//            return ResponseEntity<List<Reserva>?>(null, HttpStatus.OK)
//        }
//        /*
//        LLAMAR AL SERVICE PARA REALIZAR LA L.N. Y LA LLAMADA A LA BASE DE DATOS
//         */
//        // CÓDIGO AQUÍ
//
//        // RESPUESTA
//        return ResponseEntity<List<Reserva>?>(null, HttpStatus.OK); // cambiar null por las reservas

        val token = extractTokenFromRequest(request)
        val usuario = sessionService.getUsuarioFromToken(token)

        if (usuario.rol == Rol.USER && usuario.nombre != nombre) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        val reservas = reservaService.getReservasByNombreUsuario(nombre)
        return ResponseEntity(reservas, HttpStatus.OK)



    }

    /*
    INSERTAR UNA NUEVA RESERVA
     */
    fun insert(
        @RequestBody nuevaReserva: Reserva,
        request: HttpServletRequest
    ) : ResponseEntity<Reserva?> {

        // todo aqui nos hemos quedado
        /*
        COMPROBAR QUE LA PETICIÓN ESTÁ CORRECTAMENTE AUTORIZADA PARA REALIZAR ESTA OPERACIÓN
         */
        // CÓDIGO AQUÍ

        /*
        LLAMAR AL SERVICE PARA REALIZAR LA L.N. Y LA LLAMADA A LA BASE DE DATOS
         */
        // CÓDIGO AQUÍ

        // RESPUESTA

        val token = extractTokenFromRequest(request)
        val usuario = sessionService.getUsuarioFromToken(token)

        if (usuario.rol == Rol.USER && usuario.nombre != nuevaReserva.usuario.nombre) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        val reserva = reservaService.insertarReserva(nuevaReserva)


        return ResponseEntity<Reserva?>(reserva, HttpStatus.CREATED); // cambiar null por la reserva
    }


    private fun extractTokenFromRequest(request: HttpServletRequest): String {
        val cookie = request.cookies?.find { it.name == "tokenSession" }
        return cookie?.value ?: throw BadRequestException("Token no encontrado")
    }

}