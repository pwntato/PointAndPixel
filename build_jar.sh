#!/bin/bash

#compile
javac PointAndPixel.java pointAndPixel/*.java && jar cfm PointAndPixel.jar Manifest.txt PointAndPixel.class pointAndPixel/*.class && java -jar PointAndPixel.jar 


