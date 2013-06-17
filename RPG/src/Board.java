/*
 * COPYRIGHT: CastorAfanoso 
 * author: Jairo Caro-Accino Viciana
 * e-mail: kidandcat@gmail.com
 * License: Feel free to copy, modify, and share!
 * Share any change!
 */


//clase encargada del renderizado asi como del movimiento

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Board extends JPanel implements Runnable, ActionListener{
	/*VARIABLES*/
	private Coor cell;	//celda actual
	public Coor publicCell = cell;	//copia de cell publica para que el sistema de debug y otras clases puedan acceder en tiempo real a la variable sin necesidad de un metodo
	Coor[][] map;	//mapa actual
	private int MaxMapX = 800;	//maximo tamaño horizontal del mapa actual PIXELES
	private int MaxMapY = 800;	//maximo tamaño vertical del mapa actual PIXELES
	int public_MaxMapX;
	int public_MaxMapY;
	final int MOVEMENT_SPEED = 300;	//velocidad del movimiento (solo pasos, el movimiento de renderizado es otra cosa)
	static final int RENDER_SPEED = 5;	//velocidad de renderizado(velocidad de refresco de la pantalla y todas las acciones que implica un frame)
	private int anim = 0;	//variable para animacion
	boolean anima = true,animac = true, movv = true;	//variable para animacion
	private int o=0,p=0;	//coordenada actual;
	private String move;
	public int public_o = o, public_p = p;	//copia publica (igual que publicCell)
	//private boolean end;	legacy variable
	private Thread animator;	//variable Thread para que board pueda autoiniciarse (ver metodo addNotify())
	private String nextMov = "";	//siguiente movimiento del personaje main
	private Map<Integer, Image> images;
	private Map<Integer, Coor> coors;
	Ia ia;
	/*METODOS*/
	/*public synchronized boolean isEnd(){	legacy method
			return end;
		}*/
	
	    public Board() {
	    	setDoubleBuffered(true);	//se requiere un buffer de dibujo el cual se dibuja finalmente en pantalla (cuestiones menores de refresco de pantalla)
	        
	        
	        
	        Map<Integer, Image> images3 = Collections.synchronizedSortedMap(new TreeMap<Integer, Image>()); 
	        Map<Integer, Coor> coors3 = Collections.synchronizedSortedMap(new TreeMap<Integer, Coor>()); 
	        ImageIcon a2 = new ImageIcon("2.gif");
		    Image b2 = a2.getImage();
	        ImageIcon a3 = new ImageIcon("bosque.png");
		    Image b3 = a3.getImage();
		    ImageIcon a1 = new ImageIcon("abajo_quieto.png");
		    Image b1 = a1.getImage();
		    images3.put(-2, b3);
		    images3.put(-1, b2);
		    images3.put(3, b1);
		    coors3.put(3, new Coor(0,0));
		    coors3.put(-1, new Coor(420,310));
		    coors3.put(-2, new Coor(-300,-300));
		    Coor[][] map3 = new Mapper(800,800,this).init();
	        Mapp.constructor(2, 800, 800, images3, coors3, map3, 2, 2);
	        /*Nuevo metodo de limitacion de mapas*/ //Se bloquean las celdas exteriores del mapa
	        
	        changeMap();
	        ia = new Ia();
	    	Npc.constructor(this, 4, MOVEMENT_SPEED, "copia", ia);
	    }
	    
	    public void create_map(){
	    	new JFileChooser().showOpenDialog(this);
	    	Map<Integer, Image> images3 = Collections.synchronizedSortedMap(new TreeMap<Integer, Image>()); 
	        Map<Integer, Coor> coors3 = Collections.synchronizedSortedMap(new TreeMap<Integer, Coor>()); 
	        int num_images = Integer.parseInt(JOptionPane.showInputDialog("Numero de imagenes: "));
	        for(int i=0;i<num_images;i++){
	        	ImageIcon a2 = new ImageIcon(JOptionPane.showInputDialog("Ruta de imagen: "));
	        	Image b2 = a2.getImage();
	        	int ID = Integer.parseInt(JOptionPane.showInputDialog("ID de imagen: "));
	        	images3.put(ID, b2);
	        	coors3.put(ID, new Coor(Integer.parseInt(JOptionPane.showInputDialog("Coordenada X: ")),Integer.parseInt(JOptionPane.showInputDialog("Coordenada Y: "))));
	        }
	        int mapx = Integer.parseInt(JOptionPane.showInputDialog("Anchura del mapa: "));
	        int mapy = Integer.parseInt(JOptionPane.showInputDialog("Altura del mapa: "));
		    Coor[][] map3 = new Mapper(mapx,mapy,this).init();
	        Mapp.constructor(Integer.parseInt(JOptionPane.showInputDialog("ID del mapa: ")), mapx, mapy, images3, coors3, map3, Integer.parseInt(JOptionPane.showInputDialog("X de inicio: ")), Integer.parseInt(JOptionPane.showInputDialog("Y de inicio: ")));
	    }
	    
	    
	    
	    public void changeMap(){
	    	Mapp new_map = Mapp.changeMap(Integer.parseInt(JOptionPane.showInputDialog("ID del mapa: ")));
	    	this.images = new_map.images();
	    	this.coors = new_map.coors();
	    	this.MaxMapX = new_map.MaxMapX();
	    	this.MaxMapY = new_map.MaxMapY();
	    	this.map = new_map.map();
	    	this.o = new_map.init_X();
	    	this.p = new_map.init_Y();
	    	this.cell = map[o][p];
	    	Npc.set_list(new_map.npcs());
	    	
	    }
	    
	    
	    public void paint(Graphics g) {		//pintado de pantalla, este metodo no se ejecuta por nosotros mismos, el sistema de graficos lo ejecuta cada vez que llamamos a repaint()
	    	Graphics2D g2d = (Graphics2D) g;	//grafico actual
	    	Iterator<Image> it = images.values().iterator();
	    	Iterator<Coor> it2 = coors.values().iterator();
	    	for(int i=0; i<images.size();i++){
	    		Image a = it.next();
	    		Coor d = it2.next();
	    		if(!a.equals(images.get(3))){
	    			g2d.drawImage(a, cell.X()+d.X(), cell.Y()+d.Y(), null);
	    		}else{
	    			g2d.drawImage(a, 420, 310, null);
	    		}
	    	}
	        Toolkit.getDefaultToolkit().sync();		//sincronizacion necesaria para sistemas unix
	        g.dispose();
	    }

	    public void addNotify(){	//metodo para que esta clase pueda autoiniciar su metodo run()
	    	/*
	    	 * Notifies this component that it now has a parent component. When this method is invoked, 
	    	 * the chain of parent components is set up with KeyboardAction event listeners. This method 
	    	 * is called by the toolkit internally and should not be called directly by programs.
	    	 */
	    	super.addNotify(); 
	    	animator = new Thread(this);	//inicia el metodo run()
	    	animator.start();
	        Timer timer = new Timer(MOVEMENT_SPEED/2, this);	//crea un timer que ejecutara el ActionListener de esta clase cada vez que pase el tiempo indicado
	        timer.start();
	    }
	    
	   public synchronized void mov(String d){	//actualiza la variable de movimiento
		   this.nextMov = d;
	   }
	    
	   private void move(String s){	//inicia los metodos necesarios para moverse en la direccion indicada;
		   switch(s){
		   case "R": moveR();
		   break;
		   case "L": moveL();
		   break;
		   case "U": moveU();
		   break;
		   case "D": moveD();
		   break;
		   }
	   }
	   
	    private synchronized void moveR(){	//movimientos y control de limites de mapa (despues de una enorme rayada mental, todos los bordes limitados y celdas notAllow en funcionamiento)
	    	/*debido al nuevo metodo de limite de mapa, las comprobaciones (o<0) y (o > (MaxMapX/Mapper.cellPixels)-1) quedan fuera de uso, pero permaneceran por la dificultad de limpiar todo el codigo*/
	    	
	    	if(!anima)	//no se cambia la posicion si es el turno de la animacion
	    	o++;	//se avanza en una casilla
	    	//JOptionPane.showMessageDialog(null, (MaxMapX/13)-1);	//debug method
	    	if(o < 0){	//limite izquierdo /*Legacy*/
	    		o = 0;	/*Legacy*/
	    	}else{
	    	if(o > (MaxMapX/Mapper.cellPixels)-1){	//limite derecho /*Legacy*/
	    		o = (MaxMapX/Mapper.cellPixels)-1;	/*Legacy*/
	    		anima = true;	//se termina la animacion
	    		cell.undo_offsetR();	//se resetea el offset
	    	}else{
	    		Coor evalCell = map[o][p];	//celda de evaluacion
	    			if(anima){	//turno de la animacion
	    				if(anim == 0){	//alternando entre paso izquierdo y paso derecho
	    					newImage(3,"derecha_andando.png",new Coor(0,0));//se actualiza la imagen
	    					cell.offsetR();	//offset a la celda actual para efecto de paso
	    					anima = false;	//turno de animacion finalizado
	    					anim++;	//alternando entre pasos
	    				}else{
	    					newImage(3,"derecha_andando2.png",new Coor(0,0));//se actualiza la imagen
	    					cell.offsetR();	//offset a la celda actual para efecto de paso
	    					anima = false;	//turno de animacion finalizado
	    					anim--; //alternando entre pasos
	    				}
		    	    }else if(evalCell.isAllow()){	//turno movimiento, solo si la celda a la que nos movemos esta disponible
	    				cell.undo_offsetR();	//desacemos el offset de la celda atual
	    				cell.setAllow(true);	//la desbloqueamos
	    				this.cell = evalCell;	//avanzamos a la siguiente celda
	    				cell.setAllow(false);	//la bloqueamos
	    				newImage(3,"derecha_quieto.png",new Coor(0,0));//se actualiza la imagen
	    				anima = true;	//fin turno de movimiento
	    			}else{	//si la celda no permite el movimiento a ella
	    				anima = true;	//fin turno movimiento
	    				cell.undo_offsetR();	//desacemos offset
	    				o--;	//regresamos las coordenadas a su origen
	    		}
	    	}
	    	}
	    }
	    
	    private synchronized void moveL(){
	    	if(!anima)
	    	o--;
	    	if(o < 0){	//limite izquierdo
	    		o = 0;
	    		anima = true;
	    		cell.undo_offsetL();
	    	}else{
	    		if(o > (MaxMapX/Mapper.cellPixels)-1){	//limite derecho
	    			o = (MaxMapX/Mapper.cellPixels)-1;
	    		}else{
	    			Coor evalCell = map[o][p];
	    			if(anima){
	    				if(anim == 0){
	    					newImage(3,"izquierda_andando.png",new Coor(0,0));
	    					cell.offsetL();
	    					anima = false;
	    					anim++;
	    				}else{
	    					newImage(3,"izquierda_andando2.png",new Coor(0,0));
	    					cell.offsetL();
	    					anima = false;
	    					anim--;
	    				}
	    			}else if(evalCell.isAllow()){
	    				cell.undo_offsetL();
	    				cell.setAllow(true);
	    				this.cell = evalCell;
	    				cell.setAllow(false);
	    				newImage(3,"izquierda_quieto.png",new Coor(0,0));
	    				anima = true;
	    			}else{
	    				anima = true;
	    				cell.undo_offsetL();
	    				o++;
	    			}
	    		}
	    	}
	    }
	    
	    private synchronized void moveU(){
	    	if(!anima)
	    	p--;
	    	if(p < 0){	//limite superior
	    		p = 0;
	    		anima = true;
	    		cell.undo_offsetU();
	    	}else{
	    	if(p > (MaxMapY/Mapper.cellPixels)-1){	//limite inferior
	    		p = (MaxMapY/Mapper.cellPixels)-1;
	    	}else{
	    		Coor evalCell = map[o][p];
    			if(anima){
    				if(anim == 0){
    					newImage(3,"arriba_andando.png",new Coor(0,0));
    					cell.offsetU();
    					anima = false;
    					anim++;
    				}else{
    					newImage(3,"arriba_andando2.png",new Coor(0,0));
    					cell.offsetU();
    					anima = false;
    					anim--;
    				}
	    	    }else if(evalCell.isAllow()){
    				cell.undo_offsetU();
    				cell.setAllow(true);
    				this.cell = evalCell;
    				cell.setAllow(false);
    				newImage(3,"arriba_quieto.png",new Coor(0,0));
    				anima = true;
    			}else{
    				anima = true;
    				cell.undo_offsetU();
    				p++;
    		}
    	}
    	}
	    }
	    
	    private synchronized void moveD(){
	    	if(!anima)
	    	p++;
	    	if(p < 0){	//limite superior
	    		p = 0;
	    		anima = true;
	    		cell.undo_offsetD();
	    	}else{
	    	if(p > (MaxMapY/Mapper.cellPixels)-1){	//limite inferior
	    		p = (MaxMapY/Mapper.cellPixels)-1;
	    	}else{
	    		
	    		Coor evalCell = map[o][p];
    			if(anima){
    				if(anim == 0){
    					newImage(3,"abajo_andando.png",new Coor(0,0));
    					cell.offsetD();
    					anima = false;
    					anim++;
    				}else{
    					newImage(3,"abajo_andando2.png",new Coor(0,0));
    					cell.offsetD();
    					anima = false;
    					anim--;
    				}
	    	    }else if(evalCell.isAllow()){
    				cell.undo_offsetD();
    				cell.setAllow(true);
    				this.cell = evalCell;
    				cell.setAllow(false);
    				newImage(3,"abajo_quieto.png",new Coor(0,0));
    				anima = true;
    			}else{
    				anima = true;
    				cell.undo_offsetD();
    				p--;
    		}
    	}
    	}
	    }
	    
	    
	    
	    
	    public void newImage(int id, String file, Coor position){	//metodo para crear una nueva imagen
	    		ImageIcon a = new ImageIcon(file);
	    		Image b = a.getImage();
	    		images.put(id, b);	//anadimos la nueva imagen al mapa de imagenes junto con su id
	    		coors.put(id, position); //anadimos la posicion de la imagen junto con su id al mapa de posiciones
	    }
	    
	    public void destroyImage(int id){
	    	images.remove(id);
	    	coors.remove(id);
	    }

		public void run() {		//loop principal, Frame
			 long beforeTime = 0, timeDiff, sleep;
		     beforeTime = System.currentTimeMillis();	//tiempo actual
			while(true){
				publicCell = cell;	//actualizacion de variables publicas
				public_o = o;
				public_p = p;
				public_MaxMapX = MaxMapX;
				public_MaxMapY = MaxMapY;
				repaint();	//se actualiza la pantalla
				timeDiff = System.currentTimeMillis() - beforeTime;	//calculo del tiempo perdido ejecutando metodos
	            sleep = RENDER_SPEED - timeDiff;	//calculo del tiempo a esperar
	            if(sleep < 0){ 	//si el tiempo de ejecucion del Frame ha sido mayor al tiempo de espera, entonces no se duerme el thread (sleep = 1 milisegundo)
	            	sleep = 1;
	            }
				try {Thread.sleep(sleep);} catch (InterruptedException e) {System.out.println("interrupted");} 	//dormimos el thread (controlamos el tiempo por frame)
				beforeTime = System.currentTimeMillis();
			}
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {		//metodo ejecutado por el timer
			/*
			 * El metodo movimiento se ejecuta 2 veces, una para simular el paso del sprite, y la segunda para evaluar la siguiente posicion y reaccionar segun el resultado
			 */
			if(!(nextMov.equals(""))){	
			if(animac){
				move = nextMov;
				move(move);	//ejecutamos el movimiento de la variable nextMov
				animac = false;	//fin turno animacion
			}else{
				move(move);	//ejecutamos el movimiento de la variable nextMov
				mov("");	//reiniciamos la variable nextMov
				animac = true;	//fin turno movimiento
			}
			}
		}
}








