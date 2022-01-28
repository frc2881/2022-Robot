package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Intake_Catapult;
import frc.robot.subsystems.Intake_Catapult.Direction;

public class IntakeSequence extends SequentialCommandGroup {
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    
    public IntakeSequence(Intake_Catapult intake_catapult) {
        //bring intake down, start rollers, bring intake up, stop rollers
        //add periods of time in between different commands
        addCommands(
            //Sync the timing
            new ParallelCommandGroup(
                //intake game piece
                new Intake(intake_catapult, 1, Direction.INTAKE).withTimeout(5)
            ),
            //intake game piece
            new Intake(intake_catapult, 1, Direction.INTAKE),
            new Intake(intake_catapult, 0, Direction.INTAKE)
        );
    }
}