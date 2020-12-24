package Tomato;

import java.util.*;
import java.io.*;

class Tomato {
    static int N;
    static int M;
    static int[][] storage;
    //combination for (1,0)  (-1,0)  (0,1)  (0,-1)
    static int[] direction_X = {1, -1, 0, 0};
    static int[] direction_Y = {0, 0, 1, -1};

    static class coordinates {
        int x;
        int y;
        int day;

        public coordinates(int x, int y, int day) {
            this.x = x;
            this.y = y;
            this.day = day;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        storage = new int[M][N];

        for(int i=0; i<M; i++) {
            st = new StringTokenizer(br.readLine());

            for(int j=0; j<N; j++) {
                storage[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        bfs();
    }

    static void bfs() {
        Queue<coordinates> queue = new LinkedList<>();
        //create queue by coordinates (x,y,day)
        int day = 0;
        //finding initial tomatoes
        for(int i=0; i<M; i++) {
            for(int j=0; j<N; j++) {
                if(storage[i][j] == 1)
                    //if it is a 1
                    queue.add(new coordinates(i, j, 0));
                    //add to queue
            }
        }
        while(!queue.isEmpty()) {
            coordinates coordinates = queue.poll();
            //pop() for queue
            day = coordinates.day;
            //check the day ; initially 0
            checkFourDirections(coordinates, queue, day);
        }
        if(allTomatoesAreRipe())
            System.out.println(day);
        else
            System.out.println(-1);
    }

    static void checkFourDirections(coordinates coordinates, Queue<coordinates> queue, int day) {
        for (int i = 0; i < 4; i++) {
            //check 4 directions
            int next_X = coordinates.x + direction_X[i];
            int next_Y = coordinates.y + direction_Y[i];
            //first direction is (1,0) which is the coordinates right side
            //second is left side, third is up , fourth is down
            if (0 <= next_X && next_X < M && 0 <= next_Y && next_Y < N) {
                //this if state is if next X and next Y is in the 2d array
                if (storage[next_X][next_Y] == 0) {
                    //if next coordinates are in the storage array and the value is 0,
                    storage[next_X][next_Y] = 1;
                    //we change it to 1 and
                    queue.add(new coordinates(next_X, next_Y, day + 1));
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
        for(int i=0; i<M; i++) {
            for(int j=0; j<N; j++) {
                if(storage[i][j] == 0)
                    return false;
            }
        }
        return true;
    }
}
