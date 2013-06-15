import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.Timer;




public class Npc implements ActionListener{
	private Board a;
	private String i;
	int o = 1,p = 2;
	private boolean anima = true, animac = true;
	private String nextMov = "", move = "";
	Coor cell;
	private int anim = 0;
	private static Set<Npc> npcs = null;
	
	
	public static Npc constructor(Board a, int MOVEMENT_SPEED, String i){
		if(npcs != null){
			Npc n = new Npc(a, i);
			npcs.add(n);
			return n;
		}else{
			npcs = new HashSet<Npc>();
			Npc n = new Npc(a,i);
			npcs.add(n);
			return n;
		}
	}
	public static Set<Npc> list(){
		return npcs;
	}
	
	
	
	private Npc(Board a, String i){
		this.a = a;
		this.i = i;
        a.newImage(4, i, new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310));
        Timer timer = new Timer(a.MOVEMENT_SPEED/2, this);	//crea un timer que ejecutara el ActionListener de esta clase cada vez que pase el tiempo indicado
        timer.start();
        cell = a.map[o][p];
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
    		Coor evalCell = a.map[o][p];
			if(anima){
				if(anim == 0){
					a.newImage(4, "abajo_andando_copia.png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 +7));
					anima = false;
					anim++;
				}else{
					a.newImage(4, "abajo_andando2_copia.png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 +7));
					anima = false;
					anim--;
				}
    	    }else if(evalCell.isAllow()){
				cell.setAllow(true);
				this.cell = evalCell;
				cell.setAllow(false);
				a.newImage(4, "abajo_quieto_copia.png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310));
				anima = true;
			}else{
				anima = true;
				a.newImage(4, "abajo_quieto_copia.png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 -Mapper.cellPixels));
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
					a.newImage(4, "arriba_andando_copia.png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 -7));
					anima = false;
					anim++;
				}else{
					a.newImage(4, "arriba_andando2_copia.png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 -7));
					anima = false;
					anim--;
				}
    	    }else if(evalCell.isAllow()){
				cell.setAllow(true);
				this.cell = evalCell;
				cell.setAllow(false);
				a.newImage(4, "arriba_quieto_copia.png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310));
				anima = true;
			}else{
				anima = true;
				a.newImage(4, "arriba_quieto_copia.png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310 +Mapper.cellPixels));
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
					a.newImage(4, "izquierda_andando_copia.png", new Coor(o*Mapper.cellPixels-7 + 420,p*Mapper.cellPixels + 310));
					anima = false;
					anim++;
				}else{
					a.newImage(4, "izquierda_andando2_copia.png", new Coor(o*Mapper.cellPixels-7 + 420,p*Mapper.cellPixels + 310));
					anima = false;
					anim--;
				}
    	    }else if(evalCell.isAllow()){
				cell.setAllow(true);
				this.cell = evalCell;
				cell.setAllow(false);
				a.newImage(4, "izquierda_quieto_copia.png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310));
				anima = true;
			}else{
				anima = true;
				a.newImage(4, "izquierda_quieto_copia.png", new Coor(o*Mapper.cellPixels+Mapper.cellPixels + 420,p*Mapper.cellPixels + 310));
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
					a.newImage(4, "derecha_andando_copia.png", new Coor(o*Mapper.cellPixels+7 + 420,p*Mapper.cellPixels + 310));
					anima = false;
					anim++;
				}else{
					a.newImage(4, "derecha_andando2_copia.png", new Coor(o*Mapper.cellPixels+7 + 420,p*Mapper.cellPixels + 310));
					anima = false;
					anim--;
				}
    	    }else if(evalCell.isAllow()){
				cell.setAllow(true);
				this.cell = evalCell;
				cell.setAllow(false);
				a.newImage(4, "derecha_quieto_copia.png", new Coor(o*Mapper.cellPixels + 420,p*Mapper.cellPixels + 310));
				anima = true;
			}else{
				anima = true;
				a.newImage(4, "derecha_quieto_copia.png", new Coor(o*Mapper.cellPixels-Mapper.cellPixels + 420,p*Mapper.cellPixels + 310));
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
