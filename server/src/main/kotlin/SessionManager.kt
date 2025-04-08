package me.akymaky

import io.ktor.util.collections.*
import io.ktor.websocket.*
import me.akymaky.breakpoints.BreakpointDto

object SessionManager {
    private val sessions = ConcurrentSet<WebSocketSession>()

    fun addSession(session: WebSocketSession) {
        sessions.add(session)
    }

    fun removeSession(session: WebSocketSession) {
        sessions.remove(session)
    }

    suspend fun announce(data: String) {

        for (session in sessions) {
            try {
                session.send(Frame.Text(data))
            } catch (e: Exception) {
                e.printStackTrace()
                sessions.remove(session)
            }
        }
    }

    suspend fun announceAdd(breakpoint: BreakpointDto) {
        announce("add\n${breakpoint.id};${breakpoint.filePath} #${breakpoint.lineNumber}")
    }

    suspend fun announceRemove(breakpoint: BreakpointDto) {
        announce("remove\n${breakpoint.id}")
    }
}