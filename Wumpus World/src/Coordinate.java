import java.util.ArrayList;

public class Coordinate implements Comparable<Coordinate>
{
  public static final int CLEAR = 1;
  public static final int BREEZE = 2;
  public static final int PIT = BREEZE * 2;
  public static final int STENCH = 8;
  public static final int WUMPUS = STENCH * 2;
  public static final int GOLD = 32;

  private int x;
  private int y;
  private int type;
  private int threat;
  private static int rows =0;
  private static int columns=0;
  
  public static void updateGrid(int a, int b)
  {
    rows = a;
    columns = b;
  }
  
  public Coordinate(int a,int b) 
  { 
    x=a;
    y=b;
    type = 0;
    threat = 0;
  }
  
  public Coordinate(int a, int b, int c)
  {
    x=a;
    y=b;
    type=c;
    threat= 0;
  }

  public Coordinate(Coordinate other)
  {
    x = other.x;
    y = other.y;
    type = other.type;
    threat = other.threat;
  }
  
  //Technially, this is the first consructor
  public Coordinate softCopy()
  { 
    return new Coordinate(this.x,this.y);
  }
  
  public void goTo(Coordinate newCoord)
  {
    x = newCoord.getX();
    y = newCoord.getY();
  }
  
  public int getX()
  {
    return x;
  }
  
  public void setX(int q)
  {
    x = q;
  }
  
  public int getY()
  {
    return y;
  }
  
  public void setY(int q)
  {
    y = q;
  }
  
  public int getType()
  {
    return type;
  }
  
  public void setType(int q)
  {
    type = q;
  }
  
    public int getThreat()
  {
    return threat;
  }
  
  public void setThreat(int q)
  {
    threat = q;
  }
  
  public boolean s()
  {
    if ((x+1) >= rows)
      return false;
    else 
    {
      x++;
      return true;
    }
  }
  
  public boolean e()
  {
    if ((y+1) >= columns)
      return false;
    else 
    {
      y++;
      return true;
    }
  }
  
  public boolean n()
  {
    if ((x-1) < 0)
      return false;
    else 
    {
      x--;
      return true;
    }
  }
  public boolean w()
  {
    if ((y-1) < 0)
      return false;
    else 
    {
      y--;
      return true;
    }
  }
  
  public ArrayList<Coordinate> getSurroundings()
  {
    ArrayList<Coordinate> surroundings = new ArrayList<Coordinate>();
    Coordinate temp1 = this.softCopy();
    for (int i = 0; i < 4; i++)
    {
      boolean done = false;
      if (i == 0)
        done = temp1.n();
      else if (i == 1)
        done = temp1.e();
      else if (i == 2)
        done = temp1.s();
      else if (i == 3)
        done = temp1.w();
      if (done)
        surroundings.add(temp1);
      temp1 = this.softCopy();
    }
    return surroundings;
  }
  
  public boolean sameSpot(Object other)
  {
    if(other == null)
      return false;
    if(other == this)
      return true;
    if( !(other instanceof Coordinate) )
      return false;
    Coordinate otherCoordinate = (Coordinate)other;
    return (this.getX() == otherCoordinate.getX() && this.getY() == otherCoordinate.getY());
    
  }
  
  public int compareTo(Coordinate otherCoordinate) throws NullPointerException
  {
    if(otherCoordinate == null)
      throw new NullPointerException();
      int firstCheck = (this.getType()) - (otherCoordinate.getType());
      if (firstCheck == 0)
        return (this.y - otherCoordinate.y);
      else
        return firstCheck;
  }
  
  
  public boolean equals(Object other)
  {
    if(other == null)
      return false;
    if(other == this)
      return true;
    if( !(other instanceof Coordinate) )
      return false;
    Coordinate otherCoordinate = (Coordinate)other;
    return ((x == otherCoordinate.x) && (y == otherCoordinate.y));// && this.getType() == otherCoordinate.getType());
    
  }  

  public String toString()
  {
    return "(" + y + "," + x + ") : " + type;
  }
}
