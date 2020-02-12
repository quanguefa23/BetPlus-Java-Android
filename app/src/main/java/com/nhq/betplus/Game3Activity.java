package com.nhq.betplus;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

import static android.app.PendingIntent.getActivity;

public class Game3Activity extends BaseActivity {
    Game3Player mainPlayer;

    TextView usernameTV;
    TextView levelTV;
    TextView coinTV;
    TextView gemTV;
    TextView resultTV;
    RadioGroup betCoinGroup;
    TextView historyTV;

    int date;
    int month;
    int year;
    int hour;

    boolean isAfter17H;
    boolean isResultChecked;
    int result;

    String newDateString;
    String oldDateString;
    ArrayList<Button> listButton;

    int REQUEST_CODE_ADMOB = 1234;
    int REQUEST_CODE_HISTORY = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game3);
        mapWidget();
        freeCoinFunction();

        getNewDate();
        //setDateToTextView();

        initialMainPlayer();
        updateMainPlayer();
        getOldDateData();

        if (newDateString.equals(oldDateString)) {
            getOldLotteryData();
        }
        else {
            setResultCheckedFalse();
            updateDateData();
        }

        isAfter17H = (hour >= 17);
        if (isAfter17H) {
            SharedPreferences sharedPreferences = getSharedPreferences("dataGame3", MODE_PRIVATE);
            isResultChecked = sharedPreferences.getBoolean("checked", false);

            if (isResultChecked) {
                result = sharedPreferences.getInt("result", 0);
                setTextForResultTV();
            }
            else {
                Random random = new Random();
                result = random.nextInt(100);
                setTextForResultTV();
                isResultChecked = true;
                commitData();
            }
        }

        backFunction();
        infoDateFunction();
        guideFunction();
        historyFunction();

        loadButtonLottery();
    }

    private void guideFunction() {
        ImageView iv = findViewById(R.id.game3_guide);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogGuide();
            }
        });
    }

    private void infoDateFunction() {
        ImageView iv = findViewById(R.id.game3_info_date);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Game3Activity.this,
                        "Hôm nay là ngày " + newDateString,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void backFunction() {
        ImageView iv = findViewById(R.id.game3_back);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setTextForResultTV() {
        resultTV.setText("Kết quả: " + result);
    }

    private void setResultCheckedFalse() {
        SharedPreferences sharedPreferences = getSharedPreferences("dataGame3", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("checked", false);
        editor.apply();
    }

    private void historyFunction() {
        historyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Game3Activity.this, HistoryGame3Activity.class);
                intent.putExtra("date", newDateString);
                startActivityForResult(intent, REQUEST_CODE_HISTORY);
            }
        });
    }

    private void updateDateData() {
        mainPlayer.setDateString(newDateString);
    }

    private void getOldLotteryData() {
        SharedPreferences sharedPreferences = getSharedPreferences("dataGame3", MODE_PRIVATE);

        String numbersString = sharedPreferences.getString("numbers", "");
        if (numbersString.equals(""))
            return;

        String betRateString = sharedPreferences.getString("betRate", "");
        if (betRateString.equals(""))
            return;

        String[] arrOfNumber = numbersString.split(" ", 10);
        String[] arrOfBet = betRateString.split(" ", 10);

        ArrayList<Integer> listNumbers = new ArrayList<>();
        ArrayList<Integer> listBetRate = new ArrayList<>();

        for (String i : arrOfNumber) {
            listNumbers.add(Integer.parseInt(i));
        }

        for (String i : arrOfBet) {
            listBetRate.add(Integer.parseInt(i));
        }

        mainPlayer.setLotteryNumbers(listNumbers);
        mainPlayer.setLotteryBetRate(listBetRate);
    }

    private void getOldDateData() {
        SharedPreferences sharedPreferences = getSharedPreferences("dataGame3", MODE_PRIVATE);

        oldDateString = sharedPreferences.getString("date", "");
    }

    private void freeCoinFunction() {
        TextView addCoin = findViewById(R.id.game3_addCoin);
        addCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainPlayer.getCoin() > 200) {
                    Toast.makeText(Game3Activity.this,
                            "Không thể nhận coin miễn phí khi đang có hơn 200 coin",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(Game3Activity.this, AdmobActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_ADMOB);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_ADMOB) {
            if (resultCode == RESULT_OK) {
                mainPlayer.addCoin(200);
                commitData();
                Toast.makeText(Game3Activity.this,
                        "Chúc mừng! Bạn nhận được 200 coin miễn phí",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Game3Activity.this,
                        "Bạn chưa xem quảng cáo!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_CODE_HISTORY) {
            if (resultCode == RESULT_OK && data != null) {
                checkResultAndRewardPlayer();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getNewDate() {
        final Calendar calendar = Calendar.getInstance();
        date = calendar.get(Calendar.DATE);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        //hour = 17;
        newDateString = date + "/" + month + "/" + year;
    }

    private void
    checkResultAndRewardPlayer() {
        if (!isAfter17H) {
            Toast.makeText(Game3Activity.this,
                    "Quay lại sau 17:00 để xem kết quả nhé",
                    Toast.LENGTH_LONG).show();
            return;
        }

        int totalReward = 0;
        ArrayList<Integer> listLotteryNumber = mainPlayer.getLotteryNumbers();
        ArrayList<Integer> listBetRate = mainPlayer.getLotteryBetRate();

        if (listLotteryNumber.isEmpty() || listBetRate.isEmpty()) {
            Toast.makeText(Game3Activity.this,
                    "Hôm nay bạn chưa mua vé số, quay lại vào ngày mai nhé!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        //result = 0;
        for (int i = 0; i < listLotteryNumber.size(); i++) {
            int number = listLotteryNumber.get(i);
            if (result == number) {
                totalReward += listBetRate.get(i) * 70;
                listButton.get(number).setBackgroundResource(R.drawable.button_lottery_win);
            }
            else {
                listButton.get(number).setBackgroundResource(R.drawable.button_lottery_lose);
            }
        }

        if (totalReward == 0) {
            Toast.makeText(Game3Activity.this,
                    "Chúc bạn may mắn lần sau!",
                    Toast.LENGTH_LONG).show();
        }
        else {
            mainPlayer.reward(totalReward);
            commitData();
            updateView();
            Toast.makeText(Game3Activity.this,
                    "Chúc mừng, bạn đã trúng số!\nPhần thưởng của bạn là " + totalReward + " coin.",
                    Toast.LENGTH_LONG).show();
        }

        resetGame3Player();
    }

    private void resetGame3Player() {
        mainPlayer.setLotteryNumbers(new ArrayList<Integer>());
        mainPlayer.setLotteryBetRate(new ArrayList<Integer>());
    }

    private void commitData() {
        SharedPreferences sharedPreferences = getSharedPreferences("dataPlayer", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("level", mainPlayer.getLevel());
        editor.putFloat("exp", mainPlayer.getExp());
        editor.putInt("coin", mainPlayer.getCoin());
        editor.putInt("gem", mainPlayer.getGem());
        editor.apply();

        commitDataGame3();
    }

    private void commitDataGame3() {
        ArrayList<Integer> listLotteryNumber = mainPlayer.getLotteryNumbers();
        ArrayList<Integer> listBetRate = mainPlayer.getLotteryBetRate();
        if (listLotteryNumber.isEmpty() || listBetRate.isEmpty()) {
            SharedPreferences sharedPreferences = getSharedPreferences("dataGame3", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("numbers", "");
            editor.putString("betRate", "");
            editor.putString("date", newDateString);
            editor.putBoolean("checked", isResultChecked);
            editor.putInt("result", result);
            editor.apply();
            return;
        }

        String numbersString = "" + listLotteryNumber.get(0);
        String betRateString = "" + listBetRate.get(0);

        for (int i = 1; i < listLotteryNumber.size(); i++) {
            numbersString = numbersString + " " + listLotteryNumber.get(i);
            betRateString = betRateString + " " + listBetRate.get(i);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("dataGame3", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("numbers", numbersString);
        editor.putString("betRate", betRateString);
        editor.putString("date", newDateString);
        editor.putBoolean("checked", isResultChecked);
        editor.putInt("result", result);
        editor.apply();
    }

    private void mapWidget() {
        usernameTV = findViewById(R.id.game3_username);
        levelTV = findViewById(R.id.game3_level);
        coinTV = findViewById(R.id.game3_sumcoin);
        gemTV = findViewById(R.id.game3_sumgem);

        resultTV = findViewById(R.id.game3_result);
        betCoinGroup = findViewById(R.id.game3_radioGroup);
        historyTV = findViewById(R.id.game3_history);
    }

    private void initialMainPlayer() {
        mainPlayer = new Game3Player();
    }

    private void loadButtonLottery() {
        GridLayout gridLayout = findViewById(R.id.game3_gridLayout);

        ArrayList<String> labels = new ArrayList<>();

        //load labels
        for (int i = 0; i < 100; i++) {
            labels.add(i + "");
        }

        listButton = new ArrayList<>();
        for (int i = 0; i < labels.size(); i++)
        {
            //create button and add to layout
            final Button b = createButtonWithLabel(labels.get(i));
            gridLayout.addView(b);

            //animation
            Animation ani = AnimationUtils.loadAnimation(this, R.anim.anim1);
            b.startAnimation(ani);

            //modify width and margin
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) b.getLayoutParams();
            params.width = (getScreenWidth() - 400) /10; //2250
            params.height = (getScreenHeight() - 300) / 7;
            params.setMargins(15, 15, 15, 15);
            b.setLayoutParams(params);

            //set background
            if (mainPlayer.isNumberSelected(i)) {
                b.setBackgroundResource(R.drawable.button_lottery_selected);
            }
            else {
                b.setBackgroundResource(R.drawable.button_lottery);
            }

            b.requestLayout();
            setOnClickForButton(b);
            listButton.add(b);
        }
    }

    private void setOnClickForButton(final Button b) {
        b.setOnClickListener(new View.OnClickListener() {
            private int getCoinBet() {
                int idChecked = betCoinGroup.getCheckedRadioButtonId();
                int coinBet = 0;
                switch (idChecked) {
                    case R.id.game3_coin100:
                        coinBet = 100;
                        break;
                    case R.id.game3_coin300:
                        coinBet = 300;
                        break;
                    case R.id.game3_coin500:
                        coinBet = 500;
                        break;
                }
                return coinBet;
            }

            @Override
            public void onClick(View view) {
                if (isAfter17H) {
                    Toast.makeText(Game3Activity.this,
                            "Không thể mua thêm vé sau khi xổ, quay lại vào ngày mai nhé!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (mainPlayer.getLotteryNumbers().size() == 10) {
                    Toast.makeText(Game3Activity.this,
                            "Mỗi ngày chỉ mua được tối đa 10 tờ vé số",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                int coinBet = getCoinBet();
                if (mainPlayer.getCoin() < coinBet) {
                    Toast.makeText(Game3Activity.this,
                            "Bạn không đủ tiền cược\nẤn FREE COIN để nhận thêm coin",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    showDialogBuyLottery(b, coinBet);
                }
            }
        });
    }

    private void showDialogBuyLottery(final Button b, final int coinBet) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_buy_lottery);
        dialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(dialog.getWindow()).
                setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView info = dialog.findViewById(R.id.info);
        info.setText("Mua số " + b.getId() + " với giá " + coinBet + " coin?");
        Button bt_cancel = dialog.findViewById(R.id.out_cancel);
        Button bt_ok = dialog.findViewById(R.id.out_ok);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                b.setBackgroundResource(R.drawable.button_lottery_selected);
                mainPlayer.select(b.getId(), coinBet);
                updateView();
                Toast.makeText(Game3Activity.this,
                        "Mua vé số thành công, quay lại sau 17:00 để xem kết quả nhé",
                        Toast.LENGTH_LONG).show();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void showDialogGuide() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_guide_game3);
        dialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(dialog.getWindow()).
                setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button bt_ok = dialog.findViewById(R.id.out_ok);


        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void showDialogBackPressed() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_out_game3);
        dialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(dialog.getWindow()).
                setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button bt_cancel = dialog.findViewById(R.id.out_cancel);
        Button bt_ok = dialog.findViewById(R.id.out_ok);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Game3Activity.super.onBackPressed();
                overridePendingTransition(R.anim.anim_in1, R.anim.anim_out1);
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        showDialogBackPressed();
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private Button createButtonWithLabel(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setId(Integer.parseInt(label));
        return b;
    }

    private void updateMainPlayer() {
        SharedPreferences sharedPreferences = getSharedPreferences("dataPlayer", MODE_PRIVATE);

        String username = sharedPreferences.getString("username", "");
        int level = sharedPreferences.getInt("level", 0);
        float exp = sharedPreferences.getFloat("exp", 0);
        int coin = sharedPreferences.getInt("coin", 1000);
        int gem = sharedPreferences.getInt("gem", 0);
        mainPlayer.update(username, level, exp, coin, gem);
    }

    private void updateView() {
        usernameTV.setText("Player: " + mainPlayer.getUsername());
        String levelString = "Level : " + mainPlayer.getLevel() + " | " + mainPlayer.getExp() + "%";
        levelTV.setText(levelString);
        coinTV.setText(mainPlayer.getCoin() + " ");
        gemTV.setText(mainPlayer.getGem() + " ");
    }

    @Override
    protected void onResume() {
        updateMainPlayer();
        updateView();
        Log.d("Statee", "3 onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        commitData();
        super.onPause();
        Log.d("Statee", "3 onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Statee", "3 onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Statee", "3 onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Statee", "3 onStop");
    }
}
