// Copyright (c) 2021 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.utils;

import static java.util.stream.Collectors.joining;

import java.util.stream.Stream;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * This class provides methods for logging messages to the RioLog as the robot
 * starts, the robot mode changes, and as commands start and end.
 */
public final class Log {
  /**
   * The time that the robot code started.
   */
  private static final long startTime = System.currentTimeMillis();

  /**
   * Formats a message and writes it to the RioLog.
   *
   * @param message contains the text of the message to be logged.
   */
  public static void log(String message) {
    long time = System.currentTimeMillis() - startTime;
    System.out.printf("[%6.2f] %s\n", time / 1000.0, message);
  }

  /**
   * Logs the start of the robot code.
   */
  public static void start() {
    log("*".repeat(20) + " Robot Start, version " +
        Log.class.getPackage().getImplementationVersion() + " " +
        "*".repeat(20));
  }

  /**
   * Logs a change in the robot mode.
   *
   * @param mode is the new robot mode.
   */
  public static void mode(String mode) {
    log(">".repeat(10) + " Robot mode: " + mode + " " + "<".repeat(10));
  }

  /**
   * Logs the start of a command.
   *
   * @param command is the command class that is starting.
   */
  public static void init(Command command) {
    log("--> Start command: " + command.getClass().getSimpleName());
  }

  /**
   * Logs the start of a command.
   *
   * @param command is the command class that is starting.
   *
   * @param settings are the parameters used when starting the command.
   */
  public static void init(Command command, Object... settings) {
    log("--> Start command: " + command.getClass().getSimpleName() + " (" +
        Stream.of(settings).map(Object::toString).collect(joining(", ")) +
        ")");
  }

  /**
   * Logs the end of a command.
   *
   * @param command is the command class that is ending.
   *
   * @param interrupted is <b>true</b> if the command was interrupted.
   */
  public static void end(Command command, boolean interrupted) {
    String name = command.getClass().getSimpleName();
    if(interrupted) {
      log("--> Interrupted command: " + name);
    } else {
      log("--> End command: " + name);
    }
  }
}
