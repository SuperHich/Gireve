package com.dotit.gireve.entity;

/**
 * Created by Hichem Laroussi @SH on 01/04/2015.
 */
public class OpenTime {

    public static final String SOAP_WeekDay     = "WeekDay";
    public static final String SOAP_StartTime   = "StartTime";
    public static final String SOAP_EndTime     = "EndTime";

    private int WeekDay;
    private String StartTime;
    private String EndTime;

    public int getWeekDay() {
        return WeekDay;
    }

    public void setWeekDay(int weekDay) {
        WeekDay = weekDay;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
}
