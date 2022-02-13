package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Intake_Catapult;
import frc.robot.subsystems.Intake_Catapult.Catapult_Direction;
import frc.robot.subsystems.Intake_Catapult.Intake_Arm_Direction;

public class LaunchSequence extends SequentialCommandGroup {
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    public LaunchSequence(Intake_Catapult intake_catapult) {
        //bring intake down, start rollers, bring intake up, stop rollers
        //add periods of time in between different commands
        addCommands(

            new IntakeArm(intake_catapult, Intake_Arm_Direction.EXTEND),
            new WaitCommand(2),
            new LaunchCatapult(intake_catapult, Catapult_Direction.LAUNCH, Catapult_Direction.LAUNCH)
            /*
            //Example code
            new ParallelCommandGroup(
                //intake game piece
                new Intake_Feeder(intakeCatapult, 1, Direction.INTAKE).withTimeout(5)
            )*/
        );
    }
} 