package me.akymaky

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.websocket.*
import me.akymaky.breakpoints.BreakpointManager
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.seconds

class KoalaServer(private val breakpointManager: BreakpointManager) {
    private lateinit var server: ApplicationEngine
    private val _port = AtomicInteger(8080)

    val port = _port.get()

    fun start() {
        var currentPort = 8080
        var attempts = 0

        while (attempts < 5) {
            try {
                server = embeddedServer(Netty, port = currentPort) {
                    install(WebSockets) {
                        pingPeriod = 15.seconds
                        timeout = 15.seconds
                        maxFrameSize = Long.MAX_VALUE
                        masking = false
                    }

                    configureServer(breakpointManager)
                }.start(wait = false).engine

                break
            } catch (t: Throwable) {
                println("Failed to start on port $currentPort")
                currentPort = _port.incrementAndGet()
                attempts++
            }
        }
    }

    val url
        get() = "http://127.0.0.1:$port"

    fun stop() {
        server.stop(10, 10, TimeUnit.SECONDS)
        println("Server stopped")
    }
}

fun Application.configureServer(breakpointManager: BreakpointManager) {
    configureRouting(breakpointManager)
}