// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.utils;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.utils.PathPlanner.PathPlanner2;

/**
 * This class provides methods for managing trajectories.
 */
public class Trajectories {
  //
  // The names of the files that contain the trajectories.
  //
  public static final String autoB = "AutoB";
  public static final String straight = "Straight";
  public static final String rightL = "RightL";
  public static final String toStrategicCargo = "ToStrategicCargo";
  public static final String backupStrategic = "BackUpStrategic";
  public static final String rightR = "RightR";
  public static final String toTerminal = "ToTerminal";
  public static final String terminalToScore = "TerminalToScore";
  public static final String cargo2ToTerminal = "Cargo2ToTerminal";
  public static final String backupTerminal = "BackUpTerminal";
  public static final String backupTerminalToScore = "BackUpTerminalToScore";

  //
  // A map of file names to the loaded trajectories.
  //
  private static Map<String, Trajectory> m_trajectories =
    new HashMap<String, Trajectory>();

  //
  // The default maximum velocity for loaded trajectories.
  //
  private static final double m_maxVelocity = 3;

  //
  // The default maximum acceleration for loaded trajectories.
  //
  private static final double m_maxAcceleration = 2;

  /**
   * Loads the trajectories. Should be called at robot startup time.
   */
  public static void load() {
    //Left Tarmac
    load(autoB, m_maxVelocity, m_maxAcceleration);
    load(straight, m_maxVelocity, m_maxAcceleration);
    load(rightL, m_maxVelocity, m_maxAcceleration);
    load(toStrategicCargo, m_maxVelocity, m_maxAcceleration, true);
    load(backupStrategic, m_maxVelocity, m_maxAcceleration, true);

    //Right Tarmac
    load(rightR, m_maxVelocity, m_maxAcceleration);
    load(toTerminal, 4, m_maxAcceleration);
    load(terminalToScore, 4, m_maxAcceleration, true);
    load(cargo2ToTerminal, m_maxVelocity, m_maxAcceleration);
    load(backupTerminal, m_maxVelocity, m_maxAcceleration, true);
    load(backupTerminalToScore, m_maxVelocity, m_maxAcceleration, true);
  }

  /**
   * Loads a trajectory.
   *
   * @param name is the name of the file that contains the trajectory.
   *
   * @param velocity is the maximum velocity while following the trajectory.
   *
   * @param acceleration is the maximum acceleration while following the
   *                     trajectory.
   *
   * @param reversed is <b>true</b> if the robot should go backwards while
   *                 following the trajectory.
   */
  private static void load(String name, double velocity, double acceleration,
                           boolean reversed) {
    m_trajectories.put(name, PathPlanner2.loadPath(name, velocity,
                                                   acceleration, reversed));
  }

  /**
   * Loads a trajectory.
   *
   * @param name is the name of the file that contains the trajectory.
   *
   * @param velocity is the maximum velocity while following the trajectory.
   *
   * @param acceleration is the maximum acceleration while following the
   *                     trajectory.
   */
  private static void load(String name, double velocity, double acceleration) {
    load(name, velocity, acceleration, false);
  }

  /**
   * Gets a trajectory.
   *
   * @param name is the name of the file containing the trajectory.
   *
   * @return the {@link Trajectory} loaded from the given file.
   */
  public static Trajectory get(String name) {
    return m_trajectories.get(name);
  }
}
