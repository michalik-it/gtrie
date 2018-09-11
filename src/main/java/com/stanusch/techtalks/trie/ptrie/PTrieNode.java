package com.stanusch.techtalks.trie.ptrie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PTrieNode<V> {
    private final Map<String, PTrieNode> children = new ConcurrentHashMap<>();
    private boolean hasResults;
    private List<V> results = new ArrayList<>();

    Map<String, PTrieNode> getChildren() {
        return children;
    }

    public boolean hasResults() {
        return hasResults;
    }

    public void setEndOfSentence(boolean endOfSentence) {
        this.hasResults = endOfSentence;
    }

    public List<V> getResults() {
        return results;
    }

    public void setResults(List<V> results) {
        this.results = results;
    }
}