import java.awt.Container;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;

/*Coded by: Walter Squires
 * Main Algorithm:
 * 1) Create a frame with contain a grid, and a panel of buttons, and a two-dimentional array equal to the 
 * -Each button corresponds to a direction
 * -The grid's size "can" be changed, but should be left at 9*9 so it fits nicely in the frame
 * -Place a blue rectangle at the center of the grid, this represents the player
 * -Place green rectangles at the center of any other location
 * --The max number of rectangles is columns * rows/ 2
 * --The number and location of the recatngles is random
 * --The green rectangles represent trees
 * -Place a black rectangle at the center of any location other than the player start
 * --This can overridde a tree
 * --The black rectangle represents the goal
 * -All things placed on the grid have a value stored in the array to represent its location: 1 is the player, 2 is a tree, 3 is the goal
 * 2)Wait for a button to be pressed 
 * -Check that the location to be moved to does not have a tree in it and that it is within the grid
 * --If these conditions are satisfied, move the player to the new location
 * 3)If the player is in the same location as the goal, display a message saying they win.
 * 
 */


public class MovingOnAGrid
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
 private int [][] location;
 private JPanel buttonPanel; 
 private JButton up, down, left, right, upLeft, upRight, middle, downLeft, downRight;
 private Grid map;
 private Controls directionals;

 public MyFrame()
 {
  int rows = 9;
  int columns = 8;
  location = new int [rows][columns];
  for (int y = 0; y < rows; y++)
  {
    for (int x = 0; x < columns; x++)
    {
      location[y][x] = 0;
    }
  }
  
  setSize(1000, 600);
  map = new Grid(rows, columns, location);
  directionals = new Controls();
  getContentPane().add(map, "Center");
  
  buttonPanel = new JPanel();
  buttonPanel.setLayout( new GridLayout(3,3,0,0));
  buttonPanel.setBackground(Color.blue);
  ActionListener listener = new Choice();
  
  upLeft = new JButton("UP-LEFT");
  upLeft.addActionListener(listener);
  buttonPanel.add(upLeft);
  
  up = new JButton("UP");
  up.addActionListener(listener);
  buttonPanel.add(up);
  
  upRight = new JButton("UP-RIGHT");
  upRight.addActionListener(listener);
  buttonPanel.add(upRight);
   
  left = new JButton("LEFT");
  left.addActionListener(listener);
  buttonPanel.add(left); 
  
  middle = new JButton();
  buttonPanel.add(middle);
  
  right = new JButton("RIGHT");
  right.addActionListener(listener);
  buttonPanel.add(right);
  
  downLeft = new JButton("DOWN-LEFT");
  downLeft.addActionListener(listener);
  buttonPanel.add(downLeft);
  
  down = new JButton("DOWN");
  down.addActionListener(listener);
  buttonPanel.add(down);
  
  downRight = new JButton("DOWN-RIGHT");
  downRight.addActionListener(listener);
  buttonPanel.add(downRight);
  
  getContentPane().add(buttonPanel, "East");
  
 
  
  

 }


private class  Choice implements ActionListener
{
        public void actionPerformed(ActionEvent event)
        {
         Object source = event.getSource();
         if(source == up)
          {
           int y = map.getVerticalPostion()/50;
           int x = map.getHorizontalPositon()/50;
           if (location[y][x] == 1 && y-1 >= 0 && (location[y-1][x] == 0 || location [y-1][x] == 3))
           {
             if (location [y-1][x] == 3)
               System.out.println("You Win!");
             location[y][x] = 0;
             location[y-1][x] = 1;
             repaint();
           }
          }
         else if(source == upLeft)
          {
           int y = map.getVerticalPostion()/50;
           int x = map.getHorizontalPositon()/50;
           if (location[y][x] == 1 && y-1 >= 0 && x-1 >=0 && (location[y-1][x-1] == 0 || location [y-1][x-1] == 3))
           {
             if (location [y-1][x-1] == 3)
               System.out.println("You Win!");
             location[y][x] = 0;
             location[y-1][x-1] = 1;
             repaint();
           }
          }
          else if(source == upRight)
          {
           int y = map.getVerticalPostion()/50;
           int x = map.getHorizontalPositon()/50;
           if (location[y][x] == 1 && y-1 >= 0 && x+1 < map.getColumns() && (location[y-1][x+1] == 0 || location [y-1][x+1] == 3))
           {
             if (location [y-1][x+1] == 3)
               System.out.println("You Win!");
             location[y][x] = 0;
             location[y-1][x+1] = 1;
             repaint();
           }
          }
          else if (source == down)
          {
           int y = map.getVerticalPostion()/50;
           int x = map.getHorizontalPositon()/50;
           if (location[y][x] == 1 && y+1 < map.getRows() && (location[y+1][x] == 0 || location [y+1][x] == 3))
           {
             if (location [y+1][x] == 3)
               System.out.println("You Win!");
             location[y][x] = 0;
             location[y+1][x] = 1;
             repaint();
           }
          }
          else if(source == downLeft)
          {
           int y = map.getVerticalPostion()/50;
           int x = map.getHorizontalPositon()/50;
           if (location[y][x] == 1 && y+1 < map.getRows() && x-1 >= 0 && (location[y+1][x-1] == 0 || location [y+1][x-1] == 3))
           {
             if (location [y+1][x-1] == 3)
               System.out.println("You Win!");
             location[y][x] = 0;
             location[y+1][x-1] = 1;
             repaint();
           }
          }
          else if(source == downRight)
          {
           int y = map.getVerticalPostion()/50;
           int x = map.getHorizontalPositon()/50;
           if (location[y][x] == 1 && y+1 < map.getRows() && x+1 < map.getColumns() && (location[y+1][x+1] == 0 || location [y+1][x+1] == 3))
           {
             if (location [y+1][x+1] == 3)
               System.out.println("You Win!");
             location[y][x] = 0;
             location[y+1][x+1] = 1;
             repaint();
           }
          }
          else if (source == left)
           {
           int y = map.getVerticalPostion()/50;
           int x = map.getHorizontalPositon()/50;
           if (location[y][x] == 1 && x-1 >= 0 && (location[y][x-1] == 0 || location [y][x-1] == 3))
           {
              if (location [y][x-1] == 3)
               System.out.println("You Win!");
             location[y][x] = 0;
             location[y][x-1] = 1;
             repaint();
           }
          }
         else if (source ==  right)
           {
           int y = map.getVerticalPostion()/50;
           int x = map.getHorizontalPositon()/50;
           if (location[y][x] == 1 && x+1 < map.getColumns() && (location[y][x+1] == 0 || location [y][x+1] == 3))
           {
              if (location [y][x+1] == 3)
               System.out.println("You Win!");
             location[y][x] = 0;
             location[y][x+1] = 1;
             repaint();
           }
          }
          }
        }
}



class Grid extends JPanel
{
 private int rows;
 private int columns;
 private int h;
 private int v;
 private int [][]location;

 public Grid(int r, int c, int [][]l)
 {
  location = l;
  rows = r;
  columns = c;
  location[rows/2][columns/2] = 1;
  Random generator = new Random();
  int numberOfTrees = generator.nextInt( (rows * columns)/2);
  for (int i = 0; i < numberOfTrees; i ++)
  {
    int a = generator.nextInt(rows);
    int b = generator.nextInt(columns);
    if (a != (rows/2) && b != (columns/2))
          location[a][b] = 2;
  }
  int a = generator.nextInt(rows);
  int b = generator.nextInt(columns);
  if (a == (rows/2) && b == (columns/2))
  {
    a +=1;
    b+= 1;
      
  }
  location[a][b] = 3;
 }
 
 public int getHorizontalPositon()
 {
   return h;
 }
 
 public int getVerticalPostion()
 {
   return v;
 }
 
 public int getRows()
 {
   return rows;
 }
 
 public int getColumns()
 {
   return columns;
 }

 public void moveHorizontal(int newLocation)
 {
   h += newLocation;
   repaint();
 }
 
  public void moveVertical(int newLocation)
 {
   v += newLocation;
   repaint();
 }
  
 public void paintComponent(Graphics g)
 {
  super.paintComponent(g);
  Graphics2D g2 = (Graphics2D)g;
  g2.setColor(Color.black);
  for (int i = 50; i < ((columns * 50) + 50);)
  {
    for (int k = 50; k < ((rows * 50) + 50);)
    {
     Rectangle r = new Rectangle(i, k, 50, 50);
     g2.draw(r);
     k += 50;
    }
    i += 50;
  }
  
  for (int y = 0; y < rows; y++)
  {
    for (int x = 0; x < columns; x++)
    {
      if (location[y][x] == 1)
      {
        g2.setColor(Color.blue);
        h = x * 50;
        v = y * 50;
        Rectangle c = new Rectangle(h + 70,v + 70, 10, 10);
        g2.fill(c);
      }  
      
      else if (location[y][x] == 2)
      {
        g2.setColor(Color.green);
        int h2 = x * 50;
        int v2 = y * 50;
        Rectangle t = new Rectangle(h2 + 70,v2 + 70, 10, 10);
        g2.fill(t);
      }
      else if (location[y][x] == 3)
      {
        g2.setColor(new Color(50,50,50));
        int h3 = x * 50;
        int v3 = y * 50;
        Rectangle w = new Rectangle(h3 + 70,v3 + 70, 10, 10);
        g2.fill(w);                
      }
    }
  }
 }
}


