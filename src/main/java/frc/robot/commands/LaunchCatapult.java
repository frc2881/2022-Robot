package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake_Catapult;
import frc.robot.subsystems.Intake_Catapult.Catapult_Direction;

public class LaunchCatapult extends CommandBase {
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

private Intake_Catapult intake_catapult;
private Catapult_Direction leftCatapultState;
private Catapult_Direction rightCatapultState;

    public LaunchCatapult(Intake_Catapult intake_catapult, Catapult_Direction left, Catapult_Direction right) {

        this.intake_catapult = intake_catapult;
        this.leftCatapultState = left;
        this.rightCatapultState = right;        
        addRequirements(intake_catapult);
    }

    @Override
    public void execute() {
        // Launches the catapult or brings it back down
        if(leftCatapultState == Catapult_Direction.LAUNCH){
            intake_catapult.leftCatapult(1);
        } else if(leftCatapultState == Catapult_Direction.RESET){
            intake_catapult.leftCatapult(-.1);
          }

        if(rightCatapultState == Catapult_Direction.LAUNCH){
            intake_catapult.rightCatapult(1);
        } else if(rightCatapultState == Catapult_Direction.RESET){
            intake_catapult.rightCatapult(-.1);
        }

    }

    @Override
    public void end(boolean interrupted) {
        intake_catapult.leftCatapult(0);
        intake_catapult.rightCatapult(0);           
    }

      @Override
    public boolean isFinished() {
        return false;
    }
} 