import java.awt.Point;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * University of Central Florida
 * CAP4630 - Fall 2018
 * @author : Tyler Harbin-Giuntoli
 */

public class WeightedNode implements Comparable<WeightedNode>
{
    public Point Location;
    public int Index;
    public int Cost;
    public int PathCost;
    public List<WeightedNode> Next;
    public WeightedNode Last = null;
    public int Step;
    public boolean[] Used;

    public WeightedNode(Point location, int cost, int step, boolean[] used, int index){
        Location = location;
        Cost = cost;
        Next = new ArrayList<WeightedNode>();
        Step = step;
        Used = used;
        Index = index;
        PathCost = cost;
    }

    public WeightedNode(Point location, int cost, boolean[] used, int index)
    {
        Location = location;
        Cost = cost;
        PathCost = cost;
        Next = new ArrayList<WeightedNode>();
        Used = used;
        Index = index;
    }

    public void Add(WeightedNode child)
    {
        child.Last = this;
        child.Step = Step + 1;
        child.PathCost = PathCost + child.Cost;
        Next.add(child);
    }

    public void Print()
    {
        System.out.printf("[(%d,%d),%d]", Location.x, Location.y, Cost);
    }

    public void PrintChain()
    {
        if(Last == null)
        {
            return;
        }
        Last.PrintChain();
        Print();
    }

    public ArrayList<Point> GetChain()
    {
        ArrayList<Point> Path = (Last == null) ? new ArrayList<Point>() : Last.GetChain();
        Path.add(Location);
        return Path;
    }

    // Comparable implementation
    public int compareTo(WeightedNode n)
    {
        return this.PathCost - n.PathCost;
    }
}