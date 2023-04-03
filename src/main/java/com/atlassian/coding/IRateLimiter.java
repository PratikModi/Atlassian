package com.atlassian.coding;

public interface IRateLimiter {
    boolean checkLimit(int customerId);
}
