package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.State;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.RightCatapult;

public class ScoreLeft extends SequentialCommandGroup {
  public ScoreLeft(Intake intake, RightCatapult rightCatapult, LeftCatapult leftCatapult) {
    
    addCommands(
        //new IntakeArm(intake, State.EXTEND).withTimeout(2),
        new WaitCommand(2),
        new ShootLeft(leftCatapult).withTimeout(2),
        new EjectRight(rightCatapult).withTimeout(2),
        new ResetLeft(leftCatapult).withTimeout(4),
        new ResetRight(rightCatapult).withTimeout(4));

    
  }
}
