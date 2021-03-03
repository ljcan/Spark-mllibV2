package algorithm;

public class Test {
    private static int bytelimit=5;//小数后限制位数
    /**
     * @author zhao33699
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // 已知Pi可以用函数4 * (1 – 1/3 + 1/5 – 1/7 + …) 计算，小数点后五位----计算圆周率
        //思路：按照上述公式计算出5位值，和下一次5位值比较，如果相同则为最终结果，
        //如果不同，则保存新值，继续与在下次的值比较直至相同

        double pitemp = 0;//根据公式所得值
        double finalpi=0; //上次所得值（小数点后五位）
        double pi=0;//本次所得值（小数点后五位）
        int i = 0;//计数器
        double b = 0;//公式的括号内的值
        int ii=20;//所得相同值次数,可限制最后是有连续ii次所得相同值
        int finalii=20;//与ii值相同，用于在ii值变化后 ，恢复ii值
        //int iii=20;//限制相同次数
        while (true) {

            //次数控制
            if(i==1000000){
                break;
            }

            double rs=1+2*i;
            double d = 1/rs ;
            if (i % 2 == 0&&i!=1) {
                b=b+d;
                //System.out.println("---"+i+"----正------***"+b);
            } else {
                b=b-d;
                //System.out.println("---"+i+"----负------***"+b);
            }

            i=i+1;
            //System.out.println(b);
            pitemp = (b) * 4;
//			System.out.println(pitemp);
            //小数点后位数大于等于5位
            if(String.valueOf(pitemp).length()>bytelimit){
                pi=subInt(pitemp);//截取小数点后5位的值
                System.out.println(i+"次---pi--####"+pi+"---final--####"+finalpi);

                //如果上次结果与本次结果相同，限制连续次数-1；
                //如果不相同，不管前面连续多少次结果相同，将限制连续次数恢复初始值,并保存本次的新值，继续准备与下次比较
                if(finalpi==pi){
                    System.out.println("第"+(finalii+1-ii)+"次-----相同----------最终结果-------------finaoanoaof"+pitemp);
                    ii=ii-1;
                    if(ii==0){
                        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$最终所得的小数"+pitemp);
                        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$最终所得的前五位小数"+pi);
                        break;
                    }
                }else{
                    finalpi=pi;
                    ii=finalii;
                }
            }
        }
    }

    //格式化小数
    public static double subInt( double i) {
        String s = String.valueOf(i).substring(0, bytelimit + 2);//获取 小数点后5位
        String ss = String.valueOf(i).substring(bytelimit + 2, bytelimit + 3);//获取小数点第6位
        double dd = Double.parseDouble(s);//转化为小数点后保留5位的小数
        //如果第6位的值大于等于5，根据四舍五入，将转化后的小数加上0.00001
        if (ss.compareTo("5") >= 0) {
            dd = dd + 0.00001;
        }
        return dd;
    }
    }
