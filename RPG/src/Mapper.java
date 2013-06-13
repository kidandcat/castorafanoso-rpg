/*
	Esta clase se encarga de crear un mapa de coordenadas tras asignarle un tamaño en pixeles.
	El mapa creado es un array de la clase Coor(coordenada) de dos dimensiones.
	La primera celda es la esquina superior izquierda.
*/

public class Mapper {
	private static final int cellPixels = 14;
	private Coor[][] map;
	private int X,Y;
	
	public Mapper(int x, int y){
		this.X = x;
		this.Y = y;
		map = new Coor[(X/cellPixels)+1][(Y/cellPixels)+1];
	}
	
	public Coor[][] init(){
		for(int i = 0; i < X/cellPixels; i++){
			for(int e = 0; e < Y/cellPixels; e++){
				map[i][e] = new Coor(-(i*cellPixels),-(e*cellPixels)); //se pixela en negativo porque el sistema origen de coordenadas que hemos escogido es justo el contrario al de java
			}
		}
		//map[X/cellPixels][Y/cellPixels] = new Coor(-((X/cellPixels)*cellPixels),-((Y/cellPixels)*cellPixels));
		return map;
	}
	
}
