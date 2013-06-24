/*
 * COPYRIGHT: CastorAfanoso 
 * author: Jairo Caro-Accino Viciana
 * e-mail: kidandcat@gmail.com
 * License: Feel free to copy, modify, and share!
 * Share any change!
 */


//clase encargada del renderizado asi como del movimiento

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.UIManager;

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
	static final int RENDER_SPEED = 10;	//velocidad de renderizado(velocidad de refresco de la pantalla y todas las acciones que implica un frame)
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
	private int ID = 3;
	Ia ia;
	private SortedMap<Integer,Integer> paintOrder;
	private Map<Integer,Integer> finalOrder;
	/*METODOS*/
	/*public synchronized boolean isEnd(){	legacy method
			return end;
		}*/
	
	    public Board() {
	    	paintOrder = Collections.synchronizedSortedMap(new TreeMap<Integer, Integer>()); 
	    	setDoubleBuffered(true);	//se requiere un buffer de dibujo el cual se dibuja finalmente en pantalla (cuestiones menores de refresco de pantalla)
	        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (Exception e) {e.printStackTrace();}
	        
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
		    images3.put(ID, b1);
		    paintOrder.put(-1, -1);
		    paintOrder.put(-2, -2);
		    paintOrder.put(ID, p);
		    coors3.put(ID, new Coor(0,0));
		    coors3.put(-1, new Coor(420,310+5));
		    coors3.put(-2, new Coor(-300,-300));
		    Coor[][] map3 = new Mapper(800,800,this).init();
	        Mapp.constructor(1, 800, 800, images3, coors3, map3, 1, 20, paintOrder);
	        /*Nuevo metodo de limitacion de mapas*/ //Se bloquean las celdas exteriores del mapa
	        
	        changeMap(1);
	        ia = new Ia();
	    	Npc.constructor(this, 4, MOVEMENT_SPEED, "copia", ia, 4, 20);
	    	Npc.constructor(this, 5, MOVEMENT_SPEED, "copia", null, 4, 21);
	    }
	    
	    public void create_map(){
	    	try{
	    	Map<Integer, Image> images3 = Collections.synchronizedSortedMap(new TreeMap<Integer, Image>()); 
	        Map<Integer, Coor> coors3 = Collections.synchronizedSortedMap(new TreeMap<Integer, Coor>()); 
	        SortedMap<Integer,Integer> paintOrder = Collections.synchronizedSortedMap(new TreeMap<Integer, Integer>()); 
	        int num_images = Integer.parseInt(JOptionPane.showInputDialog("Numero de imagenes: "));
	        for(int i=0;i<num_images;i++){
	        	ImageIcon a2 = new ImageIcon(loadImage());
	        	Image b2 = a2.getImage();
	        	int ID = Integer.parseInt(JOptionPane.showInputDialog("ID de imagen: "));
	        	images3.put(ID, b2);
	        	int paintorder = Integer.parseInt(JOptionPane.showInputDialog("Prioridad de dibujado (aleatorio para Npc): "));
	        	paintOrder.put(ID, paintorder);
	        	coors3.put(ID, new Coor(Integer.parseInt(JOptionPane.showInputDialog("Coordenada X: ")),Integer.parseInt(JOptionPane.showInputDialog("Coordenada Y: "))));
	        }
	        int mapx = Integer.parseInt(JOptionPane.showInputDialog("Anchura del mapa: "));
	        int mapy = Integer.parseInt(JOptionPane.showInputDialog("Altura del mapa: "));
		    Coor[][] map3 = new Mapper(mapx,mapy,this).init();
	        Mapp.constructor(Integer.parseInt(JOptionPane.showInputDialog("ID del mapa: ")), mapx, mapy, images3, coors3, map3, Integer.parseInt(JOptionPane.showInputDialog("X de inicio: ")), Integer.parseInt(JOptionPane.showInputDialog("Y de inicio: ")), paintOrder);
	    	}catch(Exception e){
	    		JOptionPane.showMessageDialog(this, "Error creando mapa");
	    		JOptionPane.showMessageDialog(this, e.getStackTrace());
	    	}
	    }
	    
	    
	    
	    public void changeMap(){
	    	try{
	    		Mapp new_map = Mapp.changeMap(Integer.parseInt(JOptionPane.showInputDialog("ID del mapa: ")));
	    		this.images = new_map.images();
	    		Progress_bar bar = new Progress_bar();
	    		bar.set(1);
	    		this.coors = new_map.coors();
	    		this.paintOrder = new_map.paintOrder();
	    		bar.set(2);
	    		this.MaxMapX = new_map.MaxMapX();
	    		bar.set(3);
	    		this.MaxMapY = new_map.MaxMapY();
	    		bar.set(4);
	    		this.map = new_map.map();
	    		bar.set(5);
	    		this.o = new_map.init_X();
	    		bar.set(6);
	    		this.p = new_map.init_Y();
	    		bar.set(7);
	    		this.cell = map[o][p];
	    		bar.set(8);
	    		Npc.set_list(new_map.npcs());
	    		bar.set(9);
	    	}catch (Exception e){
	    		JOptionPane.showMessageDialog(this, "ID invalida");
	    	}
	    }
	    
	    public void changeMap(int ID){
	    		Mapp new_map = Mapp.changeMap(ID);
	    		this.images = new_map.images();
	    		this.coors = new_map.coors();
	    		this.MaxMapX = new_map.MaxMapX();
	    		this.MaxMapY = new_map.MaxMapY();
	    		this.map = new_map.map();
	    		this.o = new_map.init_X();
	    		this.p = new_map.init_Y();
	    		this.cell = map[o][p];
	    		Npc.set_list(new_map.npcs());
	    		if(Npc.list() != null)
	    			for(Npc npc : Npc.list())
	    				npc.unpause();
	    }
	    
	    
	    public void paint(Graphics g) {		//pintado de pantalla, este metodo no se ejecuta por nosotros mismos, el sistema de graficos lo ejecuta cada vez que llamamos a repaint()
	    	Graphics2D g2d = (Graphics2D) g;	//grafico actual
	    	finalOrder = ValueComparator.sortByValue(paintOrder);
	    	Iterator<Integer> it3 = finalOrder.keySet().iterator();
	    	for(int i=0; i<finalOrder.size();i++){
	    		int l = it3.next();
	    		Image a = images.get(l);
	    		Coor d = coors.get(l);
	    		if(!a.equals(images.get(ID))){
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
	    
	   private synchronized void move(String s){	//inicia los metodos necesarios para moverse en la direccion indicada;
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
	    		Coor evalCell = map[o+1][p];	//celda de evaluacion
	    		if(evalCell.isAllow()){
	    			if(anima){	//turno de la animacion
	    				if(anim == 0){	//alternando entre paso izquierdo y paso derecho
	    					newImage(ID,"derecha_andando.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
	    					cell.offsetR();	//offset a la celda actual para efecto de paso
	    					anima = false;	//turno de animacion finalizado
	    					anim++;	//alternando entre pasos
	    				}else{
	    					newImage(ID,"derecha_andando2.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
	    					cell.offsetR();	//offset a la celda actual para efecto de paso
	    					anima = false;	//turno de animacion finalizado
	    					anim--; //alternando entre pasos
	    				}
		    	    }else{	//turno movimiento, solo si la celda a la que nos movemos esta disponible
		    	    	o++;
	    				cell.undo_offsetR();	//desacemos el offset de la celda atual
	    				cell.setAllow(true);	//la desbloqueamos
	    				this.cell = evalCell;	//avanzamos a la siguiente celda
	    				cell.setAllow(false);	//la bloqueamos
	    				newImage(ID,"derecha_quieto.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
	    				anima = true;	//fin turno de movimiento
	    			}
	    		}else{	//si la celda no permite el movimiento a ella
	    			newImage(ID,"derecha_quieto.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
	    			anima = true;	//fin turno movimiento
	    			cell.undo_offsetR();	//desacemos offset
	    		}
	    }
	    
	    private synchronized void moveL(){
	    	Coor evalCell = map[o-1][p];	//celda de evaluacion
    		if(evalCell.isAllow()){
    			if(anima){	//turno de la animacion
    				if(anim == 0){	//alternando entre paso izquierdo y paso derecho
    					newImage(ID,"izquierda_andando.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
    					cell.offsetL();	//offset a la celda actual para efecto de paso
    					anima = false;	//turno de animacion finalizado
    					anim++;	//alternando entre pasos
    				}else{
    					newImage(ID,"izquierda_andando2.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
    					cell.offsetL();	//offset a la celda actual para efecto de paso
    					anima = false;	//turno de animacion finalizado
    					anim--; //alternando entre pasos
    				}
	    	    }else{	//turno movimiento, solo si la celda a la que nos movemos esta disponible
	    	    	o--;
    				cell.undo_offsetL();	//desacemos el offset de la celda atual
    				cell.setAllow(true);	//la desbloqueamos
    				this.cell = evalCell;	//avanzamos a la siguiente celda
    				cell.setAllow(false);	//la bloqueamos
    				newImage(ID,"izquierda_quieto.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
    				anima = true;	//fin turno de movimiento
    			}
    		}else{	//si la celda no permite el movimiento a ella
    			newImage(ID,"izquierda_quieto.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
    			anima = true;	//fin turno movimiento
    			cell.undo_offsetL();	//desacemos offset
    		}
	    }
	    
	    private synchronized void moveU(){
	    	Coor evalCell = map[o][p-1];	//celda de evaluacion
    		if(evalCell.isAllow()){
    			if(anima){	//turno de la animacion
    				if(anim == 0){	//alternando entre paso izquierdo y paso derecho
    					newImage(ID,"arriba_andando.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
    					cell.offsetU();	//offset a la celda actual para efecto de paso
    					anima = false;	//turno de animacion finalizado
    					anim++;	//alternando entre pasos
    				}else{
    					newImage(ID,"arriba_andando2.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
    					cell.offsetU();	//offset a la celda actual para efecto de paso
    					anima = false;	//turno de animacion finalizado
    					anim--; //alternando entre pasos
    				}
	    	    }else{	//turno movimiento, solo si la celda a la que nos movemos esta disponible
	    	    	p--;
    				cell.undo_offsetU();	//desacemos el offset de la celda atual
    				cell.setAllow(true);	//la desbloqueamos
    				this.cell = evalCell;	//avanzamos a la siguiente celda
    				cell.setAllow(false);	//la bloqueamos
    				newImage(ID,"arriba_quieto.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
    				anima = true;	//fin turno de movimiento
    			}
    		}else{	//si la celda no permite el movimiento a ella
    			newImage(ID,"arriba_quieto.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
    			anima = true;	//fin turno movimiento
    			cell.undo_offsetU();	//desacemos offset
    		}
	    }
	    
	    private synchronized void moveD(){
	    	Coor evalCell = map[o][p+1];	//celda de evaluacion
    		if(evalCell.isAllow()){
    			if(anima){	//turno de la animacion
    				if(anim == 0){	//alternando entre paso izquierdo y paso derecho
    					newImage(ID,"abajo_andando.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
    					cell.offsetD();	//offset a la celda actual para efecto de paso
    					anima = false;	//turno de animacion finalizado
    					anim++;	//alternando entre pasos
    				}else{
    					newImage(ID,"abajo_andando2.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
    					cell.offsetD();	//offset a la celda actual para efecto de paso
    					anima = false;	//turno de animacion finalizado
    					anim--; //alternando entre pasos
    				}
	    	    }else{	//turno movimiento, solo si la celda a la que nos movemos esta disponible
	    	    	p++;
    				cell.undo_offsetD();	//desacemos el offset de la celda atual
    				cell.setAllow(true);	//la desbloqueamos
    				this.cell = evalCell;	//avanzamos a la siguiente celda
    				cell.setAllow(false);	//la bloqueamos
    				newImage(ID,"abajo_quieto.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
    				anima = true;	//fin turno de movimiento
    			}
    		}else{	//si la celda no permite el movimiento a ella
    			newImage(ID,"abajo_quieto.png",new Coor(0,0), p);//se actualiza la imagen, se usa la coordenada Y(p) para posicionar la imagen en la cola de renderizado
    			anima = true;	//fin turno movimiento
    			cell.undo_offsetD();	//desacemos offset
    		}
	    }
	    
	    
	    
	    
	    public void newImage(int id, String file, Coor position, int Y){	//metodo para crear una nueva imagen
	    		ImageIcon a = new ImageIcon(file);
	    		Image b = a.getImage();
	    		images.put(id, b);	//anadimos la nueva imagen al mapa de imagenes junto con su id
	    		coors.put(id, position); //anadimos la posicion de la imagen junto con su id al mapa de posiciones
	    		paintOrder.put(id, Y);	//añadimos la id de la imagen a la cola de renderizado, cuando mayor sea el valor de Y, mas tarde se dibujara respecto al resto de elementos
	    }
	    
	    public void destroyImage(int id){
	    	images.remove(id);
	    	coors.remove(id);
	    	paintOrder.remove(id);
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
			if(animac){	//si es animacion:
				move = nextMov;
				move(move);	//ejecutamos el movimiento de la variable nextMov
				animac = false;	//fin turno animacion
			}else{	//si no es animacion:
				move(move);	//ejecutamos el movimiento de la variable nextMov
				mov("");	//reiniciamos la variable nextMov
				animac = true;	//fin turno movimiento
			}
			}
		}
		
		/*
		 * El siguiente metodo abre una ventana para seleccionar un archivo local, devuelve la ruta completa como un string
		 */
		public String loadImage(){
			JFileChooser fc = new JFileChooser();

			fc.showOpenDialog(this);

			File fileImagen = fc.getSelectedFile();

			
			if(fileImagen!=null){
				return fileImagen.getAbsolutePath();
	       }else{
	    	   return null;
	       }
		}
		
		
}


/*
 * La siguiente clase crea una barra de progreso en la esquina superior izquierda
 */
@SuppressWarnings("serial")
class Progress_bar extends JFrame implements ActionListener{
	JProgressBar current;
	
	public Progress_bar(){
		setTitle("Loading...");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(200, 90);
		//setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		JPanel pane = new JPanel();
		pane.setLayout(new FlowLayout());
		current = new JProgressBar(0, 9); // Crear un JProgressBar con valores 0-2000
		current.setValue(0); // Fijar valor por defecto.
		current.setStringPainted(true); // Mostrar valor numérico del progreso de la barra
		pane.add(current);
		setContentPane(pane);
		Timer timer = new Timer(100, this);	//tiempo de actualizacion de las variables del debug 
        timer.start();	//IMPORTANTE es aqui donde hay una baja probabilidad de error fatal si este metodo se ejecuta antes de la inicializacion del Board
	}
	
	public void set(int i){
		current.setValue(i);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(current.getValue() == 9)
			this.dispose();
	}
}

/*
 * Con la siguiente clase ordenamos un mapa por el orden natural de sus valores
 */
@SuppressWarnings("rawtypes")
class ValueComparator implements Comparator {

	  Map<Integer,Integer> base;
	  public ValueComparator(Map<Integer,Integer> base) {
	      this.base = base;
	  }

	  public int compare(Object a, Object b) {

	    if((Integer)base.get(a) < (Integer)base.get(b)) {
	      return 1;
	    } else if((Integer)base.get(a) == (Integer)base.get(b)) {
	      return 0;
	    } else {
	      return -1;
	    }
	  }
	  
	  @SuppressWarnings("unchecked")
	static Map<Integer, Integer> sortByValue(Map map) {
		     List list = new LinkedList(map.entrySet());
		     Collections.sort(list, new Comparator() {
		          public int compare(Object o1, Object o2) {
		               return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
		          }
		     });

		    Map result = new LinkedHashMap();
		    for (Iterator it = list.iterator(); it.hasNext();) {
		        Map.Entry entry = (Map.Entry)it.next();
		        result.put(entry.getKey(), entry.getValue());
		    }
		    return result;
		}
	}







