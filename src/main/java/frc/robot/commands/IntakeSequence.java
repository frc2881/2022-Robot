package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Intake_Catapult;
import frc.robot.subsystems.Intake_Catapult.Feeder_Direction;

public class IntakeSequence extends SequentialCommandGroup {
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    
    public IntakeSequence(Intake_Catapult intake_catapult) {
        //bring intake down, start rollers, bring intake up, stop rollers
        //add periods of time in between different commands
        addCommands(
            //Sync the timing
            new ParallelCommandGroup(
                //intake game piece
                new IntakeFeeder(intake_catapult, 1, Feeder_Direction.INTAKE).withTimeout(5)
            ),
            //intake game piece
            new IntakeFeeder(intake_catapult, 1, Feeder_Direction.INTAKE),
            new IntakeFeeder(intake_catapult, 0, Feeder_Direction.INTAKE)
        );
    }
}