package fpgrowth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FPGrowthLib {
    int noTransaction;
    int minSupport = 2;
    ArrayList<ArrayList<String> > transaction;
    Map<String, Integer> count;
    Map<String, LinkedList<TrieNode> > linkTable;
    long startTime, endTime;
    
    Trie dataTrie;
    
    public FPGrowthLib() {
        startTime = System.currentTimeMillis();
        
        transaction = new ArrayList< >();
        count = new HashMap<>();
        linkTable = new HashMap<>();
        dataTrie = new Trie();
        
        readData();
        countProduct();
        prepareData();
        insertData();
        getSet();
        
        endTime = System.currentTimeMillis();
        
        printResult();
    }
    
    private void convert(String data) {
        String[] list = data.split(" ");
        
        ArrayList<String> newArray = new ArrayList<>();
        for(String str: list)
            newArray.add(str.trim());
        
        minSupport = (int) Math.ceil((double)minSupport * 0);
        transaction.add(newArray);
        ++noTransaction;
        
    }
    
    private void readData() {
        String dir = System.getProperty("user.dir") + "\\data4.txt";
        File file = new File(dir);
        try {
            Scanner fileReader = new Scanner(file);
            
            while(fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                convert(data);
            }
            
        } catch (FileNotFoundException ex) {    
            System.out.println("File not found!");
        }  
    }
    
    private void countProduct() {
        for(ArrayList<String> arr: transaction)
            for(String str: arr) 
                count.merge(str, 1, (a, b) -> a + b);
    }
    
    public void prepareData() {
        for(ArrayList<String> arr: transaction) {
            Collections.sort(arr, (String str1, String str2) -> {
                if(count.get(str1) > count.get(str2)) return -1;
                else if(count.get(str1) < count.get(str2) ) return 1;
                else {
                    if(str1.compareToIgnoreCase(str2) < 0) return -1;
                    else return 1;
                }
            }); 
            
            while(!arr.isEmpty() && count.get(arr.get(arr.size() - 1)) < minSupport) 
                arr.remove(arr.size() - 1);
            
            for(String str: arr) 
                if(linkTable.get(str) == null) linkTable.put(str, new LinkedList<>());
        
            }
    }
    
    public void insertData() {
        for(int i = 0;i < noTransaction;++i)
            dataTrie.insert(transaction.get(i), linkTable);
    }
    
    ArrayList<ArrayList<String> > container = new ArrayList<>();
    ArrayList<Integer> cnt = new ArrayList<>();
    ArrayList<ArrayList<String> > result = new ArrayList<>();
    ArrayList<Integer> freqResult = new ArrayList<>();
    
    void getFrequencyList(Set<String> list, String str, int freqCount) {
        Iterator<String> ptr = list.iterator();
        
        String tmp = null;
        
        ArrayList<String> ans = new ArrayList<>();
        
        while(ptr.hasNext()) {
            tmp = ptr.next();
            if(tmp == str) continue;
            ans.add(tmp);
        }
        ans.add(str);
        result.add(ans);
        freqResult.add(freqCount);
        //System.out.println("");
    }
    
    void mineData(String curProduct) {
        Map<String, Integer> freq = new HashMap<>();
        
        for(int i = 0;i < container.size();++i){
            ArrayList<String> prefixList = container.get(i); 
            for(String str: prefixList)
                freq.merge(str, cnt.get(i), (a, b) -> a + b);
        }
        ArrayList<String> valid = new ArrayList<>();
        
        for(Map.Entry<String, Integer> me: freq.entrySet()) {
            if(me.getValue() >= minSupport) 
                valid.add(me.getKey());
        }
        
        int sz = valid.size();
        
        for(int state = 1;state <= Math.pow(2, sz) - 1;++state) {
            Set<String> comb = new HashSet<>();
            for(int j = 0;j <= sz - 1;++j)
                if((state >> j & 1) == 1) {
                    //System.out.println(state + " " + j + " " + (1 << j));
                    comb.add(valid.get(j));
                }
            if(!comb.contains(curProduct)) continue;
            int freqCount = 0;
            for(int i = 0;i < container.size();++i) {
                ArrayList<String> prefixList = container.get(i);
                
                Set<String> prefix = new HashSet<>(prefixList);
                prefix.retainAll(comb);
                if(prefix.size() == comb.size()) freqCount += cnt.get(i);
            }
            if(freqCount >= minSupport) getFrequencyList(comb, curProduct, freqCount);
        }
    }
    
    void addToContainer(ArrayList<TrieNode> arr) {
        ArrayList<String> tmp = new ArrayList<>();
        
        cnt.add(arr.get(0).val);
        
        for(TrieNode node: arr)
            if(node.key != null)
                tmp.add(node.key);
        Collections.reverse(tmp);
        //for(String str: tmp) System.out.println(str);
        container.add(tmp);
    }
    
    public void reset() {
        container.clear();
        cnt.clear();
    }
            
    public void getSet() {
        ArrayList<String> sortedKeys = new ArrayList<String>(linkTable.keySet());
        
        Collections.sort(sortedKeys, (String str1, String str2) -> {
            if(count.get(str1) > count.get(str2)) return -1;
                else if(count.get(str1) < count.get(str2) ) return 1;
                else {
                    if(str1.compareToIgnoreCase(str2) < 0) return -1;
                    else return 1;
                }
            }); 
        
        for(int i = sortedKeys.size() - 1;i >= 0;--i) {
            String str = sortedKeys.get(i);
            
            LinkedList<TrieNode> prefix = linkTable.get(str);
            
            reset();
            
            for(TrieNode node: prefix) {
                ArrayList<TrieNode> arr = dataTrie.reverse(node);
                //printList(arr);
                addToContainer(arr);
            }
            
            mineData(str);
        }
    }
    
    private void printFile() {
        String dir = System.getProperty("user.dir") + "\\output1.txt";
        
        /*File file = new File(dir);
        try {
            if(file.createNewFile()) System.out.println("Successfully created output!");
            else System.out.println("File existed!");
        } catch (IOException ex) {
            System.out.println("Error occured");
        }*/
        
        try {
            FileWriter output = new FileWriter(dir);
            int id = 0;

            for(ArrayList<String> arr: result) {
                for(String str: arr) output.write(str + " ");
                output.write(":" + freqResult.get(id) + "\n");
                id++;
            }
            output.close();
        } catch (IOException ex) {
            System.out.println("Error occured");
        }
    }
    
    private void printResult() {
        printFile();
        long totalTime = endTime - startTime;
        
        System.out.println("Total transaction from database: " + noTransaction);
        System.out.println("Total frequent itemsets count: " + result.size());
        System.out.println("Total time: " + totalTime + "ms");
        
    }
}
