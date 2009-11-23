/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package breakout;

/**
 *
 * @author akidarsa
 */
public class TwoDVector {

    public TwoDVector(double x, double y, boolean u) {
        tdv_x = x;
        tdv_y = y;
        unitVector = u;

        if (unitVector) {
            makeUnit();
        }
    }

    public void add(TwoDVector vec) {
        tdv_x += vec.tdv_x;
        tdv_y += vec.tdv_y;
        if (unitVector) {
            makeUnit();
        }
    }

    public void sub(TwoDVector vec) {
        tdv_x -= vec.tdv_x;
        tdv_y -= vec.tdv_y;
        if (unitVector) {
            makeUnit();
        }
    }

    public double getDiff(TwoDVector vec) {
        return ((tdv_x - vec.tdv_x) * (tdv_x - vec.tdv_x) + (tdv_y - vec.tdv_y) * (tdv_y - vec.tdv_y)); // deltaX sq + deltaY sq
    }

    public void setX(double newx) {
        tdv_x = newx;
    }

    public void setY(double newy) {
        tdv_y = newy;
    }

    public double getX() {
        return tdv_x;
    }

    public double getY() {
        return tdv_y;
    }

    public void reflect(TwoDVector vin) {
        if ((unitVector == false) || (vin.unitVector == false)) {
            makeUnit();
        }
        double S = innerProduct(vin);
        tdv_x += -2 * S * vin.tdv_x;
        tdv_y += -2 * S * vin.tdv_y;
        makeUnit();
    }

    private double innerProduct(TwoDVector vin) {
        return (tdv_x * vin.tdv_x + tdv_y * vin.tdv_y);
    }

    private void makeUnit() {
        double mag = tdv_x * tdv_x + tdv_y * tdv_y;
        if (mag != 0) {
            mag = Math.sqrt(mag);
            tdv_x /= mag;
            tdv_y /= mag;
        }
    }
    private double tdv_x;
    private double tdv_y;
    private boolean unitVector;
}
