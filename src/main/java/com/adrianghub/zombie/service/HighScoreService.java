package com.adrianghub.zombie.service;

import com.almasb.fxgl.core.EngineService;
import com.almasb.fxgl.core.serialization.Bundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class HighScoreService extends EngineService {

    private IntegerProperty score = new SimpleIntegerProperty();
    private DoubleProperty time = new SimpleDoubleProperty();
    private int numScoresToKeep = 5;

    private ArrayList<HighScoreData> highScores = new ArrayList<>();

    public IntegerProperty scoreProperty() {
        return score;
    }
    public DoubleProperty timeProperty() { return time; }

    public int getScore() {
        return score.get();
    }

    public double getTimePlayed() {
        return time.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public void setTime(double time) {
        this.time.set(time);
    }

    public void incrementScore(int value) {
        setScore(getScore() + value);
    }

    /**
     * Remember current score with given tag and reset score to 0.
     */
    public void commit(String tag) {
        highScores.add(new HighScoreData(tag, getScore(), getTimePlayed()));

        score.unbind();
        time.unbind();
        setScore(0);
        setTime(0);

        updateScores();
    }

    /**
     * @return list of high scores sorted in descending order
     */
    public List<HighScoreData> getHighScores() {
        return new ArrayList<>(highScores);
    }

    public int getNumScoresToKeep() {
        return numScoresToKeep;
    }

    public void setNumScoresToKeep(int numScoresToKeep) {
        this.numScoresToKeep = numScoresToKeep;

        updateScores();
    }

    private void updateScores() {
        highScores = highScores.stream()
                .sorted(Comparator.comparingInt(HighScoreData::getScore).reversed())
                .limit(numScoresToKeep)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    @Override
    public void write(Bundle bundle) {
        bundle.put("highScores", highScores);
    }

    @Override
    public void read(Bundle bundle) {
        highScores = bundle.get("highScores");
    }

    public static class HighScoreData implements Serializable {

        private final String tag;
        private final int score;
        private final double time;

        private HighScoreData(String tag, int score, double time) {
            this.tag = tag;
            this.score = score;
            this.time = time;
        }

        public String getTag() {
            return tag;
        }

        public int getScore() {
            return score;
        }

        public double getTime() {
            return time;
        }
    }

}