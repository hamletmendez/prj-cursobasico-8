package com.terecos.www.tictactoe;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static int gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Black Diamonds.ttf");
        TextView tv_status = (TextView)findViewById(R.id.text_title);
        tv_status.setTypeface(typeface);

        Button btnSingle = (Button)findViewById(R.id.button_single);
        btnSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = 1;
                Intent intent = new Intent(MainActivity.this,GameLogicActivity.class);
                startActivity(intent);
            }
        });

        Button btnVersus = (Button)findViewById(R.id.button_versus);
        btnVersus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = 2;
                Intent intent = new Intent(MainActivity.this,GameLogicActivity.class);
                startActivity(intent);
            }
        });


    }
}
