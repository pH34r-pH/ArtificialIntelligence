import java.awt.Point;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import pacsim.BFSPath;
import pacsim.PacAction;
import pacsim.PacCell;
import pacsim.PacFace;
import pacsim.PacSim;
import pacsim.PacUtils;
import pacsim.PacmanCell;

public class Node
{
    public Node last;
    public ArrayList<Node> next;
    public Node Move;
    public int value;
    public Point PacMove;
    public PacCell[][] state;

    public Node(){}

    public Node(int v, Node l, Point move)
    {
        next = new ArrayList<Node>();
        value = v;
        last = l;
        PacMove = move;
    }

    public Node(PacCell[][] grid, Node l, Point move)
    {
        next = new ArrayList<Node>();
        state = grid;
        last = l;
        PacMove = move;
    }
}