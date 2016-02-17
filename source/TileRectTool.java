import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;

public class TileRectTool extends EditorTool
{
	private int paintTileIndex;
	private Point rectStartPoint;
	private Point rectEndPoint;

	public TileRectTool()
	{
		super();
		paintTileIndex = -1;
		//cursor = Cursor.CROSSHAIR_CURSOR;
	}
	
	public void paint(Graphics2D g)
	{
		//If we are currently dragging a rectangle both of these will be not null
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
	}
	
	public void mouseDragged(MouseEvent e)
	{
		super.mouseDragged(e);
		if((mouseDownMask & MouseEvent.BUTTON1_DOWN_MASK) != 0)
		{
			if(Editor.instance.loadedLevel != null)
			{
				rectEndPoint = Editor.instance.loadedLevel.tileIndexToPoint(currentTileIndex);
				Viewport.getMain().repaint();	//Force repaint so our paint method gets called
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
				rectStartPoint = Editor.instance.loadedLevel.tileIndexToPoint(currentTileIndex);
			}
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{
		super.mousePressed(e);
		
		if(SwingUtilities.isLeftMouseButton(e))
		{
			if(paintTileIndex >= 0 && paintTileIndex < Editor.instance.tileSet.length)
			{
				if(Editor.instance.loadedLevel != null)
				{
					Level level =  Editor.instance.loadedLevel;
					
					rectEndPoint = Editor.instance.loadedLevel.tileIndexToPoint(currentTileIndex);
					if(rectStartPoint != null && rectEndPoint != null)
					{
						Point minPoint = new Point(Math.min(rectStartPoint.x, rectEndPoint.x), Math.min(rectStartPoint.y, rectEndPoint.y));
						Point maxPoint = new Point(Math.max(rectStartPoint.x, rectEndPoint.x), Math.max(rectStartPoint.y, rectEndPoint.y));
						for(int y = minPoint.y; y <= maxPoint.y; y++)
						{
							for(int x = minPoint.x; x <= maxPoint.x; x++)
							{
								Editor.instance.loadedLevel.setTile(x, y, Editor.instance.tileSet[paintTileIndex]);
							}
						}
					}
					
					Viewport.getMain().repaint();
				}
			}
		}
		
		rectEndPoint = null;
		rectStartPoint = null;
	}
	
	public void setPaintTileIndex(int tile)
	{
		paintTileIndex = tile;
	}
}