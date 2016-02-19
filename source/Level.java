import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class Level
{
	public String name;
	public Tile[] tiles;
	public int tileCountX;
	public int tileCountY;
	
	public Level(String name, int tileCountX, int tileCountY)
	{
		this.name = name;
		this.tileCountX = tileCountX;
		this.tileCountY = tileCountY;
		tiles = new Tile[tileCountX * tileCountY];
		
		//Level fills iteself 
		Tile t = new Tile("empty", true, new TileImage("empty.png"));
		for(int i = 0; i < tileCountX * tileCountY; i++)
		{
			tiles[i] = t;
		}
	}
	
	//Draws the tilemap to an image and saves that image as file
	public void saveTilemapAsImage(File file)
	{
		BufferedImage image = new BufferedImage(tileCountX * 32, tileCountY * 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		
		for(int y = 0; y < tileCountY; y++)
		{
			for(int x = 0; x < tileCountX; x++)
			{
				g.drawImage(tiles[x + y * tileCountX].getBufferedImage(),
					x * 32, y * 32,
					32, 32, null);
			}
		}
		
		try
		{
			ImageIO.write(image, "PNG", file);
		}
		catch (IOException ie)
		{
			
		}
		
		g.dispose();
	}
	
	//Sets the tile at location x, y to tile. Performs bounds check. Returns true on success.
	public boolean setTile(int x, int y, Tile tile)
	{
		if(x < 0 || x >= tileCountX || y < 0 || y >= tileCountY)
			return false;
		
		tiles[x + y * tileCountX] = tile;
		return true;
	}
	
	//Sets the tile at the specified index to tile. Performs bounds check. Returns true on success.
	public boolean setTile(int index, Tile tile)
	{
		if(index < 0 || index >= tiles.length)
			return false;
		
		tiles[index] = tile;
		return true;
	}
	
	//Sets the tile at location x, y to tile. Performs bounds check. Returns true on success.
	public Tile getTile(int x, int y)
	{
		if(x < 0 || x >= tileCountX || y < 0 || y >= tileCountY)
			return null;
		
		return tiles[x + y * tileCountX];
	}
	
	//Sets the tile at location x, y to tile. Performs bounds check. Returns true on success.
	public Tile getTile(int index)
	{
		if(index < 0 || index >= tiles.length)
			return null;
		
		return tiles[index];
	}
	
	public Point tileIndexToPoint(int index)
	{
		if(index < 0 || index >= tiles.length)
			return null;
		
		int y = index / tileCountX;
		int x = index % tileCountX;
		return new Point(x, y);
	}
	
	public void resize(int newSizeX, int newSizeY)
	{
		if(newSizeX <= 0 || newSizeY <= 0)
			return;
		
		int minx = Math.min(newSizeX, tileCountX);
		int miny = Math.min(newSizeY, tileCountY);
		
		Editor.instance.copyTool.copyToBuffer(new Point(0, 0), new Point(minx - 1, miny - 1));
		tiles = new Tile[newSizeX * newSizeY];
		tileCountX = newSizeX;
		tileCountY = newSizeY;
		Tile t = new Tile("empty", true, new TileImage("empty.png"));
		for(int i = 0; i < tileCountX * tileCountY; i++)
		{
			tiles[i] = t;
		}
		Editor.instance.copyTool.pasteFromBuffer(new Point(0, 0));
	}
	
	public void setName(String newName)
	{
		name = newName;
	}
}