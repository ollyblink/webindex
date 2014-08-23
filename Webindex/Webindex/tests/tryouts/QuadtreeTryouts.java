package tryouts;
 
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.objectweb.asm.Type;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.index.quadtree.Node;
import com.vividsolutions.jts.index.quadtree.Quadtree;
import com.vividsolutions.jts.index.quadtree.Root;

public class QuadtreeTryouts {
	private static final int wgs84srid = 4326;
	
	
	public static void main(String[] args) throws Exception {
		Quadtree tree = new Quadtree();
		GeometryFactory g = new GeometryFactory(new PrecisionModel(Type.DOUBLE), wgs84srid);
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		Random generator = new Random();
		double lat = 0;
		double lon = 0;
		for(int i = 0; i< 100000;++i){
			lat = getLatitude(generator);
			lon = getLongitude(generator); 
			coordinates.add(new Coordinate(lon, lat));
		}
		for(Coordinate coord: coordinates){
			Point point = g.createPoint(coord);
			tree.insert(g.createPoint(coord).getEnvelopeInternal(), point);
		}
		 
		//Query MBR:
		Coordinate[] queryMBR  = new Coordinate[5];
		
		queryMBR[0] = (new Coordinate(45.13502, 4.80038));
		queryMBR[1] = (new Coordinate(45.13502, 10.714795));
		queryMBR[3] =(new Coordinate(48.24589, 10.714795));
		queryMBR[2] = (new Coordinate(48.24589, 4.80038));
		queryMBR[4] = (new Coordinate(45.13502, 4.80038));
		
		Polygon queryPolygon = g.createPolygon(queryMBR);
//		System.out.println(queryPolygon);
		
		//Filter
		List<Point> objects = (List<Point>)tree.query(queryPolygon.getEnvelopeInternal());
		
		//Step 2
//		for(Point point: objects){ 
//			if(queryPolygon.contains(point)){ 
//				System.out.println(point +" is inside " + queryPolygon);
//			}
//		}
		
		 Field rootField = tree.getClass().getDeclaredField("root");
		 rootField.setAccessible(true);
		 Root root = (Root) rootField.get(tree);
		 Method depthMethod = root.getClass().getSuperclass().getDeclaredMethod("depth");
		 depthMethod.setAccessible(true);
		 
		 System.out.println(depthMethod.invoke(root)); 
		 
		 Field subnodesField =  root.getClass().getSuperclass().getDeclaredField("subnode");
		 subnodesField.setAccessible(true);
		 Node[] subnodes = (Node[]) subnodesField.get(root);
		 for(Node node: subnodes){
			 Field levelField = node.getClass().getDeclaredField("level");
			 levelField.setAccessible(true); 
			 System.out.println(levelField.get(node));
		 }
	}


	private static double getLatitude(Random generator) {
		return (generator.nextDouble()*180)-90;   
	}


	private static double getLongitude(Random generator) { 
		return (generator.nextDouble()*360)-180;
	}
}
