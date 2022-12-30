package com.example.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.routing.get

fun Application.configureRouting() {

    install(Resources)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get<MyLocation> {
                call.respondText("Location: name=${it.name}, arg1=${it.arg1}, arg2=${it.arg2}")
            }
            // Register nested routes
            get<Type.Edit> {
                call.respondText("Inside $it")
            }
            get<Type.List> {
                call.respondText("Inside $it")
            }
        get<Articles> { article ->
            // Get all articles ...
            call.respond("List of articles sorted starting from ${article.sort}")
        }
    }
}

@Serializable
@Resource("/location/{name}")
class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")

@Serializable
@Resource("/type/{name}") data class Type(val name: String) {
    @Serializable
    @Resource("/edit")
    data class Edit(val type: Type)

    @Serializable
    @Resource("/list/{page}")
    data class List(val type: Type, val page: Int)
}

@Serializable
@Resource("/articles")
class Articles(val sort: String? = "new")
