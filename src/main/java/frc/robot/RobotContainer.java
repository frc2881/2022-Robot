// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot;

import java.util.function.BooleanSupplier;
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
import frc.robot.commands.autonomous.LeftL;
import frc.robot.commands.autonomous.LeftM;
import frc.robot.commands.autonomous.LeftR;
import frc.robot.commands.autonomous.RightL;
import frc.robot.commands.autonomous.RightM;
import frc.robot.commands.autonomous.RightR;
import frc.robot.commands.autonomous.SimpleAutonomous;
import frc.robot.commands.catapult.CatapultOverrride;
import frc.robot.commands.catapult.Eject;
import frc.robot.commands.catapult.Score;
import frc.robot.commands.climber.ClimberOverride;
import frc.robot.commands.climber.FirstClimberSequence;
import frc.robot.commands.climber.RunArm;
import frc.robot.commands.climber.SecondClimberSequence;
import frc.robot.commands.drive.CameraSwitch;
import frc.robot.commands.drive.DriveWithJoysticks;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.feedback.RumbleNo;
import frc.robot.commands.feedback.RumbleYes;
import frc.robot.commands.intake.ExtendIntake;
import frc.robot.commands.intake.RunIntake;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.Pneumatics;
import frc.robot.subsystems.PrettyLights;
import frc.robot.subsystems.RightCatapult;
import frc.robot.utils.Log;
import frc.robot.utils.NavX;
import frc.robot.utils.PathPlanner.PathPlanner2;

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
  private final PrettyLights prettylights = new PrettyLights();

  //Autonomous Cargo to Hub Pathways
  private final Trajectory cargo1toHubL;
  private final Trajectory cargo2toHubR;
  private final Trajectory cargo3toHubR;

  //Left Tarmac
  private final Trajectory leftLtoCargo1;
  private final Trajectory leftMtoCargo1;
  private final Trajectory leftMtoCargo2;
  private final Trajectory leftRtoCargo1;
  private final Trajectory leftRtoCargo2;

  //Right Tarmac
  private final Trajectory rightLtoCargo2;
  private final Trajectory rightMtoCargo2;
  private final Trajectory rightMtoCargo3;
  private final Trajectory rightRtoCargo3;
  

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
    
    auto1part1 = PathPlanner.loadPath("Auto1Part1", maxVelocity, maxAcceleration);
    auto1part2 = PathPlanner.loadPath("Auto1Part2", maxVelocity, maxAcceleration, true);

    //Cargo to Hub
    cargo1toHubL = PathPlanner2.loadPath("Cargo1toHubL", maxVelocity, maxAcceleration, true);
    cargo2toHubR = PathPlanner2.loadPath("Cargo2toHubR", maxVelocity, maxAcceleration, true);
    cargo3toHubR = PathPlanner2.loadPath("Cargo3toHubR", maxVelocity, maxAcceleration, true);

    //Left Tarmac
    leftLtoCargo1 = PathPlanner2.loadPath("LeftLtoCargo1", maxVelocity, maxAcceleration);
    leftMtoCargo1 = PathPlanner2.loadPath("LeftLtoCargo1", maxVelocity, maxAcceleration);
    leftMtoCargo2 =PathPlanner2.loadPath("LeftMtoCargo2", maxVelocity, maxAcceleration);
    leftRtoCargo1 = PathPlanner2.loadPath("LeftLtoCargo1", maxVelocity, maxAcceleration);
    leftRtoCargo2 = PathPlanner2.loadPath("LeftLtoCargo1", maxVelocity, maxAcceleration);

    //Right Tarmac
    rightLtoCargo2 = PathPlanner2.loadPath("RightLtoCargo2", maxVelocity, maxAcceleration);
    rightMtoCargo2 = PathPlanner2.loadPath("RightMtoCargo2", maxVelocity, maxAcceleration);
    rightMtoCargo3 = PathPlanner2.loadPath("RightMtoCargo3", maxVelocity, maxAcceleration);
    rightRtoCargo3 = PathPlanner2.loadPath("RightRtoCargo3", maxVelocity, maxAcceleration);
    

    // A chooser for autonomous commands. This way we can choose between Paths for Autonomous Period.
    m_chooser = new SendableChooser<>();
    m_chooser.setDefaultOption("Auto Right L", new RightL(drive, intake, leftCatapult, rightCatapult, prettylights, driverController, rightLtoCargo2, cargo2toHubR, rightMtoCargo3, cargo3toHubR, rightMtoCargo2));
    m_chooser.addOption("Auto Right M", new RightM(drive, intake, leftCatapult, rightCatapult, prettylights, driverController, rightLtoCargo2, cargo2toHubR, rightMtoCargo3, cargo3toHubR, rightMtoCargo2));
    m_chooser.addOption("Auto Right R", new RightR(drive, intake, leftCatapult, rightCatapult, prettylights, driverController, leftLtoCargo1, cargo1toHubL, leftMtoCargo2, cargo2toHubR, rightMtoCargo2));
    m_chooser.addOption("Auto Left L", new LeftL(drive, intake, leftCatapult, rightCatapult, prettylights, driverController, leftLtoCargo1, cargo1toHubL, leftMtoCargo2, cargo2toHubR, rightMtoCargo2));
    m_chooser.addOption("Auto Left M", new LeftM(drive, intake, leftCatapult, rightCatapult, prettylights, driverController, leftLtoCargo1, cargo1toHubL, leftMtoCargo2, cargo2toHubR, rightMtoCargo2));
    m_chooser.addOption("Auto Left R", new LeftR(drive, intake, leftCatapult, rightCatapult, prettylights, driverController, leftLtoCargo1, cargo1toHubL, leftMtoCargo2, cargo2toHubR, rightMtoCargo2));
    m_chooser.addOption("Simple Auto", new SimpleAutonomous(drive, intake, leftCatapult, rightCatapult, prettylights, driverController));
    m_chooser.addOption("Auto 1 1/2", new FollowTrajectory(drive, auto1part1));
    m_chooser.addOption("Auto 1 2/2", new FollowTrajectory(drive, auto1part2));

    SmartDashboard.putNumber("Starting Delay", 0);
    SmartDashboard.putNumber("Second Delay", 0);
    SmartDashboard.putNumber("Third Delay", 0);
    SmartDashboard.putNumber("Fourth Delay", 0);
    SmartDashboard.putNumber("Fifth Delay", 0);



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
    new JoystickButton(driverController, XboxController.Button.kB.value).whenHeld(
      new RumbleYes(prettylights, driverController, manipulatorController));

    buttonFromDouble(() -> driverController.getLeftTriggerAxis()+driverController.getRightTriggerAxis()).
      whileHeld(new CameraSwitch());

    new JoystickButton(driverController, XboxController.Button.kA.value).whenHeld(
      new RumbleNo(prettylights, driverController, manipulatorController));
    // Manipulator Xbox Controller

    new JoystickButton(manipulatorController, XboxController.Button.kX.value).
      whileHeld(new RunIntake(intake));

    new JoystickButton(manipulatorController, XboxController.Button.kY.value).
      whenPressed(new InstantCommand(() -> climber.armToggle()));

    new JoystickButton(manipulatorController, XboxController.Button.kA.value).
      whileHeld(new ExtendIntake(intake));

    new JoystickButton(manipulatorController, XboxController.Button.kLeftBumper.value).
      whenHeld(new FirstClimberSequence(climber, prettylights, navx, manipulatorController));

    new JoystickButton(manipulatorController, XboxController.Button.kRightBumper.value).
      whenHeld(new SecondClimberSequence(climber, prettylights, navx, manipulatorController));

    buttonFromDPad(manipulatorController).
      whenPressed(new Eject(leftCatapult, rightCatapult));

    buttonFromDouble(() -> manipulatorController.getRightTriggerAxis()).
      whenPressed(new Score(leftCatapult, rightCatapult, prettylights, manipulatorController));

    new JoystickButton(manipulatorController, XboxController.Button.kStart.value).
      whenHeld(new ClimberOverride(climber, () -> applyDeadband(-manipulatorController.getLeftY())));

    new JoystickButton(manipulatorController, XboxController.Button.kBack.value).
      whenHeld(new CatapultOverrride(leftCatapult, rightCatapult));

    buttonFromBoolean(() -> leftCatapult.isCorrectCargo()).whenPressed(new RumbleYes(prettylights, driverController, manipulatorController));
    buttonFromBoolean(() -> leftCatapult.isIncorrectCargo()).whenPressed(new RumbleNo(prettylights, driverController, manipulatorController));
    buttonFromBoolean(() -> rightCatapult.isCorrectCargo()).whenPressed(new RumbleYes(prettylights, driverController, manipulatorController));
    buttonFromBoolean(() -> rightCatapult.isIncorrectCargo()).whenPressed(new RumbleNo(prettylights, driverController, manipulatorController));
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

  public Button buttonFromBoolean(BooleanSupplier value) {
    return new Button(){
      @Override
      public boolean get() {
        return value.getAsBoolean();
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
