package com.bhavishdoobaree.exercisetracker;

//Class storing details of log: Timestamp, distance and tim taken

public class exeLog {

    private String timestamp;
    private String distance;
    private long timeran;

    public exeLog()
    {

    }

    public exeLog(String ts, String dist, long tr){
        this.timestamp = ts;
        this.distance = dist;
        this.timeran = tr;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getDistance()
    {
        return distance;
    }

    public void setDistance(String distance)
    {
        this.distance = distance;
    }


    public long getTime()
    {
        return timeran;
    }

    public void setTime(long timeran)
    {
        this.timeran = timeran;
    }



}
