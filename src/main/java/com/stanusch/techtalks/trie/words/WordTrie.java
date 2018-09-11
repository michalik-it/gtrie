package com.stanusch.techtalks.trie.words;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordTrie {
    private WordTrieNode root;

    public WordTrie() {
        root = new WordTrieNode();
    }

    public void insert(String sentence) {
        WordTrieNode current = root;
        List<String> words = Stream.of(sentence.split("\\s")).collect(Collectors.toList());
        for (String word : words) {
            current = current.getChildren().computeIfAbsent(word, c -> new WordTrieNode());
        }
        current.setEndOfSentence(true);
    }

    public boolean delete(String sentence) {
        return delete(root, sentence, 0);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean containsNode(String sentence) {
        WordTrieNode current = root;
        List<String> words = Stream.of(sentence.split("\\s")).collect(Collectors.toList());
        for (int i = 0; i < words.size(); i++) {
            String ch = words.get(i);
            WordTrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return current.isEndOfSentence();
    }

    private boolean delete(WordTrieNode current, String sentence, int index) {
        List<String> words = Stream.of(sentence.split("\\s")).collect(Collectors.toList());
        if (index == words.size()) {
            if (!current.isEndOfSentence()) {
                return false;
            }
            current.setEndOfSentence(false);
            return current.getChildren().isEmpty();
        }
        String ch = words.get(index);
        WordTrieNode node = current.getChildren().get(ch);
        if (node == null) {
            return false;
        }
        boolean shouldDeleteCurrentNode = delete(node, sentence, index + 1) && !node.isEndOfSentence();

        if (shouldDeleteCurrentNode) {
            current.getChildren().remove(ch);
            return current.getChildren().isEmpty();
        }
        return false;
    }
}