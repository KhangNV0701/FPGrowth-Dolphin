package fpgrowth;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    String key = null;
    int val = 0;
    
    Map<String, TrieNode> child = new HashMap<>();
    TrieNode pre = null;
    TrieNode nxt = null;
    
    public TrieNode() {}
    public TrieNode(String key) {
        this.key = key;
    } 
}
