package com.example.flashcardaudiotracks;

import java.io.Serializable;

public class Card implements Serializable {
    private String term;
    private String def;

    public Card(){
        term = "";
        def = "";
    }

    public Card(String term, String def){
        this.term = term;
        this.def = def;
    }

    public void setTerm(String term){
        this.term = term;
    }

    public void setDef(String def){
        this.def = def;
    }

    public String getTerm(){
        return term;
    }

    public String getDef(){
        return def;
    }

    public boolean checkIfSet(){
        if(term.isEmpty() || def.isEmpty())
            return false;
        return true;
    }
}
