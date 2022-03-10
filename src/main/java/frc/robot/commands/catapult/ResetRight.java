// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.catapult;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.RightCatapult;

public class ResetRight extends CommandBase {
  private RightCatapult catapult;

  public ResetRight(RightCatapult catapult) {
    this.catapult = catapult;
    addRequirements(catapult);
  }

  @Override
  public void execute() {
    catapult.down();
  }

  @Override
  public void end(boolean interrupted) {
    catapult.stop();
  }

  @Override
  public boolean isFinished() {
    return catapult.reachedLowerSoftLimit();
  }
}
