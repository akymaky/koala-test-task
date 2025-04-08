package me.akymaky.breakpoints

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import me.akymaky.SessionManager

class BreakpointManager(private val project: Project) {

    init {
        val conn = project.messageBus.connect()
        conn.subscribe(XBreakpointListener.TOPIC, object : XBreakpointListener<XBreakpoint<*>> {
            override fun breakpointRemoved(breakpoint: XBreakpoint<*>) {
                ApplicationManager.getApplication().executeOnPooledThread {
                    kotlinx.coroutines.runBlocking {
                        SessionManager.announceRemove(breakpointToDto(breakpoint))
                    }
                }
            }

            override fun breakpointAdded(breakpoint: XBreakpoint<*>) {
                ApplicationManager.getApplication().executeOnPooledThread {
                    kotlinx.coroutines.runBlocking {
                        SessionManager.announceAdd(breakpointToDto(breakpoint))
                    }
                }
            }


            override fun breakpointChanged(breakpoint: XBreakpoint<*>) {

            }
        })
    }

    fun getAllBreakpoints(): List<BreakpointDto> {
        val result = mutableListOf<BreakpointDto>()

        ApplicationManager.getApplication().invokeAndWait {
            val debugManager = XDebuggerManager.getInstance(project)
            val breakpoints = debugManager.breakpointManager.allBreakpoints

            for (breakpoint in breakpoints) {
                if (breakpoint.sourcePosition == null) continue
                result.add(breakpointToDto(breakpoint))
            }
        }

        return result
    }

    fun breakpointToDto(breakpoint: XBreakpoint<*>): BreakpointDto {
        val id = breakpoint.hashCode()
        val sp = breakpoint.sourcePosition!!
        val filePath = sp.file.path.substring(project.basePath!!.length)
        val lineNumber = sp.line

        return BreakpointDto(id, filePath, lineNumber)
    }
}