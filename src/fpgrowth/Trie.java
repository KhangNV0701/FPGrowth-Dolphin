package fpgrowth;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Trie {
    TrieNode root;
    
    int cnt = 0;
    
    public Trie() {
        root = new TrieNode();
    }
    
    private TrieNode insert(TrieNode cur, TrieNode pre, ArrayList<String> arr, int id, Map<String, LinkedList<TrieNode> > linkTable) {
        cur.val++;
        cur.pre = pre;
        if(arr.size() == id) return cur;
        
        String product = arr.get(id);
        
        if(cur.child.get(product) == null) {
            cur.child.put(product, new TrieNode(product));
            linkTable.get(product).add(cur.child.get(product));
        }
        
        cur.child.put(product, insert(cur.child.get(product), cur, arr, id + 1, linkTable));
        
        return cur;
    }
    
    public void insert(ArrayList<String> transaction, Map<String, LinkedList<TrieNode> > linkTable) {
        root = insert(root, null, transaction, 0, linkTable);
    }
   
    public ArrayList<TrieNode> reverse(TrieNode node) {
        ArrayList<TrieNode> result = new ArrayList<>();
        
        while(node != null) {
            result.add(node);
            node = node.pre;
        }
        
        return result;
    }
}
