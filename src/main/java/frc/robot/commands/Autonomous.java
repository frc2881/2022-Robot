package frc.robot.commands;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.Direction;

public class Autonomous extends SequentialCommandGroup {
  /** Creates a new Autonomous. */
  public Autonomous(Drive drive, Intake intake, Trajectory auto1part1, Trajectory auto1part2) {
    addCommands(
      new WaitCommand(0),
      new InstantCommand(() -> intake.extend(), intake),
      new InstantCommand(() -> intake.run(1, Direction.INTAKE), intake),
      new WaitCommand(2),
      new FollowTrajectory(drive, auto1part1),
      new InstantCommand(() -> intake.run(0, Direction.INTAKE), intake),
      new WaitCommand(0.5),
      new FollowTrajectory(drive, auto1part2),
      new WaitCommand(0.5)
      //TODO catapult here ;)
    );
  }
}
