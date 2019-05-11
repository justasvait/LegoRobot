# LegoRobot
Lego Mindstorms navigational project

 This program purpose is to navigate LEGO Mindstorm robot through simple track. The track contains of multiple black lines, yellow, red and green circles. On the black line the robot follows it until it reaches one of the circles and does the following:
- On yellow circle, the robot beeps and turns rights, then continues to follow black line;
- On red circle, the robot beeps and turns left, then continues to follow black line;
- On green circle, the robot beeps and does a victory dance. At this point the program stops.

At the start the robot must be placed in front of the black line facing it at 90 degree angle. Once the robot starts running the program, it beeps once and waits until its color sensor detects green color. This functionality is included to delay the start in case other robots are on the way. After it detects green color it starts moving towards the black line and once it reaches it, the robot turns 90 degrees to the right and then follows the rules above.
 
 # What I learned
 - Object-Oriented Programming with Java
 - Accessing sensors and actuator of hardware
 - Robot navigation in a maze
