package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LeftCatapult;

public class ShootLeft extends CommandBase {
  private LeftCatapult catapult;

  public ShootLeft(LeftCatapult catapult) {
    this.catapult = catapult;
    addRequirements(catapult);
  }

  @Override
  public void execute() {
    catapult.run(1.0);
  }

  @Override
  public void end(boolean interrupted) {
    catapult.run(0.0);
  }

  @Override
  public boolean isFinished() {
    return catapult.reachedUpperSoftLimit();
  }
}
