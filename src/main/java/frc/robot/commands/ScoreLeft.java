package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.LeftCatapult;

public class ScoreLeft extends SequentialCommandGroup {
  public ScoreLeft(LeftCatapult leftCatapult) {
    
    addCommands(
        new ShootLeft(leftCatapult).withTimeout(2),
        new ResetLeft(leftCatapult).withTimeout(4)
        );
  }
}
