package com.example.bet1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Adapter.GuesserAdapter;
import Adapter.UserAdapter;
import Model.User;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MotherProfile extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    TextView mother_name;
    Button enter_guess;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String date;
    User user1;
    private List<User> mUsers;
    private GuesserAdapter userAdapter;
    private RecyclerView recyclerView;
    Button homepage;
    TextView text_end_game;

    Boolean set_date = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_profile);
        text_end_game = findViewById(R.id.text_end_game);
        homepage = findViewById(R.id.homepage);
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsers.clear();
                Intent intent = new Intent(MotherProfile.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        mother_name = findViewById(R.id.mother_name);
        enter_guess = findViewById(R.id.enter_guess);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String newString;
        Bundle extras = getIntent().getExtras();
        recyclerView = findViewById(R.id.recycler_view_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUsers = new ArrayList<>();
        userAdapter = new GuesserAdapter(this,mUsers, false);
        recyclerView.setAdapter(userAdapter);
        if(extras == null) {
            newString= null;
        } else {
            newString= extras.getString("publisherId");
            Log.i(newString,"sdsa");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Mother").child(newString);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user1 = dataSnapshot.getValue(User.class);
                    Log.i("mother name",user1.getName());
                    mother_name.setText(user1.getName());
                    readUsers();
                    DatabaseReference reference_3 = FirebaseDatabase.getInstance().getReference().child("EndGame").child("mothers").child((user1.getId()));
                    reference_3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                 text_end_game.setVisibility(View.VISIBLE);
                                enter_guess.setVisibility(View.GONE);
                            }else{
                                text_end_game.setText("Game is still on");
                                text_end_game.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        //


        //

        enter_guess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        MotherProfile.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                date = month + "/" + day + "/" + year;

                Query query = FirebaseDatabase.getInstance().getReference().child("Guesses").child("mothers")
                        .child(user1.getId()).child("guessers").orderByChild("date").startAt(date).endAt((date) + "\uf8ff");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //String op = (String) snapshot.getValue();
                            Log.i("set_date", "The date already guessed by someone else, choose another date");

                            //Make toast that date guessed already
                            Context context = getApplicationContext();
                            CharSequence text = "The date already guessed by someone else, choose another date";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            //
                            set_date = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                if (set_date == true) {
                    FirebaseDatabase.getInstance().getReference().child("Guesses").child("mothers").
                            child(user1.getId()).child("guessers").child(firebaseUser.getUid()).child("date").setValue(date);
                    set_date = true;
                }
            }
        };


    }
    private void readUsers(){
        //mUsers.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Guesses").child("mothers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        if(user1 != null) {
                            if (user1.getId().equals(snapshot.getKey().toString())) {
                                Log.i("hail", "rainy day");
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                        String gg = snapshot2.getKey().toString();
                                        String guessed_date_user = null;
                                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                            guessed_date_user = snapshot3.getValue().toString();
                                        }
                                        Log.i("this followrt", guessed_date_user);
                                        searchUser(gg, guessed_date_user);
                                    }

                                }

                            }
                        }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchUser(String gg, final String guessed_date) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("id").startAt(gg).endAt(gg + "\uf8ff" );
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    user.setDate(guessed_date);
                    mUsers.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
