package com.stanusch.techtalks.trie.characters;

public class CharTrie {
    private CharTrieNode root;

    public CharTrie() {
        root = new CharTrieNode();
    }

    public void insert(String word) {
        CharTrieNode current = root;

        for (int i = 0; i < word.length(); i++) {
            current = current.getChildren().computeIfAbsent(word.charAt(i), c -> new CharTrieNode());
        }
        current.setEndOfWord(true);
    }

    public boolean delete(String word) {
        return delete(root, word, 0);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean containsNode(String word) {
        CharTrieNode current = root;

        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            CharTrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return current.isEndOfWord();
    }

    private boolean delete(CharTrieNode current, String word, int index) {
        if (index == word.length()) {
            if (!current.isEndOfWord()) {
                return false;
            }
            current.setEndOfWord(false);
            return current.getChildren().isEmpty();
        }
        char ch = word.charAt(index);
        CharTrieNode node = current.getChildren().get(ch);
        if (node == null) {
            return false;
        }
        boolean shouldDeleteCurrentNode = delete(node, word, index + 1) && !node.isEndOfWord();

        if (shouldDeleteCurrentNode) {
            current.getChildren().remove(ch);
            return current.getChildren().isEmpty();
        }
        return false;
    }
}