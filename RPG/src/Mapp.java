import java.awt.Image;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


public class Mapp {
	private int MaxMapX, MaxMapY, o, p;
	private Map<Integer, Image> images;
	private Map<Integer, Coor> coors;
	private Coor[][] map;
	private int ID;
	private static Map<Integer, Mapp> maps = null;
	private Set<Npc> npcs = null;
	private SortedMap<Integer,Integer> paintOrder;

	public static void constructor(int ID, int dimensionX, int dimensionY, Map<Integer, Image> images, Map<Integer, Coor> coors, Coor[][] map, int initX, int initY, SortedMap<Integer,Integer> paintOrder){
		if(maps != null){
			Mapp mp = new Mapp(ID, dimensionX, dimensionY, images, coors, map, initX, initY, paintOrder);
			maps.put(ID, mp);
		}else{
			Mapp mp = new Mapp(ID, dimensionX, dimensionY, images, coors, map, initX, initY, paintOrder);
			maps = new TreeMap<Integer, Mapp>();
			maps.put(ID, mp);
		}
	}
	
	public static Mapp changeMap(int ID){
		return maps.get(ID);
	}
	
	private Mapp(int ID, int dimensionX, int dimensionY, Map<Integer, Image> images, Map<Integer, Coor> coors, Coor[][] map, int initX, int initY, SortedMap<Integer,Integer> paintOrder){
		this.MaxMapX = dimensionX;
    	this.MaxMapY = dimensionY;
    	this.ID = ID;
    	this.images = images;
    	this.paintOrder = paintOrder;
    	this.coors = coors;
    	this.map = map;
    	this.o = initX;
    	this.p = initY;
    	npcs = Npc.list();
    	Npc.set_list(null);
	}
	public Set<Npc> npcs(){
		return npcs;
	}
	
	public int MaxMapX(){
		return MaxMapX;
	}
	
	public void setNPCList(Set<Npc> npcs){
		this.npcs = npcs;
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
	
	public SortedMap<Integer, Integer> paintOrder(){
		return paintOrder;
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





































