package frc.robot.commands;

import java.util.function.DoubleSupplier;
import frc.robot.subsystems.Drive;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveWithJoysticks extends CommandBase {
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

    private final DoubleSupplier forward;
    private final DoubleSupplier rotation;

    private Drive drive;
    
    public DriveWithJoysticks(Drive drive, DoubleSupplier forward, DoubleSupplier rotation) {
        this.forward = forward;
        this.rotation = rotation;

        this.drive = drive;

        addRequirements(drive);
    }

    @Override
    public void execute() {

        drive.arcadeDrive(forward.getAsDouble(), rotation.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        drive.arcadeDrive(0.0, 0.0);
    }

      @Override
    public boolean isFinished() {
        return false;
    }
}