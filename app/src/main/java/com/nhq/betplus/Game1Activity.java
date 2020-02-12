package com.nhq.betplus;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Game1Activity extends BaseActivity {

    ImageView playButton;
    ImageView resetButton;
    ImageView pokeWinner;
    LinearLayout mainLayout;
    RadioGroup betCoinGroup;
    Game1Player game1Player;

    int REQUEST_CODE_ADMOB = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Statee", "1 onCreate");
        //reflection object, config landscape and background
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);
        mapWidget();

        //get username and create game1Player
        String username = getUsername();
        game1Player = initialPlayer(username);

        //array to store all pokemon in the race
        final Pokemon[] listPoke = new Pokemon[5];
        generatePokemon(listPoke);
        resetGame(listPoke);

        //save bet status
        setOnClickForListPokemon(listPoke, game1Player);

        //wait for click play button
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBetAnyPoke(listPoke)) {
                    Toast.makeText(Game1Activity.this,
                            "Bạn chưa đặt cược bất kì Pokemon nào!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    disableCheckboxListPokemon(listPoke);
                    playButton.setVisibility(View.INVISIBLE);
                    mainLayout.setBackgroundResource(R.drawable.background);

                    //start game
                    runGame(listPoke, game1Player);
                }
            }
        });

        freeCoinFunction();
        backFunction();
        guideFunction();
    }

    private void guideFunction() {
        TextView tv = findViewById(R.id.game1_guide);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogGuide();
            }
        });
    }

    private void showDialogGuide() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_guide_game1);
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

    private void backFunction() {
        ImageView iv = findViewById(R.id.game3_back);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void freeCoinFunction() {
        TextView addCoin = findViewById(R.id.addCoin);
        addCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game1Player.getCoin() > 200) {
                    Toast.makeText(Game1Activity.this, "Không thể nhận coin miễn phí khi đang có hơn 200 coin", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(Game1Activity.this, AdmobActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_ADMOB);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_ADMOB && resultCode == RESULT_OK) {
            game1Player.addCoin(200);
            Toast.makeText(Game1Activity.this, "Chúc mừng! Bạn nhận được 200 coin miễn phí", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(Game1Activity.this, "Bạn chưa xem quảng cáo!", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("dataPlayer", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }

/*    private String getUsername() {
        //folder name
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir("RacingBetLocal", Context.MODE_PRIVATE);

        String filename = "username";
        File myInternalFile = new File(directory, filename);

        try {
            //Đọc file
            FileInputStream fis = new FileInputStream(myInternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            String new_username = br.readLine();
            in.close();
            
            if (new_username == null || new_username.equals("")) {
                return "";
            }
            else {
                return new_username;
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return "";
    }*/

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog() {
        Log.d("State", "showDialog");
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_out);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                Game1Activity.super.onBackPressed();
                overridePendingTransition(R.anim.anim_in1, R.anim.anim_out1);
            }
        });

        dialog.show();
    }

    private void disableCheckboxListPokemon(Pokemon[] listPoke) {
        for (Pokemon var : listPoke) {
            var.disableCheckbox();
        }
    }

    private boolean isBetAnyPoke(Pokemon[] listPoke) {
        for (Pokemon var : listPoke) {
            if (var.isBet)
                return true;
        }

        return false;
    }

    private void setOnClickForListPokemon(Pokemon[] listPoke, Game1Player mainGame1Player) {
        for (int i = 0; i < 5; i++) {
            listPoke[i].onClickListenerStatus(betCoinGroup, mainGame1Player,
                    Game1Activity.this);
        }
    }

    private Game1Player initialPlayer(String username) {
        TextView coinTV = findViewById(R.id.sumcoin);
        TextView gemTV = findViewById(R.id.sumgem);
        TextView usernameTV = findViewById(R.id.username);
        TextView levelTV = findViewById(R.id.level);

        SharedPreferences sharedPreferences = getSharedPreferences("dataPlayer", MODE_PRIVATE);
        int level = sharedPreferences.getInt("level", 0);
        float exp = sharedPreferences.getFloat("exp", 0);
        int coin = sharedPreferences.getInt("coin", 1000);
        int gem = sharedPreferences.getInt("gem", 0);

        return new Game1Player(username, level, exp, coin, gem, usernameTV, levelTV, coinTV, gemTV);
    }

    CountDownTimer cd;
    CountDownTimer cd2;
    boolean isRunning = true;
    int winIndex = 0;
    private void runGame(final Pokemon[] listPoke, final Game1Player mainGame1Player) {
        isRunning = true;
        cd = new CountDownTimer(60000, 200) {
            @Override
            public void onTick(long l) {
                for (int i = 0; i < 5; i++) {
                    listPoke[i].runPokemon();

                    //end game?
                    if (listPoke[i].isFinish()) {
                        winIndex = i;
                        this.cancel(); //stop countdown
                        isRunning = false;
                    }
                }
            }

            @Override
            public void onFinish() {
                this.start();
            }
        };
        cd.start();

        cd2 = new CountDownTimer(60000, 100) {
            @Override
            public void onTick(long l) {
                if (!isRunning) {
                    notifyResult(winIndex, listPoke, mainGame1Player);
                    this.cancel();
                }
            }

            @Override
            public void onFinish() {
                this.start();
            }
        };
        cd2.start();
    }

    private void notifyResult(int i, final Pokemon[] listPoke, Game1Player mainGame1Player) {
        //show image of winner in the screen
        String srcSTR = "pokee" + (i + 1);
        int srcID = getResources().getIdentifier("drawable/" + srcSTR,
                null, getPackageName());
        pokeWinner.setImageResource(srcID);
        pokeWinner.setVisibility(View.VISIBLE);

        //check bet result
        if (listPoke[i].isBet) {
            //update coin
            mainGame1Player.win(listPoke[i].rewardWin());

            //notify
            Toast.makeText(Game1Activity.this,
                    "Chúc mừng, bạn đã thắng cược " + listPoke[i].rewardWin() + " xu",
                    Toast.LENGTH_LONG).show();

            //change text of betCoinTV
            listPoke[i].updateBetCoinTV();
        }
        else {
            Toast.makeText(Game1Activity.this,
                    "Chúc bạn may mắn lần sau",
                    Toast.LENGTH_LONG).show();
        }

        //update exp, level and gem of player
        mainGame1Player.updateExpAndLevel(listPoke[i]);
        mainGame1Player.updateGem(listPoke[i]);

        //show reset button and wait for click
        resetButton.setVisibility(View.VISIBLE);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButton.setVisibility(View.INVISIBLE);
                playButton.setVisibility(View.VISIBLE);
                mainLayout.setBackgroundResource(R.drawable.background1);
                pokeWinner.setVisibility(View.INVISIBLE);
                resetGame(listPoke);
            }
        });
    }

    private void mapWidget() {
        mainLayout = findViewById(R.id.mainLayout);
        playButton = findViewById(R.id.play_button);
        resetButton = findViewById(R.id.reset_button);
        betCoinGroup = findViewById(R.id.radioGroup);
        pokeWinner = findViewById(R.id.pokeWinner);
        pokeWinner.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.INVISIBLE);
    }

    private void generatePokemon(Pokemon[] listPoke) {
        for(int i = 1; i < 6; i++) {
            String tvStr = "betCoin"  + i;
            String cbStr = "checkBox"  + i;
            String sbStr = "seekBar" + i;

            int tvID = getResources().getIdentifier(tvStr, "id", getPackageName());
            int cbID = getResources().getIdentifier(cbStr, "id", getPackageName());
            int sbID = getResources().getIdentifier(sbStr, "id", getPackageName());

            TextView tv = findViewById(tvID);
            CheckBox cb = findViewById(cbID);
            SeekBar sb = findViewById(sbID);
            sb.setMax(2000);
            sb.setEnabled(false);

            listPoke[i - 1] = new Pokemon(tv, cb, sb);
        }

        listPoke[0].Name = "Pikachu";
        listPoke[1].Name = "Charizard";
        listPoke[2].Name = "Moltres";
        listPoke[3].Name = "Absol";
        listPoke[4].Name = "Bulbasaur";
    }

    private void resetGame(Pokemon[] listPoke) {
        ArrayList<Integer> rate = new ArrayList<>(Arrays.asList(2, 3, 5, 7, 9));
        Random rd = new Random();

        for (int i = 0; i < 5; i++) {
            int rdNumber = rd.nextInt(rate.size());
            listPoke[i].setRewardRate(rate.get(rdNumber));
            rate.remove(rdNumber);
            listPoke[i].resetPoke();
        }
    }

    @Override
    protected void onPause() {
        Log.d("Statee", "1 onPause");
        commitData();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        commitData();

        if (cd != null)
            cd.cancel();
        if (cd2 != null)
            cd2.cancel();

        super.onDestroy();
        Log.d("Statee", "1 onDestroy");
    }

    private void commitData() {
        SharedPreferences sharedPreferences = getSharedPreferences("dataPlayer", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("level", game1Player.getLevel());
        editor.putFloat("exp", game1Player.getExp());
        editor.putInt("coin", game1Player.getCoin());
        editor.putInt("gem", game1Player.getGem());
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Statee", "1 onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Statee", "1 onResume");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Statee", "1 onStop");
    }
}
