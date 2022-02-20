package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LeftCatapult;

public class ResetLeft extends CommandBase {
  private LeftCatapult catapult;

  public ResetLeft(LeftCatapult catapult) {
    this.catapult = catapult;
    addRequirements(catapult);
  }

  @Override
  public void execute() {
    catapult.run(-0.1);
  }

  @Override
  public void end(boolean interrupted) {
    catapult.run(0);
  }

  @Override
  public boolean isFinished() {
    return catapult.reachedLowerSoftLimit();
  }
}
