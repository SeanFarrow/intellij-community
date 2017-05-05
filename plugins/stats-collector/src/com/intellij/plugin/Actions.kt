package com.intellij.plugin

import com.intellij.codeInsight.completion.CodeCompletionHandlerBase
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupManager
import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

class ToggleManualMlSorting : AnAction() {

    private val disableText = "Disable Manual ML Sorting"
    private val enableText = "Enable Manual ML Sorting"

    init {
        templatePresentation.text = "Toggle Manual ML sorting"
    }
    
    override fun update(e: AnActionEvent) {
        if (!ManualExperimentControl.isEnabled()) {
            e.presentation.isEnabledAndVisible = false
            return
        }
        
        e.presentation.text = if (ManualMlSorting.isEnabled()) disableText else enableText
    }

    override fun actionPerformed(e: AnActionEvent) {
        val before = ManualMlSorting.isEnabled()
        ManualMlSorting.switch()
        
        val editor = CommonDataKeys.EDITOR.getData(e.dataContext)
        val project = CommonDataKeys.PROJECT.getData(e.dataContext)


        val lookup = LookupManager.getActiveLookup(editor)
        if (lookup != null) {
            CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(project!!, editor!!, 10, false, false)
        } else {
            val content = if (before) "Manual ML Sorting Disabled" else "Manual ML Sorting Enabled"
            val collector = "Completion Stats Collector"
            val notification = Notification(collector, collector, content, NotificationType.INFORMATION)
            notification.notify(project)
        }
    }
    
}

internal fun ApplicationProperty.switch() = setEnabled(!isEnabled())

abstract class ApplicationProperty {
    internal abstract val key: String
    internal abstract val default: Boolean
    
    fun isEnabled() = PropertiesComponent.getInstance().getBoolean(key, default) 
    fun setEnabled(value: Boolean) = PropertiesComponent.getInstance().setValue(key, value, default)
}

object ManualExperimentControl : ApplicationProperty() {
    override val key = "ml.control.experiment.manually"
    override val default = false
}


object ManualMlSorting : ApplicationProperty() {
    override val key = "ml.manual.sorting"
    override val default = false
}