package com.es.sessionsecurity.model

import jakarta.persistence.*

@Entity
@Table(name = "usuarios")
data class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @Column(unique = true)
    var nombre:String,
    var password:String,
    @Enumerated(EnumType.STRING)
    val rol :Rol
) {
}

enum class Rol {
    USER, ADMIN
}