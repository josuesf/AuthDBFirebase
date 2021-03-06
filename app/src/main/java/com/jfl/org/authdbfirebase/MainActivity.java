package com.jfl.org.authdbfirebase;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jfl.org.authdbfirebase.entities.UserEntity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UserEntityDetail";

    private String userId;
    private DatabaseReference mDatabase;
    private EditText etName,etLastName,etAge;
    private Button btnSaveData;
    private DatabaseReference mUserReference;
    private ValueEventListener mUserListener;
    private TextView tvBirthday,tvMsgSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.titleMain));
        //Get userId from preview Screen
        userId = getIntent().getStringExtra("userId");
        //Init Database Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Initial EditTexts
        etName = (EditText)findViewById(R.id.etName);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etAge = (EditText)findViewById(R.id.etAge);
        tvBirthday = (TextView) findViewById(R.id.tvBirthday);
        tvMsgSuccess = (TextView) findViewById(R.id.tvMsgSuccess);
        //Init Reference User
        mUserReference = mDatabase.child("users").child(userId);

        btnSaveData = (Button)findViewById(R.id.btnSaveData);
        btnSaveData.setOnClickListener(this);
        tvBirthday.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener userEntityListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get UserEntity object and use the values to update the UI
                UserEntity userEntity = dataSnapshot.getValue(UserEntity.class);
                // If user exists in database set data
                if(userEntity!=null) {
                    etName.setText(userEntity.name);
                    etLastName.setText(userEntity.lastName);
                    etAge.setText(userEntity.age);
                    tvBirthday.setText(userEntity.birthday);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(MainActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mUserReference.addValueEventListener(userEntityListener);
        mUserListener = userEntityListener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mUserListener!=null){
            mUserReference.removeEventListener(mUserListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSaveData:
                saveData();
                break;
            case R.id.tvBirthday:
                selectDate();
                break;
        }

    }

    private void selectDate() {
        final Calendar c = Calendar.getInstance();
        int mYear, mMonth, mDay;
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        String month = monthOfYear>9?String.valueOf(monthOfYear):("0"+ String.valueOf(monthOfYear));
                        String day = dayOfMonth>9?String.valueOf(dayOfMonth):("0"+ String.valueOf(dayOfMonth));
                        tvBirthday.setText(day + "/" + month + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void saveData() {
        if(dataValid()) {
            UserEntity userEntity = new UserEntity(
                    etName.getText().toString(),
                    etLastName.getText().toString(),
                    etAge.getText().toString(),
                    tvBirthday.getText().toString()
            );
            try {
                mDatabase.child("users").child(userId).setValue(userEntity);
                tvMsgSuccess.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, getString(R.string.msgSuccess), Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, getString(R.string.msgErrorSaving), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean dataValid() {
        boolean isValid = true;
        if(etName.getText().toString().isEmpty()) {
            etName.setError(getString(R.string.isRequired));
            isValid = false;
        }
        if(etLastName.getText().toString().isEmpty()) {
            etLastName.setError(getString(R.string.isRequired));
            isValid = false;
        }
        if(etAge.getText().toString().isEmpty()) {
            etAge.setError(getString(R.string.isRequired));
            isValid = false;
        }
        try{
            Integer.parseInt(etAge.getText().toString());
        }catch (Exception ex){
            etAge.setError(getString(R.string.shouldBeNumber));
            isValid = false;
        }
        return isValid;
    }
}
