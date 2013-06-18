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
        a.newImage(ID, "abajo_quieto_" + i + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310));
        timer = new Timer(a.MOVEMENT_SPEED/2, this);	//crea un timer que ejecutara el ActionListener de esta clase cada vez que pase el tiempo indicado
        timer.start();
        cell = a.map[o][p];
        a.map[o][p].setAllow(false);
        ia.init(this);
        ia.start();
	}

	
	public synchronized void mov(String d){	//actualiza la variable de movimiento
		   this.nextMov = d;
	}
	
	private synchronized void moveD(){
    	if(!anima)
    	p++;
    	if(p < 0){	//limite superior
    		p = 0;
    		anima = true;
    	}else{
    	if(p > (a.public_MaxMapY/Mapper.cellPixels)-1){	//limite inferior
    		p = (a.public_MaxMapY/Mapper.cellPixels)-1;
    	}else{
			if(anima){
				if(anim == 0){
					a.newImage(ID, "abajo_andando_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 +7));
					anima = false;
					anim++;
				}else{
					a.newImage(ID, "abajo_andando2_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 +7));
					anima = false;
					anim--;
				}
    	    }else if(a.map[o][p].isAllow()){
				a.map[o][p].setAllow(false);
				a.newImage(ID, "abajo_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310));
				anima = true;
				a.map[o][p-1].setAllow(true);
			}else{
				anima = true;
				a.newImage(ID, "abajo_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 -Mapper.cellPixels));
				p--;
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
    	}else{
    	if(p > (a.public_MaxMapY/Mapper.cellPixels)-1){	//limite inferior
    		p = (a.public_MaxMapY/Mapper.cellPixels)-1;
    	}else{
    		Coor evalCell = a.map[o][p];
			if(anima){
				if(anim == 0){
					a.newImage(ID, "arriba_andando_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 -7));
					anima = false;
					anim++;
				}else{
					a.newImage(ID, "arriba_andando2_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 -7));
					anima = false;
					anim--;
				}
    	    }else if(evalCell.isAllow()){
    	    	a.map[o][p].setAllow(false);
				a.newImage(ID, "arriba_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310));
				anima = true;
				a.map[o][p+1].setAllow(true);
			}else{
				anima = true;
				a.newImage(ID, "arriba_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 +Mapper.cellPixels));
				p++;
		}
	}
	}
    }

	
	private synchronized void moveL(){
    	if(!anima)
    	o--;
    	if(p < 0){	//limite superior
    		p = 0;
    		anima = true;
    	}else{
    	if(p > (a.public_MaxMapY/Mapper.cellPixels)-1){	//limite inferior
    		p = (a.public_MaxMapY/Mapper.cellPixels)-1;
    	}else{
    		Coor evalCell = a.map[o][p];
			if(anima){
				if(anim == 0){
					a.newImage(ID, "izquierda_andando_" + nombre + ".png", new Coor(o*Mapper.cellPixels-7 + 420,p*Mapper.cellPixels + 310));
					anima = false;
					anim++;
				}else{
					a.newImage(ID, "izquierda_andando2_" + nombre + ".png", new Coor(o*Mapper.cellPixels-7 + 420,p*Mapper.cellPixels + 310));
					anima = false;
					anim--;
				}
    	    }else if(evalCell.isAllow()){
    	    	a.map[o][p].setAllow(false);
				a.newImage(ID, "izquierda_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310));
				anima = true;
				a.map[o+1][p].setAllow(true);
			}else{
				anima = true;
				a.newImage(ID, "izquierda_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels+Mapper.cellPixels + 420,p*Mapper.cellPixels + 310));
				o++;
		}
	}
	}
    }
	
	
	private synchronized void moveR(){
    	if(!anima)
    	o++;
    	if(p < 0){	//limite superior
    		p = 0;
    		anima = true;
    	}else{
    	if(p > (a.public_MaxMapY/Mapper.cellPixels)-1){	//limite inferior
    		p = (a.public_MaxMapY/Mapper.cellPixels)-1;
    	}else{
    		Coor evalCell = a.map[o][p];
			if(anima){
				if(anim == 0){
					a.newImage(ID, "derecha_andando_" + nombre + ".png", new Coor(o*Mapper.cellPixels+7 + 420,p*Mapper.cellPixels + 310));
					anima = false;
					anim++;
				}else{
					a.newImage(ID, "derecha_andando2_" + nombre + ".png", new Coor(o*Mapper.cellPixels+7 + 420,p*Mapper.cellPixels + 310));
					anima = false;
					anim--;
				}
    	    }else if(evalCell.isAllow()){
    	    	a.map[o][p].setAllow(false);
				a.newImage(ID, "derecha_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310));
				anima = true;
				a.map[o-1][p].setAllow(true);
			}else{
				anima = true;
				a.newImage(ID, "derecha_quieto_" + nombre + ".png", new Coor(o*Mapper.cellPixels-Mapper.cellPixels + 420,p*Mapper.cellPixels + 310));
				o--;
		}
	}
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
