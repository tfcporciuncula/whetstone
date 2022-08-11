package com.deliveryhero.whetstone.compiler

import com.google.auto.service.AutoService
import dagger.model.BindingGraph
import dagger.spi.BindingGraphPlugin
import dagger.spi.DiagnosticReporter

@AutoService(BindingGraphPlugin::class)
internal class TimingPlugin : BindingGraphPlugin {

    override fun visitGraph(
        bindingGraph: BindingGraph?,
        diagnosticReporter: DiagnosticReporter?
    ) {
        val bindings = bindingGraph?.bindings().orEmpty()
        val componentNodes = bindingGraph?.componentNodes().orEmpty()
        bindings.forEach {
            println(
                """
                    ======Found binding ${it.kind()} ${it.kind().isMultibinding}
                    ======${it.scope()} ${it.componentPath()}
                """.trimIndent()
            )
        }

        componentNodes.forEach {
            println(
                """
                    ======Found component ${it.componentPath()} ${it.isRealComponent}
                """.trimIndent()
            )
        }
    }
}
