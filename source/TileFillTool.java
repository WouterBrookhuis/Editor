import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;

public class TileFillTool extends EditorTool
{
	private int paintTileIndex;

	public TileFillTool()
	{
		super();
		paintTileIndex = -1;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(Program.iconDir + "cursor-fill.png");
		cursor = toolkit.createCustomCursor(image , new Point(11, 24), "img");
		//cursor = Cursor.CROSSHAIR_CURSOR;
	}
	
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		tileFill();
	}
	
	private void tileFill()
	{
		if((mouseDownMask & MouseEvent.BUTTON1_DOWN_MASK) != 0)
		{
			if(paintTileIndex >= 0 && paintTileIndex < Editor.instance.tileSet.length)
			{
				if(Editor.instance.loadedLevel != null)
				{
					Level level =  Editor.instance.loadedLevel;
					
					Point node = level.tileIndexToPoint(currentTileIndex);
					Tile targetTileType = level.getTile(currentTileIndex);
					Tile replacementTileType =  Editor.instance.tileSet[paintTileIndex];
					
					if(targetTileType == replacementTileType)
						return;
					Queue queue = new LinkedList<Point>();
					LinkedList<Point> processed = new LinkedList<Point>();
					
					queue.offer(node);
					
					while(queue.size() > 0)
					{
						node = (Point)queue.remove();
						if(level.getTile(node.x, node.y) == targetTileType)
						{
							level.setTile(node.x, node.y, replacementTileType);
							processed.add(node);
							Point w, e, n, s;
							w = new Point(node.x + 1, node.y);
							e = new Point(node.x - 1, node.y);
							n = new Point(node.x, node.y - 1);
							s = new Point(node.x, node.y + 1);
							if(level.getTile(w.x, w.y) != null && !processed.contains(w)){
								queue.offer(w);
							}
							if(level.getTile(e.x, e.y) != null && !processed.contains(e)){
								queue.offer(e);
							}
							if(level.getTile(n.x, n.y) != null && !processed.contains(n)){
								queue.offer(n);
							}
							if(level.getTile(s.x, s.y) != null && !processed.contains(s)){
								queue.offer(s);
							}
						}
					}
					
					Viewport.getMain().repaint();
				}
			}
		}
	}
	
	public void setPaintTileIndex(int tile)
	{
		paintTileIndex = tile;
	}
}