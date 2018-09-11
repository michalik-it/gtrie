package com.stanusch.techtalks.trie;

import com.stanusch.techtalks.trie.characters.CharTrie;
import com.stanusch.techtalks.trie.gtrie.GTrie;
import com.stanusch.techtalks.trie.ptrie.PTrie;
import com.stanusch.techtalks.trie.words.WordTrie;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrieApplicationTests {

    Fact f1 = new Fact();
    Fact f2 = new Fact();
    Fact f3 = new Fact();

    @Before
    public void setUp() {
        f1 = new Fact();
        f2 = new Fact();
        f3 = new Fact();
    }

    @Test
    public void contextLoads() {

    }

    @Test
    public void characterTrieTest() {
        CharTrie trie = createExampleCharacterTrie();
        assertFalse(trie.containsNode("3"));
        assertFalse(trie.containsNode("vida"));
        assertTrue(trie.containsNode("life"));
        trie.delete("life");
        assertFalse(trie.containsNode("life"));
    }

    @Test
    public void sentenceTrieTest() {
        WordTrie trie = createExampleSentenceTrie();
        assertFalse(trie.containsNode("Kamil leci w piłeczki"));
        assertFalse(trie.containsNode("Marek jest ok"));
        assertTrue(trie.containsNode("Kamil jest spoko"));
    }

    /**
     *
     *  Generic GTree Tests Starts
     *
     */
    @Test
    public void gTrieTest() {
        GTrie trie = createExampleGTrie();

        assertTrue(trie.find(Lists.newArrayList("nie", "posiadać", "yyy","zzz")).contains(2));
        assertTrue(trie.delete(Lists.newArrayList("nie","posiadać","zzz","yyy"), 2));
        assertFalse(trie.find(Lists.newArrayList("nie", "posiadać", "yyy", "zzz")).contains(2));
        assertTrue(trie.find(Lists.newArrayList("aaa", "bolek")).contains(4));
        assertTrue(trie.find(Lists.newArrayList("aaa", "janek")).contains(3));
        assertTrue(trie.find(Lists.newArrayList("janek")).isEmpty());
        assertTrue(trie.find(Lists.newArrayList("aaa", "bolek", "janek")).size() == 2);

    }

    @Test
    public void intGTrieTest() {
        GTrie<Integer, Integer> trie = createExampleIntGTrie();

        assertTrue(trie.find(Lists.newArrayList(1, 2)).isEmpty());
        assertTrue(trie.find(Lists.newArrayList(1, 2, 4)).contains(2));


        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).size() == 2);
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).contains(1));
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).contains(3));
        boolean delete = trie.delete(Lists.newArrayList(1, 2, 3), 3);
        assertTrue(delete);
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).size() == 1);
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).contains(1));
        delete = trie.delete(Lists.newArrayList(1, 2, 3), 10);
        assertFalse(delete);
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).contains(1));
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).size() == 1);
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).contains(1));
        delete = trie.delete(Lists.newArrayList(1, 2, 3), 1);
        assertTrue(delete);
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).size() == 0);
        trie.insert(Lists.newArrayList(1,2,3), 1);
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3, 4, 5, 6, 7)).contains(1));

        assertTrue(trie.find(Lists.newArrayList(1, 2, 3, 4)).contains(3));
        trie.delete(Lists.newArrayList(1,2,3), 1);
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3, 4)).contains(3));
        trie.delete(Lists.newArrayList(1,2,4), 2);


        assertTrue(trie.find(Lists.newArrayList(1, 2, 3, 4)).contains(3));
        trie.delete(Lists.newArrayList(1,2,3), 3);
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3, 4)).contains(3));
        trie.delete(Lists.newArrayList(100, 200), 3);

        System.out.println(trie);

        assertTrue(trie.find(Lists.newArrayList(1, 2, 3, 4)).contains(3));
        assertTrue(trie.delete(Lists.newArrayList(1, 2, 3, 4), 3));

        System.out.println(trie);

        assertTrue(trie.isEmpty());
    }

    @Test
    public void intGTrieTest_shouldConsiderNumberOfMatchedItems() {
        GTrie<Integer, Fact> trie = createExampleIntGTrieForCompare();

        assertTrue(trie.find(Lists.newArrayList(1, 2, 3, 4, 5, 6)).size() == 2);
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3, 4, 5, 6)).contains(f1));
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3, 4, 5, 6)).contains(f2));
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3, 4, 5, 6)).get(0).equals(f2));
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3, 4, 5, 6)).get(1).equals(f1));
    }

    @Test
    public void intGTrieTest_shouldConsiderFilter() {
        int currentTopic1 = 0;
        GTrie<Integer, Fact> trie = createExampleFactGTrie(item -> item.topic == currentTopic1);

        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).contains(f1));
        assertFalse(trie.find(Lists.newArrayList(1, 2, 3)).contains(f3));

        int currentTopic2 = 1;
        trie = createExampleFactGTrie(item -> item.topic == currentTopic2);

        assertFalse(trie.find(Lists.newArrayList(1, 2, 3)).contains(f1));
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).contains(f3));

    }

    @Test
    public void intGTrieTest_shouldConsiderPriorityInSameNode() {
        GTrie<Integer, Fact> trie = createExampleFactGTrieWithPriorityInSameNode();

        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).contains(f1));
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).contains(f3));

        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).get(0).equals(f3));
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).get(1).equals(f1));
    }

    @Test
    public void intGTrieTest_shouldConsiderPriorityInDifferentNodes() {
        GTrie<Integer, Fact> trie = createExampleFactGTrieWithPriorityInDifferentNode();

        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).contains(f1));
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).contains(f3));

        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).get(0).equals(f3));
        assertTrue(trie.find(Lists.newArrayList(1, 2, 3)).get(1).equals(f1));
    }

    /**
     *
     *  Primitive PTree Tests Starts
     *
     */
    @Test
    public void intPTrieTest_shouldConsiderNumberOfMatchedItems() {
        PTrie<Fact> trie = createExampleIntPTrieForCompare();

        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3, 4, 5, 6))).size() == 2);
        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3, 4, 5, 6))).contains(f1));
        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3, 4, 5, 6))).contains(f2));
        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3, 4, 5, 6))).get(0).equals(f2));
        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3, 4, 5, 6))).get(1).equals(f1));
    }

    @Test
    public void intPTrieTest_shouldConsiderFilter() {
        int currentTopic1 = 0;
        PTrie<Fact> trie = createExampleFactPTrie(item -> item.topic == currentTopic1);

        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3))).contains(f1));
        assertFalse(trie.find(toArray(Lists.newArrayList(1, 2, 3))).contains(f3));

        int currentTopic2 = 1;
        trie = createExampleFactPTrie(item -> item.topic == currentTopic2);

        assertFalse(trie.find(toArray(Lists.newArrayList(1, 2, 3))).contains(f1));
        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3))).contains(f3));

    }

    @Test
    public void intPTrieTest_shouldConsiderPriorityInSameNode() {
        PTrie<Fact> trie = createExampleFactPTrieWithPriorityInSameNode();

        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3))).contains(f1));
        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3))).contains(f3));

        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3))).get(0).equals(f3));
        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3))).get(1).equals(f1));
    }

    @Test
    public void intPTrieTest_shouldConsiderPriorityInDifferentNodes() {
        PTrie<Fact> trie = createExampleFactPTrieWithPriorityInDifferentNode();

        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3))).contains(f1));
        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3))).contains(f3));

        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3))).get(0).equals(f3));
        assertTrue(trie.find(toArray(Lists.newArrayList(1, 2, 3))).get(1).equals(f1));
    }


    /**
     *
     * Sample fact class
     *
     */
    private class Fact implements Comparable{
        public int topic;
        public String content;
        public int priority;

        @Override
        public int compareTo(Object o) {
            return Integer.compare(((Fact)o).priority ,this.priority);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Fact fact = (Fact) o;
            return topic == fact.topic &&
                    priority == fact.priority &&
                    Objects.equals(content, fact.content);
        }

        @Override
        public int hashCode() {
            return Objects.hash(topic, content, priority);
        }
    }

    /**
     *
     * Fixtures
     *
     */
    private CharTrie createExampleCharacterTrie() {
        CharTrie trie = new CharTrie();
        trie.insert("Programming");
        trie.insert("is");
        trie.insert("a");
        trie.insert("way");
        trie.insert("of");
        trie.insert("life");

        return trie;
    }

    private WordTrie createExampleSentenceTrie() {
        WordTrie trie = new WordTrie();
        trie.insert("Kamil jest spoko");
        trie.insert("Marek jest spoko");
        trie.insert("Kamil leci w kulki");
        trie.insert("Marek ma bułke");

        return trie;
    }

    /**
     *
     * Gtree fixtures
     *
     */
    private GTrie createExampleGTrie() {
        GTrie trie = new GTrie();

        trie.insert(Lists.newArrayList("nie","posiadać","konto"), 1);
        trie.insert(Lists.newArrayList("nie","mieć","konto"), 1);
        trie.insert(Lists.newArrayList("nie","posiadać","zzz","yyy"), 2);
        trie.insert(Lists.newArrayList("aaa","janek"), 3);
        trie.insert(Lists.newArrayList("aaa","bolek"), 4);
        return trie;
    }

    private GTrie createExampleIntGTrie() {
        GTrie<Integer, Integer> trie = new GTrie();

        trie.insert(Lists.newArrayList(1,2,3), 1);
        trie.insert(Lists.newArrayList(1,2,4), 2);
        trie.insert(Lists.newArrayList(1,2,3), 3);
        trie.insert(Lists.newArrayList(1,2,3,4), 3);
        return trie;
    }

    private GTrie createExampleIntGTrieForCompare() {
        GTrie<Integer, Fact> trie = new GTrie();

        trie.insert(Lists.newArrayList(1,2,3), f1);
        trie.insert(Lists.newArrayList(3,4,5,6), f2);
        return trie;
    }

    private GTrie createExampleFactGTrie(TrieFilter<Fact> filter) {
        GTrie<Integer, Fact> trie = new GTrie<>(filter);

        f1 = new Fact(); f1.content="jestem f1"; f1.topic=0;
        f2 = new Fact(); f2.content="jestem f2"; f2.topic=0;
        f3 = new Fact(); f3.content="jestem f3"; f3.topic=1;

        trie.insert(Lists.newArrayList(1,2,3), f1);
        trie.insert(Lists.newArrayList(3,4), f2);
        trie.insert(Lists.newArrayList(1,2), f3);
        return trie;
    }

    private GTrie createExampleFactGTrieWithPriorityInSameNode() {
        GTrie<Integer, Fact> trie = new GTrie<>((o1, o2) -> Integer.compare(o2.priority,o1.priority));

        f1 = new Fact(); f1.content="jestem f1"; f1.topic=0; f1.priority=100;
        f2 = new Fact(); f2.content="jestem f2"; f2.topic=0; f2.priority=100;
        f3 = new Fact(); f3.content="jestem f3"; f3.topic=0; f3.priority=101;

        trie.insert(Lists.newArrayList(1,2,3), f1);
        trie.insert(Lists.newArrayList(3,4), f2);
        trie.insert(Lists.newArrayList(1,2,3), f3);
        return trie;
    }

    private GTrie createExampleFactGTrieWithPriorityInDifferentNode() {
        GTrie<Integer, Fact> trie = new GTrie<>( (o1, o2) -> Integer.compare(o2.priority,o1.priority));

        f1 = new Fact(); f1.content="jestem f1"; f1.topic=0; f1.priority=100;
        f2 = new Fact(); f2.content="jestem f2"; f2.topic=0; f2.priority=100;
        f3 = new Fact(); f3.content="jestem f3"; f3.topic=0; f3.priority=101;

        trie.insert(Lists.newArrayList(1,2,3), f1);
        trie.insert(Lists.newArrayList(3,4), f2);
        trie.insert(Lists.newArrayList(1,2), f3);
        return trie;
    }

    /**
     *
     * Ptree fixtures
     *
     */
    private PTrie createExampleIntPTrieForCompare() {
        PTrie<Fact> trie = new PTrie();

        trie.insert(toArray(Lists.newArrayList(1,2,3)), f1);
        trie.insert(toArray(Lists.newArrayList(3,4,5,6)), f2);
        return trie;
    }

    private PTrie createExampleFactPTrie(TrieFilter<Fact> filter) {
        PTrie<Fact> trie = new PTrie<>(filter);

        f1 = new Fact(); f1.content="jestem f1"; f1.topic=0;
        f2 = new Fact(); f2.content="jestem f2"; f2.topic=0;
        f3 = new Fact(); f3.content="jestem f3"; f3.topic=1;

        trie.insert(toArray(Lists.newArrayList(1,2,3)), f1);
        trie.insert(toArray(Lists.newArrayList(3,4)), f2);
        trie.insert(toArray(Lists.newArrayList(1,2)), f3);
        return trie;
    }

    private PTrie createExampleFactPTrieWithPriorityInSameNode() {
        PTrie<Fact> trie = new PTrie<>((o1, o2) -> Integer.compare(o2.priority,o1.priority));

        f1 = new Fact(); f1.content="jestem f1"; f1.topic=0; f1.priority=100;
        f2 = new Fact(); f2.content="jestem f2"; f2.topic=0; f2.priority=100;
        f3 = new Fact(); f3.content="jestem f3"; f3.topic=0; f3.priority=101;

        trie.insert(toArray(Lists.newArrayList(1,2,3)), f1);
        trie.insert(toArray(Lists.newArrayList(3,4)), f2);
        trie.insert(toArray(Lists.newArrayList(1,2,3)), f3);
        return trie;
    }

    private PTrie createExampleFactPTrieWithPriorityInDifferentNode() {
        PTrie<Fact> trie = new PTrie<>( (o1, o2) -> Integer.compare(o2.priority,o1.priority));

        f1 = new Fact(); f1.content="jestem f1"; f1.topic=0; f1.priority=100;
        f2 = new Fact(); f2.content="jestem f2"; f2.topic=0; f2.priority=100;
        f3 = new Fact(); f3.content="jestem f3"; f3.topic=0; f3.priority=101;

        trie.insert(toArray(Lists.newArrayList(1,2,3)), f1);
        trie.insert(toArray(Lists.newArrayList(3,4)), f2);
        trie.insert(toArray(Lists.newArrayList(1,2)), f3);
        return trie;
    }


    private int[] toArray(List<Integer> src) {
        return src.stream().mapToInt(i -> i).toArray();
    }
}
