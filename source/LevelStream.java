import java.io.*;
import java.lang.System.*;
import java.util.*;

public class LevelStream
{
	private File targetFile;
	private Level level;
	
	public LevelStream(File file, Level level)
	{
		this.targetFile = file;
		this.level = level;
	}
	
	//Creates an array containing the header bytes for the current level
	private byte[] createHeader()
	{
		/*
			1 byte	 				-	Lenght of the header in bytes (8 bit integer)
			null terminated string	-	Name of level
			4 bytes					-	Tile count x (32 bit integer)
			4 bytes					-	Tile count y (32 bit integer)
		*/
		if(level != null)
		{
			byte[] levelnamebytes = level.name.getBytes();
			byte[] levelname = new byte[levelnamebytes.length + 1];
			System.arraycopy(levelnamebytes, 0, levelname, 0, levelnamebytes.length);
			
			byte[] levelsizex = intToBytes(level.tileCountX);
			byte[] levelsizey = intToBytes(level.tileCountY);
			
			int headerSize = levelname.length + levelsizex.length + levelsizey.length;
			byte[] data = new byte[1 + headerSize];
			int index = 0;
			data[0] = (byte)headerSize;
			index++;
			System.arraycopy(levelname, 0, data, index, levelname.length);
			index += levelname.length;
			System.arraycopy(levelsizex, 0, data, index, levelsizex.length);
			index += levelsizex.length;
			System.arraycopy(levelsizey, 0, data, index, levelsizey.length);
			index += levelsizey.length;
			return data;
		}
		return null;		
	}
	
	//Creates an array containing the tile set bytes for the current level
	private byte[] createTileSetData()
	{
		/*
			2 bytes 	-	Lenght of the tile set data in bytes (16 bit integer)
			2 bytes		-	Amount of tiles we have (16 bit integer)
			? bytes		-	Array of tiles according to the following format
				null terminated string	-	Name of tile
				1 byte					-	Walkable boolean (1 or 0)
				null terminated string	-	Name of tile image file (e.g. grass.png)
		*/
		if(level != null)
		{
			Tile[] tileSet = Editor.instance.tileSet;
			int lastValidIndex = 0;
			for(int i = 0; i < tileSet.length; i++)
			{
				if(tileSet[i] == null){
					lastValidIndex = i - 1;
					break;
				}
			}
			Tile[] tileSetActual = new Tile[lastValidIndex + 1];
			System.arraycopy(tileSet, 0, tileSetActual, 0, tileSetActual.length);
			
			byte[][] tileBytes = new byte[tileSetActual.length][];
			int tileBytesSize = 0;
			
			for(int i = 0; i < tileSetActual.length; i++)
			{
				if(tileSetActual[i] == null)
					break;
				
				byte[] tilename;
				byte walkable;
				byte[] imagename;
				
				byte[] tilenamebytes = tileSetActual[i].name.getBytes();
				tilename = new byte[tilenamebytes.length + 1];
				System.arraycopy(tilenamebytes, 0, tilename, 0, tilenamebytes.length);
				
				walkable = (byte)(tileSetActual[i].walkable ? 1 : 0 );
				
				byte[] imagenamebytes = tileSetActual[i].image.imageFileName.getBytes();
				imagename = new byte[imagenamebytes.length + 1];
				System.arraycopy(imagenamebytes, 0, imagename, 0, imagenamebytes.length);
				
				byte[] tileData = new byte[tilename.length + 1 + imagename.length];
				int index = 0;
				System.arraycopy(tilename, 0, tileData, index, tilename.length);
				index += tilename.length;
				tileData[index] = walkable;
				index++;
				System.arraycopy(imagename, 0, tileData, index, imagename.length);
				index += imagename.length;
				
				tileBytes[i] = tileData;
				tileBytesSize += tileData.length;
			}
			
			
			byte[] data = new byte[tileBytesSize + 4];
			int index = 0;
			//Add the total size as a 16 bit integer
			byte[] size = shortToBytes((short)tileBytesSize);
			System.arraycopy(size, 0, data, index, size.length);
			index += size.length;
			//Add our tile count as a 16 bit integer
			byte[] count = shortToBytes((short)tileBytes.length);
			System.arraycopy(count, 0, data, index, count.length);
			index += count.length;
			
			for(int i = 0; i < tileBytes.length; i++)
			{
				System.arraycopy(tileBytes[i], 0, data, index, tileBytes[i].length);
				index += tileBytes[i].length;
			}
			
			return data;
		}
		return null;		
	}
	
	//Creates an array containing the tile map bytes for the current level
	private byte[] createTileMapData()
	{
		/*
			4 bytes					-	Size of tile map data in bytes (32 bit integer)
			? bytes					-	Array of tiles according to the following format
				null terminated string	-	Name of tile
		*/
		if(level != null)
		{
			//Create multidimensional array to house all tiles in the level
			byte[][] tileBytes = new byte[level.tileCountX * level.tileCountY][];
			//The total size of tileBytes (including the nested arrays)
			int tileBytesSize = 0;
			
			//Loop trough all tiles
			for(int i = 0; i < tileBytes.length; i++)
			{				
				byte[] tilename;
				
				//Get the tile's tile name
				byte[] tilenamebytes = level.tiles[i].name.getBytes();
				tilename = new byte[tilenamebytes.length + 1];
				System.arraycopy(tilenamebytes, 0, tilename, 0, tilenamebytes.length);
				
				//Add it to tileBytes
				tileBytes[i] = tilename;
				//Increase the total size counter accordingly
				tileBytesSize += tilename.length;
			}
			
			//Our result
			byte[] data = new byte[tileBytesSize + 4];
			
			int index = 0;
			//Add the total size as a 32 bit integer
			byte[] size = intToBytes(tileBytesSize);
			System.arraycopy(size, 0, data, index, size.length);
			index += size.length;
			
			//Add all tiles
			for(int i = 0; i < tileBytes.length; i++)
			{
				System.arraycopy(tileBytes[i], 0, data, index, tileBytes[i].length);
				index += tileBytes[i].length;
			}
			
			return data;
		}
		return null;
	}
	
	//Converts an int to an array of bytes
	private byte[] intToBytes(int integer)
	{
		return new byte[]{
			(byte)(integer >> 24),
			(byte)(integer >> 16),
			(byte)(integer >> 8),
			(byte)(integer)
		};
	}
	
	//Converts a short to an array of bytes
	private byte[] shortToBytes(short integer)
	{
		return new byte[]{
			(byte)(integer >> 8),
			(byte)(integer)
		};
	}
	
	//Saves the current level to targetFile
	public void save()
	{
		try
		{
			byte[] headerData = createHeader();
			byte[] tileSetData = createTileSetData();
			byte[] tileMapData = createTileMapData();
			
			byte[] data = new byte[headerData.length + tileSetData.length + tileMapData.length];
			System.arraycopy(headerData, 0, data, 0, headerData.length);
			System.arraycopy(tileSetData, 0, data, headerData.length, tileSetData.length);
			System.arraycopy(tileMapData, 0, data, headerData.length + tileSetData.length, tileMapData.length);
			
			FileOutputStream fos = new FileOutputStream(targetFile);
			fos.write(data);
			fos.close();
		}
		catch(IOException i)
		{
			
		}
	}
	
	public int getShort(byte[] bytes, int[] currentIndex)
	{
		int value = bytes[currentIndex[0]] << 8 | (bytes[currentIndex[0] + 1] & 0xFF);
		currentIndex[0] += 2;
		return value;
	}
	
	public int getInteger(byte[] bytes, int[] currentIndex)
	{
		int value = bytes[currentIndex[0]] << 24 | (bytes[currentIndex[0] + 1] & 0xFF) << 16 | (bytes[currentIndex[0] + 2] & 0xFF) << 8 | (bytes[currentIndex[0] + 3] & 0xFF);
		currentIndex[0] += 4;
		return value;
	}
	
	public boolean getBoolean(byte[] bytes, int[] currentIndex)
	{
		boolean value = (bytes[currentIndex[0]] > 0) ? true : false;
		currentIndex[0]++;
		return value;
	}
	
	public String getNullTerminatedString(byte[] bytes, int[] currentIndex)
	{
		int startIndex = currentIndex[0];
		while(bytes[currentIndex[0]] != 0)
		{
			currentIndex[0]++;
		}
		String value = new String(bytes, startIndex, currentIndex[0] - startIndex);
		//Skip the null
		currentIndex[0]++;
		return value;
	}
	
	//Loads a level from targetFile and returns the correct level object
	public LoadedLevelData load()
	{
		byte[] data = new byte[(int)targetFile.length()];
		
		FileInputStream fis = null;
		
		try
		{
			fis = new FileInputStream(targetFile);
			int numOfBytes  = fis.read(data);
			fis.close();
		}
		catch(Exception e)
		{
		}
		
		if(true)
		{
			String levelname;
			int levelsizex;
			int levelsizey;
			
			int[] currentIndex = new int[1];
			currentIndex[0] = 0;
			
			int headerSize = (int)data[0];
			currentIndex[0]++;
			
			//Get the header
			levelname = getNullTerminatedString(data, currentIndex);
			levelsizex = getInteger(data, currentIndex);
			levelsizey = getInteger(data, currentIndex);
			
			//Get the tileset
			int tileSetSize = getShort(data, currentIndex);
			int tileSetCount = getShort(data, currentIndex);
			
			String[] tileNames = new String[tileSetCount];
			boolean[] tileWalkables = new boolean[tileSetCount];
			String[] tileImageNames = new String[tileSetCount];
			
			for(int i = 0; i < tileSetCount; i++)
			{
				tileNames[i] = getNullTerminatedString(data, currentIndex);
				tileWalkables[i] = getBoolean(data, currentIndex);
				tileImageNames[i] = getNullTerminatedString(data, currentIndex);
			}
			
			//Create the tile image objects
			//Make sure whe only have one of each
			Set<String> temp = new HashSet<String>(Arrays.asList(tileImageNames));
			String[] uniqueTileImageNames = temp.toArray(new String[temp.size()]);
			
			TileImage[] tileImages = new TileImage[uniqueTileImageNames.length];
			for(int i = 0; i < uniqueTileImageNames.length; i++)
			{
				tileImages[i] = new TileImage(uniqueTileImageNames[i]);
				System.out.println("Loaded tile image " + tileImageNames[i]);
			}
			
			//Create the tile objects
			Tile[] tileSet = new Tile[tileSetCount];
			for(int i = 0; i < tileSetCount; i++)
			{
				//Our image
				TileImage image = null;
				//Find our desired image in the list
				for(int j = 0; j < tileImages.length; j++)
				{
					if(tileImages[j].imageFileName.equals(tileImageNames[i])){
						image = tileImages[j];
					}
				}
				if(image == null)
				{
					System.out.println("COULD NOT FIND TILE IMAGE " + tileImageNames[i]);
				}
				tileSet[i] = new Tile(tileNames[i], tileWalkables[i], image);
				System.out.println("Created tile " + tileNames[i]);
			}
			
			//Get the tile map!
			int tileMapSize = getInteger(data, currentIndex);
			
			Tile[] tileMap = new Tile[levelsizex * levelsizey];
			for(int i = 0; i < tileMap.length; i++)
			{
				//Read the tile name
				String tilename = getNullTerminatedString(data, currentIndex);
				
				Tile tile = null;
				//Try to find the tile
				for(int j = 0; j < tileSet.length; j++)
				{
					if(tileSet[j].name.equals(tilename)){
						tile = tileSet[j];
					}
				}
				if(tile == null)
				{
					System.out.println("COULD NOT FIND TILE " + tilename);
				}
				
				tileMap[i] = tile;
			}
			
			
			Level lvl = new Level(levelname, levelsizex, levelsizey);
			lvl.tiles = tileMap;
			LoadedLevelData result = new LoadedLevelData(lvl, tileSet, tileImages);
			
			return result;
		}
		
		return null;
	}
}

class LoadedLevelData
{
	public Level level;
	public Tile[] tileSet;
	public TileImage[] tileImages;
	
	public LoadedLevelData(Level l, Tile[] ts, TileImage[] ti)
	{
		level = l;
		tileSet = ts;
		tileImages = ti;
	}
}