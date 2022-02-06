package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Intake_Catapult;

public class LaunchSequence extends SequentialCommandGroup {
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    
    public LaunchSequence(Intake_Catapult intakeCatapult) {
        //bring intake down, start rollers, bring intake up, stop rollers
        //add periods of time in between different commands
        addCommands(
            /*
            new IntakeArm(intakeCatapult, Intake_Arm_Direction.EXTEND),
            new Catapult(intakeCatapult, Catapult_Direction.LAUNCH, Catapult_Direction.LAUNCH)
            */
            /*
            //Example code
            new ParallelCommandGroup(
                //intake game piece
                new IntakeFeeder(intakeCatapult, 1, Direction.INTAKE).withTimeout(5)
            )*/
        );
    }
}