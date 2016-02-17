import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EditorTool
{
	protected Point dragPrevPosition = new Point();		//Previous position of the mouse whilst dragging.
	public int currentTileIndex = 0;
	protected int mouseButtonMask = MouseEvent.BUTTON1_DOWN_MASK 
            | MouseEvent.BUTTON2_DOWN_MASK 
            | MouseEvent.BUTTON3_DOWN_MASK;
	protected int mouseDownMask = 0;
	
	protected Cursor cursor;
	
	public EditorTool()
	{
		//mainPane.setCursor (c);*/
		//cursor = Cursor.DEFAULT_CURSOR;
	}
	
	public void enable()
	{
		if(Viewport.getMain() != null)
			Viewport.getMain().setCursor(cursor);
	}
	
	public void disable()
	{
		if(Viewport.getMain() != null)
			Viewport.getMain().setCursor(null);
	}
	
	public void paint(Graphics2D g)
	{
	}
	
	public void mouseMoved(MouseEvent e)
	{
		//mouseDownMask = (e.getModifiersEx() & mouseButtonMask);
		currentTileIndex = Viewport.getMain().mousePointToTileIndex(e.getPoint());
		//Viewport.getMain().repaint();
	}
	
	public void mouseDragged(MouseEvent e)
	{
		mouseDownMask = (e.getModifiersEx() & mouseButtonMask);
		currentTileIndex = Viewport.getMain().mousePointToTileIndex(e.getPoint());
		//Viewport.getMain().repaint();
	}
	
	public void mousePressed(MouseEvent e)
	{
		mouseDownMask = (e.getModifiersEx() & mouseButtonMask);
		currentTileIndex = Viewport.getMain().mousePointToTileIndex(e.getPoint());
	}
	
	public void mouseReleased(MouseEvent e)
	{
		mouseDownMask = (e.getModifiersEx() & mouseButtonMask);
	}
	
	public void mouseEntered(MouseEvent e)
	{
		mouseDownMask = (e.getModifiersEx() & mouseButtonMask);
	}
	
	public void mouseExited(MouseEvent e)
	{
		mouseDownMask = (e.getModifiersEx() & mouseButtonMask);
	}
	
	public void mouseClicked(MouseEvent e)
	{
	}
	//Keyboard
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void keyPressed(KeyEvent e)
	{
	}
	
	public void keyReleased(KeyEvent e)
	{
	}	
}