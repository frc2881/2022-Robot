// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.utils.GearRatio;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants.  This class should not be used for any other
 * purpose.  All constants should be declared globally (i.e. public static).
 * Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner
 * classes) wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
  /**
   * Configuration of the catapult subsystems.
   */
  public static final class Catapult {
    /**
     * The CAN ID of the left catapult motor.
     */
    public static final int kLeftMotor = 16;

    /**
     * The CAN ID of the right catapult motor.
     */
    public static final int kRightMotor = 17;

    /**
     * The color of the red cargo, as detected by the REV Color Sensor V3.
     */
    public static final Color kRedCargo = new Color(0.5720, 0.3222, 0.1062);

    /**
     * The color of the blue cargo, as detected by the REV Color Sensor V3.
     */
    public static final Color kBlueCargo = new Color(0.1436, 0.4070, 0.4499);

    /**
     * The minimum distance to the cargo in order to consider it to be present,
     * as detected by the REV Color Sensor V3.
     */
    public static final int kDistance = 200;

    /**
     * The maximum current to send to the catapult motors.
     */
    public static final int kCurrentLimit = 200;
    //public static final int kCurrentLimit = 80;

    /**
     * The position (in rotations) that the catapult gets reset to after the
     * override sequence.
     */
    public static final double kResetPosition = -0.3;

    /**
     * The maximum distance (in rotations) that the left catapult motor can
     * move in the forward direction.  Adjusting this limit adjusts the launch
     * angle of the shot.
     */
    public static final double kForwardLimitLeft = 3;

    /**
     * The maximum distance (in rotations) that the right catapult motor can
     * move in the forward direction.  Adjusting this limit adjusts the launch
     * angle of the shot.
     */
    public static final double kForwardLimitRight = 3;

    /**
     * The minimum distance (in rotations) that the catapult motor can move in
     * the reverse direction.
     */
    public static final double kReverseLimit = 0.0;

    /**
     * The maximum amount of time to wait for the catapult to shoot a cargo.
     */
    public static final double kShootTimeout = 1.0;

    public static final double kShootVoltage = 11;

    /**
     * The maximum amount of time to wait for the catapult to eject a cargo.
     */
    public static final double kEjectTimeout = 0.25;

    public static final double kEjectVoltage = 5.5;

    /**
     * The maximum amount of time to wait for the catapult to reset.
     */
    public static final double kResetTimeout = 2.0;

    public static final double kResetVoltage = -1.0;

  
    public static final double kShootTimeDelay = 0.5;


    public static final double kLeftLowDist = 10.767;

    public static final double kLeftHighDist = 16.433;

    public static final double kLeftHighLim =  8.25;

    public static final double kRightLowDist = 6.483;

    public static final double kRightHighDist = 19.25;

    public static final double kRightHighLim =  8.00;

  
  }

  /**
   * Configuration of the climber subsystem.
   */
  public static final class Climber {
    /**
     * The CAN ID of the climber motor.
     */
    public static final int kMotor = 18;

    /**
     * The pneumatic hub channel ID of the climber solenoid.
     */
    public static final int kSolenoid = 1;

    /**
     * The maximum rate to ramp the climber motor from full stop to full speed.
     */
    public static final double kRampRate = 0.08;

    /**
     * The gear ratio for the climber that converts motor rotations into inches
     * of travel of the climber hook.
     */
    public static final double kRotationsToInches = (1.0 / 2.0) / 3.0;

    /**
     * The maximum distance (in inches) that the climber arm can move in the
     * forward direction.
     */
    public static final double kForwardLimit = 26.0;

    /**
     * The minimum distance (in inches) that the climber arm can move in the
     * reverse direction.
     */
    public static final double kReverseLimit = 0.0;

    /**
     * The match time at which the climber is allowed to start operating.  Note
     * that when practicing in teleoperated mode, the climber is always
     * operational.
     */
    public static final double kMatchSafety = 45.0;
  }

  /**
   * Configuration of the drive subsystem.
   */
  public static final class Drive {
    /**
     * The CAN ID of the left front motor.
     */
    public static final int kLeftFrontMotor = 11;

    /**
     * The CAN ID of the left rear motor.
     */
    public static final int kLeftRearMotor = 12;

    /**
     * The CAN ID of the right front motor.
     */
    public static final int kRightFrontMotor = 13;

    /**
     * The CAN ID of the right rear motorl.
     */
    public static final int kRightRearMotor = 14;

    /**
     * The maximum current to send to the drive motors.
     */
    public static final int kCurrentLimit = 60;

    /**
     * The maximum rate to ramp the motors from full stop to full speed.
     */
    public static final double kRampRate = 0.08;

    /**
     * The gear ratio that converts motor rotations into meters of travel
     * across the floor.
     */
    public static final double kRotationsToMeters =
      GearRatio.measureDist(1, 10, 50, 4);

    /**
     * The maximum amount of time that can pass between updates of the motors
     * before the motor safety kicks in and stops the motors (helping to
     * prevent a runaway robot).
     */
    public static final double kMotorSafetyTime = 0.1;

    /**
     * The Ramsete B parameter.
     */
    public static final double kRamseteB = 2.0;

    /**
     * The Ramsete Zeta parameter.
     */
    public static final double kRamseteZeta = 0.7;

    /**
     * The drive train's coefficient of static friction, corresponding to the
     * voltage required to get the drive train to just start moving.
     */
    public static final double kS = 0.16069;
        //0.16069; Competition
        //0.15734; practice bot

    /**
     * The drive train's velocity coefficient, corresponding to the voltage
     * required to maintain a speed of 1 m/s.
     */
    public static final double kV = 2.0217;
        //2.0217; competition
        //2.0487; practice bot

    /**
     * The drive train's acceleration coefficient, corresponding to the voltage
     * required to accelerate by 1 m/s^2.
     */
    public static final double kA = 0.58189;
        //0.58189; Competition
        //0.36811; practice bot

    /**
     * The P coefficient for the drive train's feedback PID controller.
     */
    public static final double kPDriveVel = 0.5;
          //0.5; Competition
          //2.6446 * 1.45; Practice bot

    /**
     * The D coefficient for the drive train's feedback PID controller.
     */
    public static final double kDDriveVel = 0;

    /**
     * The width of the drive train, in meters.  This is an empirical width,
     * and due to possible wheel slip this may not match the physical width
     * of the drive train (it may be larger).
     */
    public static final double kTrackWidth = Units.inchesToMeters(22.5 * 2.25);

    /**
     * The kinematics helper object that converts chassis velocities into wheel
     * velocities.
     */
    public static final DifferentialDriveKinematics kKinematics =
      new DifferentialDriveKinematics(kTrackWidth);
  }

  /**
   * Configuration of the intake subsystem.
   */
  public static final class Intake {
    /**
     * The CAN ID of the intake motor.
     */
    public static final int kMotor = 15;

    /**
     * The pneumatic hub channel ID of the intake solenoid.
     */
    public static final int kSolenoid = 0;

    /**
     * The maximum current to send to the intake motor.
     */
    public static final int kCurrentLimit = 30;

    /**
     * The maximum speed that the intake motor runs.
     */
    public static final double kMaxSpeed = 0.55;
  }

  /**
   * Configuration of the autonomous sequences.
   */
  public static final class Auto {
    /**
     * The SmartDashboard name for the first delay within the autonomous
     * sequences.
     */
    public static final String kStartingDel = "Starting Delay";

    /**
     * The SmartDashboard name for the second delay within the autonomous
     * sequences.
     */
    public static final String kSecondDel = "Second Delay";

    /**
     * The SmartDashboard name for the third delay within the autonomous
     * sequences.
     */
    public static final String kThirdDel = "Third Delay";

    /**
     * The SmartDashboard name for the fourth delay within the autonomous
     * sequences.
     */
    public static final String kFourthDel = "Fourth Delay";

    /**
     * The SmartDashboard name for the fifth delay within the autonomous
     * sequences.
     */
    public static final String kFifthDel = "Fifth Delay";
  }

  /**
   * Controls the logging of detailed information about the robot.
   */
  public static final boolean kEnableDetailedLogging = false;
}
