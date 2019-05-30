package com.example.weerapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.weerapp.R;
import com.example.weerapp.model.WeerObject;

import java.util.ArrayList;
import java.util.List;

public class WeerGeschiedenis extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private WeerViewModel mWeerViewModel;
    private List<WeerObject> mWeerObjects;
    private WeerObjectAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weer_geschiedenis);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.caseclosed);

        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mWeerObjects = new ArrayList<>();

        mWeerViewModel = ViewModelProviders.of(this).get(WeerViewModel.class);
        mWeerViewModel.getWeerObjects().observe(this, new Observer<List<WeerObject>>() {

            @Override
            public void onChanged(@Nullable List<WeerObject> weerObjects) {
                mWeerObjects = weerObjects;
                updateUI();
            }

        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());
                        mWeerViewModel.delete(mWeerObjects.get(position));
                        mWeerObjects.remove(position);
                        mAdapter.notifyItemRemoved(position);

                        //geluid bij swipen
                        mediaPlayer.start();
                    }

                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addOnItemTouchListener(this);
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new WeerObjectAdapter(mWeerObjects);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mWeerObjects);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_geschiedenis, menu);
        return true;
    }

    // alle objecten verwijderen door op prullenbak te klikken
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.verwijderen) {
            if (mWeerObjects.isEmpty()) {
                Toast.makeText(WeerGeschiedenis.this, "Er zijn geen items om te verwijderen", Toast.LENGTH_LONG).show();
            }
            else {

                mWeerViewModel.deleteAll();
                mediaPlayer.start();
            }

            return true;
        }
        else if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
