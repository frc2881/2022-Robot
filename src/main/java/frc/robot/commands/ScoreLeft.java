package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.LeftCatapult;

public class ScoreLeft extends SequentialCommandGroup {

  public ScoreLeft(LeftCatapult leftCatapult) {
{
    addCommands(
        new ConditionalCommand(new ShootLeft(leftCatapult).withTimeout(1), new WaitCommand(0.0001), () -> Shoot(leftCatapult)),
        new ResetLeft(leftCatapult).withTimeout(2)
        );
    }
  }

public boolean Shoot(LeftCatapult leftCatapult){
  if (((DriverStation.getAlliance() == Alliance.Red) && (leftCatapult.isRed() == true)) 
  || ((DriverStation.getAlliance() == Alliance.Blue) && (leftCatapult.isBlue() == true))) {
    return true;
  } else {
    return false;
  }
}

}
