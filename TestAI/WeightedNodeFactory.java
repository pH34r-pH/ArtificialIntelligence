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

public class WeightedNodeFactory
{
    // Private Fields
    private List<Point> path;
    private List<Point> food;
    private int[][] costMap;
    private boolean init = false;

    // Public Fields
    public PacmanCell pc;
    public PacCell[][] grid;
    public boolean isLoaded = false;
    public WeightedNode root;

    // Constructor
    public WeightedNodeFactory()
    {
        path = new ArrayList<Point>();
        food = new ArrayList<Point>();
        costMap = new int[1][1];
        costMap[0][0] = 0;
    }

    // Public Functions
    public void Init(PacmanCell inputPc, PacCell[][] inputGrid)
    {
        grid = inputGrid;
        pc = inputPc;
        SetFoodList(grid);
        SetCostMap(grid);
        SetRoot(pc);
        init = true;
    }

    public void Build()
    {
        if(!init)
        {
            System.out.println("Error: Init() must be run before building.");
            return;
        }
        BuildRootLayer();
        for(WeightedNode n : root.Next)
        {
            BuildChain(n);
        }
        isLoaded = true;
    }

    // Private Functions
    private void BuildChain(WeightedNode node)
    {
        boolean done = true;
        for(int i = 0; i < node.Used.length; ++i)
        {
            done &= node.Used[i];
        }
        if(done)
        {
            return;
        }

        int min = Integer.MAX_VALUE;
        ArrayList<Int> index = new ArrayList<Int>();
        for(int i = 0; i < costMap[node.Index].size; ++i)
        {
            if(costMap[node.Index][i] > 0 && !node.Used[i])
            {
                if(costMap[node.Index][i] < min)
                {
                    min = costMap[node.Index][i];
                    index = new ArrayList<Int>();
                    index.add(i);
                }
                else if(costMap[node.Index][i] == min)
                {
                    index.add(i);
                }
            }
        }
        for(int ind : index)
        {
            boolean[] used = node.Used;
            used[ind] = true;
            node.Add(new WeightedNode(food.get(ind), min, used, ind));
        }
        for(WeightedNode no : node.Next)
        {
            BuildChain(no);
        }
    }

    private void BuildRootLayer()
    {
        for(int i = 0; i < food.size(); ++i){
            boolean[] used = new boolean[food.size()];
            used[i] = true;
            root.Add(new WeightedNode(food.get(i), BFSPath.getPath(grid, pc.getLoc(), food.get(i)).size(), used, i));
        }
    }

    private void SetRoot(PacmanCell pc)
    {
        root = new WeightedNode(pc.getLoc(), 0, 0, new boolean[food.size()], -1);
    }

    private void SetCostMap(PacCell[][] grid)
    {
        int foodCount = food.size();
        costMap = new int[foodCount][foodCount];
        for(int i = 0; i < foodCount; ++i){
            for(int j = 0; j < foodCount; ++j){
                if(i == j){costMap[i][j] = 0;}
                else{
                    costMap[i][j] = costMap[j][i] = Math.min(
                                BFSPath.getPath(grid, food.get(i), food.get(j)).size(), 
                                BFSPath.getPath(grid, food.get(j), food.get(i)).size()
                                );
                }
            }
        }
    }

    private void SetFoodList(PacCell[][] grid)
    {
        food = PacUtils.findFood(grid);
    }

    // Print Functions
    public void PrintFoodList()
    {
        if(!init)
        {
            System.out.println("Warning: The factory has not been initialized.");
        }
        for(int i = 0; i < food.size(); ++i){
            System.out.printf("%d : (%d,%d)\n", i, food.get(i).x, food.get(i).y);
        }
    }

    public void PrintCostMap()
    {
        if(!init)
        {
            System.out.println("Warning: The factory has not been initialized.");
        }
        int len = costMap[0].length;
        for(int i = 0; i < len; ++i){
            for(int j = 0; j < len; ++j){
                System.out.printf("%4d", costMap[i][j]);
            }
            System.out.println();
        }
    }
}