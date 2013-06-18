import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Timer;




public class Npc implements ActionListener{
	private Board a;
	private String nombre;
	int o = 1,p = 2;
	private boolean anima = true, animac = true;
	private String nextMov = "", move = "";
	Coor cell;
	private Ia ia;
	private int ID = 4;
	private int anim = 0;
	private Timer timer;
	private static Set<Npc> npcs = null;	//set estatico donde se almacenan todos los npcs para su localizacion (a implementar: una id por npc para poder localizarlos en el set(cambiar el set por un mapa))
	
	public static Npc constructor(Board a, int ID, int MOVEMENT_SPEED, String i, Ia ia, int initX, int initY){
		if(npcs != null){
			Npc n = new Npc(a, ID, i, ia, initX, initY);
			npcs.add(n);
			return n;
		}else{
			npcs = new HashSet<Npc>();
			Npc n = new Npc(a, ID, i, ia, initX, initY);
			npcs.add(n);
			return n;
		}
	}
	public static Set<Npc> list(){
		return npcs;
	}
	
	public static void set_list(Set<Npc> npcs){
		Npc.npcs = npcs;
	}
	
	public void destroy(){
		timer.stop();
		ia.destroy();
		npcs.remove(this);
		a.destroyImage(ID);
		a.map[o][p].setAllow(true);
	}
	
	public int ID(){
		return ID;
	}
	
	private Npc(Board a, int ID, String i, Ia ia, int initX, int initY){
		this.o = initX;
		this.p = initY;
		this.ia = ia;
		this.ID = ID;
		this.a = a;
		this.nombre = i;
        a.newImage(ID, "abajo_quieto_" + i + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310), p);
        timer = new Timer(a.MOVEMENT_SPEED/2, this);	//crea un timer que ejecutara el ActionListener de esta clase cada vez que pase el tiempo indicado
        timer.start();
        cell = a.map[o][p];
        a.map[o][p].setAllow(false);
        if(ia != null){
        	ia.init(this);
        	ia.start();
        }
	}

	
	public synchronized void mov(String d){	//actualiza la variable de movimiento
		   this.nextMov = d;
	}
	
	private synchronized void moveD(){
		if(a.map[o][p+1].isAllow()){
			if(anima){
				if(anim == 0){
					a.newImage(ID, "abajo_andando_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 +7), p);
					anima = false;
					anim++;
				}else{
					a.newImage(ID, "abajo_andando2_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 +7), p);
					anima = false;
					anim--;
				}
    	    }else{
				a.map[o][p].setAllow(true);
				p++;
				a.newImage(ID, "abajo_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310), p);
				anima = true;
				a.map[o][p].setAllow(false);
			}
		}else{
				anima = true;
				a.newImage(ID, "abajo_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310), p);
		}
	}
	
	
	private synchronized void moveU(){
		if(a.map[o][p-1].isAllow()){
			if(anima){
				if(anim == 0){
					a.newImage(ID, "arriba_andando_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 -7), p);
					anima = false;
					anim++;
				}else{
					a.newImage(ID, "arriba_andando2_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 -7), p);
					anima = false;
					anim--;
				}
    	    }else{
				a.map[o][p].setAllow(true);
				p--;
				a.newImage(ID, "arriba_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310), p);
				anima = true;
				a.map[o][p].setAllow(false);
			}
		}else{
				anima = true;
				a.newImage(ID, "arriba_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310), p);
		}
    }

	
	private synchronized void moveL(){
		if(a.map[o-1][p].isAllow()){
			if(anima){
				if(anim == 0){
					a.newImage(ID, "izquierda_andando_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420 -7,p*Mapper.cellPixels + 310), p);
					anima = false;
					anim++;
				}else{
					a.newImage(ID, "izquierda_andando2_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420 -7,p*Mapper.cellPixels + 310), p);
					anima = false;
					anim--;
				}
    	    }else{
				a.map[o][p].setAllow(true);
				o--;
				a.newImage(ID, "izquierda_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310), p);
				anima = true;
				a.map[o][p].setAllow(false);
			}
		}else{
				anima = true;
				a.newImage(ID, "izquierda_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310), p);
		}
    }
	
	
	private synchronized void moveR(){
		if(a.map[o+1][p].isAllow()){
			if(anima){
				if(anim == 0){
					a.newImage(ID, "derecha_andando_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420 +7,p*Mapper.cellPixels + 310), p);
					anima = false;
					anim++;
				}else{
					a.newImage(ID, "derecha_andando2_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420 +7,p*Mapper.cellPixels + 310), p);
					anima = false;
					anim--;
				}
    	    }else{
				a.map[o][p].setAllow(true);
				o++;
				a.newImage(ID, "derecha_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310), p);
				anima = true;
				a.map[o][p].setAllow(false);
			}
		}else{
				anima = true;
				a.newImage(ID, "derecha_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310), p);
		}
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
	@Override
	public void actionPerformed(ActionEvent e) {
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
