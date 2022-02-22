// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.catapult;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.RightCatapult;

public class ScoreRight extends SequentialCommandGroup {
  public ScoreRight(RightCatapult rightCatapult) {
    addCommands(
        new ConditionalCommand(new ShootRight(rightCatapult).withTimeout(1).
                                 andThen(new ResetRight(rightCatapult).withTimeout(2)),
                               new WaitCommand(0.001), () -> Shoot(rightCatapult)));
  }

  public boolean Shoot(RightCatapult rightCatapult) {
    if(((DriverStation.getAlliance() == Alliance.Red) && (rightCatapult.isRed() == true)) ||
       ((DriverStation.getAlliance() == Alliance.Blue) && (rightCatapult.isBlue() == true))) {
      return true;
    } else {
      return false;
    }
  }
}
