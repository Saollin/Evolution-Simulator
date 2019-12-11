package pl.obiektowe.projekt1.simulator;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    boolean precedes(Position other){
        return this.x <= other.x && this.y <= other.y;
    }

    boolean follows(Position other){
        return this.x >= other.x && this.y >= other.y;
    }

    Position upperRight(Position other){
        int newX, newY;
        newX = this.x > other.x ? this.x : other.x;
        newY = this.y > other.y ? this.y : other.y;
        return new Position(newX, newY);
    }

    Position lowerLeft(Position other){
        int newX, newY;
        newX = this.x < other.x ? this.x : other.x;
        newY = this.y < other.y ? this.y : other.y;
        return new Position(newX, newY);
    }

    Position add(Position other){
        return new Position(this.x + other.x, this.y + other.y);
    }

    Position subtract(Position other){
        return new Position(this.x - other.x, this.y - other.y);
    }

    Position opposite(){
        return new Position((-1) * this.x, (-1) * this.y);
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
        if(!(other instanceof Position))
            return false;
        Position that = (Position) other;
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
