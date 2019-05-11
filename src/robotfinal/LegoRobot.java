/*
 * LegoRobot.java
 *
 * This is a class that sets up LEGO Mindstorms robot.
 * Default ports are: Left motor - Port A,
 *                    Right motor - Port D,
 *                    Color sensor - Port S2.
 * 
 * This class contains variables of default speeds for both wheels and angle variable
 * for 90 degree turn.
 * This class includes methods for basic movements: go forward, set motor speeds,
 * stop motors, turn 90 degrees, spin, swap right and left speeds.
 * This class includes methods to go around the track: follow the black line,
 * action sequence for yellow, red and green circles.
 *
 */

// Include packages and jar files to be able to connect and control the robot.
package legorobot;
import ShefRobot.*;

/**
 *
 * @author 150148030 Justas Vaitkevicius
 * 
 */
public class LegoRobot {
    // Creates a robot class and connects to the robot.
    Robot myRobot = new Robot();
    
    // Creates references to robot components
    Motor leftMotor = myRobot.getLargeMotor(Motor.Port.A);
    Motor rightMotor = myRobot.getLargeMotor(Motor.Port.D);
    Speaker speaker = myRobot.getSpeaker();
    ColorSensor myColor = myRobot.getColorSensor(Sensor.Port.S2);

    private final int angle; // angle value for 90 degree turn
    private int rightSpeed; // default right wheel speed
    private int leftSpeed;  // default left wheel speed

    
    // constructor
    /**
     *
     * @param ang single wheel rotate angle (adjust so robot makes a 90 degree turn).
     * @param rspeed set default right motor speed.
     * @param lspeed set default left motor speed.
     * 
     */
    public LegoRobot(final int ang, int rspeed, int lspeed) {
        angle = ang; // Single wheel rotate angle
        rightSpeed = rspeed; // Right motor speed
        leftSpeed = lspeed; // Left motor speed
    }
    public LegoRobot() {
        angle = 180;    // Single wheel rotate angle that makes 90 degree turn
        rightSpeed = 150;   // Default right motor speed
        leftSpeed = 20;     // Default left motor speed
    }
        
    // ***************************** FUNCTIONS *********************************
    /**
     * Starts moving both wheels forward.
     */
    public void goForward() {
        leftMotor.forward();
        rightMotor.forward();
    }

    /**
     * Set right and left wheel speeds.
     * The minimum speed is 0 and maximum speed can be found by getMaxSpeed() function.
     * @param rightSpeed The speed of right wheel. Must be positive.
     * @param leftSpeed The speed of left wheel. Must be positive.
     */
    public void setMotorSpeed(int rightSpeed, int leftSpeed) {
        leftMotor.setSpeed(leftSpeed);
        rightMotor.setSpeed(rightSpeed);
    }

    /**
     * Stops both wheels.
     */
    public void stopBoth() {
        leftMotor.stop();
        rightMotor.stop();
    }

    /**
     * Makes a 90 degree turn.
     * @param DIRECTION Select a direction. Use enumerator Side.LEFT or Side.RIGHT
     */
    public void turn90(Side DIRECTION) {
        if (DIRECTION == Side.RIGHT) {
            // RIGHT TURN. Angle used is from the declaration of this robot class.
            leftMotor.rotate(this.angle, true); // this method returns instantly
            // to allow another wheel to rotate simultaneously.
            rightMotor.rotate(-this.angle);
        }
        else if (DIRECTION == Side.LEFT) {
            // LEFT TURN. Same angles used as above, but reversed.
            leftMotor.rotate(-this.angle, true);
            rightMotor.rotate(this.angle);
        }
    }

    /**
     * This function allows the robot to spin.
     * If speeds of both wheels are the same, the robot will spin in one spot.
     * If speeds are different, the robot will spin in spirals.
     * @param DIRECTION Select a direction. Use enumerator Side.LEFT or Side.RIGHT
     */
    public void spin(Side DIRECTION) {
        if (DIRECTION == Side.RIGHT) {
            // spin to the right side
            leftMotor.forward();
            rightMotor.backward();
        }
        else if (DIRECTION == Side.LEFT) {
            // spin to the left side
            leftMotor.backward();
            rightMotor.forward();
        }
    }

    /**
     * This function swaps right speed with left speed.
     * Used to change side on which the robot follows the black line.
     */
    public void swapSpeeds() {
        int temp;
        temp = rightSpeed;
        rightSpeed = leftSpeed;
        leftSpeed = temp;
    }

    /**
     * This function makes a start-up procedure.
     * The robot is set to move straight forward until it reaches the black line
     * and then turns 90 degrees to the right.
     */
    public void startup () {
        // Go straight forward until black line is detected.
        setMotorSpeed(150, 150);
        while ( myColor.getColor() != ColorSensor.Color.BLACK ) {
            goForward();
        }
        
        // Stop at the black line
        stopBoth();

        // Turn right 90 degrees
        turn90(Side.RIGHT);
        
        // stop
        stopBoth();
    }

    /**
     * This function makes the robot to follow the edge of black line.
     * When the robot is on the black line, it goes one side with a slight angle.
     * When the robot is off the black line, it goes opposite side with same angle.
     * 
     * If right wheel speed is bigger than left wheel speed, then the robot follows
     * the line on the right. And vice versa.
     */
    public void followBlackLine() {
        if (myColor.getColor() == ColorSensor.Color.BLACK) {
            // ON THE LINE: turn one direction by little angle
            setMotorSpeed(leftSpeed, rightSpeed); // reverse speeds
            goForward();    // move forward
        }
        else {
            // OFF THE LINE: turn opposite direction by same angle
            setMotorSpeed(rightSpeed, leftSpeed); // set speeds back to normal
            goForward();    // move forward
        }
    }

    /**
     * This function makes the robot to beep and turn right once it reaches yellow circle.
     * The robot is set to go forward for a small delay to enter the middle of the circle
     * and then beeps once and turns right by 90 degrees. Then goes straight until
     * the robot exits the circle. The speeds are set to follow the black line
     * on the right edge.
     */
    public void yellowCircle() {
        if (myColor.getColor() == ColorSensor.Color.YELLOW) {
            // set the speeds to go straight line
            setMotorSpeed(150, 150); 
            
            // move forward for 1 second (to reach middle of the circle)
            goForward();
            myRobot.sleep(1000);
            
            // make a sound
            speaker.playTone(1000, 500); 
            
            // stop the robot
            stopBoth();

            turn90(Side.RIGHT); // turn right
            
            //go forward until robot moved out of the circle
            while (myColor.getColor() == ColorSensor.Color.YELLOW) {
                goForward();
            }
            
            // set default speeds to follow the black line from right side
            if (rightSpeed < leftSpeed) {
                swapSpeeds();
            }
        }
    }

    /**
     * This function makes the robot to beep and turn left once it reaches red circle.
     * The robot goes forward to the middle of the circle and then beeps once and turns
     * left by 90 degrees. Then goes straight out of the circle.
     * The speeds are set to follow the black line on the left edge.
     */
    public void redCircle() {
        if (myColor.getColor() == ColorSensor.Color.RED) {
            // set the speeds to go straight
            setMotorSpeed(150, 150); 
            
            // move forward for 1 second (to reach middle of the circle)
            goForward();
            myRobot.sleep(1000);
            
            // make a sound
            speaker.playTone(1000, 500); 
            
            // stop the robot
            stopBoth();
            
            // turn left
            turn90(Side.LEFT);
            
            //go forward until robot moved out of the circle
            while (myColor.getColor() == ColorSensor.Color.RED) {
                goForward();
            }
            
            // Adjust speeds to make the robot follow the black line on the left edge.
            if (rightSpeed > leftSpeed ) {
                swapSpeeds();
            }
        }
    }

    /**
     * This function makes a celebration once the robot reached the green circle.
     * The robot is set to move little bit into the green circle, plays 2 tones,
     * spins right and left for few times and then stops.
     */
    public void celebration() {
        // go in the middle of the green circle
        setMotorSpeed(150, 150);
        goForward();
        myRobot.sleep(1500);
        
        // stop
        stopBoth();
        
        // victory sound
        speaker.playTone(1000, 400);
        speaker.playTone(2000, 800);
        
        // do a celebration dance: spin right and left for few times
        setMotorSpeed(500, 500); // increase motor speeds
        for (int i=0; i<2;i++) {
            // spin right
            spin(Side.RIGHT);
            myRobot.sleep(1000);
            
            // stop
            stopBoth();
            
            //spin left
            spin(Side.LEFT);
            myRobot.sleep(1000);
            
            // stop
            stopBoth();
        }
    }
    
    /**
     * Enumerator for choosing the directions.
     * Used for rotating and spin functions.
     */
    public enum Side {
            RIGHT, LEFT
        }
}
