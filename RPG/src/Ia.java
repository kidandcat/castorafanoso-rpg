
public class Ia extends Thread{
	private Npc np = null;
	
	
	public Ia(){
		
	}
	
	public void init(Npc np){
		this.np = np;
	}
	
	
	public void run(){
		while(true){
			np.mov("D");
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			np.mov("D");
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			np.mov("D");
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			np.mov("R");
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			np.mov("R");
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			np.mov("U");
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			np.mov("L");
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
}
