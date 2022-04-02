// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
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
import frc.robot.commands.autonomous.AutoB;
import frc.robot.commands.autonomous.AutoD;
import frc.robot.commands.autonomous.RightR;
import frc.robot.commands.catapult.CatapultOverrride;
import frc.robot.commands.catapult.Eject;
import frc.robot.commands.catapult.Score;
import frc.robot.commands.catapult.ScoreNoColor;
import frc.robot.commands.climber.ClimberOverride;
import frc.robot.commands.climber.FirstClimberSequence;
import frc.robot.commands.climber.RunArm;
import frc.robot.commands.climber.SecondClimberSequence;
import frc.robot.commands.drive.DriveWithJoysticks;
import frc.robot.commands.drive.AimAtHub;
import frc.robot.commands.drive.RotateByDegrees;
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
import frc.robot.subsystems.VisionTracking;
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
  private final VisionTracking visionTracking = new VisionTracking();

  private final PrettyLights prettyLights = new PrettyLights(powerHub);
  private final Climber climber = new Climber(navx, prettyLights);
  private final Intake intake = new Intake();
  private final LeftCatapult leftCatapult = new LeftCatapult(visionTracking);
  private final RightCatapult rightCatapult = new RightCatapult(visionTracking);
  private final Drive drive = new Drive(navx);
 
  //Left Paths
  private final Trajectory autoB;
  private final Trajectory toStrategicCargo;

  //Right Paths
  private final Trajectory rightPath;
  private final Trajectory toTerminal;
  private final Trajectory terminalToScore;
  private final Trajectory autoD;
  private final Trajectory cargo2ToTerminal;
  private final Trajectory backUpTerminal;
  private final Trajectory backUpTerminalToScore;
  private final Trajectory backUpStrategic;

  private final SendableChooser<Command> m_chooser;

  public boolean robotResetState = true;
  public boolean disableVision = false;

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

    powerHub.setSwitchableChannel(false);
    
    double maxVelocity = 3;
    double maxAcceleration = 2;

    //Left Tarmac
    autoB = PathPlanner2.loadPath("AutoB", maxVelocity, maxAcceleration);
    toStrategicCargo = PathPlanner2.loadPath("ToStrategicCargo", maxVelocity, maxAcceleration, true);    
    backUpStrategic = PathPlanner2.loadPath("BackUpStrategic", maxVelocity, maxAcceleration, true);

    //Right Tarmac
    rightPath = PathPlanner2.loadPath("RightR", maxVelocity, maxAcceleration);
    toTerminal = PathPlanner2.loadPath("ToTerminal", 4, maxAcceleration);
    terminalToScore = PathPlanner2.loadPath("TerminalToScore", 4, maxAcceleration, true);
    autoD = PathPlanner2.loadPath("RightL", maxVelocity, maxAcceleration);
    cargo2ToTerminal = PathPlanner2.loadPath("Cargo2ToTerminal", maxVelocity, maxAcceleration);
    backUpTerminal = PathPlanner2.loadPath("BackUpTerminal", maxVelocity, maxAcceleration, true);
    backUpTerminalToScore = PathPlanner2.loadPath("BackUpTerminalToScore", maxVelocity, maxAcceleration, true);

    // A chooser for autonomous commands. This way we can choose between Paths for Autonomous Period.
    m_chooser = new SendableChooser<>();
    //m_chooser.addOption("Auto Right", new RightR(drive, intake, navx, leftCatapult, rightCatapult, prettyLights, driverController, rightPath, toTerminal, terminalToScore));
    m_chooser.setDefaultOption("Auto D", new AutoD(drive, intake, navx, leftCatapult, rightCatapult, prettyLights, driverController, autoD, cargo2ToTerminal, backUpTerminal, terminalToScore));
    m_chooser.addOption("Auto B", new AutoB(drive, intake, navx, leftCatapult, rightCatapult, prettyLights, driverController, autoB, toStrategicCargo, backUpStrategic));
    m_chooser.addOption("Do Nothing", null);

    //Delays between the Autonomouses
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

    // Put the autonomous chooser onto SmartDashboard.
    SmartDashboard.putData(m_chooser);

    // Start the data logger.
    DataLogManager.start();
    DriverStation.startDataLog(DataLogManager.getLog());

    SmartDashboard.putBoolean("Disable Vision", disableVision);
    SmartDashboard.putNumber("Catapult Soft Limit", 0);
  }

  private void configureButtonBindings() {
    // Driver Xbox Controller

    buttonFromDouble(() -> driverController.getLeftTriggerAxis() + driverController.getRightTriggerAxis()).
      whenHeld(new AimAtHub(navx, drive, () -> visionTracking.getYaw(), prettyLights, driverController, manipulatorController));

    // Manipulator Xbox Controller

    new JoystickButton(manipulatorController, XboxController.Button.kX.value).
      whileHeld(new RunIntake(intake));

    new JoystickButton(manipulatorController, XboxController.Button.kY.value).
      whenPressed(new InstantCommand(() -> climber.armToggle()));

    new JoystickButton(manipulatorController, XboxController.Button.kA.value).
      whileHeld(new ExtendIntake(intake));

    new JoystickButton(manipulatorController, XboxController.Button.kLeftBumper.value).
      whenHeld(new FirstClimberSequence(climber, prettyLights, navx, manipulatorController));

    new JoystickButton(manipulatorController, XboxController.Button.kRightBumper.value).
      whenHeld(new SecondClimberSequence(climber, prettyLights, navx, manipulatorController));

    buttonFromDPad(manipulatorController).
      whenPressed(new Eject(leftCatapult, rightCatapult));

    buttonFromDouble(() -> manipulatorController.getRightTriggerAxis()).
      whenPressed(new Score(leftCatapult, rightCatapult, prettyLights, manipulatorController, intake));

    buttonFromDouble(() -> manipulatorController.getLeftTriggerAxis()).
      whenPressed(new ScoreNoColor(leftCatapult, rightCatapult, prettyLights, manipulatorController, intake)); 

    new JoystickButton(manipulatorController, XboxController.Button.kStart.value).
      whenHeld(new ClimberOverride(climber, () -> applyDeadband(-manipulatorController.getLeftY())));

    new JoystickButton(manipulatorController, XboxController.Button.kBack.value).
      whenHeld(new CatapultOverrride(leftCatapult, rightCatapult));

    buttonFromBoolean(() -> leftCatapult.isCorrectCargo()).whenPressed(new RumbleYes(prettyLights, driverController, manipulatorController));
    buttonFromBoolean(() -> leftCatapult.isIncorrectCargo()).whenPressed(new RumbleNo(prettyLights, driverController, manipulatorController));
    buttonFromBoolean(() -> rightCatapult.isCorrectCargo()).whenPressed(new RumbleYes(prettyLights, driverController, manipulatorController));
    buttonFromBoolean(() -> rightCatapult.isIncorrectCargo()).whenPressed(new RumbleNo(prettyLights, driverController, manipulatorController));
}

  public void resetRobot() {
    if(robotResetState == true) {
      climber.reset();
      drive.reset();
      intake.reset();
      leftCatapult.reset();
      rightCatapult.reset();
      powerHub.setSwitchableChannel(true);
      robotResetState = false;
    }
  }

  public void resetLights() {
    prettyLights.reset();
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
