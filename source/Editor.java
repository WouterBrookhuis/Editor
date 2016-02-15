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
	private final static Editor instance = new Editor();
	
	private ArrayList<TileImage> tileImages;		//Tile images
	private ArrayList<Tile> tileSet;				//Active tileset
	private Level loadedLevel;						//Currently loaded level
	private EditorTool activeTool;					//Active viewport based tool
	
	public Editor()
	{
		tileImages = new ArrayList<>();
		tileSet = new ArrayList<>();
		loadedLevel = new Level("New Level", 20, 10);
		activeTool = new EditorTool();
		activeTool.enable();
		
		//Test
		TileImage wallImage = createNewTileImage("wall.png");
		TileImage grassImage = createNewTileImage("grass.png");
		TileImage sandImage = createNewTileImage("sand.png");
		
		Tile wallTile = createNewTile("wall", false, wallImage);
		Tile grassTile = createNewTile("grass", true, grassImage);
		Tile sandTile = createNewTile("sand", true, sandImage);
		
		for(int y = 0; y < loadedLevel.tileCountY; y++)
		{
			for(int x = 0; x < loadedLevel.tileCountX; x++)
			{
				if(x == 0 || y ==0 || x == loadedLevel.tileCountX - 1 || y == loadedLevel.tileCountY - 1)
					loadedLevel.tiles[x + y * loadedLevel.tileCountX] = wallTile;
				else if(x == 3 || x == 6)
					loadedLevel.tiles[x + y * loadedLevel.tileCountX] = sandTile;
				else
					loadedLevel.tiles[x + y * loadedLevel.tileCountX] = grassTile;
			}
		}
	}
	
	public static Editor getInstance()
	{
		return instance;
	}
	
	public void loadLevel(String level)
	{
		
	}
	
	public Level getLoadedLevel()
	{
		return loadedLevel;
	}
	
	public TileImage createNewTileImage(String imageFileName)
	{
		TileImage newImage = new TileImage(imageFileName);
		tileImages.add(newImage);
		return newImage;
	}
	
	public Tile createNewTile(String name, boolean walkable, TileImage image)
	{
		Tile newTile = new Tile(name, walkable, image);
		tileSet.add(newTile);
		return newTile;
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

class TileImage
{
	public String name;
	public String imageFileName;
	public BufferedImage image;
	
	public TileImage(String imageFileName)
	{
		//Try to load the image
		try{
			this.imageFileName = imageFileName;
			name = imageFileName;
			image = ImageIO.read(new File("../resources/" + imageFileName));
		}catch(IOException e){
			//Like I give a fuck
			System.out.println("Well, fuck");
		}
	}
}

class Tile
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

class Level
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
		
		Tile t = new Tile("Grass", true, new TileImage("grass.png"));
		for(int i = 0; i < tileCountX * tileCountY; i++)
		{
			tiles[i] = t;
		}
	}
}