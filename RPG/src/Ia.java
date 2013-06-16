
public class Ia extends Thread{
	private Npc np = null;
	private boolean stop = false;
	String nextMov1;
	
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
			if(nextMov1 == "D"){
				np.mov("D");
				nextMov1 = "";
			}
			if(nextMov1 == "U"){
				np.mov("U");
				nextMov1 = "";
			}
			if(nextMov1 == "L"){
				np.mov("L");
				nextMov1 = "";
			}
			if(nextMov1 == "R"){
				np.mov("R");
				nextMov1 = "";
			}
		}
	}
}
