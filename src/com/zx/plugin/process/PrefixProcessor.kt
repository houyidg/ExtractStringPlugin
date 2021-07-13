package com.zx.plugin.process

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiFile
import com.zx.plugin.entity.TaskHolder

object PrefixProcessor {

    fun refreshDefaultPrefix(project: Project, psiFile: PsiFile, taskHolder: TaskHolder) {
        val builder = StringBuilder()
        val virtualFile = psiFile.virtualFile

        val module = ProjectRootManager.getInstance(project).fileIndex.getModuleForFile(virtualFile)
        val moduleName =  module?.moduleFile?.parent?.name//
        builder.append(moduleName).append("_")
                .append("${formatComponentName(virtualFile.name.split(".")[0])}_")
                .append(if(taskHolder.isJavaFile()) "java"  else "xml")
        refreshPrefix(taskHolder, builder.toString())
    }

    private fun formatComponentName(name: String): String {
        return name.toLowerCase()
//        return StringUtils.underscoreString(name)
    }

    fun refreshPrefix(taskHolder: TaskHolder, prefix: String) {
        taskHolder.prefix = prefix
        val fields = taskHolder.fields
        for (i in IntRange(0, fields.size - 1)) {
            fields[i].result = prefix + fields[i].resultSrc+"_"+i
        }
    }

}