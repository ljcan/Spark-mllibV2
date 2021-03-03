package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MergeArrayDemo {
    public static void main(String[] args) {
        int[][] arr = {{1,3},
                {2,6},{8,10},{15,18}};
        merge(arr);
    }

    public static int[][] merge(int[][] intervals) {
        if(intervals.length==0){
            return new int[0][2];
        }
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0]-o2[0];
            }
        });
         List<int[]> list = new ArrayList<>();
         for(int i=0;i<intervals.length;i++){
            int left = intervals[i][0];
            int right = intervals[i][1];
            if(list.size()==0||list.get(list.size()-1)[1]<left){
                list.add(new int[]{left,right});
            }else {
                list.get(list.size()-1)[1] = Math.max(list.get(list.size()-1)[1],right);
            }
         }
         return list.toArray(new int[list.size()][]);
    }
}
