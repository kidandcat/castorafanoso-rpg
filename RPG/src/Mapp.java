import java.awt.Image;
import java.util.Map;
import java.util.TreeMap;


public class Mapp {
	private int MaxMapX, MaxMapY;
	private Map<Integer, Image> images;
	private Map<Integer, Coor> coors;
	private Coor[][] map;
	private int ID;
	private Coor cell;
	private static Map<Integer, Mapp> maps = null;

	public static void constructor(int ID, int dimensionX, int dimensionY, Map<Integer, Image> images, Map<Integer, Coor> coors, Coor[][] map){
		if(maps != null){
			Mapp mp = new Mapp(ID, dimensionX, dimensionY, images, coors, map);
			maps.put(ID, mp);
		}else{
			Mapp mp = new Mapp(ID, dimensionX, dimensionY, images, coors, map);
			maps = new TreeMap<Integer, Mapp>();
			maps.put(ID, mp);
		}
	}
	
	public static Mapp changeMap(int ID){
		return maps.get(ID);
	}
	
	private Mapp(int ID, int dimensionX, int dimensionY, Map<Integer, Image> images, Map<Integer, Coor> coors, Coor[][] map){
		MaxMapX = dimensionX;
    	MaxMapY = dimensionY;
    	this.ID = ID;
    	this.images = images;
    	this.coors = coors;
    	this.map = map;
    	cell = map[2][2];
    	
	}
	
	public int MaxMapX(){
		return MaxMapX;
	}
	
	public int MaxMapY(){
		return MaxMapY;
	}
	
	public int ID(){
		return ID;
	}
	
	public Map<Integer, Image> images(){
		return images;
	}
	
	public Map<Integer, Coor> coors(){
		return coors;
	}

	public Coor[][] map(){
		return map;
	}
	
	public Coor init_cell(){
		return cell;
	}
}





































