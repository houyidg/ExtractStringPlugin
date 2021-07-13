package com.zx.plugin.process

import com.zx.plugin.entity.TaskHolder

class JavaWriter : AbsWriter() {


    fun process(taskHolder: TaskHolder) {
        write(taskHolder)
    }

    private fun write(taskHolder: TaskHolder) {
        val file = taskHolder.currentFile ?: return
        var content = readFileContent(file)

        val extractTemplate = taskHolder.javaExtractTemplate
        for (field in taskHolder.selectedFields()) {
            val text = field.source
            val replace = extractTemplate.replace("\$id", "R.string.${field.result}")
            content = content.replace("\"$text\"", replace)
        }

        writeFileContent(file, content)
    }


}