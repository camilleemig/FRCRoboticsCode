package org.usfirst.frc.team904.robot;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
//Does this work correctly?
public class Robot extends IterativeRobot {
	//-----------------------------------Declarations--------------------------------------------
	// loop control
	static Boolean loopCheck = false;
	static Boolean timerCheck = false;
	static Boolean autonomousDone = false;
	
	Timer autonTime; 
	// joysticks
	static Joystick driverJoystick, operatorJoystick;
	
	// forklift
	static Talon forkliftMotor;
	static final double forkliftSpeed = .75;	// Forklift Speed Magnitude. Change this to make the forklift go slower. NO NEGATIVES
	
	// arms
	static Relay ArmRelay;
	
	// encoders
	static Encoder encoderFrontLeft; 
	static Encoder forkliftEncoder; 
	
	// robotDrive
	static double stickX, stickY, stickZ;
	static RobotDrive myRobot;
	
	/** auton modes enum------------ for some reason not working. Check later to see why. Try to fix.
	public enum forwardOnlyAuton {
		forward1 //make sure that this is 1200
	}
	
	enum strafeLeftOnly {
		left1 //make sure that this is -1200
	}
	
	enum hulkBackwardAutonomous {
		armGrip1, armRelease1, forklift1, armGrip2, backward1, turnLeft1, forklift2, armRelease2
	}
	
	enum hulkForwardAutonomous {
		armGrip1, armRelease1, forklift1, armGrip2, forward1, turnLeft1, forklift2, armRelease2, backward1
	}
	
	enum toteBackwardAutonomous {
		forklift1, armGrip1, forklift2, backward1, left1, forklift3, armRelease1
	}
	
	public enum toteForwardAutonomous {
		forklift1, armGrip1, forklift2, forward1, left1, forklift3, armRelease1, backward1
	}
	 */
	// autonomous modes
	String forwardOnlyAuton, strafeLeftOnly, hulkBackwardAutonomous, hulkForwardAutonomous, toteBackwardAutonomous, toteForwardAutonomous;
	//-----------------------------------Robot Init----------------------------------------------
    public void robotInit() {
    	// autonomous mode settings
    	forwardOnlyAuton = "forward1";
    	strafeLeftOnly = "left1";
    	hulkBackwardAutonomous = "armGrip1";
    	hulkForwardAutonomous = "armGrip1";
    	toteBackwardAutonomous = "forklift1";
    	toteForwardAutonomous = "forklift1";
    	autonTime = new Timer();

    	// joysticks
    	driverJoystick = new Joystick(0);
    	operatorJoystick = new Joystick(1);
    	
    	// forklift
    	forkliftMotor = new Talon(0);
    	forkliftEncoder =  new Encoder(0, 1, true, EncodingType.k4X);

    	// arms
    	ArmRelay = new Relay(1);
		ArmRelay.set(Relay.Value.kOff);
		
    	// encoders
    	encoderFrontLeft =  new Encoder(2, 3, true, EncodingType.k4X);
    	
    	// robotDrive
    	myRobot = new RobotDrive(1, 3, 2, 4);
    	myRobot.setInvertedMotor(MotorType.kFrontLeft, true);
    	myRobot.setInvertedMotor(MotorType.kRearLeft, true);
    	myRobot.setExpiration(0.1);
    	
    }

	//-----------------------------------Auton Periodic------------------------------------------
    public void autonomousPeriodic() {
    	if(autonomousDone == false){
	    	//TODO: pick only one of these! IMPORTANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    		forwardOnlyAuton();
	    	strafeLeftOnly();
	    	hulkBackwardAutonomous();
	    	hulkForwardAutonomous();
	    	toteBackwardAutonomous();
	    	toteForwardAutonomous();
    	}
    }
	
    //-----------------------------------Forward Only--------------------------------------------
    public void forwardOnlyAuton(){
		switch(forwardOnlyAuton){
			case "forward1":
				driveAutomatic(1200.00);
				if(loopCheck == true){
					encoderFrontLeft.reset();
					loopCheck = false;
					forwardOnlyAuton = "done";
				}
			case "done":
				autonomousDone = true;
    	}
    }
    
	//-----------------------------------Strafe Left Only----------------------------------------
    public void strafeLeftOnly(){
		switch(forwardOnlyAuton){
			case "left1":
				strafeAutomatic(-1200.00);
				if(loopCheck == true){
					encoderFrontLeft.reset();
					loopCheck = false;
					forwardOnlyAuton = "done";
				}
			case "done":
				autonomousDone = true;
		}
    }
    
	//-----------------------------------Hulk Backward-------------------------------------------
    public void hulkBackwardAutonomous(){
		switch(hulkBackwardAutonomous){
			case "armGrip1":
		    	armsAutomatic(.4, "close");
			    if(loopCheck = true){
			    	loopCheck = false;
			    	hulkBackwardAutonomous = "armRelease1";
			    }
			case "armRelease1":
				armsAutomatic(.08, "open");
				if(loopCheck = true){
					loopCheck = false;
		    		hulkBackwardAutonomous = "forklift1";
		    	}
			case "forklift1":
				forkliftAutomatic(6000.00);
				if(loopCheck == true){
					hulkBackwardAutonomous = "armGrip2";
					loopCheck = false;
				}
			case "armGrip2":
				armsAutomatic(.16, "close");
				if(loopCheck = true){
					loopCheck = false;
		    		hulkBackwardAutonomous = "backward1";
		    	}

			case "backward1":
				driveAutomatic(-1200.00);
				if(loopCheck == true){
					hulkBackwardAutonomous = "turnLeft1";
					loopCheck = false;
					encoderFrontLeft.reset();
				}

			case "turnLeft1":
				turnAutomatic(-270.00);
				if(loopCheck == true){
					hulkBackwardAutonomous = "forklift2";
					loopCheck = false;
					encoderFrontLeft.reset();
				}
				
			case "forklift2":
				forkliftAutomatic(1050.00);
				if(loopCheck == true){
					hulkBackwardAutonomous = "armRelease2";
					loopCheck = false;
				}

			case "armRelease2":
				armsAutomatic(.25, "open");
				if(loopCheck = true){
					loopCheck = false;    
					hulkBackwardAutonomous = "done";
				}
			case "done":
				autonomousDone = true;
		}
    }
    
	//-----------------------------------Hulk Forward--------------------------------------------
    public void hulkForwardAutonomous(){
		switch(hulkForwardAutonomous){
			case "armGrip1":
				armsAutomatic(.4, "close");
				if(loopCheck = true){
					loopCheck = false;    
					hulkForwardAutonomous = "armRelease1";
				}
			case "armRelease1":
				armsAutomatic(.08, "open");
				if(loopCheck = true){
					loopCheck = false;    
					hulkForwardAutonomous = "forklift1";
				}
			case "forklift1":
				forkliftAutomatic(6000.00);
				if(loopCheck == true){
					hulkForwardAutonomous = "armGrip2";
					loopCheck = false;
				}
			case "armGrip2":
				armsAutomatic(.16, "close");
				if(loopCheck = true){
					loopCheck = false;    
					hulkForwardAutonomous = "forward1";
				}
			case "forward1":
				driveAutomatic(1200.00);
				if(loopCheck == true){
					hulkForwardAutonomous = "turnLeft1";
					loopCheck = false;
					encoderFrontLeft.reset();
				}
	
			case "turnLeft1":
				turnAutomatic(-270.00);
				if(loopCheck == true){
					hulkForwardAutonomous = "forklift2";
					loopCheck = false;
					encoderFrontLeft.reset();
				}
				
			case "forklift2":
				forkliftAutomatic(1050.00);
				if(loopCheck == true){
					hulkForwardAutonomous = "armRelease2";
					loopCheck = false;
				}
	
			case "armRelease2":
				armsAutomatic(.25, "open");
				if(loopCheck = true){
					loopCheck = false;
					hulkForwardAutonomous = "backward1";
				}
					
			case "backward1":
				driveAutomatic(-25.00);
				if(loopCheck == true){
					hulkForwardAutonomous = "done";
					loopCheck = false;
					encoderFrontLeft.reset();
				}
			case "done":
				autonomousDone = true;
		}
    }
    
	//-----------------------------------Tote Backward-------------------------------------------
    public void toteBackwardAutonomous(){
		switch(toteBackwardAutonomous){
			case "forklift1":
				forkliftAutomatic(530.00);
				if(loopCheck == true){
					toteBackwardAutonomous = "armGrip1";
					loopCheck = false;
				}
			case "armGrip1":
				armsAutomatic(.25, "close");
				if(loopCheck = true){
					loopCheck = false;    
					toteBackwardAutonomous = "forklift2";
				}
				
			case "forklift2":
				forkliftAutomatic(1050.00);
				if(loopCheck == true){
					toteBackwardAutonomous = "backward1";
					loopCheck = false;
				}
			case "backward1":
				driveAutomatic(-1500.00);
				if(loopCheck == true){
					toteBackwardAutonomous = "turnLeft1";
					loopCheck = false;
					encoderFrontLeft.reset();
				}
			case "turnLeft1":
				turnAutomatic(-260.00);
				if(loopCheck == true){
					toteBackwardAutonomous = "forklift3";
					loopCheck = false;
					encoderFrontLeft.reset();
				}
			case "forklift3":
				forkliftAutomatic(100.00);
				if(loopCheck == true){
					toteBackwardAutonomous = "armRelease1";
					loopCheck = false;
				}
			case "armRelease1":
				armsAutomatic(.25, "open");
				if(loopCheck = true){
					loopCheck = false;
					toteBackwardAutonomous = "done";
				}
			case "done":
				autonomousDone = true;
		}
    }
    
	//-----------------------------------Tote Forward--------------------------------------------
    public void toteForwardAutonomous(){
		switch(forwardOnlyAuton){
		case "forklift1":
			forkliftAutomatic(530.00);
			if(loopCheck == true){
				toteForwardAutonomous = "armGrip1";
				loopCheck = false;
			}
			
		case "armGrip1":
			armsAutomatic(.25, "close");
			if(loopCheck = true){
				loopCheck = false;
				toteForwardAutonomous = "forklift2";
			}
			
		case "forklift2":
			forkliftAutomatic(1050.00);
			if(loopCheck == true){
				toteForwardAutonomous = "forward1";
				loopCheck = false;
			}
			
		case "forward1":
			driveAutomatic(1500.00);
			if(loopCheck == true){
				toteForwardAutonomous = "turnLeft1";
				loopCheck = false;
				encoderFrontLeft.reset();
			}
			
		case "turnLeft1":
			turnAutomatic(-260.00);
			if(loopCheck == true){
				toteForwardAutonomous = "forklift3";
				loopCheck = false;
				encoderFrontLeft.reset();
			}
			
		case "forklift3":
			forkliftAutomatic(100.00);
			if(loopCheck == true){
				toteForwardAutonomous = "armRelease1";
				loopCheck = false;
			}
			
		case "armRelease1":
			armsAutomatic(.25, "open");
			if(loopCheck = true){
				loopCheck = false;
				toteForwardAutonomous = "backward1";
			}
	
		case "backward1":
			driveAutomatic(-25.00);
			if(loopCheck == true){
				toteForwardAutonomous = "done";
				loopCheck = false;
				encoderFrontLeft.reset();
			}
			
		case "done":
			autonomousDone = true;
		}
    }

    //-----------------------------------Forklift Automatic--------------------------------------
    public void forkliftAutomatic(double desiredValue){
    	if (forkliftEncoder.get() < desiredValue){
			if (forkliftEncoder.get() < 15000){
				forkliftMotor.set(forkliftSpeed);
			}
			else{
				forkliftMotor.set(0.0);
    		}    		
    		if (((desiredValue - 100) < forkliftEncoder.get()) && (forkliftEncoder.get() < (desiredValue + 100))){
    			forkliftMotor.set(0.0);
    			loopCheck = true;
    		}
		}
		else if(forkliftEncoder.get() > desiredValue){
			if (forkliftEncoder.get() < 15000){
				forkliftMotor.set(-forkliftSpeed);
			}
			else{
				forkliftMotor.set(0.0);
    		}    		
    		if (((desiredValue - 100) < forkliftEncoder.get()) && (forkliftEncoder.get() < (desiredValue + 100))){
    			forkliftMotor.set(0.0);
    			loopCheck = true;
    		}
		}
    }
    
    //-----------------------------------Drive Automatic-----------------------------------------
    public void driveAutomatic(double desiredValue){
    	if (encoderFrontLeft.get() < desiredValue){
			if (encoderFrontLeft.get() < 5000){
				myRobot.mecanumDrive_Cartesian(0, .35, 0, 0);
			}
			else{
				myRobot.mecanumDrive_Cartesian(0, 0, 0, 0);
    		}    		
    		if (((desiredValue - 100) < encoderFrontLeft.get()) && (encoderFrontLeft.get() < (desiredValue + 100))){
    			myRobot.mecanumDrive_Cartesian(0, 0, 0, 0);
    			loopCheck = true;
    		}
    	} else if(encoderFrontLeft.get() > desiredValue){
			if (encoderFrontLeft.get() < 5000){
				myRobot.mecanumDrive_Cartesian(0, -.35, 0, 0);
			}
			else{
				myRobot.mecanumDrive_Cartesian(0, 0, 0, 0);
    		}    		
    		if (((desiredValue - 100) < encoderFrontLeft.get()) && (encoderFrontLeft.get() < (desiredValue + 100))){
    			myRobot.mecanumDrive_Cartesian(0, 0, 0, 0);
    			loopCheck = true;
    		}
    	}
    }
	
    //-----------------------------------Turn Automatic------------------------------------------
    public void turnAutomatic(double desiredValue){
    	if (desiredValue < encoderFrontLeft.get()){
			if (encoderFrontLeft.get() < 5000){
				myRobot.mecanumDrive_Cartesian(0, 0, .5, 0);
			}
			else{
				myRobot.mecanumDrive_Cartesian(0, 0, 0, 0);
    		}    		
    		if (((desiredValue - 100) < encoderFrontLeft.get()) && (encoderFrontLeft.get() < (desiredValue + 100))){
    			myRobot.mecanumDrive_Cartesian(0, 0, 0, 0);
    			loopCheck = true;
    		}
    	} else if(encoderFrontLeft.get() > desiredValue) {
    		if (encoderFrontLeft.get() < 5000){
				myRobot.mecanumDrive_Cartesian(0, 0, -.5, 0);
			}
			else{
				myRobot.mecanumDrive_Cartesian(0, 0, 0, 0);
    			}    		
    		if (((desiredValue - 100) < encoderFrontLeft.get()) && (encoderFrontLeft.get() < (desiredValue + 100))){
    			myRobot.mecanumDrive_Cartesian(0, 0, 0, 0);
    			loopCheck = true;
    		}
    	}
    }

    //-----------------------------------Strafe Automatic----------------------------------------
    public void strafeAutomatic(double desiredValue){
    	if (desiredValue < encoderFrontLeft.get()){
			if (encoderFrontLeft.get() < 5000){
				myRobot.mecanumDrive_Cartesian(.5, 0, 0, 0);
			}
			else{
				myRobot.mecanumDrive_Cartesian(0, 0, 0, 0);
    		}    		
    		if (((desiredValue - 100) < encoderFrontLeft.get()) && (encoderFrontLeft.get() < (desiredValue + 100))){
    			myRobot.mecanumDrive_Cartesian(0, 0, 0, 0);
    			loopCheck = true;
    		}
    	}
    	else if (encoderFrontLeft.get() < desiredValue){
			if (encoderFrontLeft.get() < 5000){
				myRobot.mecanumDrive_Cartesian(-.5, 0, 0, 0);
			}
			else{
				myRobot.mecanumDrive_Cartesian(0, 0, 0, 0);
    			}    		
    		if (((desiredValue - 100) < encoderFrontLeft.get()) && (encoderFrontLeft.get() < (desiredValue + 100))){
    			myRobot.mecanumDrive_Cartesian(0, 0, 0, 0);
    			loopCheck = true;
    		}
    	}
    }
    
    //-----------------------------------Arms Automatic------------------------------------------
    public void armsAutomatic(double time, String direction){
    	if(direction == "open"){
    		if(timerCheck == false){
	    		autonTime.reset();
	    		timerCheck = true;
	    	}
	    	if(autonTime.get() < time){
	    		ArmRelay.set(Relay.Value.kReverse);    
	    	}
	    	else{
	    		timerCheck = false;
		    	loopCheck = true;
	    	}
    	}
    	if(direction == "close"){
    		if(timerCheck == false){
	    		autonTime.reset();
	    		timerCheck = true;
	    	}
	    	if(autonTime.get() < time){
	    		ArmRelay.set(Relay.Value.kForward);    
	    	}
	    	else{
	    		timerCheck = false;
		    	loopCheck = true;
	    	}
    	}
    }
    
    //-----------------------------------Teleop Periodic-----------------------------------------
    public void teleopPeriodic() {
        //arms
    	operatorArms();
        // robotDrive
        drivebase();
        // joystick
        operatorForklift();
        if(operatorJoystick.getRawButton(5) && operatorJoystick.getRawButton(6)){	
        	forkliftEncoder.reset();
        }
    }
    
    //-----------------------------------TestPeriodic--------------------------------------------
    public void testPeriodic() {

    }
    
    //-----------------------------------Run Forklift--------------------------------------------
    public void operatorForklift(){
    	forkliftMotor.set(operatorJoystick.getY());
    }
    
    //-----------------------------------Run Arms------------------------------------------------
    public void operatorArms(){
		if(operatorJoystick.getRawButton(1)){
			ArmRelay.set(Relay.Value.kForward);
		}
		else if(operatorJoystick.getRawButton(2)){
			ArmRelay.set(Relay.Value.kReverse);
		}
		else{
			ArmRelay.set(Relay.Value.kOff);
		}
    }
	
    //-----------------------------------Drivebase-----------------------------------------------
    public void drivebase(){
    	//-----------------------------------Joystick Y Values---------------------------------------
    	if((-driverJoystick.getY() > -.1) && (-driverJoystick.getY() < .1) || driverJoystick.getRawButton(2)) {
    		stickY = 0;
    	}
    	else{
    		if(-driverJoystick.getY() < -0.1){
    		stickY = -1.11*driverJoystick.getY()+.11;
    		}
    		else{
    		stickY = -1.11*driverJoystick.getY()-.11;	
    		}
    	}
    	
    	//-----------------------------------Joystick X Values---------------------------------------
    	if((-driverJoystick.getX() > -.1) && (-driverJoystick.getX() < .1) || driverJoystick.getRawButton(1)) {
    		stickX = 0;
    	}
    	else{
    		if(-driverJoystick.getX() < -0.1){
    		stickX = -1.11*driverJoystick.getX()+.11;
    		}
    		else{
    		stickX = -1.11*driverJoystick.getX()-.11;	
    		}
    		
    	}	
    	
    	//-----------------------------------Joystick Z Values---------------------------------------    		
    	if((-driverJoystick.getZ() > -.1) && (-driverJoystick.getZ() < .1) || driverJoystick.getRawButton(1) || driverJoystick.getRawButton(2)) {
    		stickZ = 0;
    	}
    	else{
    		if(-driverJoystick.getZ() < -0.1){
    		stickZ = -.555*driverJoystick.getZ()+.11;  //The value 0.555 comes from multiplying the 1.11 scaling factor by a 50% dampening value.
    		}
    		else{
    		stickZ = -.555*driverJoystick.getZ()-.11;	
    		}
    	}
    	
    	//-----------------------------------Drive---------------------------------------------------
    	if (myRobot != null) myRobot.mecanumDrive_Cartesian(stickX, stickY, stickZ, 0);
    	SmartDashboard.putNumber("Encoder Front Left", encoderFrontLeft.getDistance());
    }
}

