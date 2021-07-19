package com.redspeaks.simpleban.lib;

public class Time {

    private long millis;
    public Time(String time) {
        if(time.equals("Permanent")) {
            this.millis = -1;
        }
        long raw = 0;
        String[] data = time.split(" ");
        for(String d : data) {
            raw += TimeUtil.calculate(d);
        }
        this.millis = System.currentTimeMillis() + (raw * 1000);
    }

    public Time(long millis) {
        this.millis = millis;
    }

    public long getMillis() {
        return millis;
    }


    public String parseTime() {
        this.millis -= System.currentTimeMillis();
        if(millis == -1) {
            return "Permanent";
        }
        long seconds = millis / 1000;
        long year = seconds / 31556926;
        if(year != 0) {
            seconds %= 31556926;
        }
        long months = seconds / 2629743;
        if(months != 0) {
            seconds %= 2629743;
        }
        long weeks = seconds / 604800;
        if(weeks != 0) {
            seconds %= 604800;
        }
        long days = seconds / 86400;
        if(days != 0) {
            seconds %= 86400;
        }
        long hours = seconds / 3600;
        if(hours != 0) {
            seconds %= 3600;
        }
        long minutes = seconds / 60;
        if(minutes != 0) {
            seconds %= 60;
        }
        StringBuilder builder  = new StringBuilder();
        if(year != 0) {
            builder.append(year).append(" ").append("year(s) ");
        }
        if(weeks != 0) {
            builder.append(weeks).append(" ").append("week(s) ");
        }
        if(days != 0) {
            builder.append(days).append(" ").append("day(s) ");
        }
        if(hours != 0) {
            builder.append(hours).append(" ").append("hour(s) ");
        }
        if(minutes != 0) {
            builder.append(minutes).append(" ").append("minute(s) ");
        }
        if(seconds != 0) {
            builder.append(seconds).append(" ").append("second(s).");
        }
        return builder.toString().trim();
    }
}
