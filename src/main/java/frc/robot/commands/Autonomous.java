package frc.robot.commands;

import com.pathplanner.lib.PathPlanner;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake_Catapult.Direction;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Autonomous extends SequentialCommandGroup {
  //private static final String Drive = null;
  private static final String Auto1Part1 = null;
  private static final String Auto1Part2 = null;
  private Drive m_drive;

  /** Creates a new Autonomous. */
  public Autonomous(Drive drive) {
    double maxVelocity = 2;
    double maxAcceleration = 2;
    //double timer = Timer;
    m_drive = drive;
    addRequirements(m_drive);
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ParallelCommandGroup(
        new FollowTrajectory( m_drive, PathPlanner.loadPath("Auto1Part1", maxVelocity, maxAcceleration)),
        new Intake(intake_catapult, 1, Direction.INTAKE),  
      )
      //if { timer = 5 }
      //new Intake(intake_catapult, 0, Direction.INTAKE),
      new FollowTrajectory( m_drive, PathPlanner.loadPath("Auto1Part2", maxVelocity, maxAcceleration))
      //TODO capatult here ;)
    );  
  
  }
}