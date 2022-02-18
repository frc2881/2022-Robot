package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.RightCatapult;
import frc.robot.subsystems.RightCatapult.Right_Catapult_Direction;

public class LaunchRightCatapult extends CommandBase {
  private RightCatapult catapult;
  private Right_Catapult_Direction direction;

  public LaunchRightCatapult(RightCatapult rightCatapult, Right_Catapult_Direction direction) {
    this.catapult = rightCatapult;
    this.direction = direction;        
    addRequirements(rightCatapult);
  }

  @Override
  public void execute() {
    if(direction == Right_Catapult_Direction.LAUNCH) {
      catapult.run(1);
    } else if(direction == Right_Catapult_Direction.RESET) {
      catapult.run(-.1);
    }
  }

  @Override
  public void end(boolean interrupted) {
    catapult.run(0);           
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
