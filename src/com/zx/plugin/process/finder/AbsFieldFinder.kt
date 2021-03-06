package com.zx.plugin.process.finder

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiFile
import com.zx.plugin.entity.FieldEntity
import com.zx.plugin.entity.TaskHolder
import java.io.BufferedReader
import java.io.InputStreamReader

abstract class AbsFieldFinder {

    fun find(psiFile: PsiFile): TaskHolder {
        // save before operation
        FileDocumentManager.getInstance().saveAllDocuments()

        val taskHolder = TaskHolder()

        val virtualFile = psiFile.virtualFile ?: return taskHolder
        val inputStream = virtualFile.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream,"utf-8"))

        val stringBuilder = StringBuilder()
        while (true) {
            val line = reader.readLine() ?: break
            stringBuilder.append(line).appendln()
        }
        reader.close()
        val content = stringBuilder.toString()

        val regex = Regex(regex())
        val result = regex.findAll(content)

        taskHolder.fields = result
                .map { it.value }
                .map { transformToString(it) }
                .filter { isDefaultChecked(it) }
                .distinct()
                .map { FieldEntity(it, "", true) }
                .toList()

        return taskHolder
    }

    abstract fun isDefaultChecked(it: String): Boolean

    abstract protected fun transformToString(it: String): String

    abstract protected fun regex(): String

}