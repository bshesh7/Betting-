package com.example.bet1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

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

import Adapter.UserAdapter;
import Model.User;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeActivity extends AppCompatActivity {

    Button searchButton;
    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private List<User> mUsers;
    private UserAdapter userAdapter;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button end_game;
    TextView text_end_game;
    TextView actual_date;

    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        end_game = findViewById(R.id.end_game);
        text_end_game = findViewById(R.id.text_end_game);
        actual_date = findViewById(R.id.actual_date);



        DatabaseReference reference_1 = FirebaseDatabase.getInstance().getReference().child("Users").child((firebaseUser.getUid()));
        reference_1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String nameOfuser = user.getName();
                String isMother = user.getIsMother();
                if(isMother.equals("Mother")){
                    end_game.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //
        DatabaseReference reference_2 = FirebaseDatabase.getInstance().getReference().child("EndGame").child("mothers").child((firebaseUser.getUid()));
        reference_2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    text_end_game.setVisibility(View.VISIBLE);
                    actual_date.setText(date);
                    Log.i("existss","existss");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        end_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        HomeActivity.this,
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

                FirebaseDatabase.getInstance().getReference().child("EndGame").child("mothers").
                        child(firebaseUser.getUid()).setValue(date);
                FirebaseDatabase.getInstance().getReference().child("EndGameBoolean").child("mothers").
                        child(firebaseUser.getUid()).child("id").setValue(firebaseUser.getUid());
                FirebaseDatabase.getInstance().getReference().child("EndGameBoolean").child("mothers").
                        child(firebaseUser.getUid()).child("date_ended?").setValue("true");
            }
        };
        //Query if the mother has already ended the game. If yes then is display the actual delievery date.







            searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsers.clear();
                userAdapter.notifyDataSetChanged();
                startActivity(new Intent(HomeActivity.this,SearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        recyclerView = findViewById(R.id.recycler_view_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(this,mUsers, false);
        recyclerView.setAdapter(userAdapter);
        readUsers();
    }
    private void readUsers(){
        mUsers.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child((firebaseUser.getUid())).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String gg = snapshot.getKey().toString();
                    searchUser(gg);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void searchUser(String s){
        Query query = FirebaseDatabase.getInstance().getReference().child("Mother")
                .orderByChild("id").startAt(s).endAt(s + "\uf8ff" );
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
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