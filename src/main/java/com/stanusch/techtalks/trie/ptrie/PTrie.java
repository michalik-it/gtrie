package com.stanusch.techtalks.trie.ptrie;

import com.stanusch.techtalks.trie.TrieFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PTrie<V> {
    private PTrieNode root;
    private TrieFilter<V> resultFilter;
    private Comparator<V> resultComparator = (o1, o2) -> 0;

    /**
     *
     * Constructors
     *
     */

    public PTrie() {
        root = new PTrieNode();
    }

    public PTrie(TrieFilter<V> resultFilter) {
        this.resultFilter = resultFilter;
        root = new PTrieNode();
    }

    public PTrie(Comparator<V> resultComparator) {
        this.resultComparator = resultComparator;
        root = new PTrieNode();
    }

    public PTrie(TrieFilter<V> filter, Comparator<V> comparator) {
        this.resultFilter = filter;
        this.resultComparator = comparator;
    }


    /**
     *
     * Public interface
     *
     */

    public void insert(int[] sequence, V value) {
        int[] sortedSequence = sortAndDistinct(sequence);
        PTrieNode current = root;
        for (int word : sortedSequence) {
            PTrieNode<V> existingNode = (PTrieNode<V>) current.getChildren().get(word);
            if (existingNode == null) {
                PTrieNode newNode = new PTrieNode();
                current.getChildren().put(word, newNode);
                current = newNode;
            } else {
                current = existingNode;
            }
        }
        current.setEndOfSentence(true);
        current.getResults().add(value);
    }

    public boolean delete(int[] sequence, V value) {
        int[] sortedSequence = sortAndDistinct(sequence);
        return delete(root, sortedSequence, value);
    }

    public boolean isEmpty() {
        return root == null || root.getChildren().isEmpty();
    }

    public List<V> find(int[] sequence) {
        int[] words = sortAndDistinct(sequence);
        return this.find(root, words);
    }

    /**
     *
     * Private helper methods
     *
     */

    private List<V> find(PTrieNode localRoot, int[] sequence) {
        List<V> localResults = new ArrayList<>();

        //found results
        if (!localRoot.getResults().isEmpty()) {
            localResults = addIfAccepted(localRoot, localResults);
        }

        if (localRoot.getChildren().isEmpty()) {
            return localResults;
        }

        //run for children
        for (int item : sequence) {
            PTrieNode<V> node = (PTrieNode<V>) localRoot.getChildren().get(item);
            if (node != null) { //found item - search deeper
                localResults.addAll(0, this.find(node, this.getSubsequenceAfterItem(sequence, item)));
            }
        }

        //TODO: sorting, it may be optimized maybe somewhow, maybe there not many elements to sort
        return localResults.stream().sorted(resultComparator).collect(Collectors.toList());
    }

    private List<V> addIfAccepted(PTrieNode localRoot, List<V> localResults) {
        List<V> results = localRoot.getResults();
        if (resultFilter == null) {
            localResults.addAll(results.stream().sorted(resultComparator).collect(Collectors.toList()));
        } else {
            localResults.addAll(results.stream().filter(resultFilter::accept).sorted(resultComparator).collect(Collectors.toList()));
        }
        return localResults;
    }

    private int[] getSubsequenceAfterItem(int[] sequence, int item) {
        List<Integer> sequenceCollection = mapToList(sequence);
        return sequenceCollection.stream().skip(sequenceCollection.indexOf(item) + 1).mapToInt(i -> i).toArray();
    }

    private List<Integer> mapToList(int[] sequence) {
        return Arrays.stream(sequence).boxed().collect(Collectors.toList());
    }

    private int[] sortAndDistinct(int[] sequence) {
        return mapToList(sequence).stream().distinct().sorted().mapToInt(i -> i).toArray();
    }

    private boolean delete(PTrieNode localRoot, int[] sequence, V value) {

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
        for (int item : sequence) {
            PTrieNode<V> node = (PTrieNode<V>) localRoot.getChildren().get(item);
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