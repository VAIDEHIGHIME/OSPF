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

###Routing Table for Node A

 Destinition     |      Cost            |        Hop
---------------- |  ------------------- |     ---------
A                |          0           |          _
D                |          2           |          D
B                |          3           |          B
C                |          4           |          B



