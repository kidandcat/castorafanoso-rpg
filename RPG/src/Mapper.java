

/*
	Esta clase se encarga de crear un mapa de coordenadas tras asignarle un tamaño en pixeles.
	El mapa creado es un array de la clase Coor(coordenada) de dos dimensiones.
	La primera celda es la esquina superior izquierda.
*/

public class Mapper {
	private static final int cellPixels = 14; //do not change!!
	private Coor[][] map;
	private int X,Y;
	//private Board a;
	
	public Mapper(int x, int y, Board a){
		this.X = x;
		this.Y = y;
		map = new Coor[(X/cellPixels)+1][(Y/cellPixels)+1];
		//this.a = a;
	}
	
	/*public Coor isCoor(int x, int y){
		Coor b = null;
		int z = 0,v = 0;
		for(int i = 0; i < a.public_MaxMapX/cellPixels; i++){
			for(int e = 0; e < a.public_MaxMapY/cellPixels; e++){
				if(map[i][e].are_these_pixels_from_this_coorX(x)){
					JOptionPane.showMessageDialog(null, "Celda encontrada");
					z = i;
				}
				if(map[i][e].are_these_pixels_from_this_coorY(y))v = e;
			}
		}
		
		b = new Coor(z,v);
		return b;
	}*/
	
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
