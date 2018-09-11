package com.stanusch.techtalks.trie.characters;

import java.util.HashMap;
import java.util.Map;

public class CharTrieNode {
    private final Map<Character, CharTrieNode> children = new HashMap<>();
    private boolean endOfWord;

    Map<Character, CharTrieNode> getChildren() {
        return children;
    }

    boolean isEndOfWord() {
        return endOfWord;
    }

    void setEndOfWord(boolean endOfWord) {
        this.endOfWord = endOfWord;
    }
}