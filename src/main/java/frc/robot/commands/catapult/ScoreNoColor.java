// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.catapult;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.feedback.RumbleYes;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.PrettyLights;
import frc.robot.subsystems.RightCatapult;
import frc.robot.subsystems.PrettyLights;

public class ScoreNoColor extends SequentialCommandGroup {
  public ScoreNoColor(LeftCatapult leftCatapult, RightCatapult rightCatapult, PrettyLights prettylights, XboxController manipulatorController) {
    addCommands(parallel(
      new ScoreLeftNoColor(leftCatapult, manipulatorController, prettylights),
      sequence(new WaitCommand(0.15), new ScoreRightNoColor(rightCatapult, manipulatorController, prettylights))
    ),
      new RumbleYes(prettylights, null, manipulatorController)
    );
  }
}
