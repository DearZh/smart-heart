package com.junit.test.parameter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.List;

/**
 * @author Arnold.zhao
 * @version ParameterTest.java, v 0.1 2021-12-01 16:55 Arnold.zhao Exp $$
 */
@RunWith(Parameterized.class)
public class ParameterTest {
    //https://blog.csdn.net/u011541946/article/details/95009227

    private int input;
    private int expected;


    public ParameterTest(int input, int expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {5, 5},
                {5, 10},
                {-12, 0},
                {50, 50},
                {1, 51}
        });
    }

    @Test
    public void test() {
        System.out.println("input=" + input + " expected =" + expected);
       /* if (input > 0) {
            System.out.println("input >0");
        } else {
            System.out.println("input <=0");
        }*/

//        Assert.assertEquals(expected,);
    }

}
