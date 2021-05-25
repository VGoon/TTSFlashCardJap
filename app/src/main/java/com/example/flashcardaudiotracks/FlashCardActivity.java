package com.example.flashcardaudiotracks;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class FlashCardActivity extends AppCompatActivity {

    TextView flashCardText;
    ImageButton muteBtn;
    ImageButton nextBtn;
    ImageButton backBtn;
    TextView cardsCompletedText;
    ImageButton playBtn;

    TextToSpeech tts;

    ArrayList<Card> cards;
    Handler h;
    Runnable r;
    String title;
    int cardNumber = 0;
    int totalCards;
    int seed;
    String term;
    String def;
    boolean mute = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        Intent intent = getIntent();
        cards = (ArrayList<Card>) intent.getSerializableExtra("setCARDS");
        title = intent.getStringExtra("setName");
        totalCards = cards.size();
        tts = new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.JAPANESE);
                }
            }
        });

        flashCardText = findViewById(R.id.FlashCardText);
        muteBtn = findViewById(R.id.FlashMuteButton);
        nextBtn = findViewById(R.id.FlashNextButton);
        backBtn = findViewById(R.id.FlashCardBackButton);
        cardsCompletedText = findViewById(R.id.leftOverText);
        playBtn = findViewById(R.id.FlashPlayBtn);

        h = new Handler();

        flashCardText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                onClickCard();
            }
        });

        muteBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                onClickMute();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                onClickNext();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                onEndCards();
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPlay();
            }
        });

        seed = (int)Math.floor(Math.random() * 10_000);
        randomizeCards();
        onClickNext(); // -> set cards up for first time
    }

    private void onClickCard(){
        String str = flashCardText.getText().toString();
//        cardFlipAnimation();
        if(str.equals(term)){
            flashCardText.setText(def);
            if(!mute)
                playCardSound(def);
        }else{
            flashCardText.setText(term);
            if(!mute)
                playCardSound(term);
        }
    }

//    private void cardFlipAnimation(){
//
//    };

    private void onClickMute() {
        mute = !mute;
    }

    private void onClickNext(){
        if(cardNumber == totalCards)
            onEndCards();
        else {
            changeNumberLeftOver();
            changeCardText();
        }
    }

    private void playCurrent(){
        String str = flashCardText.getText().toString();
        if(str.equals(term)){
            playCardSound(term);
        }else{
            playCardSound(def);
        }
    }

    private void onClickPlay(){
        mute = false;

        new CountDownTimer(10000, 5000) {

            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished < 5000){
                    flashCardText.setText(def);
                }
                playCurrent();
            }

            public void onFinish() {
//                Log.d("TAG","DONE");
                if(cardNumber-1 == totalCards)
                    onEndCards();

                changeNumberLeftOver();
                term = cards.get(cardNumber-1).getTerm();
                def = cards.get(cardNumber-1).getDef();
                flashCardText.setText(term);
                onClickPlay();
            }
        }.start();
    }

    private void playCardSound(String str){
        if(str.equals(term)){
            //play Jap
            tts.setLanguage(Locale.JAPANESE);
        }else {
            //play Eng
            tts.setLanguage(Locale.ENGLISH);
        }

        tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
    }

    //sets to term in beginning, when card pressed -> set to opposite
    private void changeCardText(){
        term = cards.get(cardNumber-1).getTerm();
        def = cards.get(cardNumber-1).getDef();
        flashCardText.setText(term);
        if(!mute)
            playCardSound(term);
    }

    //changes the text at the top of screen to reflect # of cards left over
    private void changeNumberLeftOver(){
        cardNumber++;
        String leftOver = cardNumber + " / " + totalCards + " cards";
        cardsCompletedText.setText(leftOver);
    }

    //places the cards in a random order to study in
    private void randomizeCards(){
        Collections.shuffle(cards, new Random(seed));
    }

    private void onEndCards(){
        tts.shutdown();
        finish();
    }
}