package com.devindi.wallpaper.misc.report

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.WorkerThread
import org.json.JSONObject
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import android.os.Process
import android.support.annotation.AnyThread
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.util.Date

private const val LOG_DIR = "logs"

@WorkerThread
@SuppressLint("LogNotTimber")
class LogWriter (context: Context) {

    private val uploader = LogUploader()

    private val logDir = File(context.cacheDir, LOG_DIR)

    private val logQueue = LinkedBlockingQueue<JSONObject>()
    @Volatile
    private var isRunning = false

    private var entryCounter = 0
    private var fileCreatedAt = 0L

    private var currentLogFile: File? = null

    @AnyThread
    fun postLogEntry(entry: JSONObject) {
        if (!isRunning) {
            isRunning = true
            Thread(Runnable {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
                start()
            }, "Log writer").start()
        }
        logQueue.add(entry)
    }


    private fun start() {
        while (true) {
            val batch = mutableListOf<JSONObject>()
            val entry: JSONObject? = logQueue.poll(10, TimeUnit.SECONDS)
            entry?.let {
                batch.add(it)
                while (!logQueue.isEmpty()) {
                    batch.add(logQueue.poll())
                }
            }
            try {
                writeBatch(batch)
            } catch (exc: IOException) {
                logQueue.addAll(batch)
                finishFile()
            }
        }
    }

    private fun getCurrentFile(): File {
        currentLogFile?.let { return it }
        val now = Date()
        if (!logDir.exists()) {
            logDir.mkdir()
        }
        return File(logDir, now.time.toString()).also {
            fileCreatedAt = now.time
            currentLogFile = it
        }
    }

    private fun finishFile() {
        currentLogFile = null
        entryCounter = 0
        fileCreatedAt = 0L
    }

    @Throws(IOException::class)
    private fun writeBatch(batch: List<JSONObject>) {
        Log.d("LogWriter", "Saving to file: $batch")
        if (batch.isEmpty()) {
            return
        }
        val target = getCurrentFile()
        val ps = PrintStream(FileOutputStream(target, true))
        batch.forEach {
            ps.println(it.toString())
        }
        ps.close()
        entryCounter += batch.size
        val fileSize = target.length()
        val fileAge = Date().time - fileCreatedAt
        Log.d("LogWriter", "counter: $entryCounter, size: $fileSize, age: ${fileAge/1000} sec")

        if (entryCounter > 100) {
            finishFile()
            upload(target)
        }

        if (fileAge/1000 > 60) { //1 min
            finishFile()
            upload(target)
        }

        if (fileSize > 1024 * 1024) { //1 MB
            finishFile()
            upload(target)
        }
    }

    private fun upload(file: File) {
        uploader.upload(file)
    }
}