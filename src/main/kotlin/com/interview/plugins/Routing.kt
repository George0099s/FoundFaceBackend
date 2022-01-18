package com.interview.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import java.io.File


fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}

fun Application.configureUploadFileRouting() {

    routing {
        var fileDescription = ""
        var fileName = ""


        post("/upload") {
            val multipartData = call.receiveMultipart()

            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        fileDescription = part.value
                    }
                    is PartData.FileItem -> {
                        fileName = part.originalFileName as String
                        var fileBytes = part.streamProvider().readBytes()
                        File("files/public/$fileName").writeBytes(fileBytes)
                    }
                }
            }

            call.respondText(
                "$fileDescription is uploaded to 'uploads/$fileName'" +
                        " \nlink: http://0.0.0.0:8080/static/$fileName \n Current directory is ${System.getProperty("user.dir")}"
            )
        }
    }
}

fun Application.configureFileSystem(){
    routing {
        static("static") {
            // When running under IDEA make sure that working directory is set to this sample's project folder
            staticRootFolder = File("files")
            files("public")
        }
    }
}