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
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Board extends JPanel implements Runnable, ActionListener{
	/*VARIABLES*/
	private Coor cell;	//celda actual
	public Coor publicCell = cell;	//copia de cell publica para que el sistema de debug y otras clases puedan acceder en tiempo real a la variable sin necesidad de un metodo
	Coor[][] map;	//mapa actual
	Mapper map2;
	private int MaxMapX = 800;	//maximo tamaño horizontal del mapa actual PIXELES
	private int MaxMapY = 800;	//maximo tamaño vertical del mapa actual PIXELES
	int public_MaxMapX;
	int public_MaxMapY;
	private static final int MOVEMENT_SPEED = 300;	//velocidad del movimiento (solo pasos, el movimiento de renderizado es otra cosa)
	private static final int RENDER_SPEED = 5;	//velocidad de renderizado(velocidad de refresco de la pantalla y todas las acciones que implica un frame)
	private int anim = 0;	//variable para animacion
	boolean anima = true,animac = true;	//variable para animacion
	private int o=0,p=0;	//coordenada actual;
	private String move;
	public int public_o = o, public_p = p;	//copia publica (igual que publicCell)
	//private boolean end;	legacy variable
	private Thread animator;	//variable Thread para que board pueda autoiniciarse (ver metodo addNotify())
	private String nextMov = "";	//siguiente movimiento del personaje main
	private Map<Integer, Image> images;
	private Map<Integer, Coor> coors;
	
	
	/*METODOS*/
	/*public synchronized boolean isEnd(){	legacy method
			return end;
		}*/
	
	    public Board() {
	    	images = new TreeMap<Integer, Image>(); 
	    	coors = new TreeMap<Integer, Coor>(); 
	    	map2 = new Mapper(MaxMapX,MaxMapY,this);	//mapeo
	    	map = map2.init();
	    	setDoubleBuffered(true);	//se requiere un buffer de dibujo el cual se dibuja finalmente en pantalla (cuestiones menores de refresco de pantalla)
	    	
	    	/*Cargamos imagenes*/ //IMPORTANTE las ids de las imagenes van de 2 para abajo (pueden ser negativas)
	    	newImage(2,"ff.jpg", new Coor(420,310));		//anadimos un par de fondos
	    	newImage(1,"bosque.png", new Coor(-300,-300));
	    	newImage(3,"abajo_quieto.png", new Coor(0,0));	//las imagenes se superpondran de acuerdo al orden de carga (la ultima por encima de todas)
	    	
	    	
	        cell = map[o][p];	//inicializacion de celda actual (si el DebugSystem lanza errores posiblemente es porque se inicia antes que esto(muy improbable))
	    }
	    
	    
	    
	    public void paint(Graphics g) {		//pintado de pantalla, este metodo no se ejecuta por nosotros mismos, el sistema de graficos lo ejecuta cada vez que llamamos a repaint()
	    	Graphics2D g2d = (Graphics2D) g;	//grafico actual
	    	Iterator<Image> it = images.values().iterator();
	    	Iterator<Coor> it2 = coors.values().iterator();
	    	for(int i=0; i<images.size();i++){
	    		Image a = it.next();
	    		if(!a.equals(images.get(3))){
	    			Coor d = it2.next();
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
	    	if(!anima)	//no se cambia la posicion si es el turno de la animacion
	    	o++;
	    	//JOptionPane.showMessageDialog(null, (MaxMapX/13)-1);	//debug method
	    	if(o < 0){	//limite izquierdo
	    		o = 0;
	    	}else{
	    	if(o > (MaxMapX/14)-1){	//limite derecho
	    		o = (MaxMapX/14)-1;
	    		anima = true;	//se termina la animacion
	    		cell.undo_offsetR();	//se resetea el offset
	    	}else{
	    		Coor evalCell = map[o][p];	//celda de evaluacion
	    			if(anima){	//turno de la animacion
	    				if(anim == 0){	//alternando entre paso izquierdo y paso derecho
	    					newImage(3,"derecha_andando.png",new Coor(0,0));
	    					cell.offsetR();	//offset a la celda actual para efect de paso
	    					anima = false;	//turno de animacion finalizado
	    					anim++;
	    				}else{
	    					newImage(3,"derecha_andando2.png",new Coor(0,0));
	    					cell.offsetR();	//offset a la celda actual para efect de paso
	    					anima = false;	//turno de animacion finalizado
	    					anim--;
	    				}
		    	    }else if(evalCell.isAllow()){	//turno movimiento, solo si la celda a la que nos movemos esta disponible
	    				cell.undo_offsetR();	//desacemos el offset de la celda atual
	    				cell.setAllow(true);	//la desbloqueamos
	    				this.cell = evalCell;	//avanzamos a la siguiente celda
	    				cell.setAllow(false);	//la bloqueamos
	    				newImage(3,"derecha_quieto.png",new Coor(0,0));
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
	    		if(o > (MaxMapX/14)-1){	//limite derecho
	    			o = (MaxMapX/14)-1;
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
	    	if(p > (MaxMapY/14)-1){	//limite inferior
	    		p = (MaxMapY/14)-1;
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
	    	}else{
	    	if(p > (MaxMapY/14)-1){	//limite inferior
	    		p = (MaxMapY/14)-1;
	    		anima = true;
	    		cell.undo_offsetD();
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
	    
	    
	    
	    
	    public void newImage(int id, String file, Coor position){
	    	 ImageIcon a = new ImageIcon(file);
		     Image b = a.getImage();
		     images.put(id, b);
		     coors.put(id, position);
	    }
	    

		public void run() {		//loop principal, Frame
			 long beforeTime = 0, timeDiff, sleep;
		     beforeTime = System.currentTimeMillis();
			while(true){
				publicCell = cell;	//actualizacion de variables publicas
				public_o = o;
				public_p = p;
				public_MaxMapX = MaxMapX;
				public_MaxMapY = MaxMapY;
				repaint();
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








