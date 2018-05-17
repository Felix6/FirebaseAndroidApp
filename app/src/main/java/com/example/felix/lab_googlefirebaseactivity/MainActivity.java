package com.example.felix.lab_googlefirebaseactivity;

import android.graphics.ColorFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.NoSuchElementException;


public class MainActivity extends AppCompatActivity {
 // ----------EDIT TEXT-------------- //
    private EditText editTextId; // 1 Id
    private EditText editTextBand; //2 band
    private EditText editTextGenre; // 3 genero
    private EditText editTextAlbum; // 4 Album
    private EditText editTextLabel; // 5 label
 // ----------EDIT TEXT-------------- //

    private ImageButton imageBtnFind;
    private Integer id;
    private String band;
    private String genre;
    private String album;
    private String label;
    private FirebaseDatabase dbFirebase;
    private DatabaseReference dbReference;
    private final String BAND = "band-";
    private static int THREAD_TIME  = 100;
    private Handler handler;
    private String bandNode;
    private int bandIndex = 0;
    private Integer idBand;
    private Bands bands;
    private ValueEventListener listener;
    private Boolean isQryRecord = false;
    private ColorFilter colorFilter;
    private BandsRunnableThread bandsRunnableThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        editTextId = findViewById(R.id.editTextId);
        editTextBand = findViewById(R.id.editTextBand);
        editTextAlbum = findViewById(R.id.editTextAlbum);
        editTextLabel = findViewById(R.id.editTextLabel);
        editTextGenre = findViewById(R.id.editTextGenre);
        imageBtnFind = findViewById(R.id.imageBtnFind);
        setFieldsValues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toast){
            Toast.makeText(MainActivity.this,
                    "Android Programming is Cool!",
                    Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.action_about){
            Toast.makeText(MainActivity.this,
                    "App Developed by:\n Felix M. Colon\n Y00-44-1204", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFieldsValues() {

        bandNode = BAND + bandIndex;

        try {

            Global.isGetRecord = getRecordFromDatabase(bandNode);
            loadContactosArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean getRecordFromDatabase(String contactoNode) {

        Global.isGetRecord = false;
        dbFirebase = FirebaseDatabase.getInstance();
        dbReference = dbFirebase.getReference(contactoNode);
        listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                id = dataSnapshot.child("id").getValue(Integer.class);

                if (id == null) {

                    setFieldClear();
                    Global.isGetRecord = false;
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.msg_record_db_error),
                            Toast.LENGTH_LONG).show();
                } else {

                    editTextId.setText(id.toString());
                    band = dataSnapshot.child("band").getValue(String.class);
                    editTextBand.setText(band);
                    genre = dataSnapshot.child("genre").getValue(String.class);
                    editTextGenre.setText(genre);
                    album = dataSnapshot.child("album").getValue(String.class);
                    editTextAlbum.setText(album);
                    label = dataSnapshot.child("label").getValue(String.class);
                    editTextLabel.setText(label);

                    Global.isGetRecord = true;
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.msg_error_db),
                        Toast.LENGTH_LONG).show();
            }
        };

        dbReference.addValueEventListener(listener);
        return Global.isGetRecord;
    }

    public void insertDatabaseRecord(View view) {
        setFieldClear();
    }

    public void updateDatabaseRecord(View view) {
        if (TextUtils.isEmpty(editTextId.getText().toString())) {
            editTextId.setError(getResources().getString(R.string.msg_error_field));
            return;
        }

        if (TextUtils.isEmpty(editTextBand.getText().toString())) {
            editTextBand.setError(getResources().getString(R.string.msg_error_field));
            return;
        }

        if (TextUtils.isEmpty(editTextGenre.getText().toString())) {
            editTextGenre.setError(getResources().getString(R.string.msg_error_field));
            return;
        }

        if (TextUtils.isEmpty(editTextAlbum.getText().toString())){
            editTextAlbum.setError(getResources().getString(R.string.msg_error_field));
            return;
        }
        if (TextUtils.isEmpty(editTextLabel.getText().toString())){
            editTextLabel.setError(getResources().getString(R.string.msg_error_field));
            return;
        }

        bandIndex = Integer.parseInt(editTextId.getText().toString());
        bandNode = BAND + bandIndex;
        bands = new Bands(
                bandIndex,
                editTextBand.getText().toString(),
                editTextGenre.getText().toString(),
                editTextAlbum.getText().toString(),
                editTextLabel.getText().toString());
        dbFirebase.getReference(bandNode).setValue(bands);
        loadContactosArray();
        Toast.makeText(this,
                getResources().getString(R.string.msg_database_mod),
                Toast.LENGTH_LONG).show();
    }

    public void deleteDatabaseRecord(View view) {
        bandIndex = Integer.parseInt(editTextId.getText().toString());
        bandNode = BAND + bandIndex;
        dbFirebase.getReference(bandNode).removeValue();
        loadContactosArray();
        Toast.makeText(this,
                getResources().getString(R.string.msg_database_del),
                Toast.LENGTH_LONG).show();
        dbReference.removeEventListener(listener);
        bandIndex = 1;
        bandNode = BAND + bandIndex;
        getRecordFromDatabase(bandNode);
    }

    public void selectDatabaseRecord(View view) {
        Global.isGetRecord = false;
        if (!isQryRecord) {
            isQryRecord = true;
            colorFilter = imageBtnFind.getColorFilter();
            imageBtnFind.setColorFilter(ContextCompat.getColor(this, R.color.red),
                    android.graphics.PorterDuff.Mode.SRC_IN);
            imageBtnFind.setBackgroundTintList(ContextCompat.getColorStateList(this,
                    R.color.yellow));
            setFieldClear();

        } else {

            if (!editTextId.getText().toString().isEmpty()) {
                bandIndex = Integer.parseInt(editTextId.getText().toString());
                bandNode = BAND + bandIndex;
                dbReference.removeEventListener(listener);
                bandQueryThread bandQueryThread = new bandQueryThread(bandNode);
                new Thread(bandQueryThread).start();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        if (!Global.isGetRecord) {

                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.msg_record_db_error),
                                    Toast.LENGTH_LONG).show();

                        } else {

                            imageBtnFind.setColorFilter(colorFilter);
                            imageBtnFind.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),
                                    R.color.gray));
                            isQryRecord = false;
                            Global.isGetRecord = false;
                        }

                    }
                }, THREAD_TIME);

            } else {

                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.msg_record_db_error),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void nextDatabaseRecord(View view) {

        idBand = Integer.parseInt(editTextId.getText().toString());
        if (Global.bandsArrayIndexPosition.intValue() == (Global.bandsArray.size() - 1)) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.msg_database_last),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = Global.bandsArrayIndexPosition; i < Global.bandsArray.size(); i++) {
            Integer idBandInterator = Global.bandsArray.get(i).getId();
            String bandIterator = Global.bandsArray.get(i).getBand();
            String genreIterator = Global.bandsArray.get(i).getGenre();
            String albumIterator = Global.bandsArray.get(i).getAlbum();
            String labelIterator = Global.bandsArray.get(i).getLabel();

            if (idBandInterator.intValue() == idBand.intValue()) {
                Global.bandsArrayIndexPosition = 0;

            } else {

                try {

                    editTextId.setText(idBandInterator.toString());
                    editTextBand.setText(bandIterator);
                    editTextGenre.setText(genreIterator);
                    editTextAlbum.setText(albumIterator);
                    editTextLabel.setText(labelIterator);
                    idBand = Integer.parseInt(editTextId.getText().toString());
                    Global.bandsArrayIndexPosition = i;

                    break;

                } catch (NoSuchElementException e) {

                    Log.d("FMC ->", "nextDatabaseRecord() -> contactosIterator.next() ->  NoSuchElementException Error.");

                    break;
                }
            }
        }
    }

    public void prevoiusDatabaseRecord(View view) {

        idBand = Integer.parseInt(editTextId.getText().toString());

        if (Global.bandsArrayIndexPosition.intValue() == 0) {

            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.msg_database_first),
                    Toast.LENGTH_SHORT).show();

            return;
        }

        for (int i = Global.bandsArrayIndexPosition; i <= Global.bandsArray.size() - 1; i--) {

            Integer idIterator = Global.bandsArray.get(i).getId();
            String bandIterator = Global.bandsArray.get(i).getBand();
            String genreIterator = Global.bandsArray.get(i).getGenre();
            String albumIterator = Global.bandsArray.get(i).getAlbum();
            String labelIterator = Global.bandsArray.get(i).getLabel();

            if (idIterator.intValue() != idBand.intValue()) {

                try {

                    editTextId.setText(idIterator.toString());
                    editTextBand.setText(bandIterator);
                    editTextGenre.setText(genreIterator);
                    editTextAlbum.setText(albumIterator);
                    editTextLabel.setText(labelIterator);
                    idBand = Integer.parseInt(editTextId.getText().toString());
                    Global.bandsArrayIndexPosition = i;

                    break;

                } catch (NoSuchElementException e) {
                    Log.d("EHM ->", "prevoiusDatabaseRecord() -> contactosIterator.next() -> NoSuchElementException Error.");
                    break;
                }
            }
        }
    }

    private void setFieldClear() {
        id = 0;
        band = "";
        genre = "";
        album = "";
        label = "";
        editTextId.setText("");
        editTextBand.setText("");
        editTextGenre.setText("");
        editTextAlbum.setText("");
        editTextLabel.setText("");
    }

    private void loadContactosArray() {
        Global.bandsArray.removeAll(Global.bandsArray);
        Global.bandsArray.clear();
        handler = new Handler();
        for(int idNumber = 0; idNumber < 100; idNumber++) {
            bandsRunnableThread = new BandsRunnableThread(idNumber);
            handler.postDelayed(bandsRunnableThread, THREAD_TIME);
        }
    }

    private class ContactoQueryRecordAsyncTask extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... bandNode) {

            int count = bandNode.length;
            for (int i = 0; i < count; i++) {
                Global.isGetRecord = getRecordFromDatabase(bandNode[i]);
                if (isCancelled()) {
                    break;
                }
            }
            return Global.isGetRecord;
        }
        @Override
        protected void onPostExecute(Boolean isRecFound) {
            if (!Global.isGetRecord) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.msg_record_db_error),
                        Toast.LENGTH_LONG).show();
            } else {

                imageBtnFind.setColorFilter(colorFilter);
                imageBtnFind.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),
                        R.color.gray));
                isQryRecord = false;
                Global.isGetRecord = false;
            }
        }
    }

    class bandQueryThread implements Runnable {
        String bandNode;
        bandQueryThread(String bandNode) {
            this.bandNode = bandNode;
        }
        public void run() {
            Global.isGetRecord = getRecordFromDatabase(this.bandNode);
        }
    }
}
