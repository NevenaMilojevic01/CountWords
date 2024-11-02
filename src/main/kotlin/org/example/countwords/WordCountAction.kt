package org.example.countwords

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import javax.swing.JPanel
import javax.swing.JCheckBox
import javax.swing.BoxLayout

class WordCountAction : AnAction("Count Words") {
    private val uniqueCheckBox = JCheckBox("Count Unique Words")

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project
        val editor: Editor? = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR)

        if (editor != null && project != null) {
            val document: Document = editor.document
            val text: String = document.text

            val dialogBuilder = DialogBuilder()
            dialogBuilder.setTitle("Word Count Options")

            val checkboxPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(uniqueCheckBox)
            }

            dialogBuilder.setCenterPanel(checkboxPanel)

            dialogBuilder.setOkOperation {
                val wordCount: Int = countWords(text)
                val uniqueCount: Int

                if (uniqueCheckBox.isSelected) {
                    uniqueCount = countUniqueWords(text)
                } else {
                    uniqueCount = 0
                }

                val upperCaseCount = countUpperCaseWords(text)
                val lowerCaseCount = countLowerCaseWords(text)
                val specialCharactersCount = countSpecialCharacters(text)
                val numberCount = countNumbers(text)

                val countMessage = """
                    Total Words: $wordCount
                    Unique Words: $uniqueCount
                    All Upper Case Words: $upperCaseCount
                    All Lower Case Words: $lowerCaseCount
                    Special Characters Count: $specialCharactersCount
                    Numbers Count: $numberCount
                """.trimIndent()

                Messages.showInfoMessage(project, countMessage, "Word Count Summary")
            }

            dialogBuilder.show()
        }
    }

    private fun countWords(text: String): Int {
        return if (text.isBlank()) {
            0
        } else {
            text.split("\\s+".toRegex()).size
        }
    }

    private fun countUniqueWords(text: String): Int {
        return if (text.isBlank()) {
            0
        } else {
            text.split("\\s+".toRegex())
                .map { it.trim() }
                .toSet()
                .size
        }
    }

    private fun countUpperCaseWords(text: String): Int {
        return text.split("\\s+".toRegex())
            .count { it.all { char -> char.isUpperCase() } }
    }

    private fun countLowerCaseWords(text: String): Int {
        return text.split("\\s+".toRegex())
            .count { it.all { char -> char.isLowerCase() } }
    }

    private fun countSpecialCharacters(text: String): Int {
        val specialCharacters = listOf('(', ')', ',', '.', '/', '*', '-', ';', '#', '!', '@', '$', '%', '&', '[', ']', '=', '_', '?', ':', '"')
        return text.count { it in specialCharacters }
    }
    private fun countNumbers(text: String): Int {
        return text.count { it.isDigit() }
    }
}


