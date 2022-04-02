// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autonomous;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.Auto;
import frc.robot.commands.catapult.ScoreLeft;
import frc.robot.commands.catapult.ScoreNoColor;
import frc.robot.commands.catapult.ScoreRightNoColor;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.feedback.WaitCommandNT;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.PrettyLights;
import frc.robot.subsystems.RightCatapult;
import frc.robot.utils.NavX;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class RightR extends SequentialCommandGroup {
  /** Creates a new RightR. */
  public RightR(
    Drive drive, 
    Intake intake, 
    NavX navx,
    LeftCatapult leftCatapult, 
    RightCatapult rightCatapult, 
    PrettyLights prettyLights, 
    XboxController driverController, 
    Trajectory rightPath,
    Trajectory toTerminal,
    Trajectory terminalToScore) 
        {
    addCommands(  
new WaitCommandNT(Auto.kStartingDel),
    new InstantCommand(() -> intake.extend(), intake),
    new WaitCommand(1),
    new ScoreRightNoColor(rightCatapult),
new WaitCommandNT(Auto.kSecondDel),
    new InstantCommand(() -> intake.run(1.0), intake),
    new FollowTrajectory(drive, rightPath, true),
    new InstantCommand(() -> intake.run(0.0), intake),
    new InstantCommand(() -> intake.retract(), intake),
    new WaitCommand(0.5),
    new InstantCommand(() -> intake.extend(), intake),
    new WaitCommand(1.2),
    new ScoreNoColor(leftCatapult, rightCatapult, prettyLights, null, null),
new WaitCommandNT(Auto.kThirdDel),
    //new WaitCommand(0.1),
    new InstantCommand(() -> intake.run(1.0), intake),
    new FollowTrajectory(drive, toTerminal, false),
    new WaitCommand(1),
    new FollowTrajectory(drive, terminalToScore, false),
    new InstantCommand(() -> intake.run(0.0), intake),
    //new RotateByDegrees(navx, drive, () -> visionTracking.getYaw())
    new WaitCommand(0.25),
    new ScoreNoColor(leftCatapult, rightCatapult, prettyLights, null, null)
);
  }
}
