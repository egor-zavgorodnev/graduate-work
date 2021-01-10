package ru.tver.tstu;

public class ClassTest implements Runnable {
    private static int squ;
    private static int x;

    public ClassTest() {
    }

    public void run() {
        x = 1;
        System.out.println(x);

        while(x < 10) {
            square();
            ++x;
            System.out.println(x);
        }

    }

    public static void square() {
        squ = x * x;
        System.out.println(squ);
    }

}