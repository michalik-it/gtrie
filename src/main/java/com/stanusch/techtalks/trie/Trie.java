package com.stanusch.techtalks.trie;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public interface Trie<K, V> {
    void insert(Collection<K> sequence, V value);
    boolean remove(Collection<K> sequence, V value);
    boolean isEmpty();
    List<V> find(Collection<K> sequence);
    List<V> find(Collection<K> sequence, Comparator<V> resultComparator);
    List<V> find(Collection<K> sequence, TrieFilter<V> resultFilter);
    List<V> find(Collection<K> sequence, Comparator<V> resultComparator, TrieFilter<V> resultFilter);
}
