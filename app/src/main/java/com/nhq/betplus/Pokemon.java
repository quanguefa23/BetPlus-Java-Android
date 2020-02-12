package com.nhq.betplus;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Pokemon {
    public String Name;
    public boolean isBet;
    private int RewardRate;
    private int BetCoin;
    private TextView BetCoinTV;
    private CheckBox Status;
    private SeekBar Race;

    public Pokemon(TextView betCoin, CheckBox status, SeekBar race) {
        BetCoinTV = betCoin;
        Status = status;
        Race = race;
        Name = "";
        RewardRate = 2;
        BetCoin = 0;
        isBet = false;
    }

    public int rewardWin() {
        return BetCoin * RewardRate;
    }

    public void setRewardRate(int rewardRate) {
        RewardRate = rewardRate;
    }

    public int getRewardRate() {
        return RewardRate;
    }

    public void resetPoke() {
        BetCoinTV.setText("0");
        BetCoinTV.setTextColor(Color.YELLOW);
        //BetCoin.setTextColor(Color.parseColor("#FFFFFF"));
        Status.setEnabled(true);
        Status.setChecked(false);
        String t = "x" + RewardRate;
        Status.setText(t);
        Race.setProgress(Race.getMax() / 50);
        isBet = false;
    }

    public boolean isFinish() {
        return (Race.getProgress() >= Race.getMax());
    }

    public void runPokemon() {
        Random rd = new Random();
        int dis = 0;
        int base = 60;
        switch(RewardRate) {
            case 2:
                dis = rd.nextInt(base + 2) + 1;
                break;
            case 3:
                dis = rd.nextInt(base + 1) + 1;
                break;
            case 5:
                dis = rd.nextInt(base) + 1;
                break;
            case 7:
                dis = rd.nextInt(base - 1) + 1;
                break;
            case 9:
                dis = rd.nextInt(base - 2) + 1;
                break;
        }

        Race.setProgress(Race.getProgress() + dis);
    }

    public void onClickListenerStatus(final RadioGroup betCoinGroup, final Game1Player mainGame1Player, final Context mainContext) {
        Status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBet = true;

                int idChecked = betCoinGroup.getCheckedRadioButtonId();
                switch (idChecked) {
                    case R.id.coin100:
                        doInEachCase(100);
                        break;
                    case R.id.coin300:
                        doInEachCase(300);
                        break;
                    case R.id.coin500:
                        doInEachCase(500);
                        break;
                }
            }

            private void doInEachCase(int betCoin) {
                if(mainGame1Player.bet(betCoin)) {
                    BetCoin = betCoin;
                    BetCoinTV.setTextColor(Color.parseColor("#cd3432"));
                    BetCoinTV.setText("-" + betCoin);
                    Status.setEnabled(false);
                }
                else {
                    isBet = false;
                    Status.setChecked(false);
                    Toast.makeText(mainContext, "Bạn không đủ tiền cược\nẤn FREE COIN để nhận thêm coin", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void disableCheckbox() {
        Status.setEnabled(false);
    }

    public void updateBetCoinTV() {
        BetCoinTV.setTextColor(Color.GREEN);
        BetCoinTV.setText("+" + rewardWin());
    }
}
