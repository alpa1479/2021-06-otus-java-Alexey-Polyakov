package ru.otus.hw04;


/*
-Xms256m
-Xmx256m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
-XX:+UseG1GC
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
*/

/*
----------------------------------------- Results (without optimization):
-Xmx256m:                   -Xmx2048m:                  -Xmx1024m:                  -Xmx4g:
spend msec:34592, sec:34    spend msec:28494, sec:28    spend msec:32810, sec:32    spend msec:30090, sec:30
spend msec:30413, sec:30    spend msec:29398, sec:29    spend msec:29370, sec:29    spend msec:28822, sec:28
spend msec:30360, sec:30    spend msec:30615, sec:30    spend msec:29705, sec:29    spend msec:28622, sec:28
------------------------------------------ Results (with int optimization):
-Xmx256m:                   -Xmx1024m:
spend msec:5585, sec:5      spend msec:5496, sec:5
spend msec:5254, sec:5      spend msec:5510, sec:5
spend msec:5489, sec:5      spend msec:5885, sec:5
------------------------------------------ Results (with singleton data instance optimization):
-Xmx1024m:
spend msec:4416, sec:4
spend msec:4439, sec:4
spend msec:3990, sec:3
*/

import java.time.LocalDateTime;

public class CalcDemo {
    public static void main(String[] args) {
        long counter = 100_000_000;
        var summator = new Summator();
        long startTime = System.currentTimeMillis();

        var data = new Data();
        for (var idx = 0; idx < counter; idx++) {
            data.setValue(idx);
            summator.calc(data);

            if (idx % 10_000_000 == 0) {
                System.out.println(LocalDateTime.now() + " current idx:" + idx);
            }
        }

        long delta = System.currentTimeMillis() - startTime;
        System.out.println(summator.getPrevValue());
        System.out.println(summator.getPrevPrevValue());
        System.out.println(summator.getSumLastThreeValues());
        System.out.println(summator.getSomeValue());
        System.out.println(summator.getSum());
        System.out.println("spend msec:" + delta + ", sec:" + (delta / 1000));
    }
}
