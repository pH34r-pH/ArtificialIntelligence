import java.awt.Point;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class WeightedNode
{
    public Point Location;
    public int Cost;
    public List<WeightedNode> Next;
    public WeightedNode Last;
    public int Step;
    public boolean[] Used;
    public int[][] CostMap;

    public WeightedNode(Point location, int cost, int step, boolean[] used, int[][] costMap){
        Location = location;
        Cost = cost;
        Next = new ArrayList<WeightedNode>();
        Step = step;
        Used = used;
        CostMap = costMap;
    }

    public WeightedNode(Point location, int cost, boolean[] used)
    {
        Location = location;
        Cost = cost;
        Next = new ArrayList<WeightedNode>();
        Used = used;
    }

    public void Add(WeightedNode child)
    {
        child.Step = Step + 1;
        child.CostMap = CostMap;
        child.Last = this;
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
            Print();
        }
        else
        {
            Last.PrintChain();
            Print();
        }
    }

    public int PathToHereCost()
    {
        if(Last == null)
        {
            return Cost;
        }
        else
        {
            return Cost + Last.PathToHereCost();
        }
    }

}