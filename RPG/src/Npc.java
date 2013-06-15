import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;


public class Npc{
	private Board a;
	private String i;
	
	
	public Npc(Board a, int MOVEMENT_SPEED, String i){
		this.a = a;
		this.i = i;
		//Timer timer = new Timer(MOVEMENT_SPEED/2, this);	//crea un timer que ejecutara el ActionListener de esta clase cada vez que pase el tiempo indicado
        //timer.start();
        a.newImage(4, i, new Coor(420,310));
	}


	public void update(){
		a.newImage(4, i, new Coor(420,310));
	}
	
	
	/*
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//new Coor(420,310);
		a.newImage(4, i, new Coor(420,310));		
	}
	*/
}
