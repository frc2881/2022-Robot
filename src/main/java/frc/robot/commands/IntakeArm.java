package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake_Catapult;
import frc.robot.subsystems.Intake_Catapult.Intake_Arm_Direction;

public class IntakeArm extends CommandBase {

  private Intake_Catapult intakeCatapult;
  private Intake_Arm_Direction state;

  /** Creates a new Intake_Arm. */
  public IntakeArm(Intake_Catapult intakeCatapult, Intake_Arm_Direction state) {

    this.intakeCatapult = intakeCatapult;
    this.state = state;
    addRequirements(intakeCatapult);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if(state == Intake_Arm_Direction.EXTEND)
      intakeCatapult.extend();
    else{
      intakeCatapult.retract();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}