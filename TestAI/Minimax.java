import java.awt.Point;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import pacsim.BFSPath;
import pacsim.PacAction;
import pacsim.PacCell;
import pacsim.HouseCell;
import pacsim.WallCell;
import pacsim.PacFace;
import pacsim.PacSim;
import pacsim.PacUtils;
import pacsim.PacmanCell;

/**
 * University of Central Florida
 * CAP4630 - Fall 2018
 * @author : Tyler Harbin-Giuntoli
 */

 public class Minimax
 {
    public int GhostScatterTime = 7;
    public int GhostChaseTime = 20;
    public int GhostPowerScatterTime = 20;
    public boolean GhostsAreChasing = true;
    public boolean PowerPelletActive = false;
    public int Timer;

    private Valuer judge;
    private int depth;
    private Node stateSpace;
    
    public Minimax(int d)
    {
        depth = d;
        Timer = GhostChaseTime;
        judge = new Valuer(this);
    }

    public PacFace Turn(PacCell[][] grid)
    {
        if(Timer == 0)
        {
            GhostsAreChasing = !GhostsAreChasing;
            Timer = GhostsAreChasing ? GhostChaseTime : GhostScatterTime;
        }

        stateSpace = new Node(grid, null, PacUtils.findPacman(grid).getLoc());
        PopulateStateSpace(0, true, stateSpace);
        
        Timer--;

        return PacUtils.direction(stateSpace.PacMove, stateSpace.Move.PacMove);
    }
    
    private void PopulateStateSpace(int currentDepth, boolean max, Node parent)
    {
        PacCell[][] grid = parent.state;

        List<Point> Ghosts = PacUtils.findGhosts(grid);
        PacmanCell PacmanLocation = PacUtils.findPacman(grid);
        if(PacmanLocation == null)
        {
            parent.next.add(new Node(judge.Evaluate(grid), parent, new Point(-1,-1)));
            return;
        }
        Point Pacman = PacmanLocation.getLoc();

        ArrayList<Point> PacmanOptions = GetMovableSpaces(grid, Pacman);
        ArrayList<ArrayList<Point>> GhostOptions = new ArrayList<ArrayList<Point>>();
        for(Point Ghost : Ghosts)
        {
            GhostOptions.add(GetMovableSpaces(grid, Ghost));
        }

        // note: I am aware that Ghost index 0 will not always be Blinky
        for(Point PM : PacmanOptions)
        {
            for(Point Blinky : GhostOptions.get(0))
            {
                for(Point Inky : GhostOptions.get(1))
                {
                    PacCell[][] state = PacUtils.movePacman(Pacman, PM, grid);
                    state = PacUtils.moveGhost(Ghosts.get(0), Blinky, state);
                    state = PacUtils.moveGhost(Ghosts.get(1), Inky, state);

                    if(PacUtils.findGhosts(state).size() < 2)
                    {
                        continue;
                    }

                    if(currentDepth == depth)
                    {
                        parent.next.add(new Node(judge.Evaluate(state), parent, PM));
                    }
                    else
                    {
                        Node child = new Node(state, parent, PM);
                        parent.next.add(child);
                        PopulateStateSpace(currentDepth+1, !max, child);
                    }
                }
            }
        }
        int val = (max ? Integer.MIN_VALUE : Integer.MAX_VALUE);
        Node valNode = new Node();
        for(Node n : parent.next)
        {
            if((max && val < n.value) || (!max && val > n.value))
            {
                val = n.value;
                valNode = n;
            }
        }
        parent.value = val;
        if(currentDepth > 1)
        {
            parent.next = new ArrayList<Node>();
        }
        else if(currentDepth <= 1)
        {
            parent.Move = valNode;
        }
    }


    private ArrayList<Point> GetMovableSpaces(PacCell[][] grid, Point start)
    {
        ArrayList<Point> valid = new ArrayList<Point>();

        if(IsValidSpace(grid[start.x-1][start.y]))
        {
            valid.add(new Point(start.x-1, start.y));
        }
        if(IsValidSpace(grid[start.x+1][start.y]))
        {
            valid.add(new Point(start.x+1, start.y));
        }
        if(IsValidSpace(grid[start.x][start.y-1]))
        {
            valid.add(new Point(start.x, start.y-1));
        }
        if(IsValidSpace(grid[start.x][start.y+1]))
        {
            valid.add(new Point(start.x, start.y+1));
        }

        return valid;
    }

    private boolean IsValidSpace(PacCell cell)
    {
        return !(WallCell.class.isInstance(cell) || HouseCell.class.isInstance(cell));
    }

    private int MaxValue(PacCell[][] grid)
    {
        int value = Integer.MIN_VALUE;

        return value;
    }

    private int MinValue(PacCell[][] grid)
    {
        int value = Integer.MAX_VALUE;

        return value;
    }
 }