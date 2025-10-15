package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.concurrent.TimeUnit;

public class HuskyLens {

    private final int READ_PERIOD = 1;
    private com.qualcomm.hardware.dfrobot.HuskyLens huskyLens;
    //This sample rate limits the reads solely to allow a user time to observe
    // what is happening on the Driver Station telemetry.
    Deadline rateLimit = new Deadline(READ_PERIOD, TimeUnit.SECONDS);

    public void init(HardwareMap hwMap, Telemetry telemetry) {
        huskyLens = hwMap.get(com.qualcomm.hardware.dfrobot.HuskyLens.class, "huskylens");

        /*
         * Immediately expire so that the first time through we'll do the read.
         */
        rateLimit.expire();

        /*
         * Basic check to see if the device is alive and communicating.
         * the hardware map.
         */
        if (!huskyLens.knock()) {
            telemetry.addData(">>", "Problem communicating with " + huskyLens.getDeviceName());
        } else {
            telemetry.addData(">>", "Press start to continue");
        }

         // The device uses the concept of an algorithm to determine what types of
          //objects it will look for and/or what mode it is in.
         // Other algorithm choices for FTC might be: OBJECT_RECOGNITION, COLOR_RECOGNITION or OBJECT_CLASSIFICATION.
        huskyLens.selectAlgorithm(com.qualcomm.hardware.dfrobot.HuskyLens.Algorithm.TAG_RECOGNITION);

        telemetry.update();
    }

    public void getData(Telemetry telemetry){
        rateLimit.reset();

        /*
         * All algorithms, except for LINE_TRACKING, return a list of Blocks where a
         * Block represents the outline of a recognized object along with its ID number.
         * ID numbers allow you to identify what the device saw.
         * Returns an empty array if no objects are seen.
         */
        com.qualcomm.hardware.dfrobot.HuskyLens.Block[] blocks = huskyLens.blocks();
        telemetry.addData("Block count", blocks.length);
        for (int i = 0; i < blocks.length; i++) {
            telemetry.addData("Block", blocks[i].toString());
            /*
             * Here inside the FOR loop, you could save or evaluate specific info for the currently recognized Bounding Box:
             * - blocks[i].width and blocks[i].height   (size of box, in pixels)
             * - blocks[i].left and blocks[i].top       (edges of box)
             * - blocks[i].x and blocks[i].y            (center location)
             * - blocks[i].id                           (Color ID)
             */
        }
    }
}
