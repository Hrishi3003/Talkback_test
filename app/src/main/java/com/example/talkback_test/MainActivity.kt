package com.example.talkback_test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.speech.tts.TextToSpeech
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView: TextView = findViewById(R.id.textView)
        val button: Button = findViewById(R.id.button)

        // Read content from the file in assets folder
        val fileContent = readFromAssets("read.txt")

        // Display content in TextView
        textView.text = fileContent

        // Initialize TextToSpeech
        tts = TextToSpeech(applicationContext) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = java.util.Locale.getDefault()
            }
        }

        // Button click listener to read content aloud
        button.setOnClickListener {
            readAloud(fileContent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Shutdown TextToSpeech when the activity is destroyed
        tts.shutdown()
    }

    // Read content from a file in the assets folder
    private fun readFromAssets(fileName: String): String {
        val stringBuilder = StringBuilder()
        try {
            val inputStream = assets.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }
            bufferedReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    // Read content aloud using TextToSpeech
    private fun readAloud(content: String) {
        val words = content.split("\\s+".toRegex())
        val limitedContent = if (words.size > 100) {
            words.subList(0, 100).joinToString(" ")
        } else {
            content
        }
        tts.speak(limitedContent, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}
