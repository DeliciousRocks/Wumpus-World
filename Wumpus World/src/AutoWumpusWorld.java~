import java.awt.Container;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;

//Coded by: Walter Squires

public class AutoWumpusWorld
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
  private Coordinate playerLocation;
  private ArrayList<Coordinate> used;
  private JPanel buttonPanel;
  private TextPanel scoreBoard; 
  private JButton up, down, left, right, upLeft, upRight, middle, downLeft, downRight;
  private Grid map;
  private Controls directionals;
  private static int score;
  
  
  public MyFrame()
  {
    int rows = 4;                              // Sets the number of rows on the grid
    int columns = 4;                           // Sets the number of columns on the grid
    Coordinate.updateGrid(rows, columns);      // Lets the Coordinate class know what the size of the grid is
    used = new ArrayList();                    // Creates an array list to keep track of all used coordinat
    setSize(500, 500);                              // Sets the size of the frame
    map = new Grid(rows, columns, used);
    
    AI.setCoord(playerLocation);
    AI.findMove(used);
                      
    directionals = new Controls();
    
    Container contentPane = getContentPane();
    scoreBoard = new TextPanel(score);
    contentPane.add(scoreBoard, "South");
    contentPane.add(map, "Center");
    
    
    buttonPanel = new JPanel();
    buttonPanel.setLayout( new GridLayout(1,1,0,0));
    ActionListener listener = new Choice();
    
    middle = new JButton();
    middle.addActionListener(listener);
    buttonPanel.add(middle);

    
    contentPane.add(buttonPanel, "East");
  }
  
  public static int getScore()
  {
    return score;
  }
  
  
  private class  Choice implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      Object source = event.getSource();
      
      if (source == middle) {
        Coordinate nextMove = AI.findMove(used);
        playerLocation.goTo(nextMove);
      }

      // If there is something else at the same location of
      // the player, we want the AI to know that. Set the
      // coordinate to be the one in used. If it's empty,
      // it won't be in used, and we'll give it the player
      // location, which has nothing but an x and y value.
      if (used.contains(playerLocation))
        AI.setCoord(used.get(used.indexOf(playerLocation)));
      else
        AI.setCoord(playerLocation);

      score--;
      for (int j = 0; j < used.size(); j++)
      {
        if (playerLocation.sameSpot(used.get(j)) && ((used.get(j).getType() == Coordinate.PIT )|| (used.get(j).getType() == Coordinate.WUMPUS))) {
          score = score -1000;
        } else if (playerLocation.sameSpot(used.get(j)) && ((used.get(j).getType() & Coordinate.GOLD) > 0)) {
          score = score +1000;
          JOptionPane.showMessageDialog(null, "Congratulations!", "Success", JOptionPane.PLAIN_MESSAGE);
        }
        repaint();
      }
    }
  }
  
  
  
  class Grid extends JPanel
  {
    private int rows;
    private int columns;
    private int h;
    private int v;
    private ArrayList<Coordinate> used;
    
    public Grid(int r, int c, ArrayList<Coordinate> u)
    {
      rows = r;
      columns = c;     
      used = u;
      playerLocation = new Coordinate(rows-1, 0);
      double holes =  ((rows*columns)*.125);                                        // Sets the number of holes to 1/8 of the area of the grid
      double wumpus = ((rows*columns)*.0625);                                       // Sets the number of wumpus to 1/16 of the area of the grid

      for (int x = 0; x < holes;)                                                   // Loop that generates holes
      {
        boolean done = randomPointMaker(rows,columns,used, Coordinate.PIT);              // Creates holes
        if (done) 
          x++;
      }
      for (int y = 0; y < wumpus;)                                                 // Loop that generates wumpus
      {
        boolean done = randomPointMaker(rows,columns,used, Coordinate.WUMPUS);              // Creates wumpus
        if (done)
          y++;
      }
      for (int z = 0; z < 1;)                                                    // Loop that generates gold
      {
        boolean done = randomPointMaker(rows,columns,used, Coordinate.GOLD);            // Creates the gold
        if (done)
          z++;
      }
      addWarnings(used);                                                           // Creates the warning signs for generated hazards
    }
    
    public void addWarnings(ArrayList<Coordinate> used)
    {
      int originalSize = used.size();
      for (int i = 0; i < originalSize; i++)
      {
        Coordinate temp = used.get(i);
        int type = temp.getType();
        if (((type & Coordinate.PIT) > 0) || ((type & Coordinate.WUMPUS) > 0))
          generateWarnings(temp, type, originalSize, used);
      }
    }
    
    public void generateWarnings(Coordinate temp, int type, int originalSize, ArrayList<Coordinate> used)
    {
      ArrayList<Coordinate> buffer = temp.getSurroundings();
      for (int j = 0; j < buffer.size(); j++)
      {
        Coordinate toBeAdded = buffer.get(j);
        boolean atHazard = false;
        for (int i = 0; i < originalSize; i++)
        {
          if (toBeAdded.sameSpot(used.get(i)) && (((used.get(i).getType() & Coordinate.PIT) > 0) || ((used.get(i).getType() & Coordinate.WUMPUS) > 0)))
            atHazard = true;
        }
        if (!atHazard)
        {
          toBeAdded.setType(type/2);
          if (used.contains(toBeAdded)) {
            Coordinate usedCoord = used.get(used.indexOf(toBeAdded));
            usedCoord.setType(usedCoord.getType() | toBeAdded.getType());
          } else {
            used.add(toBeAdded);
          }
        }
      }
    }
    
    public boolean selectedPointMaker(int x, int y, ArrayList<Coordinate> used, int type)
    {
      Coordinate coord = new Coordinate(x,y,type);   // Creates a Coordinate that contains the given coordinates
      if (used.contains(coord))
        return false;
      used.add(coord);                             // Add the point to array of used coordinates   
      return true;
    }
    
    public boolean randomPointMaker(int rows, int columns, ArrayList<Coordinate> used,int type)
    {
      Random generator = new Random();               // Creates random number generator
      ArrayList<Coordinate> safeZone = new ArrayList<Coordinate>();                // Creates an array of locations that cannot be used

      // Don't allow hazards to be added at the same spot
      // as the player spawns.
      safeZone.add(playerLocation);

      // Add all coordinates in used to safe zone so
      // that we don't overwrite existing coordaintes.
      for (Coordinate coord : used)
        safeZone.add(coord);

      // Don't allow hazards to be added to the player's immediate
      // surroudings.
      ArrayList<Coordinate> surroundings = playerLocation.getSurroundings();
      for (Coordinate coord : surroundings)
        safeZone.add(coord);
      
      int a = generator.nextInt(rows);               // Generates the x coordinate
      int b = generator.nextInt(columns);            // Generates the y coordinate
      Coordinate coord = new Coordinate(a,b,type);   // Creates a Coordinate that contains the generated coordinates

      if (safeZone.contains(coord))
        return false;

      used.add(coord);
      return true;
    }
    
    
    
    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setColor(Color.black);
      for (int i = 50; i < ((columns * 50) + 50);)              // Draws the grid
      {
        for (int k = 50; k < ((rows * 50) + 50);)               // The grid is made of squares
        {
          Rectangle r = new Rectangle(i, k, 50, 50);
          g2.draw(r);
          k += 50;
        }
        i += 50;
      }
      
      for (int u = 0; u < used.size(); u++)
      {
        Coordinate buffer = used.get(u);
        if ((buffer.getType() & Coordinate.BREEZE) > 0)
        {
          g2.setColor(Color.green);
          h = buffer.getY() * 50;
          v = buffer.getX() * 50;
          for (int i = 1; i < 49; i= i + 2)                         // Makes stripes
          {
            Rectangle c = new Rectangle(h + 51,v + 51 + i, 49, 1);  
            g2.fill(c);
          }
        }
        if ((buffer.getType() & Coordinate.STENCH) > 0)
        {
          g2.setColor(Color.orange);
          h = buffer.getY() * 50;
          v = buffer.getX() * 50;
          for (int i = 0; i < 50; i= i + 2)                         // Makes stripes
          {
            Rectangle c = new Rectangle(h + 51,v + 51 + i, 49, 1);  
            g2.fill(c);
          }
        }
      }

      for (int u = 0; u < used.size(); u++)
      {
        Coordinate buffer = used.get(u);
        
        if ((buffer.getType() & Coordinate.PIT) > 0)
        {
          g2.setColor(Color.black);
          h = buffer.getY() * 50;
          v = buffer.getX() * 50;
          Rectangle c = new Rectangle(h + 70,v + 70, 10, 10);
          g2.fill(c);
        }
        else if ((buffer.getType() & Coordinate.WUMPUS) > 0)
        {
          g2.setColor(Color.red);
          h = buffer.getY() * 50;
          v = buffer.getX() * 50;
          Rectangle c = new Rectangle(h + 70,v + 70, 10, 10);
          g2.fill(c);
        }
        else if ((buffer.getType() & Coordinate.GOLD) > 0)
        {
          g2.setColor(Color.yellow);
          h = buffer.getY() * 50;
          v = buffer.getX() * 50;
          Rectangle c = new Rectangle(h + 70,v + 70, 10, 10);  // Center of square
          g2.fill(c);
        }
      }

      // Draw the player on the board.
      g2.setColor(Color.blue);
      h = playerLocation.getY() * 50;
      v = playerLocation.getX() * 50;
      Rectangle c = new Rectangle(h + 70,v + 70, 10, 10);
      g2.fill(c);


    }
  }
  
  class TextPanel extends JPanel
  {
    private int message;
    
    public TextPanel()
    {
      message = 0;
    }
    
    public TextPanel(int m)
    {
      message = m;
    }
    
    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      message = MyFrame.getScore();
      int width = getWidth();
      int height = getHeight();
      g2.setColor(Color.black);
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


