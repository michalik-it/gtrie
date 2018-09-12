package com.stanusch.techtalks.trie.gtrie;

import com.stanusch.techtalks.trie.Trie;
import com.stanusch.techtalks.trie.TrieFilter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @param <K> A key for trie structure, must extend Comparable and must have valid hashode and equals
 * @param <V> A value (result) for trie structure
 */
public class GTrie<K extends Comparable, V> implements Trie<K,V> {
    private GTrieNode<K,V> root;
    private TrieFilter<V> resultFilter = item -> true;
    private Comparator<V> resultComparator = (o1, o2) -> 0;

    /**
     *
     * Constructors
     *
     */

    public GTrie() {
        root = new GTrieNode<>();
    }

    public GTrie(TrieFilter<V> resultFilter) {
        this.resultFilter = resultFilter;
        root = new GTrieNode<>();
    }

    public GTrie(Comparator<V> resultComparator) {
        this.resultComparator = resultComparator;
        root = new GTrieNode<>();
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
    @Override
    public void insert(Collection<K> sequence, V value) {
        List<K> sortedSequence = sortAndDistinct(sequence);
        GTrieNode<K,V> current = root;
        for (K word : sortedSequence) {
            GTrieNode<K,V> existingNode = current.getChildren().get(word);
            if (existingNode == null) {
                GTrieNode newNode = new GTrieNode();
                current.getChildren().put(word, newNode);
                current = newNode;
            } else {
                current = existingNode;
            }
        }
        //add result for current node
        current.getResults().add(value);
    }

    @Override
    public boolean remove(Collection<K> sequence, V value) {
        List<K> sortedSequence = sortAndDistinct(sequence);
        return remove(root, sortedSequence, value);
    }

    @Override
    public boolean isEmpty() {
        return root == null || root.getChildren().isEmpty();
    }

    @Override
    public List<V> find(Collection<K> sequence) {
        List<K> words = sortAndDistinct(sequence);
        return this.find(root, words);
    }

    @Override
    public List<V> find(Collection<K> sequence, Comparator<V> resultComparator) {
        this.resultComparator = resultComparator;
        return this.find(sequence);
    }

    @Override
    public List<V> find(Collection<K> sequence, TrieFilter<V> resultFilter) {
        this.resultFilter = resultFilter;
        return this.find(sequence);
    }

    @Override
    public List<V> find(Collection<K> sequence, Comparator<V> resultComparator, TrieFilter<V> resultFilter) {
        this.resultComparator = resultComparator;
        this.resultFilter = resultFilter;
        return this.find(sequence);
    }

    /**
     *
     * Private helper methods
     *
     */

    private List<V> find(GTrieNode<K,V> localRoot, List<K> sequence) {
        List<V> localResults = new ArrayList<>();

        //found results
        if (!localRoot.getResults().isEmpty()) {
            addIfAccepted(localRoot, localResults);
        }

        if (localRoot.getChildren().isEmpty()) {
            return localResults;
        }

        //run for children
        for (K item : sequence) {
            GTrieNode<K,V> node = localRoot.getChildren().get(item);
            if (node != null) { //found item - search deeper
                localResults.addAll(0, this.find(node, this.getSubsequenceAfterItem(sequence, item)));
            }
        }

        return localResults.stream().sorted(resultComparator).collect(Collectors.toList());
    }

    private List<V> addIfAccepted(GTrieNode<K,V> localRoot, List<V> localResults) {
        Set<V> results = localRoot.getResults();
        localResults.addAll(
                results.stream().filter(resultFilter::accept).sorted(resultComparator).collect(Collectors.toList()));
        return localResults;
    }

    private List<K> getSubsequenceAfterItem(List<K> sequence, K item) {
        return sequence.stream().skip(sequence.indexOf(item) + 1L).collect(Collectors.toList());
    }

    private List<K> sortAndDistinct(Collection<K> sequence) {
        return sequence.stream().distinct().sorted().collect(Collectors.toList());
    }

    private boolean remove(GTrieNode<K,V> localRoot, List<K> sequence, V value) {

        Set<V> results = localRoot.getResults();
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
            GTrieNode<K,V> node = localRoot.getChildren().get(item);
            if (node != null) { //found item - search deeper
                result = this.remove(node, this.getSubsequenceAfterItem(sequence, item), value);
                if (node.getChildren().isEmpty()) {
                    localRoot.getChildren().remove(item);
                }
            }
        }

        return result;
    }
}