import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TilePencilTool extends EditorTool
{
	private int paintTileIndex;

	public TilePencilTool()
	{
		super();
		paintTileIndex = -1;
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(Program.iconDir + "cursor-pencil.png");
		cursor = toolkit.createCustomCursor(image , new Point(9, 24), "img");
	}
	
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		tilePaint();	
	}
	
	public void mouseDragged(MouseEvent e)
	{
		super.mouseDragged(e);
		tilePaint();
	}
	
	private void tilePaint()
	{
		if((mouseDownMask & MouseEvent.BUTTON1_DOWN_MASK) != 0)
		{
			if(paintTileIndex >= 0 && paintTileIndex < Editor.instance.tileSet.length)
			{
				if(Editor.instance.loadedLevel != null)
				{
					if(Editor.instance.loadedLevel.setTile(currentTileIndex, Editor.instance.tileSet[paintTileIndex]))
					{
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