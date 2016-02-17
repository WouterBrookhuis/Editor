/*
	imports, or Why I hate Java
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.image.*;
import javax.imageio.*;
//import java.awt.image.BufferedImage;
import java.io.*;

import java.util.*;

public class Editor
{
	public static Editor instance;
	
	public TileImage[] tileImages;					//Tile images
	public Tile[] tileSet;							//Active tileset
	public Level loadedLevel;						//Currently loaded level
	public EditorTool activeTool;					//Active viewport based tool

	public EditorTool dummyTool = new EditorTool();
	public TilePencilTool pencilTool = new TilePencilTool();
	public TileFillTool fillTool = new TileFillTool();
	public TileRectTool rectTool = new TileRectTool();
	public TileCopyTool copyTool = new TileCopyTool();
	
	public Editor()
	{
		if(instance == null)
			instance = this;
		
		tileImages = new TileImage[64];
		tileSet = new Tile[64];
		//loadedLevel = new Level("New Level", 20, 20);
		
		activeTool = dummyTool;
		activeTool.enable();

		
		//Test
		Tile wallTile = createNewTile("wall", false, createNewTileImage("wall.png"));
		Tile grassTile = createNewTile("grass", true, createNewTileImage("grass.png"));
		Tile sandTile = createNewTile("sand", true, createNewTileImage("sand.png"));
	}
	
	public void saveLevel(File file)
	{
		if(loadedLevel != null)
		{
			try
			{
				FileOutputStream fileOut =
				new FileOutputStream(file);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(loadedLevel);
				out.close();
				fileOut.close();
				System.out.printf("Serialized data is saved in level.ser");
			}
			catch(IOException i)
			{
				i.printStackTrace();
			}
		}
	}
	
	public void loadLevel(File file)
	{
		Level l = null;
		
		try
		{
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			l = (Level)in.readObject();
			in.close();
			fileIn.close();
		}
		catch(IOException i)
		{
			i.printStackTrace();
			return;
		}
		catch(ClassNotFoundException c)
		{
			System.out.println("Could not find class!");
			c.printStackTrace();
			return;
		}
		
		if(l != null)
		{
			//FFS
			for(int i = 0; i < l.tiles.length; i++)
			{
				for(int j = 0; j < tileSet.length; j++)
				{
					if(tileSet[j] == null)
						break;
					if(l.tiles[i].name.equals(tileSet[j].name))
					{
						System.out.println("Replacing tile " + tileSet[j].name);
						l.tiles[i] = tileSet[j];
						break;
					}
					else if(j == tileSet.length - 1)	//None in our tileSet did not match
					{
						//This is a new tile type!
						TileImage img = createNewTileImage(l.tiles[i].image.imageFileName);
						createNewTile(l.tiles[i].name, l.tiles[i].walkable, img);
					}
				}
			}
			
			loadedLevel = l;
			Viewport.getMain().centerOnLevel();
			
			Program.frame.setTitle("Editor - " + loadedLevel.name);
		}
	}
	
	public void newLevel(String name, int sizeX, int sizeY)
	{
		loadedLevel = new Level(name, sizeX, sizeY);
		Viewport.getMain().centerOnLevel();
		Program.frame.setTitle("Editor - " + loadedLevel.name);
	}
	
	public TileImage createNewTileImage(String imageFileName)
	{
		TileImage newImage = new TileImage(imageFileName);
		for(int i = 0; i < tileImages.length; i++)
		{
			if(tileImages[i] == null){
				tileImages[i] = newImage;
				return newImage;
			}
		}
		return null;
	}
	
	public Tile createNewTile(String name, boolean walkable, TileImage image)
	{
		Tile newTile = new Tile(name, walkable, image);
		for(int i = 0; i < tileSet.length; i++)
		{
			if(tileSet[i] == null){
				tileSet[i] = newTile;
				return newTile;
			}
		}
		return null;
	}
	
	public EditorTool getActiveTool()
	{
		return activeTool;
	}
	
	public void setActiveTool(EditorTool newTool)
	{
		activeTool.disable();
		activeTool = newTool;
		activeTool.enable();
	}
}

class TileImage implements Serializable
{
	public String name;
	public String imageFileName;
	public transient BufferedImage image;
	
	public TileImage(String imageFileName)
	{
		this.imageFileName = imageFileName;
		name = imageFileName;
		loadBufferedImage();
	}
	
	public void loadBufferedImage()
	{
		try{
			image = ImageIO.read(new File("../resources/" + imageFileName));
		}catch(IOException e){
			//Like I give a fuck
			System.out.println("Well, fuck");
		}
	}
}

class Tile implements Serializable
{
	public String name;
	public boolean walkable;
	public TileImage image;
	
	public Tile(String name, boolean walkable, TileImage image)
	{
		this.name = name;
		this.walkable = walkable;
		this.image = image;
	}
	
	public BufferedImage getBufferedImage()
	{
		return image.image;
	}
}

class Level implements Serializable
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
}