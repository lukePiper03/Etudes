# COSC326 Etude 3 - Interactive Koch Snowflake

The purpose of this program is to display an interactive Koch Snowflake based on a given order by the user. Koch Snowflake starts as a normal equilateral triangle (Order 1). Suppose the order were to go up to Order 2, the middle third of each triangle side would be replaced with another equilateral going outwards. This sequence gets recursively done for each order given until the shape starts resembling a snowflake.

*First four orders:*
!["Koch Snowflake Order"](/pictures/kochSnowflakeOrder.png  "Koch Snowflake Order"){: .shadow}

  

## How to run

To run the program just click the run button on your IDE and the JFrame window for the program will appear. This Jframe display the Order 1 Snowflake at the start with a slider to control the order. To increase or decrease the order of Kochs Snowflake either drag slider or click on slider bar. The window can be dragged around and is also resizable.

*Display when first opening program:*
!["Display of program window at the order 5"](/pictures/jFrameDisplay.png  "Koch Snowflake"){: .shadow}

  

## Understanding the program

The KochSnowflake class stores 4 methods for the program -

- **main(String[] args) -** 
Creates the jFrame window that all GUI will be presented on, such as the slider which implements a changeListenser which grabs the value of slider when changed, sets a order and repaints window accordingly. This method has a componentListener to make sure the window size never goes below a certain width and height to make sure the snowflake will be easily visible. This method finally creates a new KochSnowflake object, and adds the new object and slider to the Jframe, and makes the Jframe visible to the user.

  
- **paint(Graphics g) -**
Finds each point of the wider (Order 1) triangle and will call recursiveKochSnowflake, for each side of the triangle (3). The paint method gets call when any component of the window gets changed i.e. slider change, window size changed, KochSnowflake object made.
  

- **recursiveKochSnowflake(double startLineX, double startLineY, double endLineX, double endLineY, int order, Graphics g) -**
This method finds the thirds of each line and the next point of the triangle out from the line. If the order is not 1 it will recursively call itself four times, one for each new sides created. If order is 1 it will draw the line to fill in the shape of the snowflake.

- **drawKochSnowflake(double startLineX, double startLineY, double endLineX, double endLineY, Graphics g) -**
The drawLine method will draw a line from the given parameters of startLine(x and y) and endLine (x and y). It uses Graphics2D to keep x and y coordinates as doubles for more accurary and a better look to the snowflake once drawn.

## Recursive Algorithm
The paint method with solve for the end point of the wider (Order 1) triangle and call recursiveKochSnowflake 3 times (one for each side of triangle). From there, the recursiveKochSnowflake method will calculate all three points of the next triangle getting drawn outwards, points A(aX, aY), B(bX, bY), and C(cX, cY). The method will then (with the four new sides generated) will recursively call itself to repeat the process on each line if the order is higher than one. If the order = 1, the method will call drawKochSnowflake to print out the lines given in the parameters of the method call and create the snowflake pattern.
!["Whiteboard image explaining the recursive process"](/pictures/recursiveKochSnowflakeExplaination.png  "recursiveKochSnowflake Explaination"){: .shadow}

## Author

- Name - Luke Piper
- Student ID - 3648114
- Email - piplu394@student.otago.ac.nz