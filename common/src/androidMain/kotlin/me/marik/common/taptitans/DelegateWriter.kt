package me.marik.common.taptitans

import java.io.OutputStreamWriter

class DelegateWriter(private val writer: OutputStreamWriter) {
    fun flush() {
        writer.flush()
    }

    fun append(data: String): DelegateWriter {
        println("executing command: $data")
        writer.append(data)
        return this
    }

    fun close(){
        writer.close()
    }
}