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
    setSize(1000, 600);                              // Sets the size of the frame
    map = new Grid(rows, columns, used);
    
    int index = 0;
      for (int i = 0; i <used.size();i++)
      {
        if (used.get(i).getType() == 1)
          index = i;
      }
    AI.setCoord(used.get(index));
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
      
      int index = 0;
      for (int i = 0; i <used.size();i++)
      {
        if (used.get(i).getType() == 1)
          index = i;
      }
      Coordinate player = used.get(index);
      boolean in = false;
      for (int f = 0; f < AI.getVisited().size(); f++)
      {
        if ( AI.getVisited().get(f).sameSpot(player))
          in = true;
      }
      if (!in)
        AI.addVisited(player.softCopy());
      else if (source == middle)
      {
        player.goTo(AI.findMove(used));
      }
      AI.setCoord(player);
      score--;
      for (int j = 0; j < used.size(); j++)
      {
        if (player.sameSpot(used.get(j)) && ((used.get(j).getType() == 2 )|| (used.get(j).getType() == 3))) 
          score = score -1000;
        else if (player.sameSpot(used.get(j)) && (used.get(j).getType() == 5 ))
          score = score +1000;
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
      selectedPointMaker(rows-1, 0, used,1);                              // Creates the starting point
      double holes =  ((rows*columns)*.125);                                        // Sets the number of holes to 1/8 of the area of the grid
      double wumpus = ((rows*columns)*.0625);                                       // Sets the number of wumpus to 1/16 of the area of the grid
      
      for (int x = 0; x < holes;)                                                   // Loop that generates holes
      {
        boolean done =randomPointMaker(rows,columns,used, 2);              // Creates holes
        if (done) 
          x++;
      }
      for (int y = 0; y < wumpus;)                                                 // Loop that generates wumpus
      {
        boolean done =randomPointMaker(rows,columns,used, 3);              // Creates wumpus
        if (done)
          y++;
      }
      for (int z = 0; z < 1;)                                                    // Loop that generates gold
      {
        boolean done =randomPointMaker(rows,columns,used, 5);            // Creates the gold
        if (done)
          z++;
      }
      addWarnings(used);                                                           // Creates the warning signs for generated hazards
    }
    
    public void addWarnings(ArrayList<Coordinate> used)
    {
      int originalSize = used.size();
      for (int i = 0;i < originalSize;i++)
      {
        Coordinate temp = used.get(i);
        int type = temp.getType();
        if (type==2 || type == 3)
          generateWarnings(temp,type, originalSize, used);
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
          if (toBeAdded.sameSpot(used.get(i)) && ((used.get(i).getType() == 2 )|| (used.get(i).getType() == 3)))
            atHazard = true;
        }
        if (!atHazard)
        {
          toBeAdded.setType(type*2);
          used.add(buffer.get(j));
        }
      }
    }
    
    public boolean selectedPointMaker(int x, int y, ArrayList<Coordinate> used, int type)
    {
      boolean flag = true;                           // Makes a variable to keep track of if the coordinate has been used
      Coordinate coord = new Coordinate(x,y,type);   // Creates a Coordinate that contains the given coordinates
      for (int w = 0; w < used.size(); w++)          // Loop that checks if the newly generated point is in use
      {
        if (coord.equals(used.get(w)))               // If the point has been already found, change the flag
          flag = false;
      }
      if (flag)                                      // If the point is not in the array/...
      {
        used.add(coord);                             // Add the point to array of used coordinates   
        return true;
      }
      else
        return false;
    }
    
    public boolean randomPointMaker(int rows,int columns, ArrayList<Coordinate> used,int type)
    {
      Random generator = new Random();               // Creates random number generator
      boolean flag = true;                           // Makes a variable to keep track of if the coordinate has been used
      ArrayList<Coordinate> safeZone = new ArrayList<Coordinate>();                // Creates an array of locations that cannot be used
      int index = 0;    
      for (int i = 0; i <used.size();i++)
      {
        if (used.get(i).getType() == 1)
          index = i; 
        safeZone.add(used.get(i));
      }
      Coordinate player = used.get(index);           // Finds the players spawn point
      ArrayList<Coordinate> surroundings = player.getSurroundings();  
      for (int i = 0; i <surroundings.size();i++)
      { 
        safeZone.add(surroundings.get(i));
      }
      
      int a = generator.nextInt(rows);               // Generates the x coordinate
      int b = generator.nextInt(columns);            // Generates the y coordinate
      Coordinate coord = new Coordinate(a,b,type);   // Creates a Coordinate that contains the generated coordinates
      for (int w = 0; w < safeZone.size(); w++)      // Loop that checks if the newly generated point is allowed to be used
      {
        if (coord.sameSpot(safeZone.get(w)))            // If the point has been already found, change the flag
          flag = false;
      }
      if (flag)                                      // If the point is not in the array/...
      {
        used.add(coord);                             // Add the point to the array of used coordinates
        return true;   
      }
      else
        return false;
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
        if (buffer.getType() == 4)
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
        else if (buffer.getType() == 6)
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
        
        if (buffer.getType() == 1)
        {
          g2.setColor(Color.blue);
          h = buffer.getY() * 50;
          v = buffer.getX() * 50;
          Rectangle c = new Rectangle(h + 70,v + 70, 10, 10);
          g2.fill(c);
        }
        else if (buffer.getType() == 2)
        {
          g2.setColor(Color.black);
          h = buffer.getY() * 50;
          v = buffer.getX() * 50;
          Rectangle c = new Rectangle(h + 70,v + 70, 10, 10);
          g2.fill(c);
        }
        else if (buffer.getType() == 3)
        {
          g2.setColor(Color.red);
          h = buffer.getY() * 50;
          v = buffer.getX() * 50;
          Rectangle c = new Rectangle(h + 70,v + 70, 10, 10);
          g2.fill(c);
        }
        else if (buffer.getType() == 5)
        {
          g2.setColor(Color.yellow);
          h = buffer.getY() * 50;
          v = buffer.getX() * 50;
          Rectangle c = new Rectangle(h + 70,v + 70, 10, 10);  // Center of square
          g2.fill(c);
        }
      }
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


