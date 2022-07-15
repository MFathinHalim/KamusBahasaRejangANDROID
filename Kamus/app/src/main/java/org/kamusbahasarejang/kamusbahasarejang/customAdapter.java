package org.kamusbahasarejang.kamusbahasarejang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class customAdapter extends RecyclerView.Adapter<customAdapter.MyViewHolder> {
    private Context context;
    private ArrayList id,rjg;

    customAdapter(Context context, ArrayList id, ArrayList rjg){
        this.context = context;
        this.id = id;
        this.rjg = rjg;
    }


    @NonNull
    @Override
    public customAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.myrow,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull customAdapter.MyViewHolder holder, int position) {
        holder.id_text.setText(String.valueOf(rjg.get(position))+"=");
        holder.rjg_text.setText(String.valueOf(id.get(position)));
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id_text,rjg_text;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id_text = itemView.findViewById(R.id.idtext);
            rjg_text = itemView.findViewById(R.id.rjgtext);

        }
    }
}
