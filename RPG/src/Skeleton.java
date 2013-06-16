import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class Skeleton extends JFrame implements KeyListener {
	private static final int SCREEN_X = 420;	//tama�o horizontal de la ventana principal
	private static final int SCREEN_Y = 310;	//tama�o vertical de la ventana principal
	Board a;
	static DebugSystem db;
    public Skeleton(Board a) {
    	this.a = a;
        add(a);		//anadimos el panel de graficos
        setTitle("Skeleton");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(SCREEN_X, SCREEN_Y);	
        setLocationRelativeTo(null);	//hace que la ventana aparezca en medio de la pantalla
        setVisible(true);
        setBackground(Color.BLACK);
        setResizable(false);	
        addKeyListener(this);
        setFocusable(true);
    }
    
    /*
     * MAIN METHOD
     */
    public static void main(String[] args) {
    	Board a = new Board();	//inicializacion graficos
    	db = new DebugSystem(a);		//inicializacion debugger
    	new Skeleton(a);		//inicializacion ventana
    	JOptionPane.showMessageDialog(null, "Controles de movimiento: WASD\nEliminar npc: B\nVer pixeles de coordenada: M\nCambiar estado de celda: N\nCambiar mapa: V");
    }
    /*
     * 
     */
    
    
    
	@Override
	public void keyPressed(KeyEvent arg0) {		//detectamos la entrada del teclado
		if(arg0.getKeyCode() == KeyEvent.VK_A){		//movimiento hacia izquierda (A)
			a.mov("L");
		}
		if(arg0.getKeyCode() == KeyEvent.VK_S){		//movimiento hacia abajo (S)
			a.mov("D");
		}
		if(arg0.getKeyCode() == KeyEvent.VK_D){		//movimiento hacia derecha (D)
			a.mov("R");
		}
		if(arg0.getKeyCode() == KeyEvent.VK_W){		//movimiento hacia arriba (W)
			a.mov("U");
		}
		if(arg0.getKeyCode() == KeyEvent.VK_M){
			db.print_map();
		}
		if(arg0.getKeyCode() == KeyEvent.VK_V){
			a.changeMap();
		}
		if(arg0.getKeyCode() == KeyEvent.VK_B){
			db.remove_npc(Integer.parseInt(JOptionPane.showInputDialog("ID:")));
		}
		if(arg0.getKeyCode() == KeyEvent.VK_N){
			JOptionPane.showMessageDialog(null, "ATENCION! la herramienta de debug no esta limitada, lo que implica que si te sales de los limites saltaran errores criticos");
			int x = Integer.parseInt(JOptionPane.showInputDialog("X:"));
			int y = Integer.parseInt(JOptionPane.showInputDialog("Y:"));
			db.change_cell_state(x, y);
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
	}
		
	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}