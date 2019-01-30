# OSPF
```
A Routing protocol Implementation in java using the UDP Sockets.
```
```
Open source path first constitutes 2 parts :
```
 * Reliable flooding 
 * Finding the shortest path 
 ```
The Algorithm used for finding the shortest path is the Dijkstra's Algorithm with a little modification.
This project is an implementation of such a Routing protocol considering a particular network Instance.
```
## Network Topography
![Alt text](nw.png?raw=true "Screen-Shot")

## Output

### Routing Table for Node A

 Destinition     |      Cost            |        Hop
---------------- |  ------------------- |     ---------
A                |          0           |          _
D                |          2           |          D
B                |          3           |          B
C                |          4           |          B


### Routing Table for Node B

 Destinition     |      Cost            |        Hop
---------------- |  ------------------- |     ---------
B                |          0           |          _
C                |          1           |          C
A                |          3           |          A
D                |          5           |          C

### Routing Table for Node C

 Destinition     |      Cost            |        Hop
---------------- |  ------------------- |     ---------
C                |          0           |          _
B                |          1           |          B
D                |          4           |          D
A                |          4           |          B

### Routing Table for Node D

 Destinition     |      Cost            |        Hop
---------------- |  ------------------- |     ---------
D                |          0           |          _
A                |          2           |          A
C                |          4           |          C
B                |          5           |          A



