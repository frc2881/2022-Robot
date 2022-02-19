package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.RightCatapult;

public class ScoreRight extends SequentialCommandGroup {
  public ScoreRight(RightCatapult rightCatapult) {
    
    addCommands(
        new ShootRight(rightCatapult).withTimeout(2),
        new ResetRight(rightCatapult).withTimeout(4)
        );
  }
}
