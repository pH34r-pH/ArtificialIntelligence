import java.awt.Point;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class WeightedNode
{
    public Point Location;
    public int Index;
    public int Cost;
    public List<WeightedNode> Next;
    public WeightedNode Last;
    public int Step;
    public boolean[] Used;

    public WeightedNode(Point location, int cost, int step, boolean[] used, int index){
        Location = location;
        Cost = cost;
        Next = new ArrayList<WeightedNode>();
        Step = step;
        Used = used;
        Index = index;
    }

    public WeightedNode(Point location, int cost, boolean[] used, int index)
    {
        Location = location;
        Cost = cost;
        Next = new ArrayList<WeightedNode>();
        Used = used;
        Index = index;
    }

    public void Add(WeightedNode child)
    {
        child.Last = this;
        child.Step = Step + 1;
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