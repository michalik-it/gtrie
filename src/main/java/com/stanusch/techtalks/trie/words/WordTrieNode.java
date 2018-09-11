package com.stanusch.techtalks.trie.words;

import java.util.HashMap;
import java.util.Map;

public class WordTrieNode {
    private final Map<String, WordTrieNode> children = new HashMap<>();
    private boolean endOfSentence;

    Map<String, WordTrieNode> getChildren() {
        return children;
    }

    public boolean isEndOfSentence() {
        return endOfSentence;
    }

    public void setEndOfSentence(boolean endOfSentence) {
        this.endOfSentence = endOfSentence;
    }
}