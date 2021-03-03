package chapter4;

import java.util.*;

public class Test2 {
    public static void main(String[] args) {
//        String s = "asd";
//        char[] c = s.toCharArray();
//        Arrays.sort(c);
//        System.out.println(String.valueOf(c));
        String[] str = {"eat","tea","tan","ate","nat","bat"};
        List<List<String>> res = groupAnagrams(str);
        System.out.println(res);
    }

    public static List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> res = new ArrayList<>();
        Map<String,List<String>> map = new HashMap<>();
        for(String s:strs){
            char[] c = s.toCharArray();
            Arrays.sort(c);
            String tmp = String.valueOf(c);
            List<String> ll = map.get(tmp);
            if(ll!=null){
                map.get(tmp).add(s);
            }else{
                ll = new ArrayList<>();
                ll.add(s);
                map.put(tmp,ll);
            }
        }
        for(Map.Entry<String,List<String>> entry:map.entrySet()){
            res.add(entry.getValue());
        }
        return res;
    }
}
