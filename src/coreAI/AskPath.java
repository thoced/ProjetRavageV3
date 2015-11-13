package coreAI;

import java.util.List;

import org.jbox2d.common.Vec2;

public class AskPath 
{
	private ICallBackAStar caller;
	
	private int x;
	private int y;
	private int tx;
	private int ty;
	
	private float fx;
	private float fy;
	private float tfx;
	private float tfy;
	
	private Vec2 positionInitial;
	private Vec2 positionFinal;

	
	public AskPath(ICallBackAStar caller,Vec2 positionInitial,Vec2 positionFinal)
	
	{
		this.caller = caller;
		this.positionInitial = positionInitial;
		this.positionFinal = positionFinal;
		
	}
	
	
	


	public Vec2 getPositionInitial() {
		return positionInitial;
	}





	public Vec2 getPositionFinal() {
		return positionFinal;
	}





	/**
	 * @return the fx
	 */
	public float getFx() {
		return fx;
	}




	/**
	 * @param fx the fx to set
	 */
	public void setFx(float fx) {
		this.fx = fx;
	}




	/**
	 * @return the fy
	 */
	public float getFy() {
		return fy;
	}




	/**
	 * @param fy the fy to set
	 */
	public void setFy(float fy) {
		this.fy = fy;
	}




	/**
	 * @return the tfx
	 */
	public float getTfx() {
		return tfx;
	}




	/**
	 * @param tfx the tfx to set
	 */
	public void setTfx(float tfx) {
		this.tfx = tfx;
	}




	/**
	 * @return the tfy
	 */
	public float getTfy() {
		return tfy;
	}




	/**
	 * @param tfy the tfy to set
	 */
	public void setTfy(float tfy) {
		this.tfy = tfy;
	}




	/**
	 * @return the caller
	 */
	public ICallBackAStar getCaller() {
		return caller;
	}

	/**
	 * @param caller the caller to set
	 */
	public void setCaller(ICallBackAStar caller) {
		this.caller = caller;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the tx
	 */
	public int getTx() {
		return tx;
	}

	/**
	 * @param tx the tx to set
	 */
	public void setTx(int tx) {
		this.tx = tx;
	}

	/**
	 * @return the ty
	 */
	public int getTy() {
		return ty;
	}

	/**
	 * @param ty the ty to set
	 */
	public void setTy(int ty) {
		this.ty = ty;
	}
	
	
}
