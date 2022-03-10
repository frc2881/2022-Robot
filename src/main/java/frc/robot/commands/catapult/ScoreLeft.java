// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.catapult;

import static frc.robot.Constants.Catapult.kResetTimeout;
import static frc.robot.Constants.Catapult.kShootTimeout;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.LeftCatapult;

public class ScoreLeft extends SequentialCommandGroup {
  public ScoreLeft(LeftCatapult leftCatapult) {
    Command score = sequence(new ShootLeft(leftCatapult).
                                   withTimeout(kShootTimeout),
                             new ResetLeft(leftCatapult).
                                   withTimeout(kResetTimeout));
    addCommands(new ConditionalCommand(score, new WaitCommand(0.001),
                                       () -> Shoot(leftCatapult)));
  }

  public boolean Shoot(LeftCatapult leftCatapult) {
    if(((DriverStation.getAlliance() == Alliance.Red) &&
        (leftCatapult.isRed() == true)) ||
       ((DriverStation.getAlliance() == Alliance.Blue) &&
        (leftCatapult.isBlue() == true))) {
      return true;
    } else {
      return false;
    }
  }
}
