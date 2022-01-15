// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Pneumatics;
import frc.robot.commands.DriveWithJoysticks;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.DriveWithJoysticks;
import frc.robot.subsystems.Drive;
import frc.robot.utils.Log;
import frc.robot.utils.NavX;

public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  XboxController driverController = new XboxController(0);
  XboxController manipulatorController = new XboxController(1);


  // private final Pneumatics pneumatics = new Pneumatics();
  private final NavX navx = new NavX();

  private final Drive drive = new Drive(navx);

  //Split Arcade drive 
  private final DriveWithJoysticks driveWithJoysticks = new DriveWithJoysticks(
    drive, 
    () -> -getDriverLeftY(),
    () -> getDriverRightX());

  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    drive.setDefaultCommand(driveWithJoysticks);

    // Use the scheduler to log the scheduling and execution of commands.
    // This way we don't need to put logging in every command
    CommandScheduler.getInstance().
      onCommandInitialize(command -> Log.init(command));
    CommandScheduler.getInstance().
      onCommandInterrupt(command -> Log.end(command, true));
    CommandScheduler.getInstance().
      onCommandFinish(command -> Log.end(command, false));

    //Smart Dashboard Commands 
    //SmartDashboard.putData("Path", new *());
  }

  private void configureButtonBindings() {
    /*new JoystickButton(driverController, Button.kBumperRight.value)
            .whenPressed(() -> drive.setMaxOutput(0.5))
            .whenReleased(() -> drive.setMaxOutput(1));*/
  }

  private double getDriverLeftX() {
    return(driverController.getRawAxis(0));
  }
//Used for: drive with Joysticks
private double getDriverLeftY() {
    return(driverController.getRawAxis(1));
  }
//Used for: drive with Joysticks
private double getDriverRightX() {
    return(driverController.getRawAxis(2));
  }

private double getDriverRightY() {
    return(driverController.getRawAxis(5));
  }

private double getDriverLeftTrigger() {
    return((driverController.getRawAxis(3) + 1) / 2);
  }

private double getDriverRightTrigger() {
    return((driverController.getRawAxis(4) + 1) / 2);
  }

private double getManipulatorLeftX() {
    return(manipulatorController.getRawAxis(0));
  }

private double getManipulatorLeftY() {
    return(manipulatorController.getRawAxis(1));
  }

private double getManipulatorRightX() {
    return(manipulatorController.getRawAxis(2));
  }

private double getManipulatorRightY() {
    return(manipulatorController.getRawAxis(5));
  }

private double getManipulatorLeftTrigger() {
    return((manipulatorController.getRawAxis(3) + 1) / 2);
  }

private double getManipulatorRightTrigger() {
    return((manipulatorController.getRawAxis(4) + 1) / 2);
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
