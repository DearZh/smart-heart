package com.smart.heart.java;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2020/12/7
 */
public class AutoClose {
    static class A implements AutoCloseable {
        public void out() {
            System.out.println("A out");
        }

        @Override
        public void close() throws Exception {
            System.out.println("A Close");
        }
    }

    static class B implements AutoCloseable {
        public void out() {
            System.out.println("B out");
        }

        @Override
        public void close() throws Exception {
            System.out.println("B close");
        }
    }

    public static void main(String[] args) throws Exception {
        //A 先执行Close，还是B先执行Close
        B b = new B();
        try (A a = new A()) {
            a.out();
            b.out();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            b.close();
        }
        //结果：A先执行Close，B再执行Close；
    }
}
