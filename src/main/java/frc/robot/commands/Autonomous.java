package frc.robot.commands;


import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.Feeder_Direction;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Autonomous extends SequentialCommandGroup {
  //private static final String Drive = null;


  /** Creates a new Autonomous. */
  public Autonomous(Drive drive, Intake intake, Trajectory auto1part1, Trajectory auto1part2) {

    addRequirements(drive);
    addRequirements(intake);

    addCommands(
      new WaitCommand(0),
      new InstantCommand(() -> intake.extend(), intake),
      new InstantCommand(() -> intake.intake_feed(1, Feeder_Direction.INTAKE), intake),
      new WaitCommand(2),   
      new FollowTrajectory(drive, auto1part1),
      new InstantCommand(() -> intake.intake_feed(0, Feeder_Direction.INTAKE), intake),
      new WaitCommand(0.5),
      new FollowTrajectory(drive, auto1part2),
      new WaitCommand(0.5)
      //TODO catapult here ;)
    ); 
  
  }
}
