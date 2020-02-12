package com.nhq.betplus;

import android.widget.TextView;

public class Game1Player {
    private String Username;
    private TextView UsernameTV;
    private int Level;
    private TextView LevelTV;
    private float Exp;
    private int Coin;
    private TextView CoinTV;
    private int Gem;
    private TextView GemTV;

    public Game1Player() {
        Username = "quanguefa";
        Level = 0;
        Exp = 0;
        Coin = 1000;
        Gem = 0;
    }

    public int getLevel() {
        return Level;
    }

    public float getExp() {
        return Exp;
    }

    public int getCoin() {
        return Coin;
    }

    public int getGem() {
        return Gem;
    }

    public Game1Player(String username, int level, float exp, int coin, int gem, TextView usernameTV,
                       TextView levelTV, TextView coinTV, TextView gemTV) {

        Username = username;
        Level = level;
        Exp = exp;
        Coin = coin;
        Gem = gem;

        UsernameTV = usernameTV;
        LevelTV = levelTV;
        CoinTV = coinTV;
        GemTV = gemTV;

        UsernameTV.setText("Player: " + Username);
        LevelTV.setText("Level : " + Level + " | " + Exp + "%");
        CoinTV.setText("" + Coin);
        GemTV.setText("" + Gem);
    }

    public boolean bet(int coinBet) {
        if (coinBet > Coin)
            return false;
        setCoin(Coin - coinBet);
        return true;
    }

    private void setCoin(int coin) {
        Coin = coin;
        CoinTV.setText("" + Coin);
    }

    public void win(int coinWin) {
        setCoin(Coin + coinWin);
    }

    public void updateExpAndLevel(Pokemon poke) {
        if (poke.isBet) {
            Exp += poke.rewardWin()*1.0 / (10 * (Level + 1));
        }
        else {
            Exp += 20 / (Level + 1);
        }

        while (Exp >= 100) {
            Level++;
            Exp = (float) ((Exp - 100) * 1.0 * Level / (Level + 1));
        }

        Exp = (float) (Math.floor(Exp * 10) / 10); //lam tron so
        LevelTV.setText("Level : " + Level + " | " + Exp + "%");
    }

    public void updateGem(Pokemon poke) {
        if (poke.isBet) {
            if (poke.getRewardRate() == 9)
                Gem += 2;
            if (poke.getRewardRate() == 7)
                Gem++;
            Gem += poke.rewardWin() / 2000;
            GemTV.setText("" + Gem);
        }
    }

    public void addCoin(int i) {
        setCoin(Coin + i);
    }
}
