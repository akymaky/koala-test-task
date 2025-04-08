package me.akymaky.koala

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import me.akymaky.KoalaServer
import me.akymaky.breakpoints.BreakpointManager

class KoalaToolWindowFactory : ToolWindowFactory, Disposable {
    private var server: KoalaServer? = null

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        if (server == null) {
            server = KoalaServer(BreakpointManager(project))
            server!!.start()
        }

        val panel = JCefPanel(server!!.url)

        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(panel, "KOALA", false)

        toolWindow.contentManager.addContent(content)
    }

    override fun dispose() {
        server?.stop()
    }
}