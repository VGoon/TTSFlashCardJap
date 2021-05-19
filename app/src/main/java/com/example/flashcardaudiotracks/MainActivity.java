package com.example.flashcardaudiotracks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardaudiotracks.ui.DBHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity implements RecycleMainPageAdapter.OnSetClickListener {

    int LAUNCH_EDIT_FROM_MAIN = 3;
    int LAUNCH_SET_FROM_MAIN = 4;

    //          setName , setQty
    LinkedHashMap<String, Integer> setNames;
    ArrayList<Set> setEntries;
    RecyclerView recycleView;
    FloatingActionButton addSetBtn;

    private RecyclerView.Adapter rAdapter;
    private RecyclerView.LayoutManager recycleLayoutM;
    private RecyclerView.ViewHolder vHolder;

    public DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(this);

        setEntries = new ArrayList<Set>();
        setNames = new LinkedHashMap<String, Integer>();

        //fill data from database
        ArrayList<String> names = dbHandler.showSetTable();
        for(int i = 0; i < names.size(); i++){
            String name = names.get(i);
            ArrayList<Card> x = dbHandler.showCardTable(name);

            setNames.put(name, x.size());
            setEntries.add(new Set(name, x));
        }

        recycleView = findViewById(R.id.mainSetsRecyclerView);
        recycleView.setHasFixedSize(true);

        recycleLayoutM = new LinearLayoutManager(this);
        recycleView.setLayoutManager(recycleLayoutM);

        rAdapter = new RecycleMainPageAdapter(setNames, setEntries, this);//sets, this);
        recycleView.setAdapter(rAdapter);

        addSetBtn = findViewById(R.id.addSetsButton);
        addSetBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                onClickAddSet();
            }
        });
    }

    @Override
    public void onSetClick(int position) {
        onClickSet(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_EDIT_FROM_MAIN) {
            if(resultCode == Activity.RESULT_OK){
                ArrayList<Card> result = (ArrayList<Card>) data.getSerializableExtra("cardsFromEdit");
                String title = data.getStringExtra("setName");
                if(result == null){
                    Log.d("MAIN ERROR", "Title: " + title + " | Result = Null");
                }else{
                    Log.d("MAIN", "Got Result " + title + ", updating adapter. Result #1: " + result.get(0).getTerm());
                    setNames.put(title, result.size());
                    setEntries.add(new Set(title, result));
                    rAdapter.notifyDataSetChanged();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Log.d("EDIT->MAIN ERROR", "On return from Edit. No cards made so no set made. DO NOTHING." );
            }
        }

        if(requestCode == LAUNCH_SET_FROM_MAIN){
            if(resultCode == Activity.RESULT_OK){
                ArrayList<Card> result = (ArrayList<Card>) data.getSerializableExtra("setCARDS");
                String title = data.getStringExtra("setName");
                if(result == null){
                    Log.d("MAIN ERROR", "Title: " + title + " | Result = Null");
                }else{
                    Log.d("MAIN", "Got Result: " + title);
                    setNames.put(title, result.size());
                    int pos = data.getIntExtra("position", -1);
                    setEntries.get(pos).setCards(result);
                    setEntries.get(pos).setName(title);
                    rAdapter.notifyDataSetChanged();
                }
            }
            if(resultCode == Activity.RESULT_CANCELED){
                Log.d("SET->MAIN ERROR", "On return from Set." );
            }
        }
    }

    //to set page
    public void onClickSet(int position){
        if(setEntries.size() != 0) {
            Intent toSetPage = new Intent(this, SetPageActivity.class);
            toSetPage.putExtra("CARDSET", setEntries.get(position));
            toSetPage.putExtra("SETPOSITION", position);
            startActivityForResult(toSetPage, LAUNCH_SET_FROM_MAIN);
        }
    }

    //to edit page
    public void onClickAddSet(){
        Intent toEditPage = new Intent(this, EditActivity.class);
        startActivityForResult(toEditPage, LAUNCH_EDIT_FROM_MAIN);
    }
}