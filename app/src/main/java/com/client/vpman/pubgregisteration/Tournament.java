package com.client.vpman.pubgregisteration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Tournament extends Fragment {
    View view;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private List<TournamentDetail> tournamentDetails = new ArrayList<>();
    Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ChildEventListener valueEventListener;
    List<String> key=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tournament, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        //layoutManager.setReverseLayout(true);
        layoutManager = new LinearLayoutManager(getActivity());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tournament_detail");
        adapter = new Adapter(tournamentDetails, getActivity().getApplicationContext(),key);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        attachDatabaseReadListener();

        return view;
    }


    private void attachDatabaseReadListener() {
        if (valueEventListener == null) {
            valueEventListener = new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    System.out.println("Hello" + String.valueOf(dataSnapshot.child("nameHolder1").getValue()));
                    TournamentDetail commentMessage = new TournamentDetail(
                            String.valueOf(dataSnapshot.child("nameHolder1").getValue()),
                            String.valueOf(dataSnapshot.child("descHold1").getValue()),
                            String.valueOf(dataSnapshot.child("dateHold1").getValue()),
                            String.valueOf(dataSnapshot.child("timeHold1").getValue()),
                            String.valueOf(dataSnapshot.child("moneyHold1").getValue()),
                            String.valueOf(dataSnapshot.child("image").getValue()));
                        commentMessage.setmKey(dataSnapshot.getKey());
                    tournamentDetails.add(commentMessage);
                    key.add(dataSnapshot.getKey());
                    Toast.makeText(getActivity(),dataSnapshot.getKey(),Toast.LENGTH_LONG).show();
                  //  Toast.makeText(getActivity(), "hey " + tournamentDetails.size(), Toast.LENGTH_LONG).show();
                    //mMessageAdapter.add(commentMessage);
                   /* adapter = new Adapter(tournamentDetails, getActivity().getApplicationContext());
                    recyclerView.setAdapter(adapter);*/
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            databaseReference.addChildEventListener(valueEventListener);
        }
    }

    public static Tournament newInstance(String text) {
        Tournament f = new Tournament();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

}
