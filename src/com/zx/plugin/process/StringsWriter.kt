package com.zx.plugin.process

import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.XmlElementFactory
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlComment
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.zx.plugin.entity.TaskHolder

class StringsWriter(
        private val project: Project
) : AbsWriter() {


    private fun openStringsFile(taskHolder: TaskHolder): XmlFile? {
        val virtualFile = taskHolder.desFile ?: return null
        return PsiManager.getInstance(project).findFile(virtualFile) as? XmlFile ?: return null
    }

    private fun writeComment(rootTag: XmlTag, text: String) {
        val factory = XmlElementFactory.getInstance(project)
        val container = factory.createTagFromText("<comment><!-- $text --></comment>", XMLLanguage.INSTANCE)
        val xmlComment = PsiTreeUtil.getChildOfType(container, XmlComment::class.java) ?: return
        rootTag.add(xmlComment)
    }

    private fun writeContent(rootTag: XmlTag, taskHolder: TaskHolder) {
        val fields = taskHolder.selectedFields()
        for (field in fields) {
            val childTag = rootTag.createChildTag("string", "", field.source, false)
            childTag.setAttribute("name", field.result)
            rootTag.add(childTag)
        }
    }


    fun process(taskHolder: TaskHolder) {
        if (taskHolder.selectedFields().isEmpty()) {
            return
        }
        saveAllFile()
        val xmlFile = openStringsFile(taskHolder) ?: return
        val rootTag = xmlFile.rootTag ?: return
        writeComment(rootTag, taskHolder.descTag)
        writeContent(rootTag, taskHolder)
        saveAllFile()
    }

}