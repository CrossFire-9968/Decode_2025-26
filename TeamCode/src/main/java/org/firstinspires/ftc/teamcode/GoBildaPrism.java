package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Prism.Color;
import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations;

public class GoBildaPrism
{
   GoBildaPrismDriver prism;
   
   private static final int SOLID_BRIGHTNESS = 100;
   private static final int SOLID_START_INDEX = 0;
   private static final int SOLID_STOP_INDEX = 23;

   PrismAnimations.Solid solidRed = new PrismAnimations.Solid(Color.RED);
   PrismAnimations.Solid solidGreen = new PrismAnimations.Solid(Color.GREEN);
   PrismAnimations.Solid solidBlue = new PrismAnimations.Solid(Color.BLUE);
   PrismAnimations.Solid solidWhite = new PrismAnimations.Solid(Color.WHITE);
   PrismAnimations.RainbowSnakes rainbowSnakes = new PrismAnimations.RainbowSnakes();

   public enum PrismColor
   {
      RED,
      GREEN,
      BLUE,
      WHITE
   }

   private PrismColor prismColor = null;


   public void init(HardwareMap hwMap)
   {
      prism = hwMap.get(GoBildaPrismDriver.class, "prism");

      // Configure solid colors used by robot
      configureSolid(solidRed);
      configureSolid(solidGreen);
      configureSolid(solidBlue);
      configureSolid(solidWhite);
      
      rainbowSnakes.setNumberOfSnakes(2);
      rainbowSnakes.setSnakeLength(3);
      rainbowSnakes.setSpacingBetween(6);
      rainbowSnakes.setSpeed(0.5f);

      // Initialize the led strip to do something
      prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, rainbowSnakes);
   }


   private void configureSolid(PrismAnimations.Solid solid)
   {
      solid.setBrightness(SOLID_BRIGHTNESS);
      solid.setStartIndex(SOLID_START_INDEX);
      solid.setStopIndex(SOLID_STOP_INDEX);
   }


   public void setPrismColor(PrismColor nextColor)
   {
      if (nextColor == null || nextColor == prismColor) {
         return;
      }

      switch (nextColor) {
         case RED:
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solidRed);
            break;
         case BLUE:
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solidBlue);
            break;
         case GREEN:
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solidGreen);
            break;
         case WHITE:
            break;
      }

      prismColor = nextColor;
   }
}
