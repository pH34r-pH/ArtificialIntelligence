
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

/**
 * Simple Repetitive Nearest Neighbor Agent
 * @author Tyler Harbin-Giuntoli
 */
public class PacSimRNNA implements PacAction {
   
    private List<Point> path;
    private List<Point> food;
    private int[][] costMap;
    private int simTime;
      
    public PacSimRNNA( String fname ) {
        PacSim sim = new PacSim( fname );
        sim.init(this);
    }
   
    public static void main( String[] args ) {         
        System.out.println("\nRNN Agent by Tyler Harbin-Giuntoli;");
        System.out.println("\nMaze : " + args[ 0 ] + "\n" );
        new PacSimRNNA( args[ 0 ] );
    }

    @Override
    public void init() {
        simTime = 0;
        path = new ArrayList<Point>();
        food = new ArrayList<Point>();
        costMap = new int[1][1];
    }
   
    @Override
    public PacFace action( Object state ) {

        PacCell[][] grid = (PacCell[][]) state;
        PacmanCell pc = PacUtils.findPacman( grid );
      
        // make sure Pac-Man is in this game
        if( pc == null ) return null;
            
        // if current path is empty, generate the path
      
        if( path.isEmpty() ) {
            SetFoodList(grid);
            SetCostMap(grid);

            WeightedNode root = new WeightedNode(pc.getLoc(), 0, 0, new boolean[food.size()], costMap);

            System.out.println("Cost table:");
            PrintGrid(costMap);

            System.out.println("\nFood Array:");
            for(int i = 0; i < food.size(); ++i){
                System.out.printf("%d : (%d,%d)\n", i, food.get(i).x, food.get(i).y);
                boolean[] used = new boolean[food.size()];
                used[i] = true;
                root.Add(new WeightedNode(food.get(i), BFSPath.getPath(grid, pc.getLoc(), food.get(i)).size(), used));
            }
            
            for(int i = 0; i < food.size(); ++i){
                System.out.printf("%d : cost=%d : ", i, root.Next.get(i).PathToHereCost());
                root.Next.get(i).Print();
                System.out.println();
            }

            //-----------------------------------------------------------------------------------------
            Point tgt = PacUtils.nearestFood( pc.getLoc(), grid);
            path = BFSPath.getPath(grid, pc.getLoc(), tgt);
         
            System.out.println("Pac-Man currently at: [ " + pc.getLoc().x + ", " + pc.getLoc().y + " ]");
            System.out.println("Setting new target  : [ " + tgt.x + ", " + tgt.y + " ]");
        }
      
        // take the next step on the current path
      
        Point next = path.remove( 0 );
        PacFace face = PacUtils.direction( pc.getLoc(), next );
        System.out.printf( "%5d : From [ %2d, %2d ] go %s%n", ++simTime, pc.getLoc().x, pc.getLoc().y, face );
        return face;
    }

    public void SetFoodList(PacCell[][] grid){
        food = PacUtils.findFood(grid);
    }

    public void SetCostMap(PacCell[][] grid){
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

    public void PrintGrid(int[][] grid){
        int len = grid[0].length;
        for(int i = 0; i < len; ++i){
            for(int j = 0; j < len; ++j){
                System.out.printf("%4d", grid[i][j]);
            }
            System.out.println();
        }
    }
}
