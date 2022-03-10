// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.catapult;

import static frc.robot.Constants.Catapult.kEjectTimeout;
import static frc.robot.Constants.Catapult.kResetTimeout;

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
    Command ejectOppositeLeft = sequence(new EjectLeft(leftCatapult).
                                               withTimeout(kEjectTimeout),
                                         new ResetLeft(leftCatapult).
                                               withTimeout(kResetTimeout));
    Command ejectOppositeRight = sequence(new EjectRight(rightCatapult).
                                                withTimeout(kEjectTimeout),
                                          new ResetRight(rightCatapult).
                                                withTimeout(kResetTimeout));
    Command ejectOurLeft = sequence(new EjectLeft(leftCatapult).
                                          withTimeout(kEjectTimeout),
                                    new ResetLeft(leftCatapult).
                                          withTimeout(kResetTimeout));
    Command ejectOurRight = sequence(new EjectRight(rightCatapult).
                                           withTimeout(kEjectTimeout),
                                     new ResetRight(rightCatapult).
                                           withTimeout(kResetTimeout));

    addCommands(new ConditionalCommand(ejectOppositeRight,
                  new ConditionalCommand(ejectOppositeLeft,
                    new ConditionalCommand(ejectOurRight,
                      new ConditionalCommand(ejectOurLeft, new WaitCommand(0.001),
                      () -> EjectLeftOurCargo(leftCatapult)),
                    () -> EjectRightOurCargo(rightCatapult)),
                  () -> EjectLeftOppositeCargo(leftCatapult)),
                () -> EjectRightOppositeCargo(rightCatapult)));
  }

  public boolean EjectLeftOppositeCargo(LeftCatapult leftCatapult) {
    if(((DriverStation.getAlliance() == Alliance.Red) &&
        (leftCatapult.isBlue() == true)) ||
       ((DriverStation.getAlliance() == Alliance.Blue) &&
        (leftCatapult.isRed() == true))) {
      return true;
    } else {
      return false;
    }
  }

  public boolean EjectRightOppositeCargo(RightCatapult rightCatapult) {
    if(((DriverStation.getAlliance() == Alliance.Red) &&
        (rightCatapult.isBlue() == true)) ||
       ((DriverStation.getAlliance() == Alliance.Blue) &&
        (rightCatapult.isRed() == true))) {
      return true;
    } else {
      return false;
    }
  }

  public boolean EjectLeftOurCargo(LeftCatapult leftCatapult) {
    if(((DriverStation.getAlliance() == Alliance.Red) &&
        (leftCatapult.isRed() == true)) ||
       ((DriverStation.getAlliance() == Alliance.Blue) &&
        (leftCatapult.isBlue() == true))) {
      return true;
    } else {
      return false;
    }
  }

  public boolean EjectRightOurCargo(RightCatapult rightCatapult) {
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
