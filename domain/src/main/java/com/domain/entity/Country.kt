package com.domain.entity

data class Country(
    val id: Long,
    val code: String,
    val name: String,
    val states: List<State>
)