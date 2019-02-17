package hapless.eagles.common;

import java.util.ArrayList;

/**
 * Created by chuprii on 2/16/19.
 */
public class Player {
    public static final int DIR_NORTH = 1;
    public static final int DIR_SOUTH = -1;
    public static final int DIR_EAST = 2;
    public static final int DIR_WEST = -2;

    private int boardID;
    private int playerNum;
    private int direction;
    private WorldPixel head;
    private ArrayList<WorldPixel> trail;


    /* Constructors */
    public Player(int x, int y, char dir) {
        head = new WorldPixel(x, y);
        direction = dir;
        trail = new ArrayList<>();
    }

    /* Getters */
    public WorldPixel getHead() {
        return head;
    }

    public int getDirection() {
        return direction;
    }

    public ArrayList<WorldPixel> getTrail() {
        return trail;
    }

    public int getBoardID() {
        return boardID;
    }

    public int getPlayerNum() {
        return playerNum;
    }


    /* Setters */

    public void setBoardID(int newBID) {
        boardID = newBID;
    }


    public void setDirection(int newDir) {
        if (newDir != direction && newDir != -direction)
            direction = newDir;
    }

    public void setCoordinates(int x, int y) {
        head = new WorldPixel(x, y);
    }

    public void setPlayerNum(int pn) {
        playerNum = pn;
    }

    public void setHead(WorldPixel newHead) {
        this.head = newHead;
    }

    /* Add coordinates to trail */
    public void addToTrail(WorldPixel c) {
        trail.add(c);
    }

    /* See if coordinates are in the trail */
    public boolean isInTrail(WorldPixel c)
    {
        boolean retVal = false;

        for(int i = 0; i<trail.size(); i++)
        {
            retVal = trail.get(i).isA(c);
            if(retVal)
                break;
        }

        return retVal;
    }

    public boolean isInTrail(int x, int y)
    {
        boolean retVal = false;

        for(int i = 0; i<trail.size(); i++)
        {
            retVal = trail.get(i).isA(x,y);
            if(retVal)
                break;
        }

        return retVal;
    }

    /* Move:
     * Take a step in the direction player is currently facing
     */
    public void move()
    {
        int newX = head.getX();
        int newY = head.getY();

        addToTrail(head);

        switch(direction)
        {
            case DIR_NORTH:
                newY++;
                break;
            case DIR_SOUTH:
                newY--;
                break;
            case DIR_EAST:
                newX++;
                break;
            case DIR_WEST:
                newX--;
                break;
        }

        setHead(new WorldPixel(newX, newY));
    }
}