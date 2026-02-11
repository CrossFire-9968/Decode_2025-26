package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Prism.Color;
import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations;

public class GoBildaPrism {

   GoBildaPrismDriver prism;

   PrismAnimations.Solid solidRed = new PrismAnimations.Solid(Color.RED);
   PrismAnimations.Solid solidGreen = new PrismAnimations.Solid(Color.GREEN);
   PrismAnimations.Solid solidBlue = new PrismAnimations.Solid(Color.BLUE);
   PrismAnimations.RainbowSnakes rainbowSnakes = new PrismAnimations.RainbowSnakes();

   public void init(HardwareMap hwMap)
   {
      prism = hwMap.get(GoBildaPrismDriver.class, "prism");

      // Customize prism animations
      solidRed.setBrightness(100);
      solidRed.setStartIndex(0);
      solidRed.setStopIndex(23);

      solidGreen.setBrightness(100);
      solidGreen.setStartIndex(0);
      solidGreen.setStopIndex(23);

      solidBlue.setBrightness(100);
      solidBlue.setStartIndex(0);
      solidBlue.setStopIndex(23);

      rainbowSnakes.setNumberOfSnakes(2);
      rainbowSnakes.setSnakeLength(3);
      rainbowSnakes.setSpacingBetween(6);
      rainbowSnakes.setSpeed(0.5f);

      // Initialize the light to do something awesome
      this.snake();
   }

   public void red()
   {
      prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solidRed);
   }

   public void green()
   {
      prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solidGreen);
   }

   public void blue()
   {
      prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solidBlue);
   }

   public void snake()
   {
      prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0,rainbowSnakes);
   }

}
