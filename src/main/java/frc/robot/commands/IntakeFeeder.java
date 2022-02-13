package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake_Catapult;
import frc.robot.subsystems.Intake_Catapult.Feeder_Direction;

public class IntakeFeeder extends CommandBase {
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    
    private Intake_Catapult intakeCatapult;
    private Feeder_Direction state;
    private double speed;

    public IntakeFeeder(Intake_Catapult intakeCatapult, double speed, Feeder_Direction state) {

        this.intakeCatapult = intakeCatapult;
        this.state = state;
        this.speed = speed;

        addRequirements(intakeCatapult);
    }

    @Override
    public void execute() {

        intakeCatapult.intake_feed(speed, state);
    }

    @Override
    public void end(boolean interrupted) {
        intakeCatapult.intake_feed(0, Feeder_Direction.INTAKE);
    }

      @Override
    public boolean isFinished() {
        return false;
    }
}