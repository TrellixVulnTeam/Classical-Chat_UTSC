package com.epicodus.classicalchat.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.epicodus.classicalchat.Constants;
import com.epicodus.classicalchat.R;
import com.epicodus.classicalchat.adapters.FirebaseMeetupViewHolder;
import com.epicodus.classicalchat.models.Meetup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedMeetupListActivity extends AppCompatActivity {
    private DatabaseReference mMeetupReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meetups);
        ButterKnife.bind(this);

        mMeetupReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_MEETUPS);
        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Meetup, FirebaseMeetupViewHolder>(Meetup.class, R.layout.meetup_list_item_drag, FirebaseMeetupViewHolder.class, mMeetupReference) {


            @Override
            protected void populateViewHolder(FirebaseMeetupViewHolder viewHolder, Meetup model, int position) {
                viewHolder.bindMeetup(model);
            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }
}
