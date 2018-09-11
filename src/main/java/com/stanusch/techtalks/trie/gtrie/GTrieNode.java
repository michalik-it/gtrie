package com.stanusch.techtalks.trie.gtrie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GTrieNode<V> {
    private final Map<String, GTrieNode> children = new ConcurrentHashMap<>();
    private boolean hasResults;
    private List<V> results = new ArrayList<>();

    Map<String, GTrieNode> getChildren() {
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