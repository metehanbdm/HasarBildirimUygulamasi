package com.example.rsyazilim.rs_ihbar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rsyazilim.rs_ihbar.model.DamageRecordInfo;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<DamageRecordInfo> items;
    private Context context;

    public RecyclerViewAdapter(Context context, List<DamageRecordInfo> items)
    {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View convertView = LayoutInflater.from(context).inflate(R.layout.activity_list_single, null);
        return new ViewHolder(convertView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position)
    {
        DamageRecordInfo currRecord = items.get(position);

        holder.title.setText(currRecord.getDate());
        holder.desc.setText(currRecord.getInformation());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title , desc;
        public ImageView image;

        public ViewHolder(View convertView) {
            super(convertView);

            title = convertView.findViewById(R.id.listSingle_title);
            desc = convertView.findViewById(R.id.listSingle_desc);

        }
    }
}
