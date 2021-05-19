package com.example.flashcardaudiotracks;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class RecycleMainPageAdapter extends RecyclerView.Adapter<RecycleMainPageAdapter.ViewHolder> {

    //          setName , setQty
    private LinkedHashMap<String, Integer> setNames;
    private ArrayList<Set> setEntries;
    private OnSetClickListener onSetClickListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecycleMainPageAdapter(LinkedHashMap<String, Integer> namesMap, ArrayList<Set> map, OnSetClickListener onSetClickListener){
        this.onSetClickListener = onSetClickListener;
        if(map != null){
//            Log.d("MAIN ADAP", "Map QTY: " + map.size());
            setNames = namesMap;
            setEntries = map;
        }else{
            Log.d("MAIN ADAP", "MAP null");
            setNames = new LinkedHashMap<String, Integer>();
            setEntries = new ArrayList<Set>();
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView setName;
        public TextView amount;
        public View layout;
        OnSetClickListener onSetClickListener;

        public ViewHolder(View v, OnSetClickListener onSetClickListener) {
            super(v);
            layout = v;
            setName = (TextView) v.findViewById(R.id.SetTileName);
            amount = (TextView) v.findViewById(R.id.SetTileAmount);

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

    public void add(String setName, int position, ArrayList<Card> cards) {
        Log.d("ADDING SET","setName: " + setName);
        setNames.put(setName, cards.size());
        setEntries.add(new Set(setName, cards));

        notifyItemInserted(position);
    }

    public void remove(int position) {
        setNames.remove(setEntries.get(position).getName());
        setEntries.remove(position);
        notifyItemRemoved(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecycleMainPageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.setdisplay_layout, parent, false);
        // set the view's size, margins, paddings, and layout parameters
        ViewHolder vh = new ViewHolder(v, onSetClickListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setName.setText(setEntries.get(position).getName());
        String amount = setEntries.get(position).getCount() + " cards";
        holder.amount.setText(amount);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return setEntries.size();
    }
}
