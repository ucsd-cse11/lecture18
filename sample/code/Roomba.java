// What type goes in the 2d array?
// Use a `char`: empty is '-', 'O' is furniture, '*' is clean
// Can be a class – all arrays are mutable anyway
record Room(char[][] grid) {}
// degrees can be 0 (up), 90 (right), 180 (down), 270 (left)
record Robot(int x, int y, int degrees) {
    Robot turnRight() { return new Robot(x, y, (degrees + 90) % 360); }
    Robot turnLeft() { return new Robot(x, y, (degrees + 270) % 360); }
    // one square ahead, same heading
    Robot forward() {
        if(degrees == 0) { return new Robot(x, y - 1, degrees); }
        if(degrees == 90) { return new Robot(x + 1, y, degrees); }
        if(degrees == 180) { return new Robot(x, y + 1, degrees); }
        else { return new Robot(x - 1, y, degrees); }
    }
    String show() {
        if(degrees == 0) { return "^"; }
        if(degrees == 90) { return ">"; }
        if(degrees == 180) { return "v"; }
        if(degrees == 270) { return "<"; }
        else { return "?"; }
    }
}
class Simulation {
    final Room room;
    Robot robot;
    final String mode;
    boolean rightNext = true;
    Simulation(Room room, Robot robot, String mode) {
        this.room = room;
        this.robot = robot;
        this.mode = mode;
    }
    boolean isFacingWall() {
        Robot ahead = robot.forward();
        return ahead.y() < 0 || ahead.y() >= room.grid.length
            || ahead.x() < 0 || ahead.x() >= room.grid[0].length;
    }
    boolean isFacingObstacle() {
        if(isFacingWall()) { return false; }
        Robot ahead = robot.forward();
        return room.grid[ahead.y()][ahead.x()] == 'O';
    }
    boolean isFacingWallOrObstacle() {
        return isFacingWall() || isFacingObstacle();
    }
    void step() {
        room.grid[robot.y][robot.x] = '*';
        boolean blocked = isFacingWallOrObstacle();
        if(mode.equals("right")) {
            if(blocked) { robot = robot.turnRight(); }
            else { robot = robot.forward(); }
        }
        else if(mode.equals("around")) {
            if(blocked) {
                robot = robot.turnRight().turnRight();
            }
            else { robot = robot.forward(); }
        }
        else if(mode.equals("alternate")) {
            if(blocked && rightNext) {
                robot = robot.turnRight();
                rightNext = false;
            }
            else if(blocked) {
                robot = robot.turnLeft();
                rightNext = true;
            }
            else { robot = robot.forward(); }
        }
    }
    String show() {
        String toShow = "";
        for(int row = 0; row < room.grid.length; row += 1) {
            for(int col = 0; col < room.grid[0].length; col += 1) {
                if(col == robot.x && row == robot.y) {
                    toShow += robot.show();
                }
                else {
                    toShow += room.grid[row][col];
                }
            }
            toShow += "\n";
        }
        return toShow;
    }
}

void main() {
    Room room = new Room(new char[][]{
        { '-', '-', '-', '-', '-', '-', '-' },
        { '-', '-', '-', 'O', '-', '-', '-' },
        { '-', '-', '-', '-', '-', '-', '-' },
        { '-', 'O', '-', '-', '-', '-', '-' },
    });
    Robot r = new Robot(4, 3, 90);
    Simulation s = new Simulation(room, r, "right");
    for(int i = 0; i < 10; i += 1) {
        s.step();
    }
    System.out.println(s.show());
}
