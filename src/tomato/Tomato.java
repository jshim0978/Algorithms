package tomato;

import java.util.*;
import java.io.*;

class Tomato {
    static int column;
    static int row;
    static int[][] storage;
    //combination for (1,0)  (-1,0)  (0,1)  (0,-1)
    static int[] directionX = {1, -1, 0, 0};
    static int[] directionY = {0, 0, 1, -1};

    static class Coordinates {
        int x;
        int y;
        int day;

        public Coordinates(int x, int y, int day) {
            this.x = x;
            this.y = y;
            this.day = day;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        column = Integer.parseInt(st.nextToken());
        row = Integer.parseInt(st.nextToken());
        storage = new int[row][column];

        for(int i = 0; i< row; i++) {
            st = new StringTokenizer(br.readLine());

            for(int j = 0; j< column; j++) {
                storage[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        System.out.println(bfs());
    }

    static int bfs() {
        Queue<Coordinates> queue = new LinkedList<>();
        //create queue by coordinates (x,y,day)
        int day = 0;
        //finding initial tomatoes
        for(int i = 0; i< row; i++) {
            for(int j = 0; j< column; j++) {
                if(storage[i][j] == 1)
                    //if it is a 1
                    queue.add(new Coordinates(i, j, 0));
                    //add to queue
            }
        }
        while(!queue.isEmpty()) {
            Coordinates coordinates = queue.poll();
            //pop() for queue
            day = coordinates.day;
            //check the day ; initially 0
            checkFourDirections(coordinates, queue, day);

        }
        if(allTomatoesAreRipe())
            return day;

        return -1;
    }

    static void checkFourDirections(Coordinates coordinates, Queue<Coordinates> queue, int day) {
        for (int i = 0; i < 4; i++) {
            //check 4 directions
            int next_X = coordinates.x + directionX[i];
            int next_Y = coordinates.y + directionY[i];
            //first direction is (1,0) which is the coordinates right side
            //second is left side, third is up , fourth is down
            if (0 <= next_X && next_X < row && 0 <= next_Y && next_Y < column) {
                //this if state is if next X and next Y is in the 2d array
                if (storage[next_X][next_Y] == 0) {
                    //if next coordinates are in the storage array and the value is 0,
                    storage[next_X][next_Y] = 1;
                    //we change it to 1 and
                    queue.add(new Coordinates(next_X, next_Y, day + 1));
                    //add to queue and show the epoch with incremented days
                    //by doing this, first day tomatoes are all riping its neighbors
                    //and we added the ripe neighbors till there was none left in the queue
                    //but -1 not-in-the-storage tomatoes can cause some tomatoes to not ripe
                    //so to check if all tomatoes are ripe, add another method
                }
            }
        }
    }

    static boolean allTomatoesAreRipe() {
        //checking if there is a 0 value at the end of the while loop
        for(int i = 0; i< row; i++) {
            for(int j = 0; j< column; j++) {
                if(storage[i][j] == 0)
                    return false;
            }
        }

        return true;
    }
}
