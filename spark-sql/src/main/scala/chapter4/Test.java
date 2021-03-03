package chapter4;

import java.util.*;

public class Test {
    public static void main(String[] args) {
        char c1 = 'a';
//        char c2 = ''
//        String str = "asd";
////        str.charAt()
//        System.out.println((int) c1);
//
//        StringBuilder builder = new StringBuilder();
//        Map<String, List<String>> map = new HashMap<>();
//        map.put()
//        for (Map.Entry<String,List<String>> entry : map.entrySet()){
//            List<String> value = entry.getValue();
//        }


        String[] str = {"eat","tea","tan","ate","nat","bat"};
        List<List<String>> res = groupAnagrams(str);
        System.out.println(res);
    }

    public static List<List<String>> groupAnagrams(String[] strs) {
        int len = strs.length;
        List<List<String>> res = new ArrayList<>();
        int[] has = new int[26];
        boolean flag = true;
        Map<String,List<String>> map = new HashMap<>();
        StringBuilder key = null;
        for(int i=0;i<len;i++){
            String tmp = strs[i];
            key = new StringBuilder();
            for(int j=0;j<tmp.length();j++){
                char c = tmp.charAt(j);
                int index = (int)c -97;
                key.append(index);         //key必须按照顺序
//                Arrays.sort();
                if(has[index]==0){
                    has[index]=1;
                    flag = false;
//                    break;
                }
            }
            if(flag){
//                if(map.get(key)==null){
//                    List<String> list = new ArrayList();
//                    list.add(tmp);
//                    map.put(key.toString(),list);
//                }else{
                    map.get(key.toString()).add(tmp);
//                }
            }else{
                List<String> ll = new ArrayList<>();
                ll.add(tmp);
                map.put(key.toString(),ll);
            }
            flag = true;
        }
        for (Map.Entry<String,List<String>> entry : map.entrySet()){
            List<String> value = entry.getValue();
            res.add(value);
        }
        return res;
    }
}
