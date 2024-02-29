/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.geometry.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.meteoinfo.common.MIMath;
import org.meteoinfo.geometry.geoprocess.GeoComputation;

/**
 *
 * @author Yaqiang Wang
 */
public class PolygonZShape extends PolygonShape {
    @Override
    public ShapeTypes getShapeType(){
        return ShapeTypes.POLYGON_Z;
    }
    
    /**
     * Get Z Array
     *
     * @return Z value array
     */
    public double[] getZArray() {
        double[] zArray = new double[this.getPoints().size()];
        for (int i = 0; i < this.getPoints().size(); i++) {
            zArray[i] = ((PointZ)this.getPoints().get(i)).Z;
        }

        return zArray;
    }
    
    /**
     * Get Z range - min, max
     *
     * @return Z min, max
     */
    public double[] getZRange() {
        return MIMath.arrayMinMax(getZArray());
    }
    
    /**
     * Get M Array
     *
     * @return M value array
     */
    public double[] getMArray() {
        double[] mArray = new double[this.getPoints().size()];
        for (int i = 0; i < this.getPoints().size(); i++) {
            mArray[i] = ((PointZ)this.getPoints().get(i)).M;
        }

        return mArray;
    }
    
    /**
     * Get M range - min, max
     *
     * @return M min, max
     */
    public double[] getMRange() {
        return MIMath.arrayMinMax(getMArray());
    }
    
    @Override
    protected void updatePolygons() {
        _polygons = new ArrayList<>();
        if (_numParts == 1) {
            PolygonZ aPolygon = new PolygonZ();
            aPolygon.setOutLine(points);
            ((List<PolygonZ>)_polygons).add(aPolygon);
        } else {
            PointZ[] Pointps;
            PolygonZ aPolygon = null;
            int numPoints = this.getPointNum();
            for (int p = 0; p < _numParts; p++) {
                if (p == _numParts - 1) {
                    Pointps = new PointZ[numPoints - parts[p]];
                    for (int pp = parts[p]; pp < numPoints; pp++) {
                        Pointps[pp - parts[p]] = (PointZ)points.get(pp);
                    }
                } else {
                    Pointps = new PointZ[parts[p + 1] - parts[p]];
                    for (int pp = parts[p]; pp < parts[p + 1]; pp++) {
                        Pointps[pp - parts[p]] = (PointZ)points.get(pp);
                    }
                }
                
                if (GeoComputation.isClockwise(Pointps)) {
                    if (p > 0) {
                        ((List<PolygonZ>)_polygons).add(aPolygon);
                    }
                    
                    aPolygon = new PolygonZ();
                    aPolygon.setOutLine(Arrays.asList(Pointps));
                } else if (aPolygon == null) {
                    MIMath.arrayReverse(Pointps);
                    aPolygon = new PolygonZ();
                    aPolygon.setOutLine(Arrays.asList(Pointps));
                } else {
                    aPolygon.addHole(Arrays.asList(Pointps));
                }
            }
            ((List<PolygonZ>)_polygons).add(aPolygon);
        }
    }

    @Override
    public PolygonShape valueClone() {
        PolygonShape aPGS = new PolygonZShape();
        aPGS.highValue = highValue;
        aPGS.lowValue = lowValue;
        aPGS.setVisible(this.isVisible());
        aPGS.setSelected(this.isSelected());
        aPGS.setLegendIndex(this.getLegendIndex());

        return aPGS;
    }
}
