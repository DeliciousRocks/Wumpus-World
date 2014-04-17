import java.awt.Container;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;

//Coded by: Walter Squires

public class WumpusWorld
{
  public static void main(String[] args)
  {
    MyFrame frame = new MyFrame();
    frame.setTitle("Movement Tester");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setVisible(true);
  }
}

class MyFrame extends JFrame
{
  private ArrayList<Coordinate> used;         // An arraylist that will keep track of all of the used corrdinates
  private JPanel buttonPanel;                 // Stores all of the buttons; not neccessary for final project
  private TextPanel scoreBoard;               // Displays the score
  private JButton up, down, left, right, upLeft, upRight, middle, downLeft, downRight;  // Buttons that allow movement
  private Grid map;                           // Creates and displays the grid and the entities on it
  private static int score;                   // Keeps track of the score
  
  
  public MyFrame()
  {
    int rows = 4;                              // Sets the number of rows on the grid
    int columns = 4;                           // Sets the number of columns on the grid
    Coordinate.updateGrid(rows, columns);      // Lets the Coordinate class know what the size of the grid is
    used = new ArrayList();                    // Creates an array list to keep track of all used coordinat
    setSize(1000, 600);                        // Sets the size of the frame
    map = new Grid(rows, columns, used);       // Creates the games grid; tells it what the number of rows and colums should be and gives it access to the used array              
    
    Container contentPane = getContentPane();  // Creates the frame that will hold the panels in place
    scoreBoard = new TextPanel(score);         // Creates the scoreboard, passing it the current score
    contentPane.add(scoreBoard, "South");      // Places the scoreboard at the bottom of the window
    contentPane.add(map, "Center");            // Places the grid in the center of the board
    
    buttonPanel = new JPanel();                // Makes the panel that will hold the buttons
    buttonPanel.setLayout( new GridLayout(3,3,0,0));  // Formats the button panel and tells it how to organize the buttons
    ActionListener listener = new Choice();    // Creates the action listener, which does things when buttons are pressed
    
    upLeft = new JButton("UP-LEFT");           // Makes a button labeled "UP-LEFT"
    buttonPanel.add(upLeft);                   // Puts the button in the button panel
    
    up = new JButton("UP");                    // Makes a button labeled "UP"
    up.addActionListener(listener);            // Attaches the listener to the button; when pushed moves the agent up if it is a legal move
    buttonPanel.add(up);                       // Puts the button in the button panel
    
    upRight = new JButton("UP-RIGHT");         // Makes a button labeled "UP-RIGHT"
    buttonPanel.add(upRight);                  // Puts the button in the button panel
    
    left = new JButton("LEFT");                // Makes a button labeled "LEFT"
    left.addActionListener(listener);          // Attaches the listener to the button; when pushed moves the agent left if it is a legal move
    buttonPanel.add(left);                     // Puts the button in the button panel
    
    middle = new JButton();                    // Makes an unlabeled button
    middle.addActionListener(listener);        // Attaches the listener to the button; when pushed it calles on the AI to determine the best move
    buttonPanel.add(middle);                   // Puts the button in the button panel
    
    right = new JButton("RIGHT");              // Makes a button labeled "RIGHT"
    right.addActionListener(listener);         // Attaches the listener to the button; when pushed moves the agent right if it is a legal move
    buttonPanel.add(right);                    // Puts the button in the button panel
    
    downLeft = new JButton("DOWN-LEFT");       // Makes a button labeled "DOWN-LEFT"
    buttonPanel.add(downLeft);                 // Puts the button in the button panel
    
    down = new JButton("DOWN");                // Makes a button labeled "DOWN"
    down.addActionListener(listener);          // Attaches the listener to the button; when pushed moves the agent down if it is a legal move
    buttonPanel.add(down);                     // Puts the button in the button panel
    
    downRight = new JButton("DOWN-RIGHT");     // Makes a button labeled "DOWN-RIGHT"
    buttonPanel.add(downRight);                // Puts the button in the button panel
    
    contentPane.add(buttonPanel, "East");      // Places the button panel on the right side of the window  
    
  }
  
  public static int getScore()                 // Static method that the paint method needs to update the scoreboard
  {
    return score;                              // All this does is return the score
  }
  
  
  
  
  private class  Choice implements ActionListener  // The class keeps track of everything that is supposed to happen when a button is pressed
  {
    public void actionPerformed(ActionEvent event)
    {
      Object source = event.getSource();           // Figures out what button was pressed
      
      int index = 0;                               // Sets a varible to zero
      for (int i = 0; i <used.size();i++)          // This loop searches through the used array...
      {
        if (used.get(i).getType() == 1)            // and when it finds the agent...
          index = i;                               // records its index
      }
      Coordinate player = used.get(index);         // Get the coordinate for the agent out of the array
      boolean out = true ;                         // Creates a flag variable to keep track of if the current position is in the visied array
      for (int f = 0; f < AI.getVisited().size(); f++) // This loop searches through the array of visited locations
      {
        if ( AI.getVisited().get(f).sameSpot(player))  // If the current location of the agent is already in the visited array...
          out = false;                                 // Change the flag
      }
      if (out)                                         // If the current location of the agent is not in the visited array list...
        AI.addVisited(player.softCopy());              // add the location to the array
      
      if(source == up)                                 // If the "UP" button was pressed...
        player.n();                                    // try to move the agent up
      else if (source == down)                         // If the "DOWN" button was pressed...
        player.s();                                    // try to move the agent down
      else if (source == left)                         // If the "LEFT" button was pressed
        player.w();                                    // try to move the agent left
      else if (source == middle)                       // If the middle button was pressed
      {
        AI.findMove(used);                             // Ask the AI to find the best move
      }
      else if (source ==  right)                       // If the "RIGHT" button was pressed...
        player.e();                                    // try to move the agent right
      
      AI.setCoord(player);                             // Let the AI know the new location of the agent
      score--;                                         // Deduct on point from the score for moving
      
      for (int j = 0; j < used.size(); j++)            // Search through the used array
      {
        if (player.sameSpot(used.get(j)) && ((used.get(j).getType() == 2 )|| (used.get(j).getType() == 3))) // If the agent is in the same location as a pit or wumpus...
          score = score -1000;                                                                              // deduct 1000 points from the score
        else if (player.sameSpot(used.get(j)) && (used.get(j).getType() == 5 ))                             // If the agent is in the same location as the gold...
          score = score +1000;                                                                              // add 1000 points to the score
        repaint();                                                                                          // Update the grid
      }
    }
  }
  
  
  
  class Grid extends JPanel                             // This class creates and illustrates the environment
  {
    private int rows;
    private int columns;
    private int h;
    private int v;
    private ArrayList<Coordinate> used;
    
    public Grid(int r, int c, ArrayList<Coordinate> u)
    {
      rows = r;                                                                     // Has access to number of rows
      columns = c;                                                                  // Has access to number of columns
      used = u;                                                                     // Has access to array of used locations
      selectedPointMaker(rows-1, 0, used,1);                                        // Creates the starting point
      double holes =  ((rows*columns)*.125);                                        // Sets the number of holes to 1/8 of the area of the grid
      double wumpus = ((rows*columns)*.0625);                                       // Sets the number of wumpus to 1/16 of the area of the grid
      
      for (int x = 0; x < holes;)                                                   // Loop that generates holes
      {
        boolean done =randomPointMaker(rows,columns,used, 2);                       // Try to make a hole
        if (done)                                                                   // If a hole was made...
          x++;                                                                      // make note of it
      }
      for (int y = 0; y < wumpus;)                                                  // Loop that generates wumpus
      {
        boolean done =randomPointMaker(rows,columns,used, 3);                       // Try to make a wumpus
        if (done)                                                                   // If a wumpus was made...
          y++;                                                                      // make note of it   
      }
      for (int z = 0; z < 1;)                                                       // Loop that generates gold
      {
        boolean done =randomPointMaker(rows,columns,used, 5);                       // Try to make a gold
        if (done)                                                                   // If gold was made...
          z++;                                                                      // make note of it
      }
      addWarnings(used);                                                            // Creates the warning signs for generated hazards
    }
    
    public void addWarnings(ArrayList<Coordinate> used)                             // Given the array of used locations, add warnings around the hazards
    {
      int originalSize = used.size();                                               // Keep track of the original size of the array
      for (int i = 0;i < originalSize;i++)                                          // Search through the used array
      {
        Coordinate temp = used.get(i);                                              // Look at the coordinate at the given index
        int type = temp.getType();                                                  // Find out what type it is
        if (type==2 || type == 3)                                                   // If it is a hazard...
          generateWarnings(temp,type, originalSize, used);                          // Generate warnings
      }
    }
    
    public void generateWarnings(Coordinate temp, int type, int originalSize, ArrayList<Coordinate> used)  // Given a coordinate, look at its surroundings and add them to the used array as warnings
    {
      ArrayList<Coordinate> buffer = temp.getSurroundings();                        // Creates an array of locations that surround the given point
      for (int j = 0; j < buffer.size(); j++)                                       // For each value in the array
      {
        Coordinate toBeAdded = buffer.get(j);                                       // Gets the coordinate at the given index
        boolean atHazard = false;                                                   // Creates a flag to check if the coordinate above is sharing a space with a hazard
        for (int i = 0; i < originalSize; i++)                                      // Searches through the array of used locations
        {
          if (toBeAdded.sameSpot(used.get(i)) && ((used.get(i).getType() == 2 )||(used.get(i).getType() == 3)))// If the new location is in the used array, and the location in the used array is a hazard...
            atHazard = true;                                                        // Change the flag
        }
        if (!atHazard)                                                              // If the new location is not currently in use as a hazard...
        {
          toBeAdded.setType(type*2);                                                // set the new locations type to be the warning for its hazard and...
          used.add(toBeAdded);                       // add it to the array of used coordinates (because we took the size of the array before adding new locations, we will not get stuck in an infinite loop
        }
      }
    }
    
    public boolean selectedPointMaker(int x, int y, ArrayList<Coordinate> used, int type)  // Creates a coordinate from a given x & y as long as the location is no currently in use
    {
      boolean flag = true;                           // Makes a variable to keep track of if the coordinate has been used
      Coordinate coord = new Coordinate(x,y,type);   // Creates a Coordinate that contains the given coordinates
      for (int w = 0; w < used.size(); w++)          // Loop that checks if the newly generated point is in use
      {
        if (coord.equals(used.get(w)))               // If the point has been already found, change the flag
          flag = false;
      }
      if (flag)                                      // If the point is not in the array...
      {
        used.add(coord);                             // Add the point to array of used coordinates
        if (type == 1)                               // If the point being made is the agent...
          AI.setCoord(coord);                        // let the AI know where it is starting from.
        return true;                                 // Success!
      }
      else                                           // Otherwise...
        return false;                                // Failure!
    }
    
    public boolean randomPointMaker(int rows,int columns, ArrayList<Coordinate> used,int type) // Creates a coordinate randomly that is not currently in use
    {
      Random generator = new Random();               // Creates random number generator
      boolean flag = true;                           // Makes a variable to keep track of if the coordinate has been used
      ArrayList<Coordinate> safeZone = new ArrayList<Coordinate>();                // Creates an array of locations that cannot be used
      int index = 0;                                 // Sets a varible to zero
      for (int i = 0; i <used.size();i++)            // Searches through the used array
      {
        if (used.get(i).getType() == 1)              // If the agent is found...
          index = i;                                 // record its index
        safeZone.add(used.get(i));                   // Add the location of the agent to the array of safe location
      }
      Coordinate player = used.get(index);           // Gets the agent's location
      ArrayList<Coordinate> surroundings = player.getSurroundings();   // Gets the agent' surroundings
      for (int i = 0; i <surroundings.size();i++)    // For each of the agents surroundings
      { 
        safeZone.add(surroundings.get(i));           // Add the location to the array of safe locations
      }
      
      int a = generator.nextInt(rows);               // Generates the x coordinate
      int b = generator.nextInt(columns);            // Generates the y coordinate
      Coordinate coord = new Coordinate(a,b,type);   // Creates a Coordinate that contains the generated coordinates
      for (int w = 0; w < safeZone.size(); w++)      // Loop that checks if the newly generated point is allowed to be used
      {
        if (coord.sameSpot(safeZone.get(w)))         // If the point has been already found, change the flag
          flag = false;
      }
      if (flag)                                      // If the point is not in the array...
      {
        used.add(coord);                             // Add the point to the array of used coordinates
        return true;                                 // Success!
      }
      else                                           // Otherwise...
        return false;                                // Failure!
    }
    
    
    
    public void paintComponent(Graphics g)           // This method describes how to graphically display the information
    {
      super.paintComponent(g);                       // Invokes its parents function
      Graphics2D g2 = (Graphics2D)g;                 // Creates a new graphics object
      g2.setColor(Color.black);                      // Sets the color to black
      for (int i = 50; i < ((columns * 50) + 50);)   // The grid are 50 pixel by 50 pixel squares
      {
        for (int k = 50; k < ((rows * 50) + 50);)              
        {
          Rectangle r = new Rectangle(i, k, 50, 50); // Make the square
          g2.draw(r);                                // Draw the square
          k += 50;                                   // Move to the next location
        }
        i += 50;                                     // Move to the next location 
      }
      
      for (int u = 0; u < used.size(); u++)          // Goes through the array of used locations and draw everything but the agent
      {
        Coordinate buffer = used.get(u);             // Get the location that we are currently working on
        
        if (buffer.getType() == 2)                   // If it is a pit...
        {
          g2.setColor(Color.black);                  // Set color to black
          h = buffer.getY() * 50;                    // Determine the square we are working in
          v = buffer.getX() * 50;
          Rectangle c = new Rectangle(h + 65,v + 65, 20, 20); // Create a 20 pixel by 20 pixel black square in the center of the current grid
          g2.fill(c);                                         // Fill in the square
        }
        else if (buffer.getType() == 3)              // Otherwise, if it is a wumpus...
        {
          g2.setColor(Color.red);                    // Set the color to red
          h = buffer.getY() * 50;                    // Determine the square we are working in 
          v = buffer.getX() * 50;
          Rectangle c = new Rectangle(h + 65,v + 65, 20, 20); // Create a 20 pixel by 20 pixel red square in the center of the current grid
          g2.fill(c);                                         // Fill in the square
        }
        
        else if (buffer.getType() == 4)              // Otherwise, if it is a breeze...
        {
          g2.setColor(Color.green);                  // Set the color to green
          h = buffer.getY() * 50;                    // Determine the square we are working in
          v = buffer.getX() * 50;
          for (int i = 1; i < 49; i= i + 2)          // For every other 1 pixel line in the current square...
          {
            Rectangle c = new Rectangle(h + 51,v + 51 + i, 49, 1);  // Make a green line...
            g2.fill(c);                                             // and fill it in
          }
        }
        else if (buffer.getType() == 5)               // Otherwise, if it is a gold...
        {
          g2.setColor(Color.yellow);                  // Set the color to yellow
          h = buffer.getY() * 50;                     // Determine the square we are working in
          v = buffer.getX() * 50;
          Rectangle c = new Rectangle(h + 70,v + 70, 10, 10);  // Create a 10 pixel by 10 pixel yellow square in the center of the current grid
          g2.fill(c);                                          // fill in the square
        }
        
        else if (buffer.getType() == 6)                // Otherwise, if it is a stench...
        {
          g2.setColor(Color.orange);                   // Set the color to orange
          h = buffer.getY() * 50;                      // Determine the square we are working in
          v = buffer.getX() * 50;
          for (int i = 0; i < 50; i= i + 2)            // For every other 1 pixel line in the current square...
          {
            Rectangle c = new Rectangle(h + 51,v + 51 + i, 49, 1);    // Make an orange line...
            g2.fill(c);                                               // and fill it in
          }
        }
      }
      for (int u = 0; u < used.size(); u++)             // Goes through the used array and draws the agent
      {
        Coordinate buffer = used.get(u);                // Get the location we are currently working on
        
        if (buffer.getType() == 1)                      // If the agent is found...
        {
          g2.setColor(Color.blue);                      // Set the color to blue
          h = buffer.getY() * 50;                       // Determine the square we are working in
          v = buffer.getX() * 50;
          Rectangle c = new Rectangle(h + 70,v + 70, 10, 10);         // Make a 10 pixel by 10 pixel blue square
          g2.fill(c);                                                 // fill in the square
        }
      }
    }
  }
  
  class TextPanel extends JPanel                        // This class is just a JPanel that displays text
  {
    private int message;                                // This is the score
    
    public TextPanel()                                  // Default constructor
    {
      message = 0;                                      // Score is 0
    }
    
    public TextPanel(int m)                             // Constructor where given m...
    {
      message = m;                                      // The score is set to m
    }
    
    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);                          // Calls the parent class's paint method
      Graphics2D g2 = (Graphics2D)g;                    // Creates a graphic
      message = MyFrame.getScore();                     // Gets the current score
      int width = getWidth();                           // Gets the width of the panel
      int height = getHeight();                         // Gets the height of the panel
      g2.setColor(Color.black);                         // Sets the color to black
      Font f1 = new Font("Serif", Font.BOLD, height);
      Font savedFont = g2.getFont();
      g2.setFont(f1);
      
      FontMetrics fm = g2.getFontMetrics(f1);
      int w1 = fm.stringWidth(Integer.toString(message));
      g2.drawString(Integer.toString(message),(width-w1)/2, height);
      
      g2.setFont(savedFont);
    }
  }
}


