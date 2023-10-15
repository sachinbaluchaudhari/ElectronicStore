package com.electonic.store.testing;

import org.junit.jupiter.api.*;

public class CalculationTest {
    @BeforeAll
    public static void init()
    {
        System.out.println("Before executing all method");
    }
    @AfterAll
    public static void cleanup()
    {
        System.out.println("After executing all method");
    }
    @BeforeEach
    public void beforeEach()
    {
        System.out.println("Before each");
    }
    @AfterEach
    public void AfterEach()
    {
        System.out.println("After each");
    }
    @Test
    public void addTest()
    {
        int actualResult=Calculation.add(12,12);
        int expectedResult=24;
        Assertions.assertEquals(expectedResult,actualResult,"test fail!!!");
        System.out.println("adding two number");
    }
    @Test
    public void anyNumberSumTest()
    {
        int actualResult = Calculation.anyNumberSum(5, 6, 9);
        int expectedResult=20;
        Assertions.assertEquals(expectedResult,actualResult );
        System.out.println("adding any number");
    }
    @Test
    public void subTest()
    {
        int actualResult=Calculation.sub(12,12);
        int expectedResult=0;
        Assertions.assertEquals(expectedResult,actualResult,"test fail!!!");
        System.out.println("subtracting two number");
    }

}
