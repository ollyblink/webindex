package tryouts;
 
import index.spatialindex.utils.geolocating.georeferencing.YPMPlaceExtractor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import com.vividsolutions.jts.geom.Geometry;
 

public class Geonames {
	private static class Point {
		double lat;
		double lon;
		public Point(double lat, double lon) {
			this.lat = lat;
			this.lon = lon;
		}
		
		
	}
	public static void main(String[] args) {
		YPMPlaceExtractor p = new YPMPlaceExtractor("C:\\Users\\rsp\\git\\Webindex\\Webindex\\ypm.xml");
		ArrayList<Geometry> extract = p.extract("Daubensee, Schweiz");
		for(Geometry g: extract){
			System.out.println(g);
			System.out.println(g.getArea());
		}
		
//		StandardDeviation s = new StandardDeviation();
//		Mean m = new Mean();
//		double[] lats = new double[]{46.25, 46.38,46.88, 46.38, 46.38,46.38,46.88,46.88,46.38};
//		double[] longs = new double[]{7.62,7.62,8.25,7.62,7.62,7.62,8.25,8.25,7.62};
//		
//		System.out.println(lats.length == longs.length);
//		System.out.println("Mean lats: "+m.evaluate(lats));
//		System.out.println("Mean longs: " +m.evaluate(longs));
//		System.out.println("Lats std: "+s.evaluate(lats));
//		System.out.println("Longs std: " +s.evaluate(longs));
//		
//		double latMean, lonMean,latStd, lonStd;
//		latMean = m.evaluate(lats);
//		lonMean = m.evaluate(longs);
//		latStd = s.evaluate(lats);
//		lonStd = s.evaluate(longs);
//		double latLowerBound = (latMean-(2*latStd));
//		double latUpperBound = (latMean+(2*latStd));
//		double lonLowerBound = (lonMean-(2*lonStd));
//		double lonUpperBound = (lonMean+(2*lonStd));
//		System.out.println("lats from "+ (latMean-(2*latStd)) + " to " +(latMean + (2*latStd)));
//		System.out.println("lons from "+ (lonMean-(2*lonStd)) + " to " +(lonMean + (2*lonStd)));
//		
//		List<Point> p = new ArrayList<Point>();
//		for(int i = 0; i < lats.length;++i){
//			p.add(new Point(lats[i], longs[i]));
//		}
//		
//		List<Point> remaining = new ArrayList<Point>();
//		for(Point p1:p){
//			if((p1.lat > latUpperBound || p1.lat < latLowerBound) || (p1.lon > lonUpperBound || p1.lon < lonLowerBound)){
//				continue;
//			}else{
//				remaining.add(p1);
//			}
//		}
//		
//		for(Point p1: remaining){
//			System.out.println(p1.lat + ", " +p1.lon);
//		}
	}
}
