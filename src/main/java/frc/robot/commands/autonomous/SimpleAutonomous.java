// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.catapult.Score;
import frc.robot.commands.drive.DriveWithJoysticks;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.RightCatapult;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SimpleAutonomous extends SequentialCommandGroup {
  /** Creates a new SimpleAutonomous. */
  public SimpleAutonomous(Drive drive, Intake intake, LeftCatapult leftCatapult, RightCatapult rightCatapult) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new WaitCommand(.1),
      new InstantCommand(() -> intake.extend(), intake),
      new WaitCommand(2),
      new Score(leftCatapult, rightCatapult),
      new InstantCommand(() -> intake.retract(), intake),
      new DriveWithJoysticks(drive, () -> returnOne() , () -> returnZero()).withTimeout(2)
    );
  }

  private double returnOne(){
    return .5;
  }

  private double returnZero(){
    return 0.0;
  }
}
