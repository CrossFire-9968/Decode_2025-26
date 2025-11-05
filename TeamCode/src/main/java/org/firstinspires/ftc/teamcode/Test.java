package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

Test task = new Test();
Thread thread = new Thread(task);

thread.start();

public class Test implements Runnable{
    @Override
    public void run() {
        telemetry.addLine("RUNNING");


    }
}
