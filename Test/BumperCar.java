

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
//import lejos.robotics.MirrorMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * Demonstration of the Behavior subsumption classes.
 * 
 * Requires a wheeled vehicle with two independently controlled
 * motors connected to motor ports A and C, and 
 * a touch sensor connected to sensor  port 1 and
 * an ultrasonic sensor connected to port 3;
 * 
 * @author Brian Bagnall and Lawrie Griffiths, modified by Roger Glassey
 *
 */
public class BumperCar
{
  static RegulatedMotor leftMotor = Motor.A;
  static RegulatedMotor rightMotor = Motor.C;
  
  // Use these definitions instead if your motors are inverted
  // static RegulatedMotor leftMotor = MirrorMotor.invertMotor(Motor.A);
  //static RegulatedMotor rightMotor = MirrorMotor.invertMotor(Motor.C);
  
  public static void main(String[] args)
  {
    leftMotor.setSpeed(400);
    rightMotor.setSpeed(400);
    Behavior b1 = new DriveForward();
    Behavior b2 = new CorrectPath();
    Behavior[] behaviorList =
    {
      b1, b2
    };
    Arbitrator arbitrator = new Arbitrator(behaviorList);
    LCD.drawString("Bumper Car",0,1);
    Button.waitForAnyPress();
    arbitrator.start();
  }
}


class DriveForward implements Behavior
{

  private boolean _suppressed = false;

  public boolean takeControl()
  {
    return true;  // this behavior always wants control.
  }

  public void suppress()
  {
    _suppressed = true;// standard practice for suppress methods
  }

  public void action()
  {
    _suppressed = false;
    BumperCar.leftMotor.forward();
    BumperCar.rightMotor.forward();
    while (!_suppressed)
    {
      Thread.yield(); //don't exit till suppressed
    }
    BumperCar.leftMotor.stop(); 
    BumperCar.rightMotor.stop();
  }
}

class CorrectPath implements Behavior {

	private TouchSensor leftWhisker;
	private TouchSensor rightWhisker;
	
	
	public static boolean LEFT_SIDE = false;
	public static boolean RIGHT_SIDE = false;
	
	public CorrectPath() {
		leftWhisker = new TouchSensor(SensorPort.S1);
		rightWhisker = new TouchSensor(SensorPort.S2);
	}
	
	public boolean takeControl() {
		if (leftWhisker.isPressed()) {
			LEFT_SIDE = true;
			RIGHT_SIDE = false;
		}
		if (rightWhisker.isPressed()) {
			RIGHT_SIDE = true;
			LEFT_SIDE = false;
		}
		return leftWhisker.isPressed() || rightWhisker.isPressed();
	}
	
	public void suppress(){
		//Since  this is highest priority behavior, suppress will never be called.
	}
	
	public void action() {
		try {
			// Left whisker pressed 
			if(LEFT_SIDE) {
				// Turn right a bit
				BumperCar.leftMotor.backward();
				BumperCar.rightMotor.backward();
				Thread.sleep(2000);
				BumperCar.leftMotor.forward();
				BumperCar.rightMotor.backward();
				Thread.sleep(2000);
			}
			// Right whisker pressed 
			else {
				// Turn left a bit
				BumperCar.rightMotor.backward();
				BumperCar.leftMotor.backward();
				Thread.sleep(2000);
				BumperCar.rightMotor.forward();
				BumperCar.leftMotor.backward();
				Thread.sleep(2000);
			}
		}
		catch(Exception e){
			LCD.drawString("Bumper Car",0,1);
		}
	}
}

/*
class AvoidHeadOn implements Behavior {

	private boolean _suppressed = false;
	
	private LightSensor light;
	
	public AvoidHeadOn() {
		light =  new LightSensor(SensorPort.S1);
	}
	
	public boolean takeControl() {
		return light.readValue() <= 40;
	}
	
	public void suppress(){
		_suppressed = true;
	}
	
	public void action() {
		BumperCar.leftMotor.stop(); 
		BumperCar.rightMotor.stop();
	}
	
}
*/

/*
class DetectWall implements Behavior
{

  public DetectWall()
  {
    touch = new TouchSensor(SensorPort.S1);
    sonar = new UltrasonicSensor(SensorPort.S3);
  }

  public boolean takeControl()
  {
    sonar.ping();
    
    return touch.isPressed() || sonar.getDistance() < 25;
  }

  public void suppress()
  {
    //Since  this is highest priority behavior, suppress will never be called.
  }

  public void action()
  {
    BumperCar.leftMotor.rotate(-180, true);// start Motor.A rotating backward
    BumperCar.rightMotor.rotate(-360);  // rotate C farther to make the turn
  }
  
  private TouchSensor touch;
  private UltrasonicSensor sonar;
}
*/

