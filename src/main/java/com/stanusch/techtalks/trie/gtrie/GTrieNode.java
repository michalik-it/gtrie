package com.stanusch.techtalks.trie.gtrie;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

class GTrieNode<K, V> {
    private final Map<K, GTrieNode<K,V>> children = Maps.newConcurrentMap();
    private Set<V> results = Sets.newConcurrentHashSet();

    Map<K, GTrieNode<K,V>> getChildren() {
        return children;
    }
    Set<V> getResults() {
        return results;
    }
}