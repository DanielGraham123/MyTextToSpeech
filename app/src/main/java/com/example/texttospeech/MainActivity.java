package com.example.texttospeech;

import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int TTS_ENGINE_REQUEST = 101;
    private TextToSpeech textToSpeech;
    private EditText TextForSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextForSpeech = findViewById(R.id.speech_text);
    }

    public void performSpeech(View view) {

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, TTS_ENGINE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == TTS_ENGINE_REQUEST && resultCode==TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

            textToSpeech = new TextToSpeech(this, this);

        } else {
            Intent installIntent = new Intent();
            installIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivity(installIntent);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

//            int languageStatus = textToSpeech.setLanguage(Locale.US);
            int languageStatus = textToSpeech.setLanguage(textToSpeech.getDefaultLanguage());

            if (languageStatus == TextToSpeech.LANG_MISSING_DATA || languageStatus == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language is not supported", Toast.LENGTH_SHORT).show();
            } else {
                String data = TextForSpeech.getText().toString();
                int speechStatus = textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null);
                
                if (speechStatus == TextToSpeech.ERROR) {
                    Toast.makeText(this, "Error while Speech...", Toast.LENGTH_SHORT).show();
                }
            }

        } else {
            Toast.makeText(this, "Text To Speech Engine failed...", Toast.LENGTH_SHORT).show();
        }
    }
}
