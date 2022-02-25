// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot;

import java.util.function.DoubleSupplier;

import com.pathplanner.lib.PathPlanner;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.autonomous.Autonomous;
import frc.robot.commands.autonomous.SimpleAutonomous;
import frc.robot.commands.catapult.CatapultOverrride;
import frc.robot.commands.catapult.Eject;
import frc.robot.commands.catapult.Score;
import frc.robot.commands.climber.ClimberOverride;
import frc.robot.commands.climber.FirstClimberSequence;
import frc.robot.commands.climber.RunArm;
import frc.robot.commands.climber.SecondClimberSequence;
import frc.robot.commands.drive.DriveWithJoysticks;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.intake.ExtendIntake;
import frc.robot.commands.intake.RunIntake;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.Pneumatics;
import frc.robot.subsystems.RightCatapult;
import frc.robot.utils.Log;
import frc.robot.utils.NavX;

public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final XboxController driverController = new XboxController(0);
  private final XboxController manipulatorController = new XboxController(1);

  private final PowerDistribution powerHub = new PowerDistribution(2, ModuleType.kRev);
  private final Pneumatics pneumatics = new Pneumatics();

  private final NavX navx = new NavX();

  private final Climber climber = new Climber();
  private final Intake intake = new Intake();
  private final LeftCatapult leftCatapult = new LeftCatapult();
  private final RightCatapult rightCatapult = new RightCatapult();
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

  public boolean robotResetState = true;

  private final DriveWithJoysticks driveWithJoysticks = new DriveWithJoysticks(
    drive,
    () -> applyDeadband(-driverController.getLeftY()),
    () -> applyDeadband(driverController.getRightX())
    );

  private final RunArm runArm = new RunArm(
    climber,
    () -> applyDeadband(-manipulatorController.getLeftY())
    );

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
    m_chooser.setDefaultOption("Autonomous", new Autonomous(drive, intake, auto1part1, auto1part2));
    m_chooser.addOption("Simple Auto", new SimpleAutonomous(drive, intake, leftCatapult, rightCatapult));
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
    // This way we don't need to put logging in every command.
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
    // Driver Xbox Controller

    // Manipulator Xbox Controller

    new JoystickButton(manipulatorController, XboxController.Button.kX.value).
      whileHeld(new RunIntake(intake));

    new JoystickButton(manipulatorController, XboxController.Button.kY.value).
      whenPressed(new InstantCommand(() -> climber.armToggle()));

    new JoystickButton(manipulatorController, XboxController.Button.kA.value).
      whileHeld(new ExtendIntake(intake));

    new JoystickButton(manipulatorController, XboxController.Button.kLeftBumper.value).
      whenHeld(new FirstClimberSequence(climber, navx, manipulatorController));

    new JoystickButton(manipulatorController, XboxController.Button.kRightBumper.value).
      whenHeld(new SecondClimberSequence(climber, navx, manipulatorController));

    buttonFromDPad(manipulatorController).
      whenPressed(new Eject(leftCatapult, rightCatapult));

    buttonFromDouble(() -> manipulatorController.getRightTriggerAxis()).
      whenPressed(new Score(leftCatapult, rightCatapult));

    new JoystickButton(manipulatorController, XboxController.Button.kStart.value).
      whenHeld(new ClimberOverride(climber, () -> applyDeadband(-manipulatorController.getLeftY())));

    new JoystickButton(manipulatorController, XboxController.Button.kBack.value).
      whenHeld(new CatapultOverrride(leftCatapult, rightCatapult));
  }

  public void resetRobot() {
    if(robotResetState == true) {
      climber.reset();
      drive.reset();
      intake.reset();
      leftCatapult.reset();
      rightCatapult.reset();
      robotResetState = false;
    }
  }

  public void robotShouldReset() {
    robotResetState = true;
  }

  private Button buttonFromDouble(DoubleSupplier value) {
    return new Button() {
      @Override
      public boolean get() {
        return Math.abs(value.getAsDouble()) > 0.1;
      }
    };
  }

  private Button buttonFromDPad(XboxController controller) {
    return new Button() {
      @Override
      public boolean get() {
        if(controller.getPOV() != -1) {
          return true;
        } else {
          return false;
        }
      }
    };
  }

  public double applyDeadband(double input) {
    if(Math.abs(input) < 0.1) {
      return 0.0;
    } else {
      return input;
    }
  }

  public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }
}
