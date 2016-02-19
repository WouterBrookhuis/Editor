import java.awt.image.*;		//BufferedImage
import java.awt.*;				//Point

public class Entity
{
	private String name;				//The name of this entity
	private String imageFileName;		//Image file name e.g. trashcan.png
	private BufferedImage image;		//Actual image data
	private Point imagePixelOffset;		//The offset of the image in pixels
}

class EntityWorld
{
	private Entity entity;				//The entity we want placed here
	private float x;					//The x value in tile coordinates
	private float y;					//The y value in tile coordinates
}