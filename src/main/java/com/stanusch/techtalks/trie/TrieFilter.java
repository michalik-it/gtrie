package com.stanusch.techtalks.trie;

public interface TrieFilter<V> {
    boolean accept(V item);
}
