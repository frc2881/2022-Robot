package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.RightCatapult;

public class ShootRight extends CommandBase {
  private RightCatapult catapult;

  public ShootRight(RightCatapult rightCatapult) {
    this.catapult = rightCatapult; 
    addRequirements(rightCatapult);
  }

  @Override
  public void execute() {
      catapult.run(1);
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