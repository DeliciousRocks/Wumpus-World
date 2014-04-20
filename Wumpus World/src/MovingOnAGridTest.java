import java.awt.Container;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;

public class MovingOnAGridTest
{
 public static void main(String[] args)
    {
    MyFrame frame = new MyFrame();
    frame.setTitle("Movement Tester");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //frame.setResizable(false);
    frame.setVisible(true);
   }

}

class MyFrame extends JFrame
{
 private int [][] location;
 private JPanel buttonPanel; 
 private JButton up, down, left, right;
 private Grid map;
 private Info legend;
 private Controls directionals;

 public MyFrame()
 {
  int rows = 5;
  int columns = 5;
  location = new int [rows][columns];
  setSize(800,600);
  map = new Grid(rows, columns);
  legend = new Info();
  directionals = new Controls();
  getContentPane().add(map, "Center");
  getContentPane().add(legend, "West");
  
  buttonPanel = new JPanel();
  buttonPanel.setLayout( new GridLayout(4,1,0,0));
  buttonPanel.setBackground(Color.blue);
  ActionListener listener = new Choice();
  
  up = new JButton("UP");
  up.addActionListener(listener);
  buttonPanel.add(up);
  
  down = new JButton("DOWN");
  buttonPanel.add(down);
  down.addActionListener(listener);
  
  left = new JButton("LEFT");
  buttonPanel.add(left);
  left.addActionListener(listener);
  
  right = new JButton("RIGHT");
  buttonPanel.add(right);
  right.addActionListener(listener);
  
  getContentPane().add(buttonPanel, "East");
  
 
  
  

 }


private class  Choice implements ActionListener
{
        public void actionPerformed(ActionEvent event)
        {
         Object source = event.getSource();
         if(source == up)
          {
           if (map.getVerticalPostion() > map.getCornerY()) 
           map.moveVertical(-50);
          }
          else if (source == down)
          {
            if (map.getVerticalPostion() < (map.getRows() * 50)) 
            map.moveVertical(50);
          }
          else if (source == left)
          {
            if (map.getHorizontalPositon() > map.getCornerX())
            map.moveHorizontal(-50);
          }
          else if (source ==  right)
          {
            if (map.getHorizontalPositon() < (map.getColumns() * 50))
            map.moveHorizontal(50);
          }
        }
}

}
class Info extends JPanel
{
  public Info()
  {
   setSize(100,600); 
  }
  
  public void paintComponent(Graphics g)
 {
  super.paintComponent(g);
  Graphics2D g2 = (Graphics2D)g;
  setBackground(Color.blue);
 }
  
  
}


 
 


class Grid extends JPanel
{
 private int rows;
 private int columns;
 private int h;
 private int v;
 private int cornerX;
 private int cornerY;

 public Grid(int r, int c)
 {
  setSize(60 * r, 60 * c);
  cornerX = (getWidth() - (columns * 50))/2;
  cornerY = cornerX/2;
  h = cornerX;
  v = cornerY;
  rows = r;
  columns = c;
 }
 
 public int getHorizontalPositon()
 {
   return h;
 }
 
 public int getVerticalPostion()
 {
   return v;
 }
 
 public int getCornerX()
 {
   return cornerX;
 }
 
 public int getCornerY()
 {
   return cornerY;
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
  
  int width  = getWidth();
  int height = getHeight();
  
  int rWidth = (columns * 50);
  int rHeight = rWidth/2;
  
  cornerX = (width-rWidth)/2;
  cornerY = (height-rHeight)/2;

  
  for (int i = cornerX; i < ((columns * 50)+50);)
  {
    for (int k = cornerY; k < ((rows * 50)+50);)
    {
     Rectangle r = new Rectangle(i, k, 50, 50);
     g2.draw(r);
     k += 50;
    }
    i += 50;
  }
  g2.setColor(Color.blue);
  Rectangle c = new Rectangle(h + 20,v + 20, 10, 10);
  g2.fill(c);
     


 }



}


