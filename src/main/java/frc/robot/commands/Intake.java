package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake_Catapult;
import frc.robot.subsystems.Intake_Catapult.Direction;

public class Intake extends CommandBase {
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    
    private Intake_Catapult intake_catapult;
    private Direction state;
    private double speed;

    public Intake(Intake_Catapult intake_catapult, double speed, Direction state) {

        this.intake_catapult = intake_catapult;
        this.state = state;
        this.speed = speed;

        addRequirements(intake_catapult);
    }

    @Override
    public void execute() {

        intake_catapult.intake(speed, state);
    }

    @Override
    public void end(boolean interrupted) {
        intake_catapult.intake(0, Direction.INTAKE);
    }

      @Override
    public boolean isFinished() {
        return false;
    }
}