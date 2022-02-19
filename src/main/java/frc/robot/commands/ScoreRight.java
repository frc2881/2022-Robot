package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.State;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.RightCatapult;

public class ScoreRight extends SequentialCommandGroup {
  public ScoreRight(Intake intake, RightCatapult rightCatapult, LeftCatapult leftCatapult) {
    
    addCommands(
        //new IntakeArm(intake, State.EXTEND).withTimeout(2),
        new WaitCommand(2),
        new ShootRight(rightCatapult).withTimeout(2),
        new EjectLeft(leftCatapult).withTimeout(2),
        new ResetRight(rightCatapult).withTimeout(4),
        new ResetLeft(leftCatapult).withTimeout(4)
        );
  }
}
