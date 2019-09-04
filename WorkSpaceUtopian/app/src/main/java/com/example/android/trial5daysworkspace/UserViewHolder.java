package com.example.android.trial5daysworkspace;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UserViewHolder extends RecyclerView.ViewHolder {


    // Init Textviews.
    TextView Name, Phone, College, Place, StartTime, EndTime;
    ImageView ActiveCust;

    // Constructor.
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        // itemView var Access [R.layout.textviews_retrieve_data].
        // Views By ID.
        Name = (TextView) itemView.findViewById(R.id.retrieve_name);
        Phone = (TextView) itemView.findViewById(R.id.retrieve_phone);
        College = (TextView) itemView.findViewById(R.id.retrieve_college);
        Place = (TextView) itemView.findViewById(R.id.retrieve_place);
        StartTime = (TextView) itemView.findViewById(R.id.retrieve_start_time);
        EndTime = (TextView) itemView.findViewById(R.id.retrieve_end_time);

        ActiveCust = (ImageView) itemView.findViewById(R.id.cust_active);

    }
}
