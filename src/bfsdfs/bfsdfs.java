package bfsdfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class bfsdfs { // 11049 행렬 곱셈 순서
    private StringTokenizer stringTokenizer;
    private final BufferedReader bufferedReader;

    public bfsdfs() {
        this.stringTokenizer = new StringTokenizer("");
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    private String nextLine() {
        try {
            return this.bufferedReader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    private String next() {
        while (!this.stringTokenizer.hasMoreTokens()) {
            this.stringTokenizer = new StringTokenizer(Objects.requireNonNull(this.nextLine()));
        }
        return this.stringTokenizer.nextToken();
    }

    private int nextInt() {
        return Integer.parseInt(this.next());
    }

    private long nextLong() {
        return Long.parseLong(this.next());
    }

    private boolean end() {
        try {
            this.bufferedReader.close();
        } catch (IOException e) {
            return false;
        }

        return true;
    }


    static List<List<Integer>> adjacentcyListData;

    private static void initList(int lengthOfList){
        adjacentcyListData = new ArrayList<>();
        for (int index =0; index < lengthOfList; index++) {
            adjacentcyListData.add(new ArrayList<>());
        }
    }
    //saving connected nodes

    static boolean[] isRevisit;


    static void dfsRecursive(int startingNode) {
    //doing this recursively
        if (isRevisit[startingNode])
            return;
        isRevisit[startingNode]= true;
        System.out.print(startingNode + " ");
        //for the next nodes in the problem's format
        for (int connectedNode : adjacentcyListData.get(startingNode)) {
            //in the adjacentcy list, search for connected nodes of the starting node
            if (!isRevisit[connectedNode]) {
                dfsRecursive(connectedNode);
                //do this and recursively searches every connected node in depth priority
            }
        }
    }


    static void dfsStack(int startingNode) {
        //doing this with queue
        Stack<Integer> stack = new Stack<Integer>();
        //Generic? i guess
        stack.push(startingNode);
        //starting at startingNode
        isRevisit[startingNode] = true;
        //I definately revisited this one
        while (!stack.isEmpty()) {
            //do until stack is empty
            int whatsNext = stack.pop();
            //first pop would be starting node then the next one in the stack and so on
            System.out.print(whatsNext + " ");
            //if its in the stack, we visited it
            for (int connectedNode : adjacentcyListData.get(whatsNext)) {
                //for every connected node of current node and the lookups of the adjacentcy list
                //in stack, we only need the first one.

                if (!isRevisit[connectedNode]) {
                    //if we didnt visit the node,
                    stack.push(connectedNode);
                    //add it to the stack and lookup from there
                    isRevisit[connectedNode] = true;
                    //since its in the queue, we dont want to revisit it
                    if(stack.size() > 1){
                        stack.pop();
                        //since for each searches from bottom up, the first one is the one we want so pop everything else
                        isRevisit[connectedNode] = false;
                        //we didnt visit it so remove it from the revisit list
                    }

                }

            }
        }
    }

    static void bfs(int startingNode) {
    //doing this with queue
        Queue<Integer> queue = new LinkedList<>();
        //Generic? i guess
        queue.add(startingNode);
        //starting at startingNode
        isRevisit[startingNode] = true;
        //I definately revisited this one
        while (!queue.isEmpty()) {
            //do until queue is empty
            int whatsNext = queue.remove();
            //first remove would be starting node then the next one in the queue and so on
            System.out.print(whatsNext + " ");
            //if its in the queue, we visited it
            for (int connectedNode : adjacentcyListData.get(whatsNext)) {
                //for every connected node of current node and the lookups of the adjacentcy list
                if (!isRevisit[connectedNode]) {
                    //if we didnt visit the node,
                    queue.add(connectedNode);
                    //add it to the queue and lookup from there
                    isRevisit[connectedNode] = true;
                    //since its in the queue, we dont want to revisit it
                }
            }
        }
    }

    public void solve() {

        int vertexes = nextInt();
        int edges = nextInt();
        int startingNode = nextInt();
        //vertexes, edges, and the starting node in the first three integers

        initList(vertexes+1);
        //has to cast for some reason, did vertexes + 1 for easy mapping 1~n instead of 0~n-1


        for (int i=0;i<edges;i++) {
            int thisIsConnected = nextInt();
            int withThisNode = nextInt();
            adjacentcyListData.get(thisIsConnected).add(withThisNode);
            adjacentcyListData.get(withThisNode).add(thisIsConnected);
            //5 4 is the same edge as 4 5 we just need to remember we visited for no errors
        }

        for (int i=1;i<=vertexes;i++)
            Collections.sort(adjacentcyListData.get(i));
        //sorting i think in bubble sort because we visit the smaller value first

        isRevisit = new boolean[vertexes+1];
        //emptying revisiting list
        dfsRecursive(startingNode);
        System.out.println();
        isRevisit = new boolean[vertexes+1];
        //emptying revisiting list
//        dfsStack(startingNode);
        System.out.println();
        isRevisit = new boolean[vertexes+1];
        //emptying revisiting list
        bfs(startingNode);

    }

    public static void main(String[] args) {
        new bfsdfs().solve();
    }
}