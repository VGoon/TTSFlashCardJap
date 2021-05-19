package com.example.flashcardaudiotracks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardaudiotracks.ui.DBHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Iterator;

public class EditActivity extends AppCompatActivity implements RecycleAdapter.OnSetClickListener{

    private RecyclerView recycleCards;
    private RecyclerView.Adapter rAdapter;
    private RecyclerView.LayoutManager recycleLayoutM;
    private RecyclerView.ViewHolder vHolder;

    ArrayList<Card> cardEntries;
    TextInputLayout titleField;

    String originalSetName;

    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dbHandler = new DBHandler(this);

        titleField = findViewById(R.id.editTitleInputLayout);

        recycleCards = findViewById(R.id.editRecycleCards);
        recycleCards.setHasFixedSize(true);

        recycleLayoutM = new LinearLayoutManager(this);
        recycleCards.setLayoutManager(recycleLayoutM);

        Intent intent = getIntent();
        if(intent == null){
            cardEntries = new ArrayList<Card>();
            cardEntries.add(new Card());
            cardEntries.add(new Card());
        } else{
            cardEntries = (ArrayList<Card>) intent.getSerializableExtra("setCARDS");
            titleField.getEditText().setText(intent.getStringExtra("setName"));
            originalSetName = intent.getStringExtra("setName");
            if(cardEntries == null){
                cardEntries = new ArrayList<Card>();
                cardEntries.add(new Card());
                cardEntries.add(new Card());
            }
        }

        rAdapter = new RecycleAdapter(cardEntries, this);
        recycleCards.setAdapter(rAdapter);

        ImageButton confirmBtn = findViewById((R.id.editConfirmedButton));
        confirmBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finishEdit();
            }
        });

        FloatingActionButton addCardBtn = findViewById(R.id.editAddCardButton);
        addCardBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                addCard();
            }
        });
    }

    @Override
    public void onSetClick(int position) {
        //updateCard(position);
    }

    private void addCard(){
        cardEntries.add(new Card());
        rAdapter.notifyItemInserted(cardEntries.size()-1);
    }

    //updates database
    private void updateDB(){
        String setName = titleField.getEditText().getText().toString();
        String qty = "" + cardEntries.size();

        //add set and add/update cards
        dbHandler.addSet(originalSetName, setName, qty);
        dbHandler.addAndUpdateCards(setName, cardEntries);
    }

    //returns ArrayList<Cards> to class that called it
    private void finishEdit(){
//        removeEmptyCards();
        Intent rtnIntent = new Intent(this, getIntent().getClass());
        if(cardEntries.size() != 0) {
            Log.d("EDIT -> DB","DB about to be updated.");
            updateDB();
            Log.d("EDIT -> DB","DB updated.");
            rtnIntent.putExtra("cardsFromEdit", cardEntries);
            rtnIntent.putExtra("setName", titleField.getEditText().getText().toString());
            setResult(Activity.RESULT_OK, rtnIntent);
            finish();
        }else{
            Log.d("EDIT", "No cards made");
            setResult(Activity.RESULT_CANCELED, rtnIntent);
            finish();
        }
    }

    //ISSUE
    //remove all cards that arn't properly set
    private void removeEmptyCards(){
        Card c;
        Iterator<Card> iter = cardEntries.iterator();
        while (iter.hasNext()) {
            c = iter.next();
            if(c.checkIfSet())
                iter.remove();
        }
    }
}

