package com.mkyong.concurrency.synchronizer.semaphore;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

class Foo {

    private final Semaphore sem;

    public Foo() {
        this.sem = new Semaphore(1);
    }

    public void first(Runnable r) {

        try {
            sem.acquire();
            System.out.print("first");
            Thread.sleep(200);
            Shared.count++;
        } catch (InterruptedException e) {
        }
        sem.release();
    }

    public void second(Runnable r) {
        try {
            while (Shared.count != 1) {
                Thread.sleep(200);
            }
            sem.acquire();
            System.out.print("second");
            Shared.count++;
        } catch (InterruptedException e) {
        }
        sem.release();
    }

    public void third(Runnable r) {
        try {
            while (Shared.count != 2) {
                Thread.sleep(200);
            }
            sem.acquire();
            System.out.print("third");
        } catch (InterruptedException e) {
        }
        sem.release();

    }

}

class MySem{

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Foo f = new Foo();

        Thread a = new Thread();
        Thread b = new Thread();
        Thread c = new Thread();


        executor.execute(() -> {
            f.third(c);
        });

        executor.execute(() -> {
            f.second(b);
        });

        executor.execute(() -> {
            f.first(a);
        });

        executor.shutdown();
    }
}

class Shared{
    static  int count = 0;
}
