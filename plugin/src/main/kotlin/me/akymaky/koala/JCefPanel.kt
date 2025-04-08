package me.akymaky.koala

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.ui.jcef.JBCefApp
import com.intellij.ui.jcef.JBCefBrowser
import java.awt.BorderLayout
import javax.swing.JPanel

class JCefPanel(url: String) : JPanel(), Disposable {
    private val cef: JBCefBrowser

    init {
        if (!JBCefApp.isSupported()) {
            throw RuntimeException("JBCefApp is not supported")
        }

        layout = BorderLayout()

        cef = JBCefBrowser()

        add(cef.component, BorderLayout.CENTER)

        cef.loadURL(url)
    }

    override fun dispose() {
        Disposer.dispose(cef)
    }

}