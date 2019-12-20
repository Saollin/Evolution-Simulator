package pl.obiektowe.projekt1.simulator.Classes;

public class Vector2d {
    private final int x;
    private final int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    //constructor to clone Vector2d
    public Vector2d(Vector2d other){
        this.x = other.getX();
        this.y = other.getY();
    }

    boolean precedes(Vector2d other){
        return this.x <= other.x && this.y <= other.y;
    }

    boolean follows(Vector2d other){
        return this.x >= other.x && this.y >= other.y;
    }

    Vector2d upperRight(Vector2d other){
        int newX, newY;
        newX = this.x > other.x ? this.x : other.x;
        newY = this.y > other.y ? this.y : other.y;
        return new Vector2d(newX, newY);
    }

    Vector2d lowerLeft(Vector2d other){
        int newX, newY;
        newX = this.x < other.x ? this.x : other.x;
        newY = this.y < other.y ? this.y : other.y;
        return new Vector2d(newX, newY);
    }

    Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    Vector2d subtract(Vector2d other){
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    Vector2d opposite(){
        return new Vector2d((-1) * this.x, (-1) * this.y);
    }

    @Override
    public String toString(){
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object other){
        if(other == null)
            return false;
        if(this == other)
            return true;
        if(!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return (this.x == that.x) && (this.y == that.y);
    }

    @Override
    public int hashCode() {
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
