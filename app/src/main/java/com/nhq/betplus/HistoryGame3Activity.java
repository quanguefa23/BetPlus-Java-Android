package com.nhq.betplus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import static android.widget.GridLayout.LayoutParams.*;

public class HistoryGame3Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_game3);
        Log.d("Statee", "his onCreate");
        Objects.requireNonNull(getWindow()).
                setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Intent intent = getIntent();
        //set date titlte
        String newDateString = intent.getStringExtra("date");
        TextView dateTV = findViewById(R.id.date);
        dateTV.setText("Vé đã mua hôm nay (mã vé - coin)");

        setOnClickForButton();

        //get data and add textView to gridLayout
        SharedPreferences sharedPreferences = getSharedPreferences("dataGame3", MODE_PRIVATE);
        String numbersString = sharedPreferences.getString("numbers", "");
        String betRateString = sharedPreferences.getString("betRate", "");

        if (!numbersString.equals("") && !betRateString.equals("")) {
            String[] arrOfNumber = numbersString.split(" ", 10);
            String[] arrOfBet = betRateString.split(" ", 10);

            ArrayList<Integer> lotteryNumbers = new ArrayList<>();
            ArrayList<Integer> lotteryBetRate = new ArrayList<>();

            for (String i : arrOfNumber) {
                lotteryNumbers.add(Integer.parseInt(i));
            }
            for (String i : arrOfBet) {
                lotteryBetRate.add(Integer.parseInt(i));
            }

            GridLayout gridLayout = findViewById(R.id.game3_data_layout);
            for (int i = 0; i < lotteryNumbers.size(); i++) {
                //create button and add to layout
                Button b = createButtonWithLabel(i, lotteryNumbers.get(i), lotteryBetRate.get(i));
                gridLayout.addView(b);

                //modify width and margin
                GridLayout.LayoutParams params = (GridLayout.LayoutParams) b.getLayoutParams();
                params.width = (getScreenWidth() - 500) / 5; //2250
                params.height = (getScreenHeight() - 300) / 4;
                params.setMargins(25, 25, 25, 25);
                b.setLayoutParams(params);
            }
        }
    }

    private void setOnClickForButton() {
        Button bt_ok = findViewById(R.id.out_ok);
        Button bt_cancel = findViewById(R.id.out_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private Button createButtonWithLabel(int label, int number, int betRate) {
        Button b = new Button(this);
        b.setText(Integer.toString(label));
        b.setId(label);
        b.setText(number + " - " + betRate);
        b.setTextSize(20);
        b.setBackgroundResource(R.drawable.button_lottery_selected);
        return b;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Statee", "his onDestroy");
    }
}
