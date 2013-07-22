package nl.krisborg.gwt.scrabblesolver.client.utils;

import nl.krisborg.gwt.scrabblesolver.client.grammar.Field;

/**
 * User: Kris
 * Since: 15-10-11 14:39
 */
public abstract class RotationHelper {

    public static Field[][] rotateRight(Field[][] matrix){
        return rotateRight(1, matrix);
    }

    public static Field[][] rotateLeft(Field[][] matrix){
        return rotateRight(3, matrix);
    }

    public static int[] rotateLeft(int[] coordinates, int boardSize){
        int x = coordinates[0];
        int y = coordinates[1];
        int x2 = boardSize - 1 - y;
        int y2 = x;
        return new int[] {x2, y2};
    }

    public static int[] rotateRight(int[] coordinates, int boardSize){
        int x = coordinates[0];
        int y = coordinates[1];
        int x2 = y;
        int y2 = boardSize - 1 - x;
        return new int[] {x2, y2};
    }

    private static Field[][] rotateRight(int times, Field[][] matrix){
        matrix = cloneMatrix(matrix);

        for (int j = 0; j < times; j++){
            // see http://stackoverflow.com/questions/4295418/rotating-a-2-d-array-by-90-degrees
            int matrixSize = matrix.length;
            for (int layer = 0; layer < matrixSize/2; layer++){
                int first = layer;
                int last = matrixSize -1 - layer;
                for(int i = first;i<last;++i){
                    int offset = i - first;
                    Field top = matrix[first][i];
                    matrix[first][i] = matrix[last-offset][first];
                    matrix[last-offset][first] = matrix[last][last-offset];
                    matrix[last][last-offset] = matrix[i][last];
                    matrix[i][last] = top;
                }
            }
        }
        return matrix;
    }

    private static Field[][] cloneMatrix(Field[][] matrix){
        int width = matrix.length;
        int height = matrix[0].length;
        Field[][] result = new Field[width][height];
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                result[x][y] = matrix[x][y].clone();
            }
        }
        return result;
    }
}
