package com.CAB302_EuclidSolver.model.question;

public class scoreKeeper {
    private final int totalQuestions;
    private int correctCount = 0;
    private int attempted = 0;

    public scoreKeeper(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    /** Record the outcome of an attempted question. */
    public void record(boolean correct) {
        attempted++;
        if (correct) correctCount++;
    }

    public int getCorrect() {
        return correctCount;
    }

    public int getAttempted() {
        return attempted;
    }

    public int getTotal() {
        return totalQuestions;
    }

    public double getPercent() {
        if (attempted == 0) return 0.0;
        return (100.0 * correctCount) / totalQuestions;
    }

    public String summary() {
        return correctCount + "/" + totalQuestions + " (" + String.format("%.1f", getPercent()) + "%)";
    }
}

