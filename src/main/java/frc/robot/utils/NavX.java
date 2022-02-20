package frc.robot.utils;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.SPI;

public class NavX extends AHRS {
  public NavX() {
    super(SPI.Port.kMXP);
  }

  @Override
  public float getYaw() {
    return -super.getYaw();
  }

  // Override the method for retrieving the angle from the NavX. What the NavX
  // considers to be a positive angle is the opposite of what WPILib considers
  // to be a positive angle, so this handles that difference.
  @Override
  public double getAngle() {
    return -super.getAngle();
  }

  @Override
  public float getRoll() {
    return -super.getRoll();
  }

  /**
   * Gets the velocity of the robot along its current direction of travel.
   *
   * @return the velocity of the robot.
   */
/*
  private double getVelocity() {
    // Get the X and Y velocity of the robot.
    double x = getVelocityX();
    double y = getVelocityY();

    // Compute and return the Euclidean velocity of the robot.
    return Math.sqrt((x * x) + (y * y));
  }
*/

  /**
   * Gets a rotation matrix representing the robot's current heading.
   *
   * @return the rotation matrix.
   */
  public Rotation2d getRotation2D() {
    return new Rotation2d(Math.toRadians(getYaw()));
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Yaw", this::getYaw, null);
    builder.addDoubleProperty("Pitch", this::getPitch, null);
    builder.addDoubleProperty("Roll", this::getRoll, null);
  }
}
