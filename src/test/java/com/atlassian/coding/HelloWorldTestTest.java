package com.atlassian.coding;

import org.junit.Assert;
import org.junit.Test;

public class HelloWorldTestTest{

    @Test
    public void testGetMessage() {
        HelloWorldTest test = new HelloWorldTest();
        String actual = test.getMessage("hello world");
        Assert.assertEquals("hello world",actual);
    }
}