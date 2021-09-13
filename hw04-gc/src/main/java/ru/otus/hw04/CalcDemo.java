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
spend msec:31647, sec:31  |  spend msec:15158, sec:15  |  spend msec:13718, sec:13  |  spend msec:14520, sec:14
spend msec:31666, sec:31  |  spend msec:13839, sec:13  |  spend msec:14719, sec:14  |  spend msec:13527, sec:13
spend msec:31914, sec:31  |  spend msec:14504, sec:14  |  spend msec:14899, sec:14  |  spend msec:15159, sec:15
-Xmx1500m:                |  -Xmx1600m:                |  -Xmx1800m:                |  -Xmx1850m:
spend msec:15430, sec:15  |  spend msec:15365, sec:15  |  spend msec:14497, sec:14  |  spend msec:12175, sec:12
spend msec:16087, sec:16  |  spend msec:15709, sec:15  |  spend msec:15502, sec:15  |  spend msec:11855, sec:11
spend msec:16120, sec:16  |  spend msec:15693, sec:15  |  spend msec:15031, sec:15  |  spend msec:13062, sec:13
-Xmx2148m:                | -Xmx2248m                  | -Xmx2448m
spend msec:12334, sec:12  |  spend msec:14962, sec:14  |  spend msec:14858, sec:14
spend msec:14971, sec:14  |  spend msec:15050, sec:15  |  spend msec:15702, sec:15
spend msec:14991, sec:14  |  spend msec:14688, sec:14  |  spend msec:14916, sec:14
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
