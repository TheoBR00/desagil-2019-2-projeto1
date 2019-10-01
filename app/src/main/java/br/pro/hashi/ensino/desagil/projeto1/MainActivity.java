package br.pro.hashi.ensino.desagil.projeto1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SEND_SMS = 1;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sos_button = findViewById(R.id.sos);

        //FUNÇÃO PRO TTS FUNCIONAR
        tts = new TextToSpeech(getApplicationContext(), status -> {
            if(status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.US);
            }
        });


        // VAMOS USAR ESSA FUNÇÃO PARA USAR O TEXT-TO-SPEECH
        //tts.speak("This is a test", TextToSpeech.QUEUE_FLUSH, null, null);

        sos_button.setOnLongClickListener(view -> {
            sosClicked();
            return true;
        });

    }

    private void showToast(String text) {

        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);

        toast.show();
    }

    private void sendSMS(){

        String message = "Preciso de ajuda";


        String phone = "11956180714";

        if (!PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
            showToast("Número inválido!");
            return;
        }

        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(phone, null, message, null, null);
        showToast("Mensagem enviada com sucesso.");
    }
    public void sosClicked(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

            sendSMS();

        } else {

            String[] permissions = new String[]{
                    Manifest.permission.SEND_SMS,
            };

            ActivityCompat.requestPermissions(this, permissions, REQUEST_SEND_SMS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_SEND_SMS && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            sendSMS();
        }
    }
}

