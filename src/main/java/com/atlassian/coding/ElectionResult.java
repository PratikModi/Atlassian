package com.atlassian.coding;

import java.util.*;

public class ElectionResult {

    public static void main(String[] args) {
        List<List<String>> votes = new ArrayList<>();
        List<String> vote1 = Arrays.asList("A","B","C");
        List<String> vote2 = Arrays.asList("A","B","D");
        List<String> vote3 = Arrays.asList("B","C","A");
        votes.add(vote1);
        votes.add(vote2);
        votes.add(vote3);
        //String winner = findWinnerWithMultipleVotes(votes);
        String winner = findWinnerWithMultipleVotesDifferentStrategy(votes);
        System.out.println(winner);
    }

    public String findWinner(List<String> votes){
        int highestVotes = 0;
        String winner = "";
        if(votes==null || votes.isEmpty())
            return winner;

        Map<String, Integer> candidateVotes = new LinkedHashMap<>();

        for(String vote : votes){
            candidateVotes.putIfAbsent(vote,0);
            candidateVotes.put(vote,candidateVotes.get(vote)+1);
            int totalVotesSoFar = candidateVotes.get(vote);
            if(totalVotesSoFar>highestVotes){
                highestVotes = totalVotesSoFar;
                winner=vote;
            }
        }
        return winner;
    }

    public static String findWinnerWithMultipleVotes(List<List<String>> votes){
        int highestVotes = 0;
        String winner = "";
        if(votes==null || votes.isEmpty())
            return winner;

        Map<String, Integer> candidateVotes = new LinkedHashMap<>();

        for(List<String> voteList : votes){
            for(int i =0;i<voteList.size();i++){
                String candidate = voteList.get(i);
                candidateVotes.putIfAbsent(candidate,0);
                if(i==0) {
                    candidateVotes.put(candidate, candidateVotes.get(candidate) + 3);
                }else if(i==1) {
                    candidateVotes.put(candidate, candidateVotes.get(candidate) + 2);
                }else {
                    candidateVotes.put(candidate, candidateVotes.get(candidate) + 1);
                }
                int totalVotesSoFar = candidateVotes.get(candidate);
                if(totalVotesSoFar>highestVotes){
                    highestVotes = totalVotesSoFar;
                    winner=candidate;
                }
            }
        }
        return winner;
    }

    public static String findWinnerWithMultipleVotesDifferentStrategy(List<List<String>> votes){
        int highestVotes = 0;
        String winner = "";
        if(votes==null || votes.isEmpty())
            return winner;

        Map<String, Map<Integer,Integer>> candidateVotes = new LinkedHashMap<>();

        for(List<String> voteList : votes){
            for(int i =0;i<voteList.size();i++){
                String candidate = voteList.get(i);
                candidateVotes.putIfAbsent(candidate,new HashMap<>());
                candidateVotes.get(candidate).putIfAbsent(i,0);
                if(i==0) {
                    candidateVotes.get(candidate).put(i,(candidateVotes.get(candidate).get(i)+3));
                }else if(i==1) {
                    candidateVotes.get(candidate).put(i,(candidateVotes.get(candidate).get(i)+2));
                }else {
                    candidateVotes.get(candidate).put(i,(candidateVotes.get(candidate).get(i)+1));
                }
            }
        }

        for(Map.Entry<String, Map<Integer,Integer>> entry : candidateVotes.entrySet()){
            String candidate = entry.getKey();
            int totalVotes = entry.getValue().values().stream().reduce((i1,i2) -> i1+i2).get();
            if(totalVotes>=highestVotes){
                if(totalVotes>highestVotes){
                    highestVotes=totalVotes;
                    winner=candidate;
                }else{
                    Map<Integer,Integer> oldWinner = candidateVotes.get(winner);
                    Map<Integer,Integer> newWinner = candidateVotes.get(candidate);
                    for(int i=0;i<3;i++){
                        int oVotes = oldWinner.getOrDefault(i,0);
                        int nVotes = newWinner.getOrDefault(i,0);
                        System.out.println(candidate+"--"+oVotes+"--"+nVotes);
                        if(oVotes!=nVotes && nVotes>oVotes){
                            winner = candidate;
                            break;
                        }
                    }
                }
            }
        }
        System.out.println(candidateVotes);
        return winner;
    }


}
