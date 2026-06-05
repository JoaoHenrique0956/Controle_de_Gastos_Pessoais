package com.example.projetopdm2

data class Despesa(
    val id: String = "",
    val descricao: String = "",
    val valor: Double = 0.0,
    val categoria: String = "",
    val data: Long = 0L
)
