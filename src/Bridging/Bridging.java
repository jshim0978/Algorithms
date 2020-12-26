package Bridging;

import java.util.*;
import java.io.*;

class Bridging {

    static int size;
    static int[][] map;
    static int[][] continents;
    static int[][] distance;
    //combination for (1,0)  (-1,0)  (0,1)  (0,-1)
    private static final int[] DIRECTION_X = {1, -1, 0, 0};
    private static final int[] DIRECTION_Y = {0, 0, 1, -1};

    private static class Coordinates {
        int x;
        int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        size = Integer.parseInt(st.nextToken());
        map = new int[size][size];

        for (int i = 0; i < size; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < size; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        solve();
    }

    static void solve() {

        continents = new int[size][size];
        distance = new int[size][size];
        int continentNumber = 0;

        /*first, we identify the given map and see how many continents there are and where they are*/

        //check all coordinates
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //if i haven't labeled the land on the continent,
                if (map[i][j] == 1 && continents[i][j] == 0) {
                    Queue<Bridging.Bridging.Coordinates> continentQueue = new LinkedList<>();
                    //give the continent a number
                    continents[i][j] = ++continentNumber;
                    //and add it to the continent Queue
                    continentQueue.add(new Bridging.Bridging.Coordinates(i, j));
                    //we're checking 4 directions of a coordinate to see which continent they belong to
                    while (!continentQueue.isEmpty()) {
                        Bridging.Bridging.Coordinates coordinates = continentQueue.remove();
                        for (int k = 0; k < 4; k++) {
                            //this method does it
                            //if i reach the edge of a continent, the while loop increments the continent number so i know its a different continent
                            identifyContinents(coordinates, continentQueue, continentNumber);
                        }
                    }
                }
            }
        }
        /*then we compute all distances */

        //make a new queue for distances
        Queue<Bridging.Bridging.Coordinates> distanceQueue = new LinkedList<>();
        //check every coordinates
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //set all distances to -1 initially
                distance[i][j] = -1;
                //if it is a land,
                if (map[i][j] == 1) {
                    //add it to the queue
                    distanceQueue.add(new Bridging.Bridging.Coordinates(i, j));
                    //and set the distance to 0 -> this marks all water -1 and all land 0 at first
                    distance[i][j] = 0;
                }
            }
        }
        //for every land coordinate
        while (!distanceQueue.isEmpty()) {
            Bridging.Bridging.Coordinates coordinates = distanceQueue.remove();
            //figure out the distance of the neighboring coordinates
            for (int i = 0; i < 4; i++) {
                identifyContinentsDistance(coordinates, distanceQueue);
            }
        }
        //and find the fastest route
        System.out.println(findFastestRoute());
    }

    static void identifyContinents(Bridging.Bridging.Coordinates coordinates, Queue<Bridging.Bridging.Coordinates> queue, int continentNumber) {
        for (int i = 0; i < 4; i++) {
            //check 4 directions
            //first direction is (1,0) which is the coordinates right side
            //second is left side, third is up , fourth is down
            int nextX = coordinates.x + DIRECTION_X[i];
            int nextY = coordinates.y + DIRECTION_Y[i];
            //this if state is if next X and next Y is in the 2d array
            if (0 <= nextX && nextX < size && 0 <= nextY && nextY < size) {
                //if the next land coordinate is not marked as a continent
                if (map[nextX][nextY] == 1 && continents[nextX][nextY] == 0) {
                    //add it to the queue
                    queue.add(new Bridging.Bridging.Coordinates(nextX, nextY));
                    //and give it a continent number
                    continents[nextX][nextY] = continentNumber;
                }
            }
        }
    }

    static void identifyContinentsDistance(Bridging.Bridging.Coordinates coordinates, Queue<Bridging.Bridging.Coordinates> queue) {
        for (int i = 0; i < 4; i++) {
            //check 4 directions
            //first direction is (1,0) which is the coordinates right side
            //second is left side, third is up , fourth is down
            int next_X = coordinates.x + DIRECTION_X[i];
            int next_Y = coordinates.y + DIRECTION_Y[i];
            //this if state is if next X and next Y is in the 2d array
            if (0 <= next_X && next_X < size && 0 <= next_Y && next_Y < size) {
                //if the neighboring coordinates is water,
                if (distance[next_X][next_Y] == -1) {
                    //increment the distance of the neighboring coordinates from current coordinates -> this means its the edge
                    distance[next_X][next_Y] = distance[coordinates.x][coordinates.y] + 1;
                    //now lets say that the water on the edge is also part of the continent
                    //this shows the distance to another continent of every coordinate
                    continents[next_X][next_Y] = continents[coordinates.x][coordinates.y];
                    //and add these coordinates to the queue, judging how much water there is between continents
                    queue.add(new Bridging.Bridging.Coordinates(next_X, next_Y));
                }
            }
        }
    }

    static int findFastestRoute() {
        //init
        int fastestRoute = -1;
        //check every coordinates
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //for every neighbor,
                for (int k = 0; k < 4; k++) {
                    int x = i + DIRECTION_X[k];
                    int y = j + DIRECTION_Y[k];
                    //if the neighbor is in the map
                    if (0 <= x && x < size && 0 <= y && y < size)
                        //and if the searched coordinate's neighbor is another continent,
                        if (continents[i][j] != continents[x][y])
                            //and if this is the first search,
                            //or if my distance integer + next distance integer is smaller than the fastest route
                            if (fastestRoute == -1 || fastestRoute > distance[i][j] + distance[x][y])
                                //replace the fastest route with the newly found route
                                //because the distance 2d array shows how long it takes from a continent to that water
                                //which means the distance value of the current coordinates is the number of leaps it needs to get there
                                //and since the neighbor has the number of leaps to get there, is a BFS from both sides
                                //we just need to add the two and search other edges for the shortest route
                                fastestRoute = distance[i][j] + distance[x][y];
                }
            }
        }
        //this ultimately searches shortest route between continents
        return fastestRoute;
    }
}
