// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.catapult;

import static frc.robot.Constants.Catapult.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.RightCatapult;

public class Eject extends SequentialCommandGroup {
  /** Creates a new Eject. */
  public Eject(LeftCatapult leftCatapult, RightCatapult rightCatapult) {
    Command ejectLeft = sequence(new EjectLeft(leftCatapult).
                                       withTimeout(kEjectTimeout),
                                 new ResetLeft(leftCatapult).
                                       withTimeout(kResetTimeout));
    Command ejectRight = sequence(new EjectRight(rightCatapult).
                                        withTimeout(kEjectTimeout),
                                  new ResetRight(rightCatapult).
                                        withTimeout(kResetTimeout));
    addCommands(new ConditionalCommand(ejectRight,
                                       new ConditionalCommand(ejectLeft,
                                                              new WaitCommand(0.001),
                                                              () -> EjectLeftCargo(leftCatapult)),
                                       () -> EjectRightCargo(rightCatapult)));
  }

  public boolean EjectLeftCargo(LeftCatapult leftCatapult) {
    if(((DriverStation.getAlliance() == Alliance.Red) &&
        (leftCatapult.isBlue() == true)) ||
       ((DriverStation.getAlliance() == Alliance.Blue) &&
        (leftCatapult.isRed() == true))) {
      return true;
    } else {
      return false;
    }
  }

  public boolean EjectRightCargo(RightCatapult rightCatapult) {
    if(((DriverStation.getAlliance() == Alliance.Red) &&
        (rightCatapult.isBlue() == true)) ||
       ((DriverStation.getAlliance() == Alliance.Blue) &&
        (rightCatapult.isRed() == true))) {
      return true;
    } else {
      return false;
    }
  }
}
