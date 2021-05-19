package com.example.flashcardaudiotracks;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class RecycleSetAdapter extends RecyclerView.Adapter<RecycleSetAdapter.ViewHolder> {
//    private LinkedHashMap<String, String> cards;
    private ArrayList<Card> cardEntries;
    private TextToSpeech tts;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView term;
        public TextView def;
        public View layout;
        public RadioGroup radioGroup;
        public RadioButton radioBtn1;
        public RadioButton radioBtn2;
        public RadioButton radioBtn3;
        public RadioButton radioBtn4;
        public ImageButton speakerBtn;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            term = (TextView) v.findViewById(R.id.setPageTerm1);
            def = (TextView) v.findViewById(R.id.setPageDef1);

            speakerBtn = (ImageButton) v.findViewById(R.id.setPageSound1);
            speakerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPlaySound(getAdapterPosition());
                }
            });

            radioGroup = (RadioGroup) v.findViewById(R.id.setPageCategories1);
            radioBtn1 = (RadioButton) v.findViewById(R.id.radioButton1);
            radioBtn2 = (RadioButton) v.findViewById(R.id.radioButton2);
            radioBtn3 = (RadioButton) v.findViewById(R.id.radioButton3);
            radioBtn4 = (RadioButton) v.findViewById(R.id.radioButton4);
        }
    }

    public void add(String term, String def, int position) {
        cardEntries.add(new Card(term, def));//.put(term, def);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        cardEntries.remove(position);
        notifyItemRemoved(position);
    }

    public void exchangeData(ArrayList<Card> newData){
        cardEntries = newData;
        notifyDataSetChanged();
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecycleSetAdapter(ArrayList<Card> map, TextToSpeech tts){
        cardEntries = map;
        this.tts = tts;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecycleSetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.cardset_display_layout, parent, false);
        // set the view's size, margins, paddings, and layout parameters
        RecycleSetAdapter.ViewHolder vh = new RecycleSetAdapter.ViewHolder(v);
        return vh;
    }

    // use LinkedHashMap if you want to read values from the hashmap in the same order as you put them into it
    private String getKeyAtIndex(LinkedHashMap<String, String> map, int index)
    {
        String[] keys = (String [])map.keySet().toArray();
        return keys[index];
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecycleSetAdapter.ViewHolder holder, final int position) {
        holder.term.setText(cardEntries.get(position).getTerm());
        holder.def.setText(cardEntries.get(position).getDef());
    }

    //NEED TO SET FOR BOTH TERM AND DEF VALUES
    public void onPlaySound(int position){
        //sound stuff - need an API
        Log.d("SPEAK", "Speaking " + cardEntries.get(position).getTerm());
        String str = cardEntries.get(position).getTerm();
        tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cardEntries.size();
    }
}
