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
import frc.robot.commands.catapult.Score;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.feedback.WaitCommandNT;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.PrettyLights;
import frc.robot.subsystems.RightCatapult;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class RightM extends SequentialCommandGroup {
  /** Creates a new RightL. */
  public RightM(
      Drive drive, 
      Intake intake, 
      LeftCatapult leftCatapult, 
      RightCatapult rightCatapult, 
      PrettyLights prettylights, 
      XboxController controller, 
      Trajectory rightMtoCargo2,
      Trajectory cargo2toHubR,
      Trajectory rightMtoCargo3,
      Trajectory cargo3toHubR
) {
    addCommands(
  new WaitCommandNT(Auto.kStartingDel),
      new InstantCommand(() -> intake.extend(), intake),
      //new WaitCommand(1),
      //new Score(leftCatapult, rightCatapult, prettylights, null),
      new InstantCommand(() -> intake.run(1.0), intake),
      new FollowTrajectory(drive, rightMtoCargo2),
  new WaitCommandNT(Auto.kSecondDel),
      new FollowTrajectory(drive, cargo2toHubR),
      new InstantCommand(() -> intake.run(0), intake),
      new WaitCommand(0.25),
      new Score(leftCatapult, rightCatapult, prettylights, null),
  new WaitCommandNT(Auto.kThirdDel),
      new InstantCommand(() -> intake.run(1.0), intake), 
      new FollowTrajectory(drive, rightMtoCargo3),
  new WaitCommandNT(Auto.kFourthDel),
      new FollowTrajectory(drive, cargo3toHubR),
      new InstantCommand(() -> intake.run(0), intake),
      new WaitCommand(0.25),
      new Score(leftCatapult, rightCatapult, prettylights, null),
  new WaitCommandNT(Auto.kFifthDel),
      new FollowTrajectory(drive, rightMtoCargo2)
    );
  }
}
