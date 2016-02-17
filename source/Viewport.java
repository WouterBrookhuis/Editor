/*
	Viewport class.
	This is a JPanel that renders the current level.
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.image.*;


public class Viewport extends JPanel implements MouseListener, MouseMotionListener, KeyListener
{
	private static Viewport main;						//The main viewport. We usually only have one. Use Viewport.getMain() to get it.
	private int offsetX;								//Draw offset x
	private int offsetY;								//Draw offset y
	private int pixelsPerTile;							//Pixels per tile
	private Point dragPrevPosition = new Point();		//Previous position of the mouse whilst dragging.
	
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
		Level ll = Editor.instance.loadedLevel;
		if(ll != null)
		{
			for(int y = 0; y < ll.tileCountY; y++)
			{
				for(int x = 0; x < ll.tileCountX; x++)
				{
					BufferedImage tileImage = ll.tiles[x + y * ll.tileCountX].getBufferedImage();
					if(tileImage != null)
					{
						graphics.drawImage(tileImage,
						x * pixelsPerTile + offsetX, y * pixelsPerTile + offsetY,
						pixelsPerTile, pixelsPerTile, null);
						
						/*if(x + y * ll.tileCountX == Editor.instance.getActiveTool().currentTileIndex)
							graphics.fillRect(x * pixelsPerTile + offsetX, y * pixelsPerTile + offsetY, pixelsPerTile, pixelsPerTile);*/
					}
				}
			}
		}
		
		//Tool overlay painting
		Editor.instance.getActiveTool().paint(graphics);
		
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
	
	public void centerOnLevel()
	{
		Level ll = Editor.instance.loadedLevel;
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
		repaint();
	}
	
	public int mousePointToTileIndex(Point mousePoint)
	{
		int result = -1;
		
		Level ll = Editor.instance.loadedLevel;
		if(ll != null)
		{
			int mapEndLeft = offsetX;
			int mapEndRight = offsetX + pixelsPerTile * ll.tileCountX;
			int mapEndTop = offsetY;
			int mapEndBottom = offsetY + pixelsPerTile * ll.tileCountY;
			if(mousePoint.x < mapEndRight && mousePoint.x >= mapEndLeft
				&& mousePoint.y < mapEndBottom && mousePoint.y >= mapEndTop)
			{
				int x = (mousePoint.x- offsetX) / pixelsPerTile;
				int y = (mousePoint.y - offsetY) / pixelsPerTile;
				result = x + y * ll.tileCountX;
			}
		}
		return result;
	}
	
	public int getPixelsPerTile()
	{
		return pixelsPerTile;
	}
	
	/*
		Our event listeners
	*/
	public void mouseMoved(MouseEvent e)
	{
		Editor.instance.getActiveTool().mouseMoved(e);
	}
	
	public void mouseDragged(MouseEvent e)
	{
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			Point mouseDelta = new Point(e.getX() - dragPrevPosition.x, e.getY() - dragPrevPosition.y);
			Point newOffset = getOffset();
			newOffset.x += mouseDelta.x;
			newOffset.y += mouseDelta.y;
			setOffset(newOffset);
			repaint();
			dragPrevPosition = e.getPoint();
		}
		Editor.instance.getActiveTool().mouseDragged(e);
	}
	
	public void mousePressed(MouseEvent e)
	{
		requestFocusInWindow();
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			dragPrevPosition = e.getPoint();
		}
		Editor.instance.getActiveTool().mousePressed(e);
	}
	
	public void mouseReleased(MouseEvent e)
	{
		Editor.instance.getActiveTool().mouseReleased(e);
	}
	
	public void mouseEntered(MouseEvent e)
	{
		Editor.instance.getActiveTool().mouseEntered(e);
	}
	
	public void mouseExited(MouseEvent e)
	{
		Editor.instance.getActiveTool().mouseExited(e);
	}
	
	public void mouseClicked(MouseEvent e)
	{
		Editor.instance.getActiveTool().mouseClicked(e);
	}
	
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_F)
		{
			centerOnLevel();
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
	}	
}