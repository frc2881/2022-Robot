package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LeftCatapult;

public class EjectLeft extends CommandBase {
  private LeftCatapult catapult;

  public EjectLeft(LeftCatapult leftCatapult) {
    this.catapult = leftCatapult;
    addRequirements(leftCatapult);
  }

  @Override
  public void execute() {
        catapult.run(0.6);
  }

  @Override
  public void end(boolean interrupted) {
    catapult.run(0);     
  }

  @Override
  public boolean isFinished() {
    return catapult.reachedUpperSoftLimit();
  }
}
