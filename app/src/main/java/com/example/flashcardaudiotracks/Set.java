package com.example.flashcardaudiotracks;

import java.io.Serializable;
import java.util.ArrayList;

public class Set implements Serializable {
    private ArrayList<Card> setOfCards;
    private String name;

    public Set(){
        name = "";
        setOfCards = new ArrayList<Card>();
    }

    public Set(ArrayList<Card> setOfCards){
        name = "";
        this.setOfCards = setOfCards;
    }

    public Set(String name, ArrayList<Card> setOfCards){
        this.name = name;
        this.setOfCards = setOfCards;
    }

    public boolean isEmpty(){
        if(setOfCards.size() == 0)
            return true;
        return false;
    }

    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }

    public int getCount(){
        if(setOfCards != null)
            return setOfCards.size();
        else
            return 0;
    }

    public ArrayList<Card> getCards(){
        return setOfCards;
    }

    public void setCards(ArrayList<Card> cards){ this.setOfCards = cards; }
}
