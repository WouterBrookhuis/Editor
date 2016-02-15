/*
	Viewport class.
	This is a JPanel that renders the current level.
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Viewport extends JPanel implements MouseListener, MouseMotionListener, KeyListener
{
	private static Viewport main;						//The main viewport. We usually only have one. Use Viewport.getMain() to get it.
	private int offsetX;								//Draw offset x
	private int offsetY;								//Draw offset y
	private int pixelsPerTile;							//Pixels per tile
	
	private int currentTileIndex = 0;
	
	public Viewport()
	{
		super();
		offsetX = 0;
		offsetY = 0;
		pixelsPerTile = 32;
		setPreferredSize(new Dimension(800, 600));
		if(main == null)
		{
			main = this;
		}		
	}
	
	//Returns the main viewport
	public static Viewport getMain()
	{
		return main;
	}

	//Override for paintComponent
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D)g.create();
		Insets insets = getInsets();
		//graphics.translate(insets.left, insets.top);
		//graphics.drawString("This is my panel, bitch!", x, y);
		//graphics.drawString("This is my panel, bitch!", 10, 20);
		Level ll = Editor.getInstance().getLoadedLevel();
		if(ll != null)
		{
			for(int y = 0; y < ll.tileCountY; y++)
			{
				for(int x = 0; x < ll.tileCountX; x++)
				{
					graphics.drawImage(ll.tiles[x + y * ll.tileCountX].getBufferedImage(),
					x * pixelsPerTile + offsetX, y * pixelsPerTile + offsetY,
					pixelsPerTile, pixelsPerTile, null);
					
					if(x + y * ll.tileCountX == currentTileIndex)
						graphics.fillRect(x * pixelsPerTile + offsetX, y * pixelsPerTile + offsetY, pixelsPerTile, pixelsPerTile);
				}
			}
		}
		
		graphics.dispose();
	}
	
	public void setOffset(int x, int y)
	{
		offsetX = x;
		offsetY = y;
	}
	
	public void setOffset(Point newOffset)
	{
		offsetX = newOffset.x;
		offsetY = newOffset.y;
	}
	
	public Point getOffset()
	{
		return new Point(offsetX, offsetY);
	}
	
	/*
		Our event listeners
	*/
	public void mouseMoved(MouseEvent e)
	{
		Editor.getInstance().getActiveTool().mouseMoved(e);
		currentTileIndex = mousePointToTileIndex(e.getPoint());
		repaint();
	}
	
	public void mouseDragged(MouseEvent e)
	{
		Editor.getInstance().getActiveTool().mouseDragged(e);
	}
	
	public void mousePressed(MouseEvent e)
	{
		requestFocusInWindow();
		Editor.getInstance().getActiveTool().mousePressed(e);
	}
	
	public void mouseReleased(MouseEvent e)
	{
		Editor.getInstance().getActiveTool().mouseReleased(e);
	}
	
	public void mouseEntered(MouseEvent e)
	{
		Editor.getInstance().getActiveTool().mouseEntered(e);
	}
	
	public void mouseExited(MouseEvent e)
	{
		Editor.getInstance().getActiveTool().mouseExited(e);
	}
	
	public void mouseClicked(MouseEvent e)
	{
		Editor.getInstance().getActiveTool().mouseClicked(e);
	}
	
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_F)
		{
			centerOnLevel();
			repaint();
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
	}
	
	public void centerOnLevel()
	{
		Level ll = Editor.getInstance().getLoadedLevel();
		if(ll != null)
		{
			offsetX = (getWidth() - ll.tileCountX * pixelsPerTile) / 2;
			offsetY = (getHeight() - ll.tileCountY * pixelsPerTile) / 2;
		}
		else
		{
			offsetX = 0;
			offsetY = 0;
		}
	}
	
	public int mousePointToTileIndex(Point mousePoint)
	{
		int result = -1;
		
		Level ll = Editor.getInstance().getLoadedLevel();
		if(ll != null)
		{
		
			int x = (mousePoint.x - offsetX) / pixelsPerTile;
			int y = (mousePoint.y - offsetY) / pixelsPerTile;
			result = x + y * ll.tileCountX;
		}
		return result;
	}
}