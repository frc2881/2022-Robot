// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.feedback.RumbleYes;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.PrettyLights;
import frc.robot.utils.NavX;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AimAtHub extends SequentialCommandGroup {
  /** Creates a new Rotate. */
  public AimAtHub(NavX navx, Drive drive, DoubleSupplier turn, PrettyLights prettylights, XboxController driverController, XboxController manipulatorController) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new RotateByDegrees(navx, drive, turn),
      new RumbleYes(prettylights, driverController, manipulatorController)
    );
  }
}
