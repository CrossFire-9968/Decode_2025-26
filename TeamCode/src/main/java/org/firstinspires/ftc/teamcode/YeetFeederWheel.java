package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class YeetFeederWheel
{
   private DcMotor Motor_Feed;

   public void init(HardwareMap hwMap){
      // Initialize your servo in hardware map
      Motor_Feed = hwMap.get(DcMotor.class, "Motor_Feed");
      Motor_Feed.setDirection(DcMotorSimple.Direction.FORWARD);
      Motor_Feed.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
   }

   // Start feeder servo
   public void yeetStart()
   {
      double launchPower = 1.0;
      Motor_Feed.setPower(launchPower);
   }

   // Stop feeder servo
   public void stop()
   {
      double stopPower = 0.0;
      Motor_Feed.setPower(stopPower);
   }
}