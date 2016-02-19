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
	
	private static final int tileImageCount = 64;
	private static final int tileSetCount = 64;
	
	public Editor()
	{
		if(instance == null)
			instance = this;
		
		tileImages = new TileImage[tileImageCount];
		tileSet = new Tile[tileSetCount];
		//loadedLevel = new Level("New Level", 20, 20);
		
		activeTool = dummyTool;
		activeTool.enable();
		
		scanTiles();
		
		//Test
		Tile wallTile = createNewTile("wall", false, findTileImage("wall.png"));
		Tile grassTile = createNewTile("grass", true, findTileImage("grass.png"));
		Tile sandTile = createNewTile("sand", true, findTileImage("sand.png"));
	}
	
	//Saves a level
	//Currently testing
	public void saveLevel(File file)
	{
		LevelStream ls = new LevelStream(file, loadedLevel);
		ls.save();
	}
	
	//Loads a level
	//Currently broken
	public void loadLevel(File file)
	{
		LevelStream ls = new LevelStream(file, null);
		LoadedLevelData l = ls.load();
		if(l != null)
		{
			loadedLevel = l.level;
			Arrays.fill(tileSet, null);
			Arrays.fill(tileImages, null);
			System.arraycopy(l.tileSet, 0, tileSet, 0, l.tileSet.length);
			System.arraycopy(l.tileImages, 0, tileImages, 0, l.tileImages.length);
			
			Viewport.getMain().centerOnLevel();
			Program.frame.setTitle("Editor - " + loadedLevel.name);
		}
		return;
	}
	
	//Creates a new level
	public void newLevel(String name, int sizeX, int sizeY)
	{
		loadedLevel = new Level(name, sizeX, sizeY);
		Viewport.getMain().centerOnLevel();
		Program.frame.setTitle("Editor - " + loadedLevel.name);
	}
	
	//Creates a new TileImage object and adds it to the array if possible.
	//Returns the new TileImage on success
	//Returns null if the array is full
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
	
	//Creates a new TileImage object and adds it to the array if possible.
	//Returns the new TileImage on success
	//Returns null if the array is full
	public TileImage createNewTileImage(File file)
	{
		TileImage newImage = new TileImage(file);
		for(int i = 0; i < tileImages.length; i++)
		{
			if(tileImages[i] == null){
				tileImages[i] = newImage;
				return newImage;
			}
		}
		return null;
	}
	
	//Finds a TileImage object in our tileImages array with the specified filename
	public TileImage findTileImage(String filename)
	{
		for(int i = 0; i < tileImages.length; i++)
		{
			if(tileImages[i] == null){
				return null;
			}
			if(tileImages[i].imageFileName.equals(filename)){
				return tileImages[i];
			}
		}
		return null;
	}
	
	//Creates a new Tile object and adds it to the array if possible.
	//Returns the new Tile on success
	//Returns null if the array is full
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
	
	//Returns the active editor tool
	public EditorTool getActiveTool()
	{
		return activeTool;
	}
	
	//Sets the active editor tool properly
	public void setActiveTool(EditorTool newTool)
	{
		activeTool.disable();
		activeTool = newTool;
		activeTool.enable();
	}
	
	//Scans for tile images in the resources/tiles folder
	//It replaces the tileImages array with a new one
	//Refernces to replaced TileImage objects get changed to the new ones
	public void scanTiles()
	{
		tileImages = new TileImage[tileImageCount];
		
		//Get all png, jpg and bmp images in the tiles folder
		File dir = new File(Program.resourceDir + "tiles");
		File [] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".bmp")) && !name.startsWith("empty");
			}
		});
		//If we found files
		if(files != null)
		{
			//Create new TileImage objects and add them to the array (createNewTileImage does this)
			for(int i = 0; i < files.length; i++)
			{
				createNewTileImage(files[i]);
				System.out.println("Found tile image " + files[i].getName());
			}
			//Loop trough our tile set
			for(int i = 0; i < tileSet.length; i++)
			{
				//tileSet should always have all tiles bunched together, so the first null means we are out of tiles
				if(tileSet[i] == null)
					break;
				//Loop trough our now updated tile image list
				for(int j = 0; j < tileImages.length; j++)
				{
					//Same as with tileSet
					if(tileImages[j] == null)
						break;
					//If the file names match we replace the reference
					if(tileSet[i].image.imageFileName.equals(tileImages[j].imageFileName))
					{
						tileSet[i].image = tileImages[j];
						break;
					}
				}
			}
		}
	}
}

class TileImage
{
	public String imageFileName;
	public BufferedImage image;
	
	public TileImage(String imageFileName)
	{
		this.imageFileName = imageFileName;
		loadBufferedImage();
	}
	
	public TileImage(File file)
	{
		this.imageFileName = file.getName();
		loadBufferedImage();
	}
	
	public void loadBufferedImage()
	{
		try{
			image = ImageIO.read(new File(Program.resourceDir + "tiles/" + imageFileName));
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