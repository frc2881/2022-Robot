// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.PathPlanner;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.Autonomous;
import frc.robot.commands.DriveWithJoysticks;
import frc.robot.commands.FollowTrajectory;
import frc.robot.commands.RunArm;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake_Catapult;
import frc.robot.subsystems.Intake_Catapult.Direction;
import frc.robot.subsystems.Pneumatics;
import frc.robot.utils.Log;
import frc.robot.utils.NavX;

public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  PS4Controller driverController = new PS4Controller(0);
  XboxController manipulatorController = new XboxController(1);

  public double applyDeadband(double input){
    if(Math.abs(input) < 0.1){
        return 0.0;
    } else {
        return input;
    }
  }

  private final Climber climber = new Climber();

  private final Intake_Catapult intake_catapult = new Intake_Catapult();

  private final Pneumatics pneumatics = new Pneumatics();
  // private final Climber climber = new Climber();
  private final NavX navx = new NavX();

  private final Drive drive = new Drive(navx);

  private final Trajectory straight;
  private final Trajectory grabCargo;
  private final Trajectory spiral;
  private final Trajectory j;
  private final Trajectory m;
  private final Trajectory o;
  private final Trajectory s;
  private final Trajectory u;
  private final Trajectory v;
  private final Trajectory auto1part1;
  private final Trajectory auto1part2;

  private final SendableChooser<Command> m_chooser;
  
  //Arcade drive 
  PowerDistribution powerHub = new PowerDistribution(2, ModuleType.kRev);

  //Split Arcade drive 
  private final DriveWithJoysticks driveWithJoysticks = new DriveWithJoysticks(
    drive, 
    () -> applyDeadband(driverController.getLeftY()),
    () -> applyDeadband(driverController.getRightY()),
    () -> applyDeadband(-driverController.getRightX())
    );


  private final RunArm runArm = new RunArm(
    climber, 
    () -> applyDeadband(-manipulatorController.getLeftY())
    );

  //public final boolean competitionMode = new 

  public void PneumaticDisabledState() {  
    intake_catapult.retract();
    climber.armUp(); 
  }
 
  public RobotContainer() {

    double maxVelocity = 2;
    double maxAcceleration = 2;

    //Trajectories from Path Planner
    straight = PathPlanner.loadPath("Straight", maxVelocity, maxAcceleration);
    grabCargo = PathPlanner.loadPath("Grab cargo", maxVelocity, maxAcceleration);
    spiral = PathPlanner.loadPath("Spiral", maxVelocity, maxAcceleration);
    j = PathPlanner.loadPath("J", maxVelocity, maxAcceleration);
    m = PathPlanner.loadPath("M", maxVelocity, maxAcceleration);
    o = PathPlanner.loadPath("O", maxVelocity, maxAcceleration);
    s = PathPlanner.loadPath("S", maxVelocity, maxAcceleration);
    u = PathPlanner.loadPath("U", maxVelocity, maxAcceleration);
    v = PathPlanner.loadPath("V", maxVelocity, maxAcceleration);
    auto1part1 = PathPlanner.loadPath("Auto1Part1", maxVelocity, maxAcceleration);
    auto1part2 = PathPlanner.loadPath("Auto1Part2", maxVelocity, maxAcceleration, true);

    // A chooser for autonomous commands. This way we can choose between Paths for Autonomous Period.
    m_chooser = new SendableChooser<>();
    m_chooser.setDefaultOption("Autonomous", new Autonomous(drive, intake_catapult, auto1part1, auto1part2));
    m_chooser.addOption("Straight", new FollowTrajectory(drive, straight));
    m_chooser.addOption("Grab cargo", new FollowTrajectory(drive, grabCargo));
    m_chooser.addOption("Spiral", new FollowTrajectory(drive, spiral));
    m_chooser.addOption("J", new FollowTrajectory(drive, j));
    m_chooser.addOption("M", new FollowTrajectory(drive, m));
    m_chooser.addOption("O", new FollowTrajectory(drive, o));
    m_chooser.addOption("S", new FollowTrajectory(drive, s));
    m_chooser.addOption("U", new FollowTrajectory(drive, u));
    m_chooser.addOption("V", new FollowTrajectory(drive, v));
    m_chooser.addOption("Auto 1 1/2", new FollowTrajectory(drive, auto1part1));
    m_chooser.addOption("Auto 1 2/2", new FollowTrajectory(drive, auto1part2));

    // Configure the button bindings
    configureButtonBindings();

    drive.setDefaultCommand(driveWithJoysticks);
    climber.setDefaultCommand(runArm);

    // Use the scheduler to log the scheduling and execution of commands.
    // This way we don't need to put logging in every command
    CommandScheduler.getInstance().
      onCommandInitialize(command -> Log.init(command));
    CommandScheduler.getInstance().
      onCommandInterrupt(command -> Log.end(command, true));
    CommandScheduler.getInstance().
      onCommandFinish(command -> Log.end(command, false));

    //Smart Dashboard Commands 
    SmartDashboard.putData(m_chooser);
  }

  private void configureButtonBindings() {

    //DRIVER PS4 CONTROLLER

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
            .whileHeld(new ArmOut(climber));

      new JoystickButton(manipulatorController, XboxController.Button.kA.value)
            .whileHeld(new ArmIn(climber));
*/
    //new JoystickButton(driverController, PS4Controller.Button.kCircle.value).whenPressed(
      //new InstantCommand(() -> intake_catapult.intake(1, Direction.INTAKE), intake_catapult));

    //new JoystickButton(driverController, PS4Controller.Button.kCross.value).whenPressed(
      //new InstantCommand(() -> intake_catapult.intake(0, Direction.INTAKE), intake_catapult));

    //MANIPULATOR XBOX CONTROLLER

    new JoystickButton(manipulatorController, XboxController.Button.kB.value).whenPressed(
      new InstantCommand(() -> intake_catapult.intake(1, Direction.INTAKE), intake_catapult));

    new JoystickButton(manipulatorController, XboxController.Button.kA.value).whenPressed(
      new InstantCommand(() -> intake_catapult.intake(0, Direction.INTAKE), intake_catapult));

    new JoystickButton(manipulatorController, XboxController.Button.kX.value).whenPressed(
      new InstantCommand(() -> intake_catapult.extend(), intake_catapult));
    
    new JoystickButton(manipulatorController, XboxController.Button.kY.value).whenPressed(
      new InstantCommand(() -> intake_catapult.retract(), intake_catapult));

    new JoystickButton(manipulatorController, XboxController.Button.kLeftBumper.value).whenPressed(
      new InstantCommand(() -> climber.armBack(), climber));

    new JoystickButton(manipulatorController, XboxController.Button.kRightBumper.value).whenPressed(
      new InstantCommand(() -> climber.armUp(), climber));
  }

public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }
}
