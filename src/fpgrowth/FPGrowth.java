package fpgrowth;

public class FPGrowth {

    public static void main(String[] args) {
        //System.out.println(System.getProperty("user.dir"));
        FPGrowthLib lib = new FPGrowthLib();
        //System.out.println(4 & (1 << 2));
    }
    
    /*private void debug() {
        for(ArrayList<String> arr: transaction) {
            System.out.println(".");
            for(String str: arr) System.out.print(str + " ");
        }
        for(ArrayList<String> arr: transaction) {
            for(String str: arr) 
                System.out.println(str + " " + count.get(str));
        }
        for (Map.Entry<String, LinkedList<TrieNode>> mi : linkTable.entrySet()) {

            for (TrieNode node : mi.getValue()) {
                System.out.println(node.key + " " + node.val);
            }

        }   
    }*/
    
    /*
    public void debug() {
        Queue<TrieNode> q = new LinkedList<>();
        Queue<Integer> h = new LinkedList<>();
        
        q.add(root);
        h.add(0);
        
        while(!q.isEmpty()) {
            TrieNode cur = q.poll();
            int hi = h.poll();
            
            if(cur == null) continue;
            
            System.out.println(cur.key + " " + hi);
            
            for(Map.Entry<String, TrieNode> me: cur.child.entrySet()) {
                q.add(me.getValue());
                h.add(hi + 1);
            }
        }
    }*/
}
