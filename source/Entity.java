import java.awt.image.*;		//BufferedImage / ImageIO
import java.awt.*;				//Point

public class Entity
{
	private String name;				//The name of this entity
	private String imageFileName;		//Image file name e.g. trashcan.png
	private BufferedImage image;		//Actual image data
	private Point imagePixelOffset;		//The offset of the image in pixels
	
	public Entity(String name, String imageFileName, Point imagePixelOffset)
	{
		this.name = name;
		this.imagePixelOffset = imagePixelOffset;
		setImageFileName(imageFileName);
	}
	
	public Entity(String name, String imageFileName, int pixelOffsetX, int pixelOffsetY)
	{
		this.name = name;
		this.imagePixelOffset = new Point(pixelOffsetX, pixelOffsetY);
		setImageFileName(imageFileName);
	}
	
	public String getImageFileName()
	{
		return imageFileName;
	}
	
	public boolean setImageFileName(String newImageFileName)
	{
		imageFileName = newImageFileName;
		try{
			this.image = ImageIO.read(new File(Program.resourceDir + "entities/" + imageFileName));
		}catch(Exception e){
			System.out.println("Something went wrong when trying to load " + imageFileName + " for entity " + name);
			return false;
		}
		return true;
	}
	
	public String getName()
	{
		return name;
	}
	
	public BufferedImage getBufferedImage()
	{
		if(image != null){
			return image;
		}
		return null;
	}
	
	public Point getOffset()
	{
		return imagePixelOffset;
	}
	
	public void setOffset(Point newOffset)
	{
		imagePixelOffset = newOffset;
	}
}

class EntityWorld
{
	private Entity entity;				//The entity we want placed here
	private Point position;				//The position in the world in tile coordinates
	
	public EntityWorld(Entity entity)
	{
		this.entity = entity;
		this.position = new Point(0, 0);
	}
	
	public EntityWorld(Entity entity, Point position)
	{
		this.entity = entity;
		this.position = position;
	}
	
	public EntityWorld(Entity entity, int x, int y)
	{
		this.entity = entity;
		this.position = new Point(x, y);
	}
	
	public void setPosition(Point newPos)
	{
		position = newPos;
	}
	
	public Point getPosition()
	{
		return position;
	}
	
	public Entity getEntity()
	{
		return entity;
	}
}