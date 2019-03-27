package com.gmail.safordog.newsreport.model;

public class DateAndCount {

    public String date;
    public Long count;

    public DateAndCount() {
    }

    public DateAndCount(String date, Long count) {
        this.date = date;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
