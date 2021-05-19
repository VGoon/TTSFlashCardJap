package com.example.flashcardaudiotracks;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
//        private LinkedHashMap<String, String> cards = new LinkedHashMap<String, String>();
        private ArrayList<Card> cardEntries;
        private OnSetClickListener onSetClickListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecycleAdapter(ArrayList<Card> map, OnSetClickListener onSetClickListener){
        this.onSetClickListener = onSetClickListener;
        if(map != null){
            Log.d("EDIT", "Map QTY: " + map.size());
            cardEntries = map;
        }else{
            Log.d("EDIT", "MAP null");
            cardEntries = new ArrayList<Card>();
        }
    }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            // each data item is just a string in this case
            public TextInputLayout termInput;
            public TextInputLayout defInput;
            public View layout;
            OnSetClickListener onSetClickListener;

            public ViewHolder(View v, OnSetClickListener onSetClickListener){//, MyEditTextListener editTextListener) {
                super(v);
                layout = v;
                termInput = (TextInputLayout) v.findViewById(R.id.cardTermInput);
                defInput = (TextInputLayout) v.findViewById(R.id.cardDefInput);

                termInput.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //Log.d("BEFORE TXT CHANGE", "Data: " + cardEntries.get(getAdapterPosition()).getTerm());
                    }
                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        cardEntries.get(getAdapterPosition()).setTerm(s.toString());
                        //Log.d("TXT CHANGE", "Data: " + s.toString());
                    }
                });

                defInput.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //Log.d("BEFORE TXT CHANGE", "Data: " + cardEntries.get(getAdapterPosition()).getDef());
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        cardEntries.get(getAdapterPosition()).setDef(s.toString());
                        //Log.d("TXT CHANGE", "Data: " + s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                this.onSetClickListener = onSetClickListener;
                v.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                onSetClickListener.onSetClick(getAdapterPosition());
            }

        }

        public interface OnSetClickListener{
            void onSetClick(int position);
        }

        public void add(String term, String def, int position) {
            cardEntries.add(new Card());
            notifyItemInserted(position);
        }

        public void remove(int position) {
            cardEntries.remove(position);
            notifyItemRemoved(position);
        }

    public void updateItem(String term, String def, int position) {
        cardEntries.get(position).setTerm(term);
        cardEntries.get(position).setDef(def);
        notifyItemInserted(position);
    }

        // Create new views (invoked by the layout manager)
        @Override
        public RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.card_layout, parent, false);
            // set the view's size, margins, paddings, and layout parameters
            ViewHolder vh = new ViewHolder(v, onSetClickListener);//, new MyEditTextListener());
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.termInput.getEditText().setText(cardEntries.get(position).getTerm());
            holder.defInput.getEditText().setText(cardEntries.get(position).getDef());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return cardEntries.size();//return cards.size();
        }
}
