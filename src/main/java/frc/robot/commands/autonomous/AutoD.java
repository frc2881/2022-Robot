// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.autonomous;

import static frc.robot.Constants.Catapult.kShootTimeDelay;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.Auto;
import frc.robot.commands.catapult.ResetLeft;
import frc.robot.commands.catapult.ResetRight;
import frc.robot.commands.catapult.Score;
import frc.robot.commands.catapult.ShootLeft;
import frc.robot.commands.catapult.ShootRightConditional;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.feedback.WaitCommandNT;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.PrettyLights;
import frc.robot.subsystems.RightCatapult;
import frc.robot.subsystems.VisionTracking;
import frc.robot.utils.NavX;
import frc.robot.utils.Trajectories;

public class AutoD extends SequentialCommandGroup {
  /** Creates a new RightL. */
  public AutoD(Drive drive, Intake intake, NavX navx,
               LeftCatapult leftCatapult, RightCatapult rightCatapult,
               PrettyLights prettylights, XboxController driverController,
               VisionTracking vision) {
    addCommands(new WaitCommandNT(Auto.kStartingDel),
                new InstantCommand(() -> intake.extend(), intake),
                new InstantCommand(() -> intake.run(1.0), intake),
                new FollowTrajectory(drive, Trajectories.get(Trajectories.rightL), true),
                new WaitCommand(0.25),
                new InstantCommand(() -> intake.run(0.0), intake),
                new WaitCommand(0.25),
                new ShootLeft(leftCatapult),
                new WaitCommand(kShootTimeDelay),
                new ShootRightConditional(rightCatapult),
                parallel(new ResetLeft(leftCatapult),
                         new ResetRight(rightCatapult),
                         new InstantCommand(() -> intake.run(1.0), intake),
                         new FollowTrajectory(drive, Trajectories.get(Trajectories.cargo2ToTerminal), false)),
                new WaitCommand(0.4),
                new FollowTrajectory(drive, Trajectories.get(Trajectories.terminalToScore), false),
                new InstantCommand(() -> intake.run(0.0), intake),
                //new WaitCommand(0.1),
                new Score(leftCatapult, rightCatapult, prettylights, null, null, vision));
  }
}
