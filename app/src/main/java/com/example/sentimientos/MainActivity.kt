package com.example.sentimientos
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var buttonSpeak: Button? = null
    private var editText: EditText? = null
    private val positivo: List<String> = listOf("genial","bien","excelente","feliz","contento","alegre","alegria","felicidad","encanta","perfecto")
    private val negativo: List<String> = listOf("no","mal","odio","horrible","triste","feo","malo","extraño","deprimido","llorar")
    private var sump=0
    private var summ=0
    private var total=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSpeak = this.button_speak
        editText = this.edittext_input

        buttonSpeak!!.isEnabled = false;
        tts = TextToSpeech(this, this)

        buttonSpeak!!.setOnClickListener { speakOut() }
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
                buttonSpeak!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }

    }

    private fun speakOut() {
        val text = editText!!.text.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
        puntaje()
        recomendacion()
    }
    private fun puntaje(){
        val text = editText!!.text.toString()
        val delimiter = " "
        val parts = text.split(delimiter)
        for(i in parts.indices){
            for(j in positivo.indices){
                if(positivo[j].contains(parts[i])){
                    sump++
                }
            }
        }
        for(i in parts.indices){
            for(j in negativo.indices){
                if(negativo[j].contains(parts[i])){
                    summ++
                }
            }
        }
        total=sump-summ
    }
    private fun recomendacion(){
        if(total==0){
            val text="neutral"
            editText!!.setText(text)
            val textoR="Come un sopa de letras, un jugo de naranja y un poco de pescado"
            tts!!.speak(textoR, TextToSpeech.QUEUE_FLUSH, null,"")
            total=0
            summ=0
            sump=0
        }else if(total>0){
            val text="positivo"
            editText!!.setText(text)
            val textoR="Come un sopa de pollo, un agua de jamaica y milanesa"
            tts!!.speak(textoR, TextToSpeech.QUEUE_FLUSH, null,"")
            total=0
            summ=0
            sump=0
        }else if(total<0){
            val text="negativo"
            editText!!.setText(text)
            val textoR="Come un caldo de gallina, un agua de limon y lassagna"
            tts!!.speak(textoR, TextToSpeech.QUEUE_FLUSH, null,"")
            total=0
            summ=0
            sump=0
        }
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

}