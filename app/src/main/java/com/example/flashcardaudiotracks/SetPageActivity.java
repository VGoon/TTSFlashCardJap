package com.example.flashcardaudiotracks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.mlkit.nl.languageid.LanguageIdentifier;

import java.util.ArrayList;

public class SetPageActivity extends AppCompatActivity {

    int LAUNCH_FLASHCARD_ACTIVITY = 1;
    int SET_TO_EDIT_ACTIVITY = 2;

    Set currentSet;
    ArrayList<Card> cardEntries;

    RecyclerView recycleView;
    private RecyclerView.Adapter rAdapter;
    private RecyclerView.LayoutManager recycleLayoutM;
    private RecyclerView.ViewHolder vHolder;

    private TextView header;
    private TextView cardCount;
    ImageButton backButton;

    TextToSpeech textToSpeech;
    LanguageIdentifier languageIdentifier;
    String language;

//    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_page);

//        dbHandler = new DBHandler(this);

        currentSet = (Set) getIntent().getSerializableExtra("CARDSET");
//        Log.d("SET", "Retrieve currentSet: " + currentSet.getCount());
        cardEntries = currentSet.getCards();

        header = findViewById(R.id.SetPageHeader);
        header.setText(currentSet.getName());

        cardCount = findViewById(R.id.setCardCount);
        cardCount.setText(currentSet.getCount() + " cards");

//        textToSpeech = new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(status != TextToSpeech.ERROR) {
//                    textToSpeech.setLanguage(Locale.JAPANESE);
//                }
//            }
//        });

        //setting play btn onClick
        ImageButton playBtn = findViewById(R.id.setPlayButton);
        playBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                playBtnClick();
            }
        });

        //setting flashCard btn onCLick
        ImageButton flashCardBtn = findViewById((R.id.setFlashCardButton));
        flashCardBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                flashCardBtnClick();
            }
        });

        ImageButton editBtn = findViewById(R.id.setPageEditButton);
        editBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                editClick();
            }
        });

        backButton = findViewById(R.id.setsBackButton);
        backButton.setOnClickListener(new View .OnClickListener(){
            public void onClick(View v){ backButtonClick(); }
        });

        recycleView = findViewById(R.id.setRecycleCards);
        recycleView.setHasFixedSize(true);

        recycleLayoutM = new LinearLayoutManager(this);
        recycleView.setLayoutManager(recycleLayoutM);
        rAdapter = new RecycleSetAdapter(cardEntries, textToSpeech);//cards);
        recycleView.setAdapter(rAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == LAUNCH_FLASHCARD_ACTIVITY) {
//            if(resultCode == Activity.RESULT_OK){
//                //String result = data.getStringExtra("result");
////                cardEntries = (ArrayList<Card>)data.getSerializableExtra("cards");
////                fillCards();
//                rAdapter.notifyDataSetChanged();
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
//        }

        //return from edit activity from pressing edit button
        if (requestCode == SET_TO_EDIT_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                ArrayList<Card> result = (ArrayList<Card>) data.getSerializableExtra("cardsFromEdit");
//                Log.d("SET", "Entered Set Activity Return.");
                String title = data.getStringExtra("setName");
                if(result == null){
                    Log.d("SET ERROR", "Title: " + title + " | Result = Null");
                }else{
                    //update the title
                    currentSet.setName(title);
                    header.setText(currentSet.getName());

                    Log.d("SET", "Result QTY: " + result.size());

                    cardEntries.clear();
                    cardEntries.addAll(result);
                    rAdapter.notifyDataSetChanged();
                    currentSet.setCards(cardEntries);

                    cardCount.setText(cardEntries.size() + " cards");
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("SETPAGE", "No cards left?");
                //Write your code if there's no result
            }
        }
    }

//    @Override
//    public void onBackPressed() {
//        backButtonClick();
//    }

//    private void checkLanguage(String strToCheck){
//        languageIdentifier.identifyLanguage(strToCheck)
//                .addOnSuccessListener(
//                        new OnSuccessListener<String>() {
//                            @Override
//                            public void onSuccess(@Nullable String languageCode) {
//                                if (languageCode.equals("und")) {
//                                    Log.i("LANGUAGE", "Can't identify language.");
//                                } else {
//                                    Log.i("LANGUAGE", strToCheck + " Language: " + languageCode);
//                                    language = languageCode.toString();
//                                }
//                            }
//                        })
//                .addOnFailureListener(
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Model couldn’t be loaded or other internal error.
//                                // ...
//                                Log.d("LANGUAGE ERROR","error in language");
//                            }
//                        });
//    }

    private void editClick(){
        Intent editIntent = new Intent(this, EditActivity.class);
        editIntent.putExtra("setCARDS", cardEntries);
        editIntent.putExtra("setName", currentSet.getName());
        startActivityForResult(editIntent, SET_TO_EDIT_ACTIVITY);
    }

    //playBtn onClick method
    private void playBtnClick(){
        //sends to play activity
//        Intent playIntent = new Intent(this, );
//        this.startActivity(playIntent);
    }

    //flashCardBtn onClick method
    private void flashCardBtnClick(){
        //sends to flashcard activity
//        Intent flashCardIntent = new Intent(this, FlashCardActivity.class);
//        this.startActivityForResult(flashCardIntent, LAUNCH_FLASHCARD_ACTIVITY);
    }

    private void backButtonClick(){
        int position = getIntent().getIntExtra("SETPOSITION", -1);
        Intent toMain = new Intent(this, MainActivity.class);
        toMain.putExtra("setCARDS", cardEntries);
        toMain.putExtra("setName", currentSet.getName());
        toMain.putExtra("position", position);
        setResult(Activity.RESULT_OK, toMain);
        finish();
    }
}
