package me.akymaky

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.html.*
import me.akymaky.breakpoints.BreakpointManager

fun Application.configureRouting(breakpointManager: BreakpointManager) {
    routing {
        staticResources("/static", "static")
        get("/") {
            val breakpoints = breakpointManager.getAllBreakpoints()

            call.respondHtml {
                head {
                    title { +"KOALA" }
                }
                body {
                    h1 { +"Breakpoints [${breakpoints.size}]"}
                    ul {
                        for (breakpoint in breakpoints) {
                            li {
                                attributes["data-id"] = breakpoint.id.toString()
                                +"${breakpoint.filePath} #${breakpoint.lineNumber}"
                            }
                        }
                    }
                    script { src = "/static/koala.js" }
                }
            }
        }
        webSocket("/breakpoints") {
            println("New session")
            SessionManager.addSession(this)
        }
    }
}