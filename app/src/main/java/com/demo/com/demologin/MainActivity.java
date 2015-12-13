package com.demo.com.demologin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.et_userame);

    }

    public void submit(View view){
        Intent intent = new Intent(this,WorkService.class);
        userName = editText.getText().toString();
        intent.putExtra("name", userName);
        startService(intent);
    }

}
