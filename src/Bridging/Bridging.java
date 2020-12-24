package Bridging;

import java.util.*;
import java.io.*;

class Bridging {

    static int N;
    static int[][] Map;
    static int[][] Continents;
    static int[][] Distance;
    //combination for (1,0)  (-1,0)  (0,1)  (0,-1)
    static int[] direction_X = {1, -1, 0, 0};
    static int[] direction_Y = {0, 0, 1, -1};

    static class coordinates {
        int x;
        int y;

        public coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        Map = new int[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                Map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        solve();
    }

    static void solve() {

        Continents = new int[N][N];
        Distance = new int[N][N];
        int continentNumber = 0;

        /*first, we identify the given map and see how many continents there are and where they are*/

        //check all coordinates
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                //if i haven't labeled the land on the continent,
                if (Map[i][j] == 1 && Continents[i][j] == 0) {
                    Queue<Bridging.coordinates> continentQueue = new LinkedList<>();
                    //give the continent a number
                    Continents[i][j] = ++continentNumber;
                    //and add it to the continent Queue
                    continentQueue.add(new coordinates(i, j));
                    //we're checking 4 directions of a coordinate to see which continent they belong to
                    while (!continentQueue.isEmpty()) {
                        coordinates coordinates = continentQueue.remove();
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
        Queue<Bridging.coordinates> distanceQueue = new LinkedList<>();
        //check every coordinates
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                //set all distances to -1 initially
                Distance[i][j] = -1;
                //if it is a land,
                if (Map[i][j] == 1) {
                    //add it to the queue
                    distanceQueue.add(new coordinates(i, j));
                    //and set the distance to 0 -> this marks all water -1 and all land 0 at first
                    Distance[i][j] = 0;
                }
            }
        }
        //for every land coordinate
        while (!distanceQueue.isEmpty()) {
            coordinates coordinates = distanceQueue.remove();
            //figure out the distance of the neighboring coordinates
            for (int i = 0; i < 4; i++) {
                identifyContinentsDistance(coordinates, distanceQueue);
            }
        }
        //and find the fastest route
        System.out.println(findFastestRoute());
    }

    static void identifyContinents(Bridging.coordinates coordinates, Queue<Bridging.coordinates> queue, int continentNumber) {
        for (int i = 0; i < 4; i++) {
            //check 4 directions
            //first direction is (1,0) which is the coordinates right side
            //second is left side, third is up , fourth is down
            int next_X = coordinates.x + direction_X[i];
            int next_Y = coordinates.y + direction_Y[i];
            //this if state is if next X and next Y is in the 2d array
            if (0 <= next_X && next_X < N && 0 <= next_Y && next_Y < N) {
                //if the next land coordinate is not marked as a continent
                if (Map[next_X][next_Y] == 1 && Continents[next_X][next_Y] == 0) {
                    //add it to the queue
                    queue.add(new coordinates(next_X, next_Y));
                    //and give it a continent number
                    Continents[next_X][next_Y] = continentNumber;
                }
            }
        }
    }

    static void identifyContinentsDistance(Bridging.coordinates coordinates, Queue<Bridging.coordinates> queue) {
        for (int i = 0; i < 4; i++) {
            //check 4 directions
            //first direction is (1,0) which is the coordinates right side
            //second is left side, third is up , fourth is down
            int next_X = coordinates.x + direction_X[i];
            int next_Y = coordinates.y + direction_Y[i];
            //this if state is if next X and next Y is in the 2d array
            if (0 <= next_X && next_X < N && 0 <= next_Y && next_Y < N) {
                //if the neighboring coordinates is water,
                if (Distance[next_X][next_Y] == -1) {
                    //increment the distance of the neighboring coordinates from current coordinates -> this means its the edge
                    Distance[next_X][next_Y] = Distance[coordinates.x][coordinates.y] + 1;
                    //now lets say that the water on the edge is also part of the continent
                    //this shows the distance to another continent of every coordinate
                    Continents[next_X][next_Y] = Continents[coordinates.x][coordinates.y];
                    //and add these coordinates to the queue, judging how much water there is between continents
                    queue.add(new coordinates(next_X, next_Y));
                }
            }
        }
    }

    static int findFastestRoute() {
        //init
        int fastestRoute = -1;
        //check every coordinates
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                //for every neighbor,
                for (int k = 0; k < 4; k++) {
                    int x = i + direction_X[k];
                    int y = j + direction_Y[k];
                    //if the neighbor is in the map
                    if (0 <= x && x < N && 0 <= y && y < N)
                        //and if the searched coordinate's neighbor is another continent,
                        if (Continents[i][j] != Continents[x][y])
                            //and if this is the first search,
                            //or if my distance integer + next distance integer is smaller than the fastest route
                            if (fastestRoute == -1 || fastestRoute > Distance[i][j] + Distance[x][y])
                                //replace the fastest route with the newly found route
                                //because the distance 2d array shows how long it takes from a continent to that water
                                //which means the distance value of the current coordinates is the number of leaps it needs to get there
                                //and since the neighbor has the number of leaps to get there, is a BFS from both sides
                                //we just need to add the two and search other edges for the shortest route
                                fastestRoute = Distance[i][j] + Distance[x][y];
                }
            }
        }
        //this ultimately searches shortest route between continents
        return fastestRoute;
    }
}
