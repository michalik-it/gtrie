package com.stanusch.techtalks.trie.gtrie;

import com.stanusch.techtalks.trie.TrieFilter;

import java.util.*;
import java.util.stream.Collectors;

public class GTrie<K extends Comparable, V> {
    private GTrieNode root;

    private TrieFilter<V> resultFilter;
    private Comparator<V> resultComparator = (o1, o2) -> 0;

    /**
     *
     * Constructors
     *
     */

    public GTrie() {
        root = new GTrieNode();
    }

    public GTrie(TrieFilter<V> resultFilter) {
        this.resultFilter = resultFilter;
        root = new GTrieNode();
    }

    public GTrie(Comparator<V> resultComparator) {
        this.resultComparator = resultComparator;
        root = new GTrieNode();
    }

    public GTrie(TrieFilter<V> filter, Comparator<V> comparator) {
        this.resultFilter = filter;
        this.resultComparator = comparator;
    }

    /**
     *
     * Public interface
     *
     */

    public void insert(List<K> sequence, V value) {
        List<K> sortedSequence = sortAndDistinct(sequence);
        GTrieNode current = root;
        for (K word : sortedSequence) {
            GTrieNode<V> existingNode = (GTrieNode<V>) current.getChildren().get(word);
            if (existingNode == null) {
                GTrieNode newNode = new GTrieNode();
                current.getChildren().put(word, newNode);
                current = newNode;
            } else {
                current = existingNode;
            }
        }
        current.setEndOfSentence(true);
        current.getResults().add(value);
    }

    public boolean delete(List<K> sequence, V value) {
        List<K> sortedSequence = sortAndDistinct(sequence);
        return delete(root, sortedSequence, value);
    }

    public boolean isEmpty() {
        return root == null || root.getChildren().isEmpty();
    }

    public List<V> find(List<K> sequence) {
        List<K> words = sortAndDistinct(sequence);
        return this.find(root, words);
    }

    /**
     *
     * Private helper methods
     *
     */

    private List<V> find(GTrieNode localRoot, List<K> sequence) {
        List<V> localResults = new ArrayList<>();

        //found results
        if (!localRoot.getResults().isEmpty()) {
            localResults = addIfAccepted(localRoot, localResults);
        }

        if (localRoot.getChildren().isEmpty()) {
            return localResults;
        }

        //run for children
        for (K item : sequence) {
            GTrieNode<V> node = (GTrieNode<V>) localRoot.getChildren().get(item);
            if (node != null) { //found item - search deeper
                localResults.addAll(0, this.find(node, this.getSubsequenceAfterItem(sequence, item)));
            }
        }

        //TODO: sorting, it may be optimized maybe somewhow
        return localResults.stream().sorted(resultComparator).collect(Collectors.toList());
    }

    private List<V> addIfAccepted(GTrieNode localRoot, List<V> localResults) {
        List<V> results = localRoot.getResults();
        if (resultFilter == null) {
            localResults.addAll(results.stream().sorted(resultComparator).collect(Collectors.toList()));
        } else {
            localResults.addAll(results.stream().filter(resultFilter::accept).sorted(resultComparator).collect(Collectors.toList()));
        }
        return localResults;
    }

    private List<K> getSubsequenceAfterItem(List<K> sequence, K item) {
        return sequence.stream().skip(sequence.indexOf(item) + 1).collect(Collectors.toList());
    }

    private List<K> sortAndDistinct(List<K> sequence) {
        return sequence.stream().distinct().sorted().collect(Collectors.toList());
    }

    private boolean delete(GTrieNode localRoot, List<K> sequence, V value) {

        List results = localRoot.getResults();
        if (!results.isEmpty() && results.contains(value)) {
            results.remove(value);
            if (results.isEmpty() && localRoot.getChildren().isEmpty()) {
                return true;
            }
            return true;
        }

        if (localRoot.getChildren().isEmpty()) {
            if (results.isEmpty()) {
                return false;
            }
            return false;
        }

        //run for children
        boolean result = false;
        for (K item : sequence) {
            GTrieNode<V> node = (GTrieNode<V>) localRoot.getChildren().get(item);
            if (node != null) { //found item - search deeper
                result = this.delete(node, this.getSubsequenceAfterItem(sequence, item), value);
                if (node.getChildren().isEmpty()) {
                    localRoot.getChildren().remove(item);
                }
            }
        }

        return result;
    }
}