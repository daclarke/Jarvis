//package AI;

/***
 * A node that represents a paticular board setup in the decision tree.
 * 
 * @author hoshi
 *
 */
public class GameNode {
    
    public GameTree gameTree;
    public GameNode parent;
    public GameNode identical = null;
    public GameNode[] children;
    public byte gamePiece;
    public int column;
    public byte[][] board=null;
    public double score = 0;
    public double childrenScore = 0;

    /**
     * Initial GameNode, used as parent node in the GameTree
     * @param boardWidth
     */
    public GameNode(int boardWidth, GameTree gameTree){
        this.gameTree = gameTree;
        children = new GameNode[boardWidth * 2];        
    }
    
    /**
     * A child GameNode representing an additional piece played 
     * @param parent
     * @param xPos
     * @param piece
     * @param boardWidth
     */
    public GameNode(GameNode parent, int xPos, byte piece, int boardWidth){        
        this.parent = parent;
        column = xPos;
        gamePiece = piece;
        children = new GameNode[boardWidth * 2];        
    }
    
    public double calculateScore(IsWin isWin, int last_col){        
        score = isWin.winFunction(this, last_col);        
        return score;
    }
    
    public double calculateChildrensScore(){
        double totalScore = score; 
        
        int depth = 1;
        GameNode father = this.parent;
        while(father != null){
            father = father.parent;
            depth++;
        }
        
        if(children == null || children.length == 0){
            if(identical!=null)
            {
                return identical.calculateChildrensScore() + score / depth;
                
            }
            return score / depth;
        }       
        
        for(GameNode gameNode: children){
            if(gameNode == null){
                continue;
            }
            totalScore += gameNode.calculateChildrensScore();
        }
        return totalScore / depth;
    }

    /**
     * Used for looking 1 level deep to see if there is a win condition for the opponent.
     * Output will determine if an immediate move is needed to prevent a win.
     * @return
     */
    public int preventChildVictory(){
        
        for(GameNode gameNode: children){
            if(gameNode == null || gameNode.column == column){
                continue;
            }            
            if(gameNode.score < 0){
                return gameNode.column;
            }
        }
        return 0;
    }
}
