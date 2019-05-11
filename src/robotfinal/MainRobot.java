/*
 * MainRobot.java
 *
 * The program is made for COM160 module assignment.
 * This program purpose is to navigate LEGO Mindstorms robot through simple track.
 * The track contains of multiple black lines, yellow, red and green circles.
 * On the black line the robot follows it until it reaches one of the circles and
 * does the following:
 *      -On yellow circle, the robot beeps and turns rights, then continues
 *       to follow black line;
 *      -On red circle, the robot beeps and turns left, then continues to
 *       follow black line;
 *      -On green circle, the robot beeps and does a victory dance. At this
 *       point the program stops.
 *
 * At the start the robot must be placed in front of the black line facing it
 * at 90 degree angle. Once the robot starts running the program, it beeps once
 * and waits until its color sensor detects green color. This functionality is
 * included to delay the start in case other robots are on the way. After it
 * detects green color it starts moving towards the black line and once it
 * reaches it, the robot turns 90 degrees to the right and then follows the rules
 * above.
 *
 * In order for the program to work, connect left motor cable to port A,
 * right motor - port D, color sensor - port S2.
 */

// Include packages and jar files to be able to connect and control the robot.
package legorobot;
import ShefRobot.*;

/**
 *
 * @author 150148030 Justas Vaitkevicius
 */
public class MainRobot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* CREATE ROBOT OBJECT AND CONNECT TO IT.*/
        // Three parameters: turn angle (must make 90 degree turn), right wheel
        // speed and left wheel speed are chosen to achieve the goal.
        LegoRobot myLegoRobot = new LegoRobot(180, 150, 20);
        
        
        /* DELAYED START*/
        // Makes a sound to indicate the robot is active and waits until the
        // robot's color sensor detects green color.
        myLegoRobot.speaker.playTone(1000, 100);
        while (myLegoRobot.myColor.getColor() != ColorSensor.Color.GREEN);
        myLegoRobot.myRobot.sleep(500);
        
        /* ROBOT STARTS TO NAVIGATE ACROSS THE TRACK.*/
        // Start-up sequence to reach the black line and turn right 90 degrees.
        myLegoRobot.startup();
        
        // Loops the code sequence until the robot reaches the green circle.
        // All three methods have if-statements to find out which color the robot is under.
        // On the black line, the robot follows it.
        // On yellow circle, the robot beeps and turns right.
        // On red circle, the robot beeps and turn left.
        while ( myLegoRobot.myColor.getColor() != ColorSensor.Color.GREEN) {
            myLegoRobot.followBlackLine();
            myLegoRobot.yellowCircle();
            myLegoRobot.redCircle();
        }
        
        /* THE ROBOT IS AT THE FINISH (GREEN CIRCLE).*/
        // The robot beeps and does a victory dance.
        myLegoRobot.celebration();
        
        // Disconnects the robot
        myLegoRobot.myRobot.close();
    }
    
}
