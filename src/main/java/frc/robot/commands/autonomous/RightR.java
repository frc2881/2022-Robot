// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autonomous;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.catapult.Score;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.PrettyLights;
import frc.robot.subsystems.RightCatapult;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class RightR extends SequentialCommandGroup {
  /** Creates a new RightR. */
  public RightR(
    Drive drive, 
    Intake intake, 
    LeftCatapult leftCatapult, 
    RightCatapult rightCatapult, 
    PrettyLights prettylights, 
    XboxController controller, 
    Trajectory cargo2toHubM,
    Trajectory rightMtoCargo3,
    Trajectory cargo3toHubR,
    Trajectory rightRtoCargo2,
    Trajectory rightMtoCargo2
  ) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(  
new WaitCommand(0),
    new InstantCommand(() -> intake.extend(), intake),
    new InstantCommand(() -> intake.run(1.0), intake),
    new FollowTrajectory(drive, rightRtoCargo2),
    new InstantCommand(() -> intake.run(0), intake),
new WaitCommand(0),
    new FollowTrajectory(drive, cargo2toHubM),
    new Score(leftCatapult, rightCatapult, prettylights, null),
new WaitCommand(0),
    new InstantCommand(() -> intake.run(1.0), intake), 
    new FollowTrajectory(drive, rightMtoCargo3),
    new InstantCommand(() -> intake.run(0), intake),
new WaitCommand(0),
    new FollowTrajectory(drive, cargo3toHubR),
    new Score(leftCatapult, rightCatapult, prettylights, null),
new WaitCommand(0),
    new FollowTrajectory(drive, rightMtoCargo2));
  }
}
