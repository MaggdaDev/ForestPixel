package org.maggdadev.forestpixel.canvas.utils;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class PixelUtils {

    public static List<int[]> straightLineFromTo(int fromX, int fromY, int toX, int toY) {
        List<int[]> retList = new ArrayList<>();
        int currX = fromX;
        int currY = fromY;
        int tempX, tempY;
        double bestScore = 0, currScore;
        int bestX = 0, bestY = 0;
        while(currX != toX || currY != toY) {
            retList.add(new int[]{currX, currY});
            bestScore = 0;
            for(int i = 0; i < 4; i ++) {
                tempX = currX;
                tempY = currY;
                switch (i) {
                    case 0 -> tempX += 1;
                    case 1 -> tempY += 1;
                    case 2 -> tempX -= 1;
                    case 3 -> tempY -= 1;
                }
                currScore = 1.0 / (Math.pow(toX - tempX, 2.0) + Math.pow(toY - tempY, 2.0));
                if(currScore > bestScore) {
                    bestScore = currScore;
                    bestX = tempX;
                    bestY = tempY;
                }

            }
            currX = bestX;
            currY = bestY;
        }
        retList.add(new int[]{toX, toY});
        return retList;
    }
}
