import java.awt.Point;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import pacsim.BFSPath;
import pacsim.PacAction;
import pacsim.PacCell;
import pacsim.PacFace;
import pacsim.PacSim;
import pacsim.PacUtils;
import pacsim.PacmanCell;

/**
 * University of Central Florida
 * CAP4630 - Fall 2018
 * @author : Tyler Harbin-Giuntoli
 */

public class WeightedNodeFactory
{
    // Private Fields
    private PacmanCell pc;
    private PacCell[][] grid;
    private List<Point> food;
    private int[][] costMap;
    private boolean init = false;
    private boolean debug = false;
    private WeightedNode root;
    private ArrayList<ArrayList<WeightedNode>> nodeMap;
    private List<Point> steps;
    private List<Point> path;
    
    // Public Fields
    public boolean isLoaded = false;
    public int step;

    // Constructor
    public WeightedNodeFactory()
    {
        steps = new ArrayList<Point>();
        path = new ArrayList<Point>();
        food = new ArrayList<Point>();
        costMap = new int[1][1];
        costMap[0][0] = 0;
        nodeMap = new ArrayList<ArrayList<WeightedNode>>();
        step = 0;
        if(debug){ System.out.println("Node Factory created"); }
    }

    // Public Functions
    public void Init(PacmanCell inputPc, PacCell[][] inputGrid)
    {
        grid = inputGrid;
        pc = inputPc;
        nodeMap.add(new ArrayList<WeightedNode>());
        SetFoodList(grid);
        SetCostMap(grid);
        SetRoot(pc);
        init = true;
        if(debug){ System.out.println("Node Factory initialized"); }
    }

    public void Build()
    {
        if(!init)
        {
            System.out.println("Error: Init() must be run before building.");
            return;
        }
        BuildRootLayer();
        for(int i = 1; i < food.size(); ++i)
        {
            BuildChain(i);
        }
        SetPath();
        isLoaded = true;
        if(debug){ System.out.println("Built and ready to run"); }
    }

    public Point TakeStep()
    {
        step++;
        return path.get(step - 1);
    }

    public void SetDebug(boolean mode)
    {
        debug = mode;
    }

    // Private Functions
    private void SetPath()
    {
        ArrayList<WeightedNode> options = nodeMap.get(nodeMap.size()-1);
        Collections.sort(options);
        steps.add(root.Location);
        steps.addAll(options.get(0).GetChain());
        for(int i = 1; i < steps.size(); ++i)
        {
            path.addAll(BFSPath.getPath(grid, steps.get(i-1), steps.get(i)));
        }
        if(debug){ System.out.println("Path set"); }
    }

    private void BuildChain(int step)
    {
        if(debug){ System.out.println("Building step " + step); }
        for(WeightedNode node : nodeMap.get(step))
        {
            BuildChain(node);
        }
    }

    private void BuildChain(WeightedNode node)
    {
        boolean done = true;
        for(int i = 0; i < node.Used.length; ++i)
        {
            done &= node.Used[i];
        }
        if(done)
        {
            if(debug){ System.out.println("Chain completed at step " + node.Step); }
            return;
        }

        int min = Integer.MAX_VALUE;
        ArrayList<Integer> index = new ArrayList<Integer>();
        for(int i = 0; i < costMap[node.Index].length; ++i)
        {
            if(costMap[node.Index][i] > 0 && !node.Used[i])
            {
                if(costMap[node.Index][i] < min)
                {
                    min = costMap[node.Index][i];
                    index = new ArrayList<Integer>();
                    index.add(i);
                }
                else if(costMap[node.Index][i] == min)
                {
                    index.add(i);
                }
            }
        }
        if(debug && index.size() == 0)
        {
            System.out.println("No minimum found at step " + node.Step + " with the following options:");
            for(int i = 0; i < costMap[node.Index].length; ++i)
            {
                System.out.printf("%d, Used:%d - ", costMap[node.Index][i], (node.Used[i] ? 1 : 0));
            }
            System.out.println();
        }
        for(int ind : index)
        {
            boolean[] used = new boolean[node.Used.length];
            System.arraycopy(node.Used, 0, used, 0, node.Used.length);
            used[ind] = true;
            WeightedNode newNode = new WeightedNode(food.get(ind), min, used, ind);
            node.Add(newNode); 

            if(nodeMap.size() < newNode.Step+1) { nodeMap.add(new ArrayList<WeightedNode>()); }
            nodeMap.get(newNode.Step).add(newNode);
        }
    }

    private void BuildRootLayer()
    {
        for(int i = 1; i < food.size(); ++i){
            boolean[] used = new boolean[food.size()];
            used[i] = true;
            used[0] = true;
            WeightedNode newNode = new WeightedNode(food.get(i), BFSPath.getPath(grid, pc.getLoc(), food.get(i)).size(), used, i);
            nodeMap.get(1).add(newNode);
            root.Add(newNode);
        }
        if(debug){ System.out.println("Root Layer built"); }
    }

    private void SetRoot(PacmanCell pc)
    {
        root = new WeightedNode(pc.getLoc(), 0, 0, new boolean[food.size()], -1);
        nodeMap.get(0).add(root);
        nodeMap.add(new ArrayList<WeightedNode>());
        if(debug){ System.out.println("Created Root"); }
    }

    private void SetCostMap(PacCell[][] grid)
    {
        int foodCount = food.size();
        costMap = new int[foodCount][foodCount];
        for(int i = 0; i < foodCount; ++i){
            for(int j = 0; j < foodCount; ++j){
                if(i == j){costMap[i][j] = 0;}
                else{
                    costMap[i][j] = costMap[j][i] = BFSPath.getPath(grid, food.get(i), food.get(j)).size();
                }
            }
        }
        if(debug){ System.out.println("Cost Map Built"); }
    }

    private void SetFoodList(PacCell[][] grid)
    {
        food.add(pc.getLoc());
        food.addAll(PacUtils.findFood(grid));
        if(debug){ System.out.println("Food List set"); }
    }

    // Print Functions
    public void PrintFoodList()
    {
        if(!init)
        {
            System.out.println("Warning: The factory has not been initialized.");
        }
        for(int i = 1; i < food.size(); ++i){
            System.out.printf("%d : (%d,%d)\n", i-1, food.get(i).x, food.get(i).y);
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

    public void PrintSteps()
    {
        if(!isLoaded)
        {
            System.out.println("Warning: the path tree has not been built.");
        }
        for(int step = 1; step < nodeMap.size(); ++step)
        {
            ArrayList<WeightedNode> nodeList = nodeMap.get(step);
            Collections.sort(nodeList);
            System.out.printf("\nPopulation at step %d:\n", step);
            int count = 0;
            for(WeightedNode node : nodeList)
            {
                System.out.printf("%4d : cost=%d : ", count, node.PathCost);
                node.PrintChain();
                System.out.println();
                count++;
            }
        }
    }
}