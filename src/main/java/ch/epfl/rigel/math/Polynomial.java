package ch.epfl.rigel.math;

import static ch.epfl.rigel.Preconditions.checkArgument;

/**
 * A Polynomial.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class Polynomial {

    private double[] coefficientsArray;

    private Polynomial(double coefficientN, double... coefficients) {
        coefficientsArray = new double[coefficients.length + 1];
        coefficientsArray[0] = coefficientN;
        System.arraycopy(coefficients, 0, coefficientsArray, 1,
                coefficients.length);
    }

    /**
     * Returns a Polynomial from a list of values (in a decreasing order). Throws an IllegalArgumentException if the first value equals 0.
     * 
     * @param double (coefficientN)
     * @param double... (coefficients)
     * @return Polynomial (polynomial)
     */
    public static Polynomial of(double coefficientN, double... coefficients) {
        checkArgument(coefficientN != 0);
        return new Polynomial(coefficientN, coefficients);
    }

    /**
     * Evaluates the polynomial at a given value x.
     * 
     * @param double (x)
     * @return double (result)
     */
    public double at(double x) {
        double result = 0;
        for (int i = 0; i < coefficientsArray.length - 1; ++i) {
            result = (result + coefficientsArray[i]) * x;
        }
        result += coefficientsArray[coefficientsArray.length - 1];
        return result;
    }

    /**
     * Redefines the toString method. Expresses a Polynomial in the form : anx^n+an-1x^n-1+...+a1x+a0. Only
     * expresses useful terms.
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coefficientsArray.length; ++i) {
            int power = coefficientsArray.length - i - 1;
            double coefficient = coefficientsArray[i];
            if (coefficient != 0) {
                if (i == 0) {
                    if (power == 1) {
                        if (coefficient == 1) {
                            sb.append("x");
                        } else if (coefficient == -1) {
                            sb.append("-x");
                        } else {
                            sb.append(coefficient + "x");
                        }
                    } else if (power == 0) {
                        sb.append(coefficient);
                    } else {
                        if (coefficient == 1) {
                            sb.append("x^" + power);
                        } else if (coefficient == -1) {
                            sb.append("-" + "x^" + power);
                        } else {
                            sb.append(coefficient + "x^" + power);
                        }
                    }
                } else {
                    if (coefficient > 0) {
                        sb.append("+");
                    }
                    if (power == 1) {
                        if (coefficient == 1) {
                            sb.append("x");
                        } else if (coefficient == -1) {
                            sb.append("-x");
                        } else {
                            sb.append(coefficient + "x");
                        }
                    } else if (power == 0) {
                        sb.append(coefficient);
                    } else {
                        if (coefficient == 1) {
                            sb.append("x^" + power);
                        } else if (coefficient == -1) {
                            sb.append("-" + "x^" + power);
                        } else {
                            sb.append(coefficient + "x^" + power);
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}