package com.blastedstudios.crittercaptors.util;

/**
 * Following information from http://wiki.openstreetmap.org/wiki/Mercator
 * Adapted from c# version by Florian Muller
 */
public class MercatorUtil {
	private static final double R_MAJOR = 6378137.0;
    private static final double R_MINOR = 6356752.3142;
    private static final double RATIO = R_MINOR / R_MAJOR;
    private static final double ECCENT = Math.sqrt(1.0 - (RATIO * RATIO));
    private static final double COM = 0.5 * ECCENT;
    private static final double PI_2 = Math.PI / 2.0;
 
    public static double[] toPixel(double lon, double lat)
    {
        return new double[] { lonToX(lon), latToY(lat) };
    }
 
    public static double[] toGeoCoord(double x, double y)
    {
        return new double[] { xToLon(x), yToLat(y) };
    }
 
    private static double lonToX(double lon)
    {
        return R_MAJOR * Math.toRadians(lon);
    }
 
    private static double latToY(double lat)
    {
        lat = Math.min(89.5, Math.max(lat, -89.5));
        double phi = Math.toRadians(lat);
        double sinphi = Math.sin(phi);
        double con = ECCENT * sinphi;
        con = Math.pow(((1.0 - con) / (1.0 + con)), COM);
        double ts = Math.tan(0.5 * ((Math.PI * 0.5) - phi)) / con;
        return 0 - R_MAJOR * Math.log(ts);
    }
 
    private static double xToLon(double x)
    {
        return Math.toDegrees(x) / R_MAJOR;
    }
 
    private static double yToLat(double y)
    {
        double ts = Math.exp(-y / R_MAJOR);
        double phi = PI_2 - 2 * Math.atan(ts);
        double dphi = 1.0;
        int i = 0;
        while ((Math.abs(dphi) > 0.000000001) && (i < 15))
        {
            double con = ECCENT * Math.sin(phi);
            dphi = PI_2 - 2 * Math.atan(ts * Math.pow((1.0 - con) / (1.0 + con), COM)) - phi;
            phi += dphi;
            i++;
        }
        return Math.toDegrees(phi);
    }
}
