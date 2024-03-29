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
import frc.robot.commands.catapult.ShootLeft;
import frc.robot.commands.catapult.ShootRightConditional;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.feedback.WaitCommandNT;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.PrettyLights;
import frc.robot.subsystems.RightCatapult;
import frc.robot.utils.NavX;
import frc.robot.utils.Trajectories;
import frc.robot.commands.catapult.CatapultOverrride;

public class AutoB extends SequentialCommandGroup {
  /** Creates a new LeftL. */
  public AutoB(Drive drive, Intake intake, NavX navx,
               LeftCatapult leftCatapult, RightCatapult rightCatapult,
               PrettyLights prettylights, XboxController controller) {
  addCommands(new WaitCommandNT(Auto.kStartingDel),
              new CatapultOverrride(leftCatapult, rightCatapult).withTimeout(0.25),
              new InstantCommand(() -> intake.extend(), intake),
              new WaitCommand(0.1),
              new InstantCommand(() -> intake.run(1.0), intake),
              new FollowTrajectory(drive, Trajectories.get(Trajectories.autoB), true),
              new WaitCommand(0.5),
              new InstantCommand(() -> intake.run(0), intake),
              new WaitCommand(0.50),
              new ShootLeft(leftCatapult),
              new WaitCommand(kShootTimeDelay),
              new ShootRightConditional(rightCatapult),
              parallel(new ResetLeft(leftCatapult),
                       new ResetRight(rightCatapult),
                       new InstantCommand(() -> intake.run(0.1), intake),
                       new FollowTrajectory(drive, Trajectories.get(Trajectories.toStrategicCargo), false)),
              new InstantCommand(() -> intake.runReverse(0.8), intake),
              new FollowTrajectory(drive, Trajectories.get(Trajectories.backupStrategic), false)
    );
  }
}
