import java.awt.Point;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.util.ElementScanner6;

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

    private int powerPelletCount;
    private Valuer judge;
    private int depth;
    private Node stateSpace;

    private boolean DEBUG = false;
    
    public Minimax(int d)
    {
        powerPelletCount = -1;

        // one "layer" for pacmans move, and one layer for ghost move, is one turn
        depth = d*2;
        Timer = GhostChaseTime;
        judge = new Valuer(this);
    }

    public PacFace Turn(PacCell[][] grid)
    {
        if(powerPelletCount == -1)
        {
            powerPelletCount = PacUtils.numPower(grid);
        }
        else if(powerPelletCount > 0 && PacUtils.numPower(grid) < powerPelletCount)
        {
            powerPelletCount = PacUtils.numPower(grid);
            PowerPelletActive = true;
            Timer = GhostPowerScatterTime;
            GhostsAreChasing = false;
        }

        if(Timer == 0)
        {
            GhostsAreChasing = !GhostsAreChasing;
            Timer = GhostsAreChasing ? GhostChaseTime : GhostScatterTime;
        }

        Point PacLoc = PacUtils.findPacman(grid).getLoc();

        stateSpace = new Node(grid, null, PacLoc);
        PopulateStateSpace(0, true, stateSpace);
        
        Timer--;

        if(DEBUG) System.out.println("Moving from " + stateSpace.PacMove.x + ", " + stateSpace.PacMove.y + " to " + stateSpace.Move.PacMove.x + ", " + stateSpace.Move.PacMove.y);

        // Occasional edge cases lose the next move; in these cases, pacman guesses where to go randomly.
        if(stateSpace == null || stateSpace.Move == null || stateSpace.Move.PacMove == null)
        {
            return PacUtils.randomFace();
        }
        return PacUtils.direction(stateSpace.PacMove, stateSpace.Move.PacMove);
    }
    
    private void PopulateStateSpace(int currentDepth, boolean max, Node parent)
    {
        if(DEBUG) System.out.println("Beginning populate at level " + currentDepth);

        PacCell[][] grid = parent.state;

        List<Point> Ghosts = PacUtils.findGhosts(grid);
        PacmanCell PacmanLocation = PacUtils.findPacman(grid);
        if(PacmanLocation == null)
        {
            if(DEBUG) System.out.println("Pacman died :(");
            parent.next.add(new Node(judge.Evaluate(grid), parent, new Point(-1,-1)));
            return;
        }
        Point Pacman = PacmanLocation.getLoc();

        ArrayList<Point> PacmanOptions = GetMovableSpaces(grid, Pacman, false);
        if(DEBUG) System.out.println("Found " + PacmanOptions.size() + " places Pacman can move to");

        ArrayList<ArrayList<Point>> GhostOptions = new ArrayList<ArrayList<Point>>();
        for(Point Ghost : Ghosts)
        {
            GhostOptions.add(GetMovableSpaces(grid, Ghost, true));
        }


        if(max)
        {
            for(Point PM : PacmanOptions)
            {
                PacCell[][] state = PacUtils.movePacman(Pacman, PM, grid);

                if(PacUtils.findGhosts(state).size() < 2)
                {
                    if(DEBUG) System.out.println("Dropped a ghost");
                    continue;
                }

                if(currentDepth == depth)
                {
                    if(DEBUG) System.out.println("Hit depth; scoring");
                    parent.next.add(new Node(judge.Evaluate(state), parent, PM));
                }
                else
                {
                    if(DEBUG) System.out.println("Did not hit depth");
                    Node child = new Node(state, parent, PM);
                    parent.next.add(child);
                    if(DEBUG) System.out.println("Added child number " + parent.next.size() + " and recursing");
                    PopulateStateSpace(currentDepth+1, !max, child);
                    if(DEBUG) System.out.println("Returned from recurse");
                }
            }
        }
        else
        {
            // note: I am aware that Ghost index 0 will not always be Blinky
            for(Point Blinky : GhostOptions.get(0))
            {
                for(Point Inky : GhostOptions.get(1))
                {
                    PacCell[][] state = PacUtils.moveGhost(Ghosts.get(0), Blinky, grid);
                    state = PacUtils.moveGhost(Ghosts.get(1), Inky, state);

                    if(PacUtils.findGhosts(state).size() < 2)
                    {
                        if(DEBUG) System.out.println("Dropped a ghost");
                        continue;
                    }

                    if(currentDepth == depth)
                    {
                        if(DEBUG) System.out.println("Hit depth; scoring");
                        parent.next.add(new Node(judge.Evaluate(state), parent, Pacman));
                    }
                    else
                    {
                        if(DEBUG) System.out.println("Did not hit depth");
                        Node child = new Node(state, parent, Pacman);
                        parent.next.add(child);
                        if(DEBUG) System.out.println("Added child number " + parent.next.size() + " and recursing");
                        PopulateStateSpace(currentDepth+1, !max, child);
                        if(DEBUG) System.out.println("Returned from recurse");
                    }
                }
            }
        }
        
        if(DEBUG) System.out.println("Finished triple loop, now " + (max ? "maximizing" : "minimising"));
        int val = (max ? Integer.MIN_VALUE : Integer.MAX_VALUE);
        Node valNode = new Node();
        for(Node n : parent.next)
        {
            if((max && val < n.value) || (!max && val > n.value))
            {
                if(DEBUG) System.out.println("Found new " + (max ? "max" : "min") + " value: " + n.value);
                val = n.value;
                valNode = n;
            }
        }
        parent.value = val;
        if(currentDepth > 0)
        {
            if(DEBUG) System.out.println("Completed node at depth " + currentDepth + ", clearing children");
            parent.next = new ArrayList<Node>();
        }
        else if(currentDepth == 0)
        {
            if(DEBUG) System.out.println("Completed at depth 0, setting Move node");
            parent.Move = valNode;
        }
        if(DEBUG) System.out.println("Exiting populate");
    }


    private ArrayList<Point> GetMovableSpaces(PacCell[][] grid, Point start, boolean isGhost)
    {
        ArrayList<Point> valid = new ArrayList<Point>();

        if(IsValidSpace(grid[start.x-1][start.y], isGhost))
        {
            valid.add(new Point(start.x-1, start.y));
        }
        if(IsValidSpace(grid[start.x+1][start.y], isGhost))
        {
            valid.add(new Point(start.x+1, start.y));
        }
        if(IsValidSpace(grid[start.x][start.y-1], isGhost))
        {
            valid.add(new Point(start.x, start.y-1));
        }
        if(IsValidSpace(grid[start.x][start.y+1], isGhost))
        {
            valid.add(new Point(start.x, start.y+1));
        }

        return valid;
    }

    private boolean IsValidSpace(PacCell cell, boolean isGhost)
    {
        return !(WallCell.class.isInstance(cell) || (HouseCell.class.isInstance(cell) && !isGhost));
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