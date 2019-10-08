package com.example.sqldata;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private GroceryAdapter mAdapter;
    private EditText mEditTextName;
    private TextView mTextViewAmount;
    private int mAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            GroceryDBHelper dbHelper = new GroceryDBHelper(this);
            mDatabase = dbHelper.getWritableDatabase();

        //   RecyclerView recyclerView = findViewById(R.id.recyclerview);
       //     recyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new GroceryAdapter(this, getAllItems());
         //   recyclerView.setAdapter(mAdapter);

            mEditTextName = findViewById(R.id.edittext_name);
            mTextViewAmount = findViewById(R.id.textview_amount);

            Button buttonGo = findViewById(R.id.go_activity);
            Button buttonIncrease = findViewById(R.id.button_increase);
            Button buttonDecrease = findViewById(R.id.button_decrease);
            Button buttonAdd = findViewById(R.id.button_add);

            buttonIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increase();
                }
            });

            buttonDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decrease();
                }
            });

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItem();
                }
            });
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(MainActivity.this,Favorite.class);
                startActivity(go);
            }
        });

        }

        private void increase() {
            mAmount++;
            mTextViewAmount.setText(String.valueOf(mAmount));
        }

        private void decrease() {
            if (mAmount > 0) {
                mAmount--;
                mTextViewAmount.setText(String.valueOf(mAmount));
            }
        }

        private void addItem() {

            if (mEditTextName.getText().toString().trim().length() == 0 || mAmount == 0) {
                return;
            }
            //mEditTextName.setText(getResources().getString(R.string.app_name));
           // String name = getResources().getString(R.string.app_name);
            String name = mEditTextName.getText().toString();
            ContentValues cv = new ContentValues();
            cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, name);
            cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, mAmount);

            mDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
            mAdapter.swapCursor(getAllItems());

            mEditTextName.getText().clear();
        }

        private Cursor getAllItems() {
            return mDatabase.query(
                    GroceryContract.GroceryEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
            );
        }
    }
