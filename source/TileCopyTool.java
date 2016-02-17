import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;

public class TileCopyTool extends EditorTool
{
	private Point rectStartPoint;
	private Point rectEndPoint;
	private Tile[] copyBuffer;
	private Point copyBufferSize;
	private Point pastePosition;		//Top left tile

	public TileCopyTool()
	{
		super();
		//cursor = Cursor.CROSSHAIR_CURSOR;
	}
	
	public void disable()
	{
		super.disable();
		clearBuffer();
	}
	
	public void paint(Graphics2D g)
	{
		//Draw the copy rectangle
		if(rectStartPoint != null && rectEndPoint != null)
		{
			int pixelsPerTile = Viewport.getMain().getPixelsPerTile();
			Point offset = Viewport.getMain().getOffset();
			Point minPoint = new Point(Math.min(rectStartPoint.x, rectEndPoint.x), Math.min(rectStartPoint.y, rectEndPoint.y));
			Point maxPoint = new Point(Math.max(rectStartPoint.x, rectEndPoint.x), Math.max(rectStartPoint.y, rectEndPoint.y));
			
			//Draw a translucent green rectangle that indicates our fill area
			g.setPaint(new Color(0, 255, 0, 64));
			g.fill(new Rectangle(minPoint.x * pixelsPerTile + offset.x, minPoint.y * pixelsPerTile + offset.y,
										(maxPoint.x + 1- minPoint.x) * pixelsPerTile, (maxPoint.y + 1 - minPoint.y) * pixelsPerTile));
			
		}
		else if(copyBuffer != null)
		{
			//Draw a black transparent rectangle over the rest of the map to indicate that we are in paste mode
			g.setPaint(new Color(0, 0, 0, 64));
			g.fill(new Rectangle(0, 0, Viewport.getMain().getWidth(), Viewport.getMain().getHeight()));
			//Handy references
			int pixelsPerTile = Viewport.getMain().getPixelsPerTile();
			Point offset = Viewport.getMain().getOffset();
			//Draw our buffer
			for(int y = 0; y < copyBufferSize.y; y++)
			{
				for(int x = 0; x < copyBufferSize.x; x++)
				{
					BufferedImage tileImage = copyBuffer[x + y * copyBufferSize.x].getBufferedImage();
					if(tileImage != null)
					{
						g.drawImage(tileImage,
							(x + pastePosition.x) * pixelsPerTile + offset.x, (y + pastePosition.y) * pixelsPerTile + offset.y,
							pixelsPerTile, pixelsPerTile, null);
					}
				}
			}
			//Draw some text that indicates what to do
			g.setPaint(new Color(200, 200, 200, 255));
			g.fill(new Rectangle(0, 0, 230, 30));
			g.setPaint(new Color(0, 0, 0, 255));
			g.drawString("Press enter to confirm, escape to exit", 10, 20);
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{
		super.mouseDragged(e);
		if((mouseDownMask & MouseEvent.BUTTON1_DOWN_MASK) != 0)
		{
			if(Editor.instance.loadedLevel != null)
			{
				//If our buffer is empty
				if(copyBuffer == null)
				{
					rectEndPoint = Editor.instance.loadedLevel.tileIndexToPoint(currentTileIndex);
					Viewport.getMain().repaint();	//Force repaint so our paint method gets called
				}
				else
				{
					//Set the paste position
					pastePosition = Editor.instance.loadedLevel.tileIndexToPoint(currentTileIndex);
					Viewport.getMain().repaint();
				}
			}
		}
	}
	
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		if(SwingUtilities.isLeftMouseButton(e))
		{
			if(Editor.instance.loadedLevel != null)
			{
				//If our buffer is empty
				if(copyBuffer == null)
				{
					//Set the start point
					rectStartPoint = Editor.instance.loadedLevel.tileIndexToPoint(currentTileIndex);
				}
				else
				{
					//Set the paste position
					pastePosition = Editor.instance.loadedLevel.tileIndexToPoint(currentTileIndex);
					Viewport.getMain().repaint();
				}
			}
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{
		super.mousePressed(e);
		
		if(SwingUtilities.isLeftMouseButton(e))
		{
			if(Editor.instance.loadedLevel != null)
			{
				//If our buffer is empty
				if(copyBuffer == null)
				{
					Level level =  Editor.instance.loadedLevel;
					
					rectEndPoint = Editor.instance.loadedLevel.tileIndexToPoint(currentTileIndex);
					if(rectStartPoint != null && rectEndPoint != null)
					{
						Point minPoint = new Point(Math.min(rectStartPoint.x, rectEndPoint.x), Math.min(rectStartPoint.y, rectEndPoint.y));
						Point maxPoint = new Point(Math.max(rectStartPoint.x, rectEndPoint.x), Math.max(rectStartPoint.y, rectEndPoint.y));
						copyToBuffer(minPoint, maxPoint);
					}
					
					Viewport.getMain().repaint();
				}
			}
		}
		
		rectEndPoint = null;
		rectStartPoint = null;
	}
	
	private void copyToBuffer(Point minPoint, Point maxPoint)
	{
		copyBufferSize = new Point(maxPoint.x - minPoint.x + 1, maxPoint.y - minPoint.y + 1);
		copyBuffer = new Tile[copyBufferSize.x * copyBufferSize.y];
		for(int y = minPoint.y; y <= maxPoint.y; y++)
		{
			for(int x = minPoint.x; x <= maxPoint.x; x++)
			{
				copyBuffer[(x - minPoint.x) + (y - minPoint.y) * copyBufferSize.x] = Editor.instance.loadedLevel.getTile(x, y);
			}
		}
		pastePosition = minPoint;
	}
	
	private void pasteFromBuffer(Point topLeft)
	{
		if(Editor.instance.loadedLevel != null)
		{
			for(int y = 0; y < copyBufferSize.y; y++)
			{
				for(int x = 0; x < copyBufferSize.x; x++)
				{
					Editor.instance.loadedLevel.setTile(x + pastePosition.x, y + pastePosition.y, copyBuffer[x + y * copyBufferSize.x]);
				}
			}
		}
			
		//End with resetting our buffer
		copyBufferSize = null;
		copyBuffer = null;
		pastePosition = null;
	}
	
	private void clearBuffer()
	{
		copyBuffer = null;
		copyBufferSize = null;
		pastePosition = null;
	}
	
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if(copyBuffer != null)
			{
				pasteFromBuffer(pastePosition);
				Viewport.getMain().repaint();
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			clearBuffer();
			Viewport.getMain().repaint();
		}
	}	
}