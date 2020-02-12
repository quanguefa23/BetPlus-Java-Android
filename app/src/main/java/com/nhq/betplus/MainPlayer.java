package com.nhq.betplus;

public class MainPlayer {
    protected String Username;
    protected int Level;
    protected float Exp;
    protected int Coin;
    protected int Gem;

    public String getUsername() {
        return Username;
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

    private void setCoin(int coin) {
        Coin = coin;
    }

    public int getGem() {
        return Gem;
    }

    private void setGem(int gem) {
        Gem = gem;
    }

    public MainPlayer(String username, int level, float exp, int coin, int gem) {
        Username = username;
        Level = level;
        Exp = exp;
        Coin = coin;
        Gem = gem;
    }

    public MainPlayer() {
        Username = "notSetYet";
        Level = 0;
        Exp = 0;
        Coin = 1000;
        Gem = 0;
    }

    public void update(String username, int level, float exp, int coin, int gem) {
        Username = username;
        Level = level;
        Exp = exp;
        Coin = coin;
        Gem = gem;
    }

    public void addCoin(int i) {
        setCoin(Coin + i);
    }

    protected void subtractCoin(int i) {
        setCoin(Coin - i);
    }
}
