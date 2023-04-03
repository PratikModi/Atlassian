package com.atlassian.coding;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ElectionResultTest{

    private ElectionResult electionResult = new ElectionResult();

    @Test
    public void findWinnerTest(){
        List<String> votes = Arrays.asList("A", "B", "A", "C", "D", "B", "A");
        String actual = electionResult.findWinner(votes);
        Assert.assertEquals("A",actual);
    }

    @Test
    public void findWinnerNullTest(){
        List<String> votes = null;
        String actual = electionResult.findWinner(votes);
        Assert.assertEquals("",actual);
    }

    @Test
    public void findWinnerSameVotesTest(){
        List<String> votes = Arrays.asList("A", "A", "A", "B", "B", "B", "C");
        String actual = electionResult.findWinner(votes);
        Assert.assertEquals("A",actual);
    }

}