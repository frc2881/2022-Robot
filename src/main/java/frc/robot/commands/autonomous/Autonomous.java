// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.autonomous;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;

public class Autonomous extends SequentialCommandGroup {
  /** Creates a new Autonomous. */
  public Autonomous(Drive drive, Intake intake, Trajectory auto1part1, Trajectory auto1part2) {
    addCommands(
      new WaitCommand(0),
      new InstantCommand(() -> intake.extend(), intake),
      new InstantCommand(() -> intake.run(1.0), intake),
      new WaitCommand(2),
      new FollowTrajectory(drive, auto1part1),
      new InstantCommand(() -> intake.run(0.0), intake),
      new WaitCommand(0.5),
      new FollowTrajectory(drive, auto1part2),
      new WaitCommand(0.5)
      //TODO catapult here ;)
    );
  }
}