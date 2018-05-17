package com.example.felix.lab_googlefirebaseactivity;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class BandsRunnableThread implements Runnable {

    private final Integer idBand;

    private FirebaseDatabase dbFirebase;
    private DatabaseReference dbReferenceCursor;

    private final String BAND = "band-";
    private String bandNode;
    private int bandIndex = 0;

    private Integer id;
    private String band;
    private String genre;
    private String album;
    private String label;

    private ValueEventListener listenerCursor;

    public BandsRunnableThread(Integer idBand) {
        this.idBand = idBand;
    }

    @Override
    public void run() {

        loadContactosArray(idBand);
    }

    private void loadContactosArray(Integer idBand) {

        dbFirebase = FirebaseDatabase.getInstance();

        bandIndex = idBand;
        bandNode = BAND + bandIndex;

        if (dbReferenceCursor != null) {

            dbReferenceCursor.removeEventListener(listenerCursor);
        }

        dbReferenceCursor = dbFirebase.getReference(bandNode);

        listenerCursor = null;

        listenerCursor = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                id = dataSnapshot.child("id").getValue(Integer.class);

                if (id == null) {

                    Log.d("EHM ->", "BandsRunnableThread - > loadContactosArray() -> onDataChange id == null !!!");

                } else {

                    id = dataSnapshot.child("id").getValue(Integer.class);
                    band = dataSnapshot.child("band").getValue(String.class);
                    genre = dataSnapshot.child("genre").getValue(String.class);
                    album = dataSnapshot.child("album").getValue(String.class);
                    label = dataSnapshot.child("label").getValue(String.class);
                    Global.bandsArray.add(new Bands(id, band, genre, album, label));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.w("EHM ->", "BandsRunnableThread - > Database Error: ", error.toException());
            }
        };

        dbReferenceCursor.addValueEventListener(listenerCursor);
    }
}