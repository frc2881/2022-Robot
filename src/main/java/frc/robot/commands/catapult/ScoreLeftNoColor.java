// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.catapult;

import static frc.robot.Constants.Catapult.kResetTimeout;
import static frc.robot.Constants.Catapult.kShootTimeout;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.feedback.RumbleYes;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.PrettyLights;

public class ScoreLeftNoColor extends SequentialCommandGroup {
  public ScoreLeftNoColor(LeftCatapult leftCatapult, XboxController manipulatorController, PrettyLights prettyLights) {
    addCommands(new ShootLeft(leftCatapult).withTimeout(kShootTimeout),
                             new ResetLeft(leftCatapult).
                                   withTimeout(kResetTimeout),
                new RumbleYes(prettyLights, null, manipulatorController));
  }
}