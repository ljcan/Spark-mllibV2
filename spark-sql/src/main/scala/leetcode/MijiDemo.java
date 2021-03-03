package leetcode;

public class MijiDemo {
    public static void main(String[] args) {
//        int a = (int) Math.pow(2,3);
//        System.out.println(a);
        String s = to2(8);
        System.out.println(s);
    }

    public static String to2(int i){
        StringBuilder builder = new StringBuilder();
        while((i/2)>=0){
            if(i/2==0){
                builder.append(i%2);
                break;
            }
            builder.append(i%2);
            i=i/2;
        }
        return builder.toString();
    }
}
