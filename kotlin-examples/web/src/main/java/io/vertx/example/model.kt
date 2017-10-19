package io.vertx.example

data class Country(val name: String, val code: String)

data class Island(val name: String, val country: Country)
