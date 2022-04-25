package com.smart.heart.classloader.re02;

import com.smart.heart.classloader.inface.re02.Phone;

/**
 * @author Arnold.zhao
 * @version IPhone.java, v 0.1 2022-04-25 11:05 Arnold.zhao Exp $$
 */
public class IPhone implements Phone {
    @Override
    public void call() {
        System.out.println("IPhone call in..");
    }
}
