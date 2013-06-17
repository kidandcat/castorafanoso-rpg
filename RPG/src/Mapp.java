import java.awt.Image;
import java.util.Map;
import java.util.TreeMap;


public class Mapp {
	private int MaxMapX, MaxMapY, o, p;
	private Map<Integer, Image> images;
	private Map<Integer, Coor> coors;
	private Coor[][] map;
	private int ID;
	private static Map<Integer, Mapp> maps = null;

	public static void constructor(int ID, int dimensionX, int dimensionY, Map<Integer, Image> images, Map<Integer, Coor> coors, Coor[][] map, int initX, int initY){
		if(maps != null){
			Mapp mp = new Mapp(ID, dimensionX, dimensionY, images, coors, map, initX, initY);
			maps.put(ID, mp);
		}else{
			Mapp mp = new Mapp(ID, dimensionX, dimensionY, images, coors, map, initX, initY);
			maps = new TreeMap<Integer, Mapp>();
			maps.put(ID, mp);
		}
	}
	
	public static Mapp changeMap(int ID){
		return maps.get(ID);
	}
	
	private Mapp(int ID, int dimensionX, int dimensionY, Map<Integer, Image> images, Map<Integer, Coor> coors, Coor[][] map, int initX, int initY){
		this.MaxMapX = dimensionX;
    	this.MaxMapY = dimensionY;
    	this.ID = ID;
    	this.images = images;
    	this.coors = coors;
    	this.map = map;
    	this.o = initX;
    	this.p = initY;
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
	
	public int init_X(){
		return o;
	}
	public int init_Y(){
		return p;
	}
}





































