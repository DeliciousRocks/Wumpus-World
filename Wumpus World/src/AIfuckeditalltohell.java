import java.util.ArrayList;
import java.util.TreeMap;
//import java.util.TreeSet;
import java.util.Stack;
import javax.swing.*;

public class AIfuckeditalltohell 
{
  private static ArrayList<Coordinate> unexplored = new ArrayList<Coordinate>();
  private static ArrayList<Coordinate> priorityMoves = new ArrayList<Coordinate>();
  private static ArrayList<Coordinate> visited = new ArrayList<Coordinate>();
  private static ArrayList<Coordinate> possibleHazards = new ArrayList<Coordinate>();
  private static ArrayList<Coordinate> hazards = new ArrayList<Coordinate>();
  private static Coordinate location;

  public static void generateFog(int a, int b)
  {
    for (int s = 0; s <= a; s++)
    {
      for (int t = 0; t <=b; t++)
        unexplored.add(new Coordinate(s,t));
    }
  }
  
  public static void setCoord(Coordinate x) 
  {
    location = x;
    Coordinate newCoord = new Coordinate(x);
    // Remove it from priority list because those
    // should only be unvisited coordinates.
    priorityMoves.remove(newCoord);

    // If we know it's a hazard, we definitely don't
    // want to go there. We always pick from priority,
    // used, and potential hazards, so remove make sure
    // it is not in any of those lists. Add to hazard
    // list so we don't add it to visited later.
    if (isHazardAtCoordinate(newCoord)) 
    {
      hazards.add(newCoord);
      possibleHazards.remove(newCoord);
      return;
    }

    // We don't want duplicate visited coordinates.
    if (!visited.contains(newCoord) && !hazards.contains(newCoord)) {
      ArrayList<Coordinate> moves = newCoord.getSurroundings();
      // If there is no warning at this coordinate, make all
      // non-visited adjacent coordinates priority.
      // Otherwise, add it to the possible hazards list or,
      // if it's already in there, increase its threat level.
      if (!isHazardWarningAtCoordinate(location)) {
        for (Coordinate m : moves) {
          if (!priorityMoves.contains(m) && !visited.contains(m))
            priorityMoves.add(m);
          // The location is clear, so remove it cannot be a possible
          // hazard. (If it's not in there, this line will do nothing).
          possibleHazards.remove(m);
        }
      } else 
      {
        for (Coordinate m : moves) 
        {
          if (!priorityMoves.contains(m) && !visited.contains(m)) 
          {
            if (possibleHazards.contains(m))
            {
              Coordinate coord = possibleHazards.get(possibleHazards.indexOf(m));
              coord.setThreat(coord.getThreat() + 1);
            } else
            {
              Coordinate coord = new Coordinate(m);
              m.setThreat(1);
              possibleHazards.add(m);
            }
          }
        }
      }
    visited.add(newCoord);
    }
  }
  
  public static ArrayList<Coordinate> getVisited()
  {
    return visited;
  }

  public static boolean isHazardWarningAtCoordinate(Coordinate coord)
  {
    boolean hasBreeze = (coord.getType() & Coordinate.BREEZE) > 0;
    boolean hasStench = (coord.getType() & Coordinate.STENCH) > 0;
    return hasBreeze || hasStench;
  }
  
  public static boolean isHazardAtCoordinate(Coordinate coord)
  {
    boolean hasPit = (coord.getType() & Coordinate.PIT) > 0;
    boolean hasWumpus = (coord.getType() & Coordinate.WUMPUS) > 0;
    return hasPit || hasWumpus;
  }

  public static int getLowestThreat()
  {
    // Set min threat to an arbitrary large number so that we know
    // there will be a value in the map that is less than it. If we
    // can, it'd be better to use another approach, but this is what
    // I thought of.
    int minThreat = 10000;
    for (Coordinate h : possibleHazards)
      if (h.getThreat() < minThreat)
        minThreat = h.getThreat();
    return minThreat;
  }

  public static ArrayList<Coordinate> getCoordinatesWithLowestThreat()
  {
    int minThreat = getLowestThreat();
    ArrayList<Coordinate> minThreatCoords = new ArrayList<Coordinate>();
      for (Coordinate h : possibleHazards)
        if (h.getThreat() == minThreat)
          minThreatCoords.add(h);
      return minThreatCoords;
  }

  public static Coordinate getRandomVisitedCoordinateFromCoordinates(ArrayList<Coordinate> moves)
  {
    ArrayList<Coordinate> possibleVisitedMoves = new ArrayList<Coordinate>();
    for (Coordinate m : moves) {
      if (visited.contains(m))
        possibleVisitedMoves.add(m);
    }
    int index = (int)(Math.random() * possibleVisitedMoves.size());
    return possibleVisitedMoves.get(index);
  }

  public static Coordinate findMove(ArrayList<Coordinate> used)
  {
    // If we're on the gold, stay.
    if ((location.getType() & Coordinate.GOLD) > 0)
      return location;

    System.out.println("Priority moves");
    for (Coordinate m : priorityMoves)
      System.out.println(m);
    System.out.println("\nPossible hazards");
    for (Coordinate m : possibleHazards)
      System.out.println(m + " -- " + m.getThreat());
    System.out.println("\nHazards");
    for (Coordinate m : hazards)
      System.out.println(m);
    System.out.println("\nVisited");
    for (Coordinate m : visited)
      System.out.println(m);
    System.out.println("***********************************\n\n");

    ArrayList<Coordinate> moves = location.getSurroundings();
    for (Coordinate bad : moves)
    {
      for (Coordinate reallyBad : hazards)
      {
        if (bad.sameSpot(reallyBad))
          moves.remove(bad);
      }
    }
    
    boolean noSafe= false;
    for (Coordinate check :moves)
    {
      for(Coordinate doubleCheck : possibleHazards)
      {
        if (check.sameSpot(doubleCheck))
          noSafe= true;
      }   
    }

    // If there are still known clear spaces we haven't
    // visited, we're not taking risks.
    if (!priorityMoves.isEmpty())// && noSafe)
    {
      // Go to the closest space to a priority space
      int lIndex = 0;
      double bestDis = priorityMoves.get(0).distance(moves.get(lIndex));
      for (int n = 1; n < moves.size();n++)
      {
        if (priorityMoves.get(0).distance(moves.get(n)) < bestDis)
        {
          lIndex = n;
          bestDis = priorityMoves.get(0).distance(moves.get(n));
        }
      }
      return moves.get(lIndex);
    } 
    else 
    {
      // If there are no known clear spaces, we'll have to take a risk, but we
      // want it to be as small as possible. Find the coordinates with the lowest
      // threat level. If we're adjacent, go there. Otherwise, take a random
      // visited space.
      ArrayList<Coordinate> minThreatCoords = getCoordinatesWithLowestThreat();
      for (Coordinate m : moves) {
        if (minThreatCoords.contains(m))
        {
          return m;
        }
      }
      Coordinate newCoord = new Coordinate(location);
      boolean worked =newCoord.n();
      if(worked)
        return newCoord;
      else
      {
        worked = newCoord.e();
        if (worked)
          return newCoord;
        else
        {
          worked = newCoord.s();
          if (worked)
            return newCoord;
          else
          {
            newCoord.w();
            return newCoord;
          }
        }
      }
    }
  }
   
  public static <T extends Comparable<T> >
    void selectionSort(ArrayList<T> table)
  {
    for(int target = 0; target < table.size() - 1; target++)
    {
      int smallest = target;
      for(int k = target + 1; k < table.size(); k++)
      {
        int n = table.get(smallest).compareTo(table.get(k));
        if(n > 0)
          smallest = k;
      }
      if(smallest != target)
      {
        T temp = table.get(target);
        table.set(target, table.get(smallest));
        table.set(smallest, temp);
      }
    }
  }
}
