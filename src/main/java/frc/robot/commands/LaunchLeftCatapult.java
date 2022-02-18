package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.LeftCatapult.Left_Catapult_Direction;

public class LaunchLeftCatapult extends CommandBase {
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

private LeftCatapult leftCatapult;
private Left_Catapult_Direction leftCatapultState;

    public LaunchLeftCatapult(LeftCatapult leftCatapult, Left_Catapult_Direction left, Left_Catapult_Direction right) {

        this.leftCatapult = leftCatapult;
        this.leftCatapultState = left;
        //this.rightCatapultState = right;        
        addRequirements(leftCatapult);
    }

    @Override
    public void execute() {
        // Launches the catapult or brings it back down
        if(leftCatapultState == Left_Catapult_Direction.LAUNCH){
            leftCatapult.leftCatapult(1);
        } else if(leftCatapultState == Left_Catapult_Direction.RESET){
            leftCatapult.leftCatapult(-.1);
          }

    }

    @Override
    public void end(boolean interrupted) {
        leftCatapult.leftCatapult(0);     
    }

      @Override
    public boolean isFinished() {
        return false;
    }
} 