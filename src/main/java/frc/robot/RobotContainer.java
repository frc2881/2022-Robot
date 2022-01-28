// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.PathPlanner;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.DriveWithJoysticks;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake_Catapult;
import frc.robot.subsystems.Intake_Catapult.Direction;
import frc.robot.subsystems.Pneumatics;
import frc.robot.utils.Log;
import frc.robot.utils.NavX;

public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  XboxController driverController = new XboxController(0);
  XboxController manipulatorController = new XboxController(1);

  private final Intake_Catapult intake_catapult = new Intake_Catapult();

   //private final Pneumatics pneumatics = new Pneumatics();
  // private final Climber climber = new Climber();
  private final NavX navx = new NavX();

  private final Drive drive = new Drive(navx);

  private final Trajectory trajectory1;


  
  //Arcade drive 
  PowerDistribution powerHub = new PowerDistribution(2, ModuleType.kRev);

  //Split Arcade drive 
  private final DriveWithJoysticks driveWithJoysticks = new DriveWithJoysticks(
    drive, 
    () -> getDriverLeftY(),
    () -> getDriverRightY(),
    () -> -getDriverRightX());
/*
  private final RunArm runArm = new RunArm(
    climber, 
    () -> getManipulatorLeftY());
*/
  
  public RobotContainer() {

    // Auton Paths
    // private final OffTarmac driveOffTarmac = new FollowTrajectory(1, Straight);
    // A chooser for autonomous commands
    // This way we can choose between Paths for Autonomous Period
    SendableChooser<Command> m_chooser = new SendableChooser<>();

    // Configure the button bindings

    trajectory1 = PathPlanner.loadPath("Straight", 1, 1);
    
    configureButtonBindings();

    drive.setDefaultCommand(driveWithJoysticks);
    // climber.setDefaultCommand(runArm);

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

     /*new JoystickButton(driverController, Button.kA.value)
            .whenHeld(new FollowTrajectory(drive, trajectory1));

     new JoystickButton(driverController, Button.kB.value)
            .whenPressed(() -> drive.driveTankVolts(3, 3))
            .whenReleased(() -> drive.driveTankVolts(0, 0));*/
    
/*
      new JoystickButton(manipulatorController, XboxController.Button.kY.value)
            .whenPressed(new InstantCommand(climber::armUp, climber));

      new JoystickButton(manipulatorController, XboxController.Button.kA.value)
            .whenPressed(new InstantCommand(climber::armBack, climber));

*/
    new JoystickButton(manipulatorController, Button.kB.value).whenPressed(
      new InstantCommand(() -> intake_catapult.intake(-.75, Direction.INTAKE), intake_catapult));

    new JoystickButton(manipulatorController, Button.kA.value).whenPressed(
      new InstantCommand(() -> intake_catapult.intake(0, Direction.INTAKE), intake_catapult));

    new JoystickButton(manipulatorController, Button.kX.value).whenPressed(
      new InstantCommand(() -> intake_catapult.extend(), intake_catapult));
    
    new JoystickButton(manipulatorController, Button.kY.value).whenPressed(
      new InstantCommand(() -> intake_catapult.retract(), intake_catapult));
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
    return(driverController.getRawAxis(4));
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
