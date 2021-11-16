package com.deliveryhero.whetstone.compiler

import com.squareup.anvil.compiler.internal.asClassName
import com.squareup.anvil.compiler.internal.findAnnotationArgument
import com.squareup.anvil.compiler.internal.requireFqName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import dagger.multibindings.ClassKey
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtAnnotationEntry

internal class InjectorModuleInfoProvider : ModuleInfoProvider {
    private val multibindingKeyCn = ClassKey::class.asClassName()
    private val anvilInjectorCn = ClassName("com.deliveryhero.whetstone.injector", "AnvilInjector")

    override val supportedAnnotation = FqName("com.deliveryhero.whetstone.injector.ContributesInjector")

    override fun getScope(annotation: KtAnnotationEntry, module: ModuleDescriptor): ClassName {
        val componentScope = annotation.findAnnotationArgument<PsiElement>("scope", 0) ?: error("Scope not found")
        return componentScope.requireFqName(module).asClassName(module)
    }

    override fun getMultibindingKey() = multibindingKeyCn

    override fun getTarget(annotatedClass: ClassName) = annotatedClass.peerClass(annotatedClass.simpleName + "Injector")

    override fun getOutput(annotatedClass: ClassName) = anvilInjectorCn.parameterizedBy(STAR)
}

internal class InstanceModuleInfoProvider(
    override val supportedAnnotation: FqName,
    private val scopeCn: ClassName,
    private val multibindingKeyCn: ClassName,
    private val baseClass: TypeName
) : ModuleInfoProvider {

    override fun getScope(annotation: KtAnnotationEntry, module: ModuleDescriptor) = scopeCn

    override fun getMultibindingKey() = multibindingKeyCn

    override fun getTarget(annotatedClass: ClassName) = annotatedClass

    override fun getOutput(annotatedClass: ClassName) = baseClass
}
