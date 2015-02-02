# Julia set using Hadoop MR2

Implementation of [Julia set](https://en.wikipedia.org/wiki/Julia_set) using Hadoop 2.6.0 MapReduce (MR2)

### Guide

Build using maven :
```
$ mvn install
```
Generate the input file into a directory named `input` :
```
$ mkdir input
$ java -cp target/julia-0.0.1-SNAPSHOT.jar InputGenerator 1000 > input/file
```

Launch the jobs :
```
$ hadoop jar target/julia-0.0.1-SNAPSHOT.jar Julia -0.75 0.1 600
```

Inspect the result :
```
$ java -cp target/julia-0.0.1-SNAPSHOT.jar OutputViewer 600
```

![](https://i.imgur.com/tKCSHyL.png)
