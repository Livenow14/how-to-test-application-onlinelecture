package com.livenow.howtotestapplicationonlinelecture;

public class Study {

    private StudyStatus status;

    private int limit;

    public Study(int limit) {
        this(StudyStatus.DRAFT, limit);
    }

    public Study(StudyStatus status, int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit는 0보다 커야한다.");
        }
        this.status = status;
        this.limit = limit;
    }

    public StudyStatus getStatus() {
        return this.status;
    }

    public int getLimit() {
        return limit;
    }
}

