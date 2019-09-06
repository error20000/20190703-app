
package com.jian.system.model;


public class StopwatchItem {

    private String name;
    private String date;
    private String time;

    public StopwatchItem(String name, String date, String time){
        this.name = name;
        this.date = date;
        this.time = time;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
