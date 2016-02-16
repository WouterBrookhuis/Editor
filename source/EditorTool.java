import java.awt.*;
import java.awt.event.*;

public class EditorTool
{
	private Point dragPrevPosition = new Point();		//Previous position of the mouse whilst dragging.
	public int currentTileIndex = 0;
	
	public void enable()
	{
	}
	
	public void disable()
	{
	}
	
	public void mouseMoved(MouseEvent e)
	{
		currentTileIndex = Viewport.getMain().mousePointToTileIndex(e.getPoint());
		Viewport.getMain().repaint();
	}
	
	public void mouseDragged(MouseEvent e)
	{
		Point mouseDelta = new Point(e.getX() - dragPrevPosition.x, e.getY() - dragPrevPosition.y);
		Point newOffset = Viewport.getMain().getOffset();
		newOffset.x += mouseDelta.x;
		newOffset.y += mouseDelta.y;
		Viewport.getMain().setOffset(newOffset);
		Viewport.getMain().repaint();
		dragPrevPosition = e.getPoint();
	}
	
	public void mousePressed(MouseEvent e)
	{
		dragPrevPosition = e.getPoint();
	}
	
	public void mouseReleased(MouseEvent e)
	{
	}
	
	public void mouseEntered(MouseEvent e)
	{
	}
	
	public void mouseExited(MouseEvent e)
	{
	}
	
	public void mouseClicked(MouseEvent e)
	{
		System.out.println("Clicked tile index: " + Viewport.getMain().mousePointToTileIndex(e.getPoint()));
	}
}