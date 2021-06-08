package com.twc.guanlang.common.annotation;

import lombok.Data;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;


/**
 * wait notify test
 * <p>
 * the memory of one object used by threads
 * before call the methods  like  wait,nofity ,notifyAll of  the object
 * the  thread shoud monopolize the object's monitor lock,
 * the meaning of monopolize here  is not exact,because  each object has only one monitor lock which will be gotted
 * by the threads     one by one originally
 */
@Data
public class WaitNotify {


    private static int k;
    private static int count;
    private String lock1 = "lock1";

    private String lock2 = "locak2";

    public static void main(String s[]) throws InterruptedException {

        WaitNotify annoTest = new WaitNotify();

        WaitNotify annoTest2 = new WaitNotify();
        List list = new ArrayList();


        while (k < 9) {
            k++;
            if (k == 9) {
                System.out.println(Thread.currentThread().getName() + " k==9  times:" + System.currentTimeMillis());
            }


            new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    synchronized (annoTest) {
                        try {
                            list.add(Thread.currentThread());
                            System.out.println(Thread.currentThread().getName() + " list====size==:" + list.size());
                            annoTest.setLock1(System.currentTimeMillis() + "");
                            annoTest.wait(1000000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }


        Thread.sleep(3000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (annoTest) {
                    try {

                        annoTest.notifyAll();
                        System.out.println(Thread.currentThread().getName() + "  notifyAll=========time===:" + System.currentTimeMillis());
                        System.out.println(Thread.currentThread().getName() + "  notifyAll=========getLock1===:" + annoTest2.getLock1());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }


}
