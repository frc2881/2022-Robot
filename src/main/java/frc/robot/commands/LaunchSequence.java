package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.State;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.RightCatapult;
import frc.robot.subsystems.RightCatapult.Right_Catapult_Direction;

public class LaunchSequence extends SequentialCommandGroup {
  public LaunchSequence(Intake intake, RightCatapult rightCatapult, LeftCatapult leftCatapult) {
    //bring intake down, start rollers, bring intake up, stop rollers
    //add periods of time in between different commands
    addCommands(
        new IntakeArm(intake, State.EXTEND),
        new WaitCommand(2),
        new LaunchRightCatapult(rightCatapult, Right_Catapult_Direction.LAUNCH),
        new LaunchRightCatapult(rightCatapult, Right_Catapult_Direction.RESET)
        /*
        new LaunchLeftCatapult(leftCatapult, Left_Catapult_Direction.LAUNCH),
        new LaunchLeftCatapult(leftCatapult, Left_Catapult_Direction.RESET),
        */
    );
  }
}
