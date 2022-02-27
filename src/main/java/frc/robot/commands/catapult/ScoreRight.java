// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.catapult;

import static frc.robot.Constants.Catapult.kResetTimeout;
import static frc.robot.Constants.Catapult.kShootTimeout;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.feedback.RumbleYes;
import frc.robot.subsystems.RightCatapult;
import frc.robot.subsystems.PrettyLights;

public class ScoreRight extends SequentialCommandGroup {
  public ScoreRight(RightCatapult rightCatapult, XboxController manipulatorController, PrettyLights prettyLights) {
    Command score = sequence(new ShootRight(rightCatapult).
                                   withTimeout(kShootTimeout),
                             new ResetRight(rightCatapult).
                                   withTimeout(kResetTimeout));
    addCommands(new ConditionalCommand(score, new WaitCommand(0.001),
                                       () -> Shoot(rightCatapult)),
                new RumbleYes(prettyLights, null, manipulatorController));

                
  }

  public boolean Shoot(RightCatapult rightCatapult) {
    if(((DriverStation.getAlliance() == Alliance.Red) &&
        (rightCatapult.isRed() == true)) ||
       ((DriverStation.getAlliance() == Alliance.Blue) &&
        (rightCatapult.isBlue() == true))) {
      return true;
    } else {
      return false;
    }
  }
}
