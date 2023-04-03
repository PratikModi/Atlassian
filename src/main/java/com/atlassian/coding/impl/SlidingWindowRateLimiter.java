package com.atlassian.coding.impl;

import com.atlassian.coding.IRateLimiter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SlidingWindowRateLimiter implements IRateLimiter {

    private int noOfTransactions;
    private int time;
    private TimeUnit timeUnit;

    Map<Integer, List<Long>> customerRateLimit;

    public SlidingWindowRateLimiter(int limit, int time, TimeUnit timeUnit) {
        this.noOfTransactions = noOfTransactions;
        this.time = time;
        this.timeUnit = timeUnit;
        this.customerRateLimit = new HashMap<>();
    }

    public SlidingWindowRateLimiter() {
        this.noOfTransactions=3;
        this.time=1;
        this.timeUnit = TimeUnit.SECONDS;
        this.customerRateLimit = new HashMap<>();
    }

    @Override
    public boolean checkLimit(int customerId) {
        // Step: -1  If this exist in Map
        Long currentTime = Instant.now().toEpochMilli();
        if(!customerRateLimit.containsKey(customerId)){
            customerRateLimit.putIfAbsent(customerId,new ArrayList<Long>());
            customerRateLimit.get(customerId).add(currentTime);
            return true;
        }else{
            List<Long> customerAccessTime = customerRateLimit.get(customerId);
            if(customerAccessTime.size()<noOfTransactions){
                customerAccessTime.add(currentTime);
                return true;
            }else{
                Long firstTime = customerAccessTime.get(0);
                long duration = ChronoUnit.NANOS.between(LocalDateTime.ofInstant(Instant.ofEpochMilli(firstTime), ZoneId.systemDefault()),LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTime),ZoneId.systemDefault()));
                long allowedDuration = convertSeconds(time,timeUnit);
                if(duration>allowedDuration){
                    customerAccessTime.remove(customerAccessTime.indexOf(firstTime));
                    customerAccessTime.add(currentTime);
                    return true;
                }
            }
        }
        return false;
    }

    private static long convertSeconds(long time, TimeUnit timeUnit){
        if(timeUnit.equals(TimeUnit.SECONDS))
            return TimeUnit.SECONDS.toNanos(time);
        else if(timeUnit.equals(TimeUnit.MILLISECONDS))
            return TimeUnit.MILLISECONDS.toNanos(time);
        else if(timeUnit.equals(TimeUnit.MINUTES))
            return TimeUnit.MINUTES.toNanos(time);
        else if(timeUnit.equals(TimeUnit.HOURS))
            return TimeUnit.HOURS.toNanos(time);
        else
            return time;
    }


    public static void main(String[] args) throws Exception {
        IRateLimiter rateLimiter = new SlidingWindowRateLimiter(3,1, TimeUnit.SECONDS);
        int customerId = 1;
        rateLimiter.checkLimit(customerId);
        rateLimiter.checkLimit(customerId);
        rateLimiter.checkLimit(customerId);
        Thread.sleep(1001);
        boolean actual = rateLimiter.checkLimit(customerId);
        System.out.println(actual);
    }
}
