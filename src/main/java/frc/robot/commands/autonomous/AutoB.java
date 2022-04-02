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
import frc.robot.commands.catapult.ResetLeft;
import frc.robot.commands.catapult.ResetRight;
import frc.robot.commands.catapult.ShootLeft;
import frc.robot.commands.catapult.ShootRightConditional;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.feedback.WaitCommandNT;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.PrettyLights;
import frc.robot.subsystems.RightCatapult;
import frc.robot.utils.NavX;
import static frc.robot.Constants.Catapult.kShootTimeDelay;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoB extends SequentialCommandGroup {
  /** Creates a new LeftL. */
  public AutoB(
    Drive drive, 
    Intake intake, 
    NavX navx,
    LeftCatapult leftCatapult, 
    RightCatapult rightCatapult, 
    PrettyLights prettylights, 
    XboxController controller, 
    Trajectory autoB,
    Trajectory toStrategicCargo,
    Trajectory backUpStrategic
  ) {
    addCommands(
  new WaitCommandNT(Auto.kStartingDel),
      new InstantCommand(() -> intake.extend(), intake),
      new InstantCommand(() -> prettylights.lightShow(), prettylights),
      new WaitCommand(0.1),
      new InstantCommand(() -> intake.run(1.0), intake),
      new FollowTrajectory(drive, autoB, true),
      new WaitCommand(0.5),
      new InstantCommand(() -> intake.run(0), intake),
      new WaitCommand(0.50),
      new ShootLeft(leftCatapult),
      new WaitCommand(kShootTimeDelay),
      new ShootRightConditional(rightCatapult),
      parallel(
          new ResetLeft(leftCatapult),
          new ResetRight(rightCatapult),
          new InstantCommand(() -> intake.run(0.1), intake), 
          new FollowTrajectory(drive, toStrategicCargo, false)),
      new InstantCommand(() -> intake.runReverse(0.8), intake),
      new FollowTrajectory(drive, backUpStrategic, false)
    );
  }
}
