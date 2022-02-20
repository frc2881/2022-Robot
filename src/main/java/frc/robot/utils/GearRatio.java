package frc.robot.utils;

import edu.wpi.first.math.util.Units;

public class GearRatio
{
  public static double measureDist(double ticks, double tooth1, double tooth2, double diameter) {
    double dist = ((ticks * tooth1) / tooth2) * diameter * Math.PI;
    return Units.inchesToMeters(dist);
  }

  public static void main(String... args) {
    System.out.println(measureDist(1, 10, 50, 4));
  }
}
