package com.nhq.betplus;

import java.util.ArrayList;

public class Game3Player extends MainPlayer {
    String dateString;
    ArrayList<Integer> lotteryNumbers;
    ArrayList<Integer> lotteryBetRate;
    public Game3Player() {
        super();
        dateString = "";
        lotteryNumbers = new ArrayList<>();
        lotteryBetRate = new ArrayList<>();
    }

    public ArrayList<Integer> getLotteryBetRate() {
        return lotteryBetRate;
    }

    public void setLotteryBetRate(ArrayList<Integer> lotteryBetRate) {
        this.lotteryBetRate = lotteryBetRate;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public ArrayList<Integer> getLotteryNumbers() {
        return lotteryNumbers;
    }

    public void setLotteryNumbers(ArrayList<Integer> lotteryNumbers) {
        this.lotteryNumbers = lotteryNumbers;
    }

    public boolean isNumberSelected(int number) {
        for (int i : lotteryNumbers) {
            if (i == number) {
                return true;
            }
        }

        return false;
    }

    public void select(int id, int coinBet) {
        lotteryNumbers.add(id);
        lotteryBetRate.add(coinBet);
        subtractCoin(coinBet);

        //update exp
        Exp += coinBet / (10 * (Level + 1));

        //update level
        while (Exp >= 100) {
            Level++;
            Exp = (float) ((Exp - 100) * 1.0 * Level / (Level + 1));
        }
        //lam tron so
        Exp = (float) (Math.floor(Exp * 10) / 10);
    }

    public void reward(int totalReward) {
        addCoin(totalReward);

        //update exp
        Exp += totalReward / (100 * (Level + 1));

        //update level
        while (Exp >= 100) {
            Level++;
            Exp = (float) ((Exp - 100) * 1.0 * Level / (Level + 1));
        }
        //lam tron so
        Exp = (float) (Math.floor(Exp * 10) / 10);

        //update gem
        Gem += totalReward / 1400;
    }
}
