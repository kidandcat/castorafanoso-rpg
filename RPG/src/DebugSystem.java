import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class DebugSystem extends JFrame implements ActionListener{
	Board a;					//instancia Board para acceder a diversas variables
	JLabel b,c,d;
	public DebugSystem(Board a){
		this.a = a;
		JPanel ce = new JPanel(new FlowLayout());
		setTitle("Debug System");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(100, 80);
        setVisible(true);
        setResizable(false);
        b = new JLabel("X: ");
        c = new JLabel("");
        ce.add(b);
        ce.add(c);
        this.setContentPane(ce);
        Timer timer = new Timer(100, this);	//tiempo de actualizacion de las variables del debug 
        timer.start();	//IMPORTANTE es aqui donde hay una baja probabilidad de error fatal si este metodo se ejecuta antes de la inicializacion del Board
	}
	
	public void print_map(){
		JOptionPane.showMessageDialog(null, "ATENCION! la herramienta de debug no esta limitada, lo que implica que si te sales de los limites saltaran errores criticos");
		int i = Integer.parseInt(JOptionPane.showInputDialog("X: "));
		int e = Integer.parseInt(JOptionPane.showInputDialog("Y: "));
		try{
			JOptionPane.showMessageDialog(null, e + " " + i + " " + a.map[i][e].toString());
		}catch(Exception e2){
			JOptionPane.showMessageDialog(null, e2.toString());
		}	
	}
	
	public void change_cell_state(int x, int y){
		int i = JOptionPane.showConfirmDialog(null, "El estado de la celda [" + x + "][" + y + "] es: " + a.map[x][y].isAllow() + "\n Quiere cambiarlo?");
		if(i == 0){
			a.map[x][y].setAllow(!a.map[x][y].isAllow());
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		b.setText("Pixels: " + a.publicCell.toString());		//muestra en el Debug System los pixeles en los que se encuentra el centro de camara
		c.setText("Cell: [" + a.public_o + "] [" + a.public_p + "]");	//igual que el anterior pero con el numero de celda			//debug method: comprueba que Mapper funciona correctamente
	}
}

	