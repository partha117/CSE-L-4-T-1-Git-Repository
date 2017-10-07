package Game_Playing;

public class SearchNode  {

    SearchNode parent;
    int [][]board;
    int color;
    int g;
    int h;

    public SearchNode(SearchNode parent, int[][] board, int color) {
        this.parent = parent;
        this.board = board;
        this.color = color;
    }

    public SearchNode(SearchNode parent, int[][] board, int color, int g, int h) {
        this.parent = parent;
        this.board = board;
        this.color = color;
        this.g = g;
        this.h = h;
    }

    public SearchNode getParent() {
        return parent;
    }

    public void setParent(SearchNode parent) {
        this.parent = parent;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }
    public  int getF()
    {
        return h+g;
    }



    @Override
    public boolean equals(Object obj) {
        SearchNode temp=(SearchNode)obj;
        for(int i=0;i<board.length;i++)
        {
            for(int j=0;j<board[i].length;j++)
            {
                if(board[i][j]!=temp.board[i][j])
                {
                    return  false;
                }
            }
        }
        return true;
    }
}
