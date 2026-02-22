package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Prism.Color;
import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations;

public class PrisimColor
{
    GoBildaPrismDriver prism;

    private static final int SOLID_BRIGHTNESS = 50;
    private static final int SOLID_START_INDEX = 0;
    private static final int SOLID_STOP_INDEX = 23;

    PrismAnimations.Solid solidRed = new PrismAnimations.Solid(Color.RED);
    PrismAnimations.Solid solidGreen = new PrismAnimations.Solid(Color.GREEN);
    PrismAnimations.Solid solidBlue = new PrismAnimations.Solid(Color.BLUE);
    PrismAnimations.Solid solidWhite = new PrismAnimations.Solid(Color.WHITE);
    PrismAnimations.Solid solidCyan = new PrismAnimations.Solid(Color.CYAN);
    PrismAnimations.Solid solidPurple = new PrismAnimations.Solid(Color.PURPLE);
    PrismAnimations.Solid solidOrange = new PrismAnimations.Solid(Color.ORANGE);
    PrismAnimations.RainbowSnakes rainbowSnakes = new PrismAnimations.RainbowSnakes();

    public enum PrismColor
    {
        RED,
        GREEN,
        BLUE,
        WHITE,
        CYAN,
        PURPLE,
        ORANGE


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
        configureSolid(solidCyan);
        configureSolid(solidPurple);
        configureSolid(solidOrange);



        rainbowSnakes.setNumberOfSnakes(2);
        rainbowSnakes.setSnakeLength(3);
        rainbowSnakes.setSpacingBetween(6);
        rainbowSnakes.setSpeed(0.5f);

        // Initialize the led strip to do something
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0,solidWhite);
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
            case CYAN:
                prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solidCyan);
                break;
            case PURPLE:
                prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solidPurple);
                break;
            case ORANGE:
                prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solidOrange);
                break;
            case WHITE:
                prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solidWhite);
                break;
        }

        prismColor = nextColor;
    }
}