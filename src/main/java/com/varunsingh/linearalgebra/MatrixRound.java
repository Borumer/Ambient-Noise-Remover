package com.varunsingh.linearalgebra;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MatrixRound {
    static Matrix roundMatrixToNearestThousandth(Matrix unroundedMatrix) {
        Matrix roundedMatrix = new Matrix(new double[unroundedMatrix.getRows()][unroundedMatrix.getColumns()]);
        
        for (int i = 0; i < roundedMatrix.getRows(); i++) {
            for (int j = 0; j < roundedMatrix.getColumns(); j++) {
                roundedMatrix.set(i, j, roundDouble(unroundedMatrix.get(i, j), 3));
            }
        }

        return roundedMatrix;
    }

    static double roundDouble(double d, int places) {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(d));
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        
        return bigDecimal.doubleValue();
    }
}