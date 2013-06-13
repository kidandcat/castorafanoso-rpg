/*
 * COPYRIGHT: CastorAfanoso 
 * author: Jairo Caro-Accino Viciana
 * e-mail: kidandcat@gmail.com
 * License: Feel free to copy, modify, and share!
 */


//clase encargada del renderizado asi como del movimiento

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
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
	private static final int MOVEMENT_SPEED = 300;	//velocidad del movimiento (solo pasos, el movimiento de renderizado es otra cosa)
	private static final int RENDER_SPEED = 5;	//velocidad de renderizado(velocidad de refresco de la pantalla y todas las acciones que implica un frame)
	private Image bardejov;	//imagen del personaje main (no preguntes por el nombre)
	private Image background, bosque;	//mas imagenes (ahora mismo uso dos backgrounds)
	private int anim = 0;	//variable para animacion
	boolean anima = true,animac = true;	//variable para animacion
	private int o=0,p=0;	//coordenada actual;
	private String move;
	public int public_o = o, public_p = p;	//copia publica (igual que publicCell)
	//private boolean end;	legacy variable
	private Thread animator;	//variable Thread para que board pueda autoiniciarse (ver metodo addNotify())
	private String nextMov = "";	//siguiente movimiento del personaje main
		
	
	
	/*METODOS*/
	/*public synchronized boolean isEnd(){	legacy method
			return end;
		}*/
	
	    public Board() {
	    	map = new Mapper(MaxMapX,MaxMapY).init();	//mapeo
	    	setDoubleBuffered(true);	//se requiere un buffer de dibujo el cual se dibuja finalmente en pantalla (cuestiones menores de refresco de pantalla)
	        ImageIcon ii = new ImageIcon("abajo_quieto.png");	//inicializacion de imagenes
	        bardejov = ii.getImage();
	        ImageIcon iiii = new ImageIcon("bosque.png");
	        bosque = iiii.getImage();
	        ImageIcon iii = new ImageIcon("ff.jpg");
	        background = iii.getImage();
	        cell = map[o][p];	//inicializacion de celda actual (si el DebugSystem lanza errores posiblemente es porque se inicia antes que esto(muy improbable))
	        map[2][2].setAllow(false);	//prubea de colision en celda [2][2] (sin problemas)
	    }
	    
	    
	    
	    public void paint(Graphics g) {		//pintado de pantalla, este metodo no se ejecuta por nosotros mismos, el sistema de graficos lo ejecuta cada vez que llamamos a repaint()
	    	Graphics2D g2d = (Graphics2D) g;	//grafico actual
	    	g2d.drawImage(bosque, cell.X()-300, cell.Y()-300, null);	//dibujado y posicionamiento de imagenes
	    	g2d.drawImage(background, cell.X()+420, cell.Y()+310, null);
	        g2d.drawImage(bardejov, 420, 310, null); 
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
	    					ImageIcon ii = new ImageIcon("derecha_andando.png");	//sprite caminando
	    					bardejov = ii.getImage();
	    					cell.offsetR();	//offset a la celda actual para efect de paso
	    					anima = false;	//turno de animacion finalizado
	    					anim++;
	    				}else{
	    					ImageIcon ii = new ImageIcon("derecha_andando2.png");	//sprite caminando
	    					bardejov = ii.getImage();
	    					cell.offsetR();	//offset a la celda actual para efect de paso
	    					anima = false;	//turno de animacion finalizado
	    					anim--;
	    				}
		    	    }else if(evalCell.isAllow()){	//turno movimiento, solo si la celda a la que nos movemos esta disponible
	    				cell.undo_offsetR();	//desacemos el offset de la celda atual
	    				cell.setAllow(true);	//la desbloqueamos
	    				this.cell = evalCell;	//avanzamos a la siguiente celda
	    				cell.setAllow(false);	//la bloqueamos
	    				ImageIcon ii = new ImageIcon("derecha_quieto.png");		//cambiamos el sprite
	    				bardejov = ii.getImage();
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
	    					ImageIcon ii = new ImageIcon("izquierda_andando.png");	
	    					bardejov = ii.getImage();
	    					cell.offsetL();
	    					anima = false;
	    					anim++;
	    				}else{
	    					ImageIcon ii = new ImageIcon("izquierda_andando2.png");	
	    					bardejov = ii.getImage();
	    					cell.offsetL();
	    					anima = false;
	    					anim--;
	    				}
	    			}else if(evalCell.isAllow()){
	    				cell.undo_offsetL();
	    				cell.setAllow(true);
	    				this.cell = evalCell;
	    				cell.setAllow(false);
	    				ImageIcon ii = new ImageIcon("izquierda_quieto.png");	
	    				bardejov = ii.getImage();
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
    					ImageIcon ii = new ImageIcon("arriba_andando.png");	
    					bardejov = ii.getImage();
    					cell.offsetU();
    					anima = false;
    					anim++;
    				}else{
    					ImageIcon ii = new ImageIcon("arriba_andando2.png");	
    					bardejov = ii.getImage();
    					cell.offsetU();
    					anima = false;
    					anim--;
    				}
	    	    }else if(evalCell.isAllow()){
    				cell.undo_offsetU();
    				cell.setAllow(true);
    				this.cell = evalCell;
    				cell.setAllow(false);
    				ImageIcon ii = new ImageIcon("arriba_quieto.png");	
    				bardejov = ii.getImage();
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
    					ImageIcon ii = new ImageIcon("abajo_andando.png");	
    					bardejov = ii.getImage();
    					cell.offsetD();
    					anima = false;
    					anim++;
    				}else{
    					ImageIcon ii = new ImageIcon("abajo_andando2.png");	
    					bardejov = ii.getImage();
    					cell.offsetD();
    					anima = false;
    					anim--;
    				}
	    	    }else if(evalCell.isAllow()){
    				cell.undo_offsetD();
    				cell.setAllow(true);
    				this.cell = evalCell;
    				cell.setAllow(false);
    				ImageIcon ii = new ImageIcon("abajo_quieto.png");	
    				bardejov = ii.getImage();
    				anima = true;
    			}else{
    				anima = true;
    				cell.undo_offsetD();
    				p--;
    		}
    	}
    	}
	    }
	    
	    

		public void run() {		//loop principal, Frame
			 long beforeTime = 0, timeDiff, sleep;
		     beforeTime = System.currentTimeMillis();
			while(true){
				publicCell = cell;	//actualizacion de variables publicas
				public_o = o;
				public_p = p;
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








