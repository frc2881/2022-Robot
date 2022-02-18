package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.Feeder_Direction;

public class IntakeFeeder extends CommandBase {
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    
    private Intake intake;
    private Feeder_Direction state;
    private double speed;

    public IntakeFeeder(Intake intake, double speed, Feeder_Direction state) {

        this.intake = intake;
        this.state = state;
        this.speed = speed;

        addRequirements(intake);
    }

    @Override
    public void execute() {

        intake.intake_feed(speed, state);
    }

    @Override
    public void end(boolean interrupted) {
        intake.intake_feed(0, Feeder_Direction.INTAKE);
    }

      @Override
    public boolean isFinished() {
        return false;
    }
}