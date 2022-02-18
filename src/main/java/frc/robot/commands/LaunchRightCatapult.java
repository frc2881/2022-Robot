package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.RightCatapult;
import frc.robot.subsystems.RightCatapult.Right_Catapult_Direction;

public class LaunchRightCatapult extends CommandBase {
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

private RightCatapult rightCatapult;
private Right_Catapult_Direction rightCatapultState;

    public LaunchRightCatapult(RightCatapult rightCatapult, Right_Catapult_Direction left, Right_Catapult_Direction right) {

        this.rightCatapult = rightCatapult;
        this.rightCatapultState = right;        
        addRequirements(rightCatapult);
    }

    @Override
    public void execute() {

        if(rightCatapultState == Right_Catapult_Direction.LAUNCH){
            rightCatapult.rightCatapult(1);
        } else if(rightCatapultState == Right_Catapult_Direction.RESET){
            rightCatapult.rightCatapult(-.1);
        }

    }

    @Override
    public void end(boolean interrupted) {
        rightCatapult.rightCatapult(0);           
    }

      @Override
    public boolean isFinished() {
        return false;
    }
} 