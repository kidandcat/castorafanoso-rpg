import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
//import javax.swing.Timer;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class DebugSystem extends JFrame implements ActionListener{
	Board a;					//instancia Board para acceder a diversas variables
	JLabel c,d;
	JTextArea b;
	Npc np;
	public DebugSystem(Board a){
		this.a = a;
		JPanel ce = new JPanel(new FlowLayout());
		setTitle("Debug System");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(340, 160);
        setVisible(true);
        setResizable(false);
        b = new JTextArea(40,40);
        b.setText("Controles de movimiento: WASD\nEliminar npc: R\nVer pixeles de coordenada: P\nCambiar estado de celda: C\nCambiar mapa: N\nCrear nuevo mapa: M");
        b.setVisible(true);
        // c = new JLabel("");
        ce.add(b);
        // ce.add(c);
        this.setContentPane(ce);
        /*Timer timer = new Timer(100, this);	//tiempo de actualizacion de las variables del debug 
        try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
        timer.start();	//IMPORTANTE es aqui donde hay una baja probabilidad de error fatal si este metodo se ejecuta antes de la inicializacion del Board
        */
        Set<Npc> u = Npc.list();
		Iterator<Npc> it = u.iterator();
		if(it.hasNext())
		np = it.next();
	}
	
	public void print_map(){
		int i = a.public_o;
		int e = a.public_p;
		try{
			JOptionPane.showMessageDialog(null, i + " " + e + " " + a.map[i][e].toString());
		}catch(Exception e2){
			JOptionPane.showMessageDialog(null, e2.toString());
		}	
	}
	
	public void remove_npc(int ID){
		for(Npc np : Npc.list())
			if(np.ID() == ID){
				np.destroy();
				JOptionPane.showMessageDialog(null, "Npc ID: " + ID + " removed");
			}
	}
	
	public void change_cell_state(int x, int y){
		int i = JOptionPane.showConfirmDialog(null, "El estado de la celda [" + x + "][" + y + "] es: " + a.map[x][y].isAllow() + "\n Quiere cambiarlo?");
		if(i == 0){
			a.map[x][y].setAllow(!a.map[x][y].isAllow());
		}
	}
	
	public void controls(){
		JOptionPane.showMessageDialog(this, "Controles de movimiento: WASD\nEliminar npc: R\nVer pixeles de coordenada: P\nCambiar estado de celda: C\nCambiar mapa: N\nCrear nuevo mapa: M");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//b.setText("Pixels: " + np.cell.toString());		//muestra en el Debug System los pixeles en los que se encuentra el centro de camara
		//c.setText("Cell: [" + np.o + "] [" + np.p + "]");	//igual que el anterior pero con el numero de celda			//debug method: comprueba que Mapper funciona correctamente
	}
}

	