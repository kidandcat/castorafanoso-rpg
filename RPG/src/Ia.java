
public class Ia extends Thread{
	private Npc np = null;
	private boolean stop = false;
	
	public Ia(){
		
	}
	
	public void init(Npc np){
		this.np = np;
	}
	
	public void destroy(){
		stop = true;
	}
	
	public void run(){
		
		while(!stop){
			np.mov("D");
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		}
		
	}
}
