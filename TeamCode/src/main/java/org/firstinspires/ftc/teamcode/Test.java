package org.firstinspires.ftc.teamcode;

public class Test {
    public static void main(String[] args) {
        Runnable task = () -> {
            System.out.println("This code is running in a separate thread using a lambda.");
        };
        Thread thread = new Thread(task);
        thread.start();
    }
}