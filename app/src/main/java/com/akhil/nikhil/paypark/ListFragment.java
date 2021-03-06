package com.akhil.nikhil.paypark;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {


    List<createaccount> parking_list ;

    RecyclerView recyclerView ;

    LinearLayout search_view ;


    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parking_list = new ArrayList<>();

        search_view = view.findViewById(R.id.search_view);

        recyclerView = view.findViewById(R.id.recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));


        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(getActivity() , PlacePickerActivity.class) , 100);
            }
        });


        get_parkings();

    }


    public class view_holder extends RecyclerView.ViewHolder
    {

        public TextView location , car_charges , bike_charges ;

        public view_holder(View itemView) {
            super(itemView);

            location = itemView.findViewById(R.id.location);

            car_charges = itemView.findViewById(R.id.car_charge);

            bike_charges = itemView.findViewById(R.id.bike_charge);

        }
    }


    public class Adapter extends RecyclerView.Adapter<view_holder>
    {

        @Override
        public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new view_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_detail_list_cell , parent , false));
        }

        @Override
        public void onBindViewHolder(view_holder holder, int position) {

            final createaccount data = parking_list.get(position);

            holder.location.setText(data.address);

            holder.bike_charges.setText(data.bike_charges);

            holder.car_charges.setText(data.car_charges);

            holder.location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = String.format(Locale.ENGLISH, "geo:%s,%s", data.lat, data.lng);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    getContext().startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return parking_list.size();
        }
    }

    private void get_parkings()
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        database.getReference().child("parking_details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    createaccount data = dataSnapshot1.getValue(createaccount.class);

                    if(data.available.equals("yes")) {

                        parking_list.add(data);
                    }
                }

                recyclerView.setAdapter(new Adapter());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100)
        {


        }

    }
}
