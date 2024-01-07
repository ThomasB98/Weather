package com.example.weather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class weatherRVAdapter extends RecyclerView.Adapter<weatherRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<weatherRvModal> weatherRvModalArrayList;

    public weatherRVAdapter(Context context, ArrayList<weatherRvModal> weatherRvModalArrayList) {
        this.context = context;
        this.weatherRvModalArrayList = weatherRvModalArrayList;
    }


    @NonNull
    @Override
    public weatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.weather_rv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull weatherRVAdapter.ViewHolder holder, int position) {
        weatherRvModal modal=weatherRvModalArrayList.get(position);
        holder.TempTV.setText(modal.getTemperature());
        Picasso.get().load("https:".concat(modal.getIcon())).into(holder.conditionIV);
        holder.windTV.setText(modal.getWindSpeed()+"km/h");
        SimpleDateFormat input=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output=new SimpleDateFormat("hh:mm aa");
        try{
            Date t=input.parse(modal.getTime());
            holder.TimeTV.setText(output.format(t));
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weatherRvModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView windTV,TempTV,TimeTV;
        private ImageView conditionIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            windTV=itemView.findViewById(R.id.idTVWindSpeed);
            TempTV=itemView.findViewById(R.id.idTVTemp);
            TimeTV=itemView.findViewById(R.id.idTVTime);

            conditionIV=itemView.findViewById(R.id.idIVCondition);
        }
    }
}
