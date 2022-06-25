// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.catapult.Score;
import frc.robot.commands.drive.DriveWithJoysticks;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.PrettyLights;
import frc.robot.subsystems.RightCatapult;

public class SimpleAutonomous extends SequentialCommandGroup {
  /** Creates a new SimpleAutonomous. */
  public SimpleAutonomous(Drive drive, Intake intake, LeftCatapult leftCatapult, RightCatapult rightCatapult, PrettyLights prettylights, XboxController controller) {
    addCommands(
      new WaitCommand(.1),
      new InstantCommand(() -> intake.extend(), intake),
      new WaitCommand(2),
      new Score(leftCatapult, rightCatapult, prettylights, null, null, null),
      new InstantCommand(() -> intake.retract(), intake),
      new DriveWithJoysticks(drive, () -> 0.5, () -> 0.0).withTimeout(2)
    );
  }
}
