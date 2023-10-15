package com.electonic.store.testing;

public class Calculation {
    public static int product(int a ,int b)
    {
        return a*b;
    }
    public static int sub(int a,int b)
    {
        return a-b;
    }
    public static int division(int a,int b)
    {
        return a/b;
    }
    public static int anyNumberSum(int ...numbers)
    {
        int sum=0;
        for (int number:numbers
             ) {
          sum+=number;
        }
        return sum;


    }

    public static int add(int a, int b) {
        return a+b;
    }
}
