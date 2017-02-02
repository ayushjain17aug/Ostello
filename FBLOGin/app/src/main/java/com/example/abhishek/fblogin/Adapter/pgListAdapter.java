package com.example.abhishek.fblogin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.abhishek.fblogin.API.PG;
import com.example.abhishek.fblogin.R;
import com.example.abhishek.fblogin.helper.App;
import com.example.abhishek.fblogin.helper.DataBaseHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class pgListAdapter extends RecyclerView.Adapter<pgListAdapter.MyViewHolder> {

    private final DataBaseHandler db;
    public Context context;
    public ArrayList<PG> hostelList;
    public ArrayList<Integer> favList;

    public pgListAdapter(Context context, ArrayList<PG> hostelList, ArrayList<Integer> favList) {
        this.hostelList = hostelList;
        this.context = context;
        this.favList = favList;
        db = new DataBaseHandler(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_pg_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PG pg = hostelList.get(position);
        App.showProgressBar(holder.progressBar);
        Picasso.with(context).load(pg.getImage_url()).into(holder.img, new com.squareup.picasso.Callback() {

            @Override
            public void onSuccess() {
                App.hideProgressBar(holder.progressBar);
            }

            @Override
            public void onError() {
                App.hideProgressBar(holder.progressBar);
            }
        });
        if (favList.contains(pg.getId()))
            holder.favButton.setImageResource(R.drawable.red_heart);
        else
            holder.favButton.setImageResource(R.drawable.heart_outline);
        holder.txt.setText(pg.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(context, EventDetailsActivity.class);
                // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //i.putExtra("event_description", boundEvent.getEventName() + "\n\n"
                //        + boundEvent.getEventType() + "\n" + boundEvent.getEventDescription() + "\n");
                //i.putExtra("id", boundEvent.getEventId());
                //i.putExtra("event_name", boundEvent.getEventName());
                //context.startActivity(i);
            }
        });
        holder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!favList.contains(pg.getId())) {
                    holder.favButton.setImageResource(R.drawable.red_heart);
                    db.addFavPg(pg.getId(), pg.getName());
                    favList = db.getFavPGs();
                } else {
                    holder.favButton.setImageResource(R.drawable.heart_outline);
                    (new DataBaseHandler(context)).deleteFavPg(pg.getId());
                    favList = db.getFavPGs();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return hostelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView txt;
        public final ImageView img;
        public final ProgressBar progressBar;
        public final ImageButton favButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            txt = (TextView) itemView.findViewById(R.id.hostel_name);
            img = (ImageView) itemView.findViewById(R.id.hostel_pic);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            favButton = (ImageButton) itemView.findViewById(R.id.favButton1);
        }
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }
}