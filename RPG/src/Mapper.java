

/*
	Esta clase se encarga de crear un mapa de coordenadas tras asignarle un tamaño en pixeles.
	El mapa creado es un array de la clase Coor(coordenada) de dos dimensiones.
	La primera celda es la esquina superior izquierda.
*/

public class Mapper {
	static final int cellPixels = 14; //do not change!!
	private Coor[][] map;
	private int X1,Y1;
	private Board a;
	
	public Mapper(int x1, int y1, Board a){
		this.a = a;
		this.X1 = x1;
		this.Y1 = y1;
		map = new Coor[(X1/cellPixels)+1][(Y1/cellPixels)+1];
		
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
		for(int i = 0; i < X1/cellPixels; i++){
			for(int e = 0; e < Y1/cellPixels; e++){
				map[i][e] = new Coor(-(i*cellPixels),-(e*cellPixels)); //se pixela en negativo porque el sistema origen de coordenadas que hemos escogido es justo el contrario al de java
			}
		}
		for(int x = 0;x < X1/cellPixels ; x++ )
        	map[x][0].setAllow(false);
        for(int y = 0;y < Y1/cellPixels ; y++ )
        	map[0][y].setAllow(false);
        for(int x = 0;x < X1/cellPixels ; x++ )
        	map[x][X1/cellPixels-1].setAllow(false);
        for(int y = 0;y < Y1/cellPixels ; y++ )
        	map[Y1/cellPixels-1][y].setAllow(false);
		//map[X/cellPixels][Y/cellPixels] = new Coor(-((X/cellPixels)*cellPixels),-((Y/cellPixels)*cellPixels));
		return map;
	}
	
}
