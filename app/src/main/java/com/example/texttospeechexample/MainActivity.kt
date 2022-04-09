package com.example.texttospeechexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import org.w3c.dom.Text
import java.util.*

//이프로그램은 문제가 없는데 TTS가 안깔려 있어서 그런거다. 프로그램에 대한 내용은 다 이해했다.
class MainActivity : AppCompatActivity() {
    private lateinit var mTTS: TextToSpeech
    private lateinit var mEditText_Sentence: EditText
    private lateinit var mSeekbar_Pitch: SeekBar
    private lateinit var mSeekbar_Speed: SeekBar
    private lateinit var mButton_SayIt: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mButton_SayIt = findViewById(R.id.button_speak)
        //초기화를 시킬때 해당 Listener를 만들어놨고 여기에 Context를 통해서 초기 OnInitListener를 실행해서 문제가 없는지 확인하도록 한다.
        //문제가 없으면 Button이 활성화가 되어서 이제 실제로 speak 할 수 있게 되겠지.
        mTTS = TextToSpeech(this) { p0 ->
            if (p0 == TextToSpeech.SUCCESS) {
                val result: Int = mTTS.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported")
                } else {
                    mButton_SayIt.isEnabled = true
                }
            } else {
                Log.e("TTS", "Initialization failed")
            }
        }
        mEditText_Sentence = findViewById(R.id.edit_text)
        mSeekbar_Pitch = findViewById(R.id.seek_bar_pitch)
        mSeekbar_Speed = findViewById(R.id.seek_bar_speed)
        mButton_SayIt.setOnClickListener {
            speak()
        }
    }

    private fun speak() {
        val text : String = mEditText_Sentence.text.toString()
        var pitch : Float = (mSeekbar_Pitch.progress / 50) as Float
        if (pitch < 0.1) pitch = 0.1f
        var speed : Float = (mSeekbar_Speed.progress / 50) as Float
        if (speed < 0.1) speed = 0.1f
        mTTS.setPitch(pitch)
        mTTS.setSpeechRate(speed)
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }

    override fun onDestroy() {
        if (mTTS != null) {
            mTTS.stop()
            mTTS.shutdown()
        }
        super.onDestroy()
    }
}