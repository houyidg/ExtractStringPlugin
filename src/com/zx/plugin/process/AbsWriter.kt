package com.zx.plugin.process

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

open class AbsWriter {

    protected fun saveAllFile() {
        FileDocumentManager.getInstance().saveAllDocuments()
    }


    protected fun writeFileContent(file: VirtualFile, content: String) {
        val outputStream = file.getOutputStream(this)
        val writer = BufferedWriter(OutputStreamWriter(outputStream,"utf-8"))
        writer.write(content)
        writer.close()
    }

    protected fun readFileContent(file: VirtualFile): String {
        val inputStream = file.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream,"utf-8"))
        var line: String
        val builder = StringBuilder()
        while (true) {
            line = reader.readLine() ?: break
            builder.append(line).appendln()
        }
        val content = builder.toString()
        reader.close()
        return content
    }

}