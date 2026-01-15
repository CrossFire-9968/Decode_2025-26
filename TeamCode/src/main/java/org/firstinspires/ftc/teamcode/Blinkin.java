

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Blinkin {
    public RevBlinkinLedDriver blinkin;


    public void init(HardwareMap hwMap) {
        blinkin = hwMap.get(RevBlinkinLedDriver.class, "blinkin");
        blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.AQUA);
    }

    public void setColor(RevBlinkinLedDriver.BlinkinPattern color) {
        blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.VIOLET);

    }

    public void setPattern(RevBlinkinLedDriver.BlinkinPattern blinkinPattern) {
        blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.ORANGE);
    }


    // Purple manuel and orange last 20 seconds
    // targeting color by camera will be green
}