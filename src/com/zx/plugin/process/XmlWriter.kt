package com.zx.plugin.process

import com.zx.plugin.entity.TaskHolder

class XmlWriter : AbsWriter() {

    fun process(taskHolder: TaskHolder) {
        val file = taskHolder.currentFile ?: return
        var content = readFileContent(file)

        for (field in taskHolder.selectedFields()) {
            val text = field.source
            content = content.replace("android:text=\"$text\"", "android:text=\"@string/${field.result}\"")
        }

        writeFileContent(file, content)
    }

}