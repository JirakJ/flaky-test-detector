package com.jakubjirak.flakytestdetector.ui

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.jakubjirak.flakytestdetector.model.FlakinessCategory
import com.jakubjirak.flakytestdetector.model.TestRecord
import com.jakubjirak.flakytestdetector.settings.FlakyTestSettings
import com.jakubjirak.flakytestdetector.storage.FlakyTestStorage
import java.util.Locale
import javax.swing.Icon

/**
 * Gutter icon on the name of a test method (Java or Kotlin) whose recorded history is not stable.
 * Registered for the Java and Kotlin languages through optional plugin dependencies.
 */
class FlakyGutterIconProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        val settings = FlakyTestSettings.getInstance()
        if (!settings.enabled || !settings.gutterIconsEnabled) return null
        val record = recordFor(element)?.takeIf { it.results.size >= settings.minimumRuns } ?: return null
        val category = FlakinessCategory.fromScore(record.flakinessScore, settings.thresholds())
        if (category == FlakinessCategory.STABLE) return null

        val tooltip = tooltip(record, category)
        return LineMarkerInfo(
            element,
            element.textRange,
            iconFor(category),
            { tooltip },
            null,
            GutterIconRenderer.Alignment.RIGHT,
            { tooltip }
        )
    }

    /** History of the test method whose name identifier is [element], if any. */
    private fun recordFor(element: PsiElement): TestRecord? {
        if (element.node?.elementType?.toString() != "IDENTIFIER") return null
        val method = (element.parent as? PsiNamedElement)?.takeIf { it.name == element.text && isTestMethod(it) }
        val className = method?.let { enclosingClassName(it) } ?: return null
        return FlakyTestStorage.getInstance(element.project).findBySimpleName(className, element.text)
    }

    private fun isTestMethod(method: PsiElement): Boolean =
        method.children.any { child -> TEST_ANNOTATIONS.any { child.text.contains(it) } }

    /** Name of the nearest enclosing class (Java `PsiClass` or Kotlin `KtClass`/`KtObjectDeclaration`). */
    private fun enclosingClassName(method: PsiElement): String? {
        var parent = method.parent
        while (parent != null) {
            val kind = parent.javaClass.simpleName
            if (parent is PsiNamedElement && (kind.contains("Class") || kind.contains("Object"))) return parent.name
            parent = parent.parent
        }
        return null
    }

    companion object {
        private val TEST_ANNOTATIONS = listOf("@Test", "@ParameterizedTest", "@RepeatedTest")

        fun tooltip(record: TestRecord, category: FlakinessCategory): String =
            "Flaky: ${String.format(Locale.US, "%.1f", record.flakinessScore)}% (${category.displayName}) — " +
                "${record.results.size} runs" + if (record.quarantined) " — quarantined" else ""

        fun iconFor(category: FlakinessCategory): Icon = when (category) {
            FlakinessCategory.SUSPICIOUS -> AllIcons.General.Warning
            FlakinessCategory.FLAKY -> AllIcons.General.BalloonWarning
            FlakinessCategory.HIGHLY_UNSTABLE -> AllIcons.General.Error
            FlakinessCategory.STABLE -> AllIcons.General.InspectionsOK
        }
    }
}
