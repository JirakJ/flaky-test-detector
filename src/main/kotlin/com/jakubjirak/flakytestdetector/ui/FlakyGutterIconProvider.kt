package com.jakubjirak.flakytestdetector.ui

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.jakubjirak.flakytestdetector.model.FlakinessCategory
import com.jakubjirak.flakytestdetector.settings.FlakyTestSettings
import com.jakubjirak.flakytestdetector.storage.FlakyTestStorage
import javax.swing.Icon

class FlakyGutterIconProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (element.node?.elementType?.toString() != "IDENTIFIER") return null

        val settings = FlakyTestSettings.getInstance()
        if (!settings.enabled || !settings.gutterIconsEnabled) return null

        val parent = element.parent ?: return null
        if (!isTestMethod(parent)) return null

        val project = element.project
        val methodName = element.text
        val className = extractClassName(parent)
        val testId = "$className.$methodName"

        val storage = FlakyTestStorage.getInstance(project)
        val record = storage.getRecord(testId) ?: return null

        if (record.results.size < settings.minimumRuns) return null

        val score = record.flakinessScore
        val category = FlakinessCategory.fromScore(score)
        if (category == FlakinessCategory.STABLE) return null

        val icon = getIconForCategory(category)
        @Suppress("MagicNumber")
        val tooltip = "Flaky: ${String.format("%.1f", score)}% (${category.displayName}) — " +
            "${record.results.size} runs"

        return LineMarkerInfo(
            element,
            element.textRange,
            icon,
            { tooltip },
            null,
            GutterIconRenderer.Alignment.RIGHT,
            { tooltip }
        )
    }

    private fun isTestMethod(element: PsiElement): Boolean {
        val annotations = element.children
        return annotations.any { child ->
            val text = child.text
            text.contains("@Test") || text.contains("@ParameterizedTest") ||
                text.contains("@RepeatedTest")
        }
    }

    private fun extractClassName(methodElement: PsiElement): String {
        var parent = methodElement.parent
        while (parent != null) {
            val className = parent.javaClass.simpleName
            if (className.contains("Class") || className.contains("Object")) {
                return parent.text.substringBefore("{").trim()
                    .substringAfterLast(" ")
            }
            parent = parent.parent
        }
        return "Unknown"
    }

    private fun getIconForCategory(category: FlakinessCategory): Icon {
        return when (category) {
            FlakinessCategory.SUSPICIOUS -> AllIcons.General.Warning
            FlakinessCategory.FLAKY -> AllIcons.General.BalloonWarning
            FlakinessCategory.HIGHLY_UNSTABLE -> AllIcons.General.Error
            FlakinessCategory.STABLE -> AllIcons.General.InspectionsOK
        }
    }
}
