

//Esta clase representa una celda
public class Coor{
	private int X,Y;
	private boolean allow = true;
	private boolean offsetR = false, offsetL = false, offsetD = false, offsetU = false;
	
	public Coor(int x, int y){
		this.X = x;
		this.Y = y;
	}
	
	public String toString(){
		return "<" + X + "," + Y + ">";
	}
	
	public int X(){
		return X;
	}
	
	/*
	public boolean are_these_pixels_from_this_coorX(int x){
		if(x >= X && x < X+14){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean are_these_pixels_from_this_coorY(int y){
		if(y >= Y && y < Y+14){
			return true;
		}else{
			return false;
		}
	}
	*/
	public int Y(){
		return Y;
	}
	
	public void setAllow(boolean a){
		this.allow = a;
		if(a){
			undo_offsetR();
			undo_offsetL();
			undo_offsetD();
			undo_offsetU();
		}
	}
	
	public boolean isAllow(){
		return allow;
	}
	
	public void offsetR(){
		if(!offsetR){
			this.X = X-7;
			offsetR = true;
		}
	}
	
	public void undo_offsetR(){
		if(offsetR){
			this.X = X+7;
			offsetR = false;
		}
	}
	
	public void offsetL(){
		if(!offsetL){
			this.X = X+7;
			offsetL = true;
		}
	}
	
	public void undo_offsetL(){
		if(offsetL){
			this.X = X-7;
			offsetL = false;
		}
	}
	
	public void offsetU(){
		if(!offsetU){
			this.Y = Y+7;
			offsetU = true;
		}
	}
	
	public void undo_offsetU(){
		if(offsetU){
			this.Y = Y-7;
			offsetU = false;
		}
	}
	
	public void offsetD(){
		if(!offsetD){
			this.Y = Y-7;
			offsetD = true;
		}
	}
	
	public void undo_offsetD(){
		if(offsetD){
			this.Y = Y+7;
			offsetD = false;
		}
	}
}