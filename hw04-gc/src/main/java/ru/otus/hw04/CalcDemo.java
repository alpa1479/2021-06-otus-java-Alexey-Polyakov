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
----------------------------------------- Results (without optimization): -------------------------------------------
-Xmx256m:                 |  -Xmx2048m:                |  -Xmx4096m:                |  -Xmx1900m:
spend msec:45482, sec:45  |  spend msec:21232, sec:21  |  spend msec:22613, sec:22  |  spend msec:20792, sec:20
spend msec:42244, sec:42  |  spend msec:21376, sec:21  |  spend msec:21808, sec:21  |  spend msec:20331, sec:20
spend msec:43268, sec:43  |  spend msec:21094, sec:21  |  spend msec:22316, sec:22  |  spend msec:20702, sec:20
-Xmx1500m:                |  -Xmx1600m:                |  -Xmx1800m:                |  -Xmx1850m:
spend msec:22243, sec:22  |  spend msec:21718, sec:21  |  spend msec:21857, sec:21  |  spend msec:19772, sec:19
spend msec:22678, sec:22  |  spend msec:21923, sec:21  |  spend msec:22074, sec:22  |  spend msec:19770, sec:19
spend msec:22567, sec:22  |  spend msec:22145, sec:22  |  spend msec:21784, sec:21  |  spend msec:19811, sec:19
                                                                                    |  spend msec:19533, sec:19
                                                                                    |  spend msec:19415, sec:19
------------------------------------------ Results (with int optimization): -----------------------------------------
-Xmx256m:                 |  -Xmx1850m:              |  -Xmx500m:
spend msec:6582, sec:6    |  spend msec:4512, sec:4  |  spend msec:4152, sec:4
spend msec:6472, sec:6    |  spend msec:4318, sec:4  |  spend msec:4266, sec:4
spend msec:6710, sec:6    |  spend msec:4463, sec:4  |  spend msec:4260, sec:4
------------------------------------------ Results (with singleton data instance optimization): ---------------------
-Xmx256m:                 |  -Xmx500m:               |  -Xmx150m:
spend msec:3394, sec:3    |  spend msec:3485, sec:3  |  spend msec:3543, sec:3
spend msec:3306, sec:3    |  spend msec:3262, sec:3  |  spend msec:3866, sec:3
spend msec:3261, sec:3    |  spend msec:3335, sec:3  |  spend msec:3598, sec:3
---------------------------------------------------------------------------------------------------------------------
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
