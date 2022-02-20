// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

  public static final class Drive {

    public static final double kRamseteB = 2.0;

    public static final double kRamseteZeta = 0.7;

    public static final double kS = 0.15734;

    public static final double kV = 2.0487;

    public static final double kA = 0.36811;

    public static final double kPDriveVel = 2.6446;

    public static final double kDDriveVel = 0;

    public static final double kTrackWidth = Units.inchesToMeters(22.5);

    public static final DifferentialDriveKinematics kKinematics = new DifferentialDriveKinematics(kTrackWidth);

    public static final int kCurrentLimit = 60;
  }
}
