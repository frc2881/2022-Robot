package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.LeftCatapult.Left_Catapult_Direction;

public class LaunchLeftCatapult extends CommandBase {
  private LeftCatapult catapult;
  private Left_Catapult_Direction direction;

  public LaunchLeftCatapult(LeftCatapult leftCatapult, Left_Catapult_Direction direction) {
    this.catapult = leftCatapult;
    this.direction = direction;
    addRequirements(leftCatapult);
  }

  @Override
  public void execute() {
    // Launches the catapult or brings it back down
    if(direction == Left_Catapult_Direction.LAUNCH) {
        catapult.run(1);
    } else if(direction == Left_Catapult_Direction.RESET) {
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
