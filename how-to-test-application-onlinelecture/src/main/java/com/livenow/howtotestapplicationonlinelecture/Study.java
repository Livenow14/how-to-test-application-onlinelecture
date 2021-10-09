package com.livenow.howtotestapplicationonlinelecture;

public class Study {

    private StudyStatus status;

    private int limit;
    private String name;

    public Study(int limit) {
        this(StudyStatus.DRAFT, limit, "");
    }

    public Study(int limit, String name) {
        this(StudyStatus.DRAFT, limit, name);
    }

    public Study(StudyStatus status, int limit, String name) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit는 0보다 커야한다.");
        }
        this.status = status;
        this.limit = limit;
        this.name = name;
    }

    public StudyStatus getStatus() {
        return this.status;
    }

    public int getLimit() {
        return limit;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Study{" +
                "status=" + status +
                ", limit=" + limit +
                ", name='" + name + '\'' +
                '}';
    }
}

