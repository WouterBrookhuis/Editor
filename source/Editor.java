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
	private static JFrame frame;
	private static ArrayList<TileImage> tileset;
	private static Level loadedLevel;
	
	public static void main(String[] args)
	{
		frame = new JFrame("Editor");
		Container contentPane = frame.getContentPane();
		
		Viewport viewport = new Viewport();
		viewport.addMouseListener(viewport);
		viewport.addMouseMotionListener(viewport);
		contentPane.add(viewport);
		
		//We better do this!
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create menubar
		JMenuBar menubar = new JMenuBar();
		frame.setJMenuBar(menubar);
		//Create file menu
		JMenu fileMenu = new JMenu("File");
		menubar.add(fileMenu);
		JMenuItem openItem = new JMenuItem("Open");
		fileMenu.add(openItem);
		JMenuItem saveItem = new JMenuItem("Save");
		fileMenu.add(saveItem);
		JMenuItem closeItem = new JMenuItem("Close");
		fileMenu.add(closeItem);
		JMenuItem quitItem = new JMenuItem("Quit");
		fileMenu.add(quitItem);
		//Create help menu
		JMenu helpMenu = new JMenu("Help");
		menubar.add(helpMenu);
		JMenuItem aboutItem = new JMenuItem("About");
		helpMenu.add(aboutItem);
		//Pack it (set size automatically)
		frame.pack();
		//Speaks for itself
		frame.setVisible(true);
	}
	
	public void loadLevel(String level)
	{
		
	}
}

class TileImage
{
	public String imageFileName;
	public BufferedImage image;
	
	public TileImage(String imageFileName)
	{
		//Try to load the image
		try{
			this.imageFileName = imageFileName;
			image = ImageIO.read(new File("../resources/" + imageFileName));
		}catch(IOException e){
			//Like I give a fuck
			System.out.println("Well, fuck");
		}
	}
}

class Tile
{
	private String name;
	private boolean walkable;
	private TileImage image;
	
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
	private String name;
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

/*
	Wut
*/
class Viewport extends JPanel implements MouseListener, MouseMotionListener
{
	private int offsetX;
	private int offsetY;
	private Point dragStartPosition = new Point();
	private BufferedImage image;
	private Level level;
	
	public Viewport()
	{
		super();
		offsetX = 10;
		offsetY = 10;
		try{
			image = ImageIO.read(new File("../resources/grass.png"));
		}catch(IOException e){
			//Like I give a fuck
		}
		
		level = new Level("Test Level", 10, 10);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D)g.create();
		Insets insets = getInsets();
		//graphics.translate(insets.left, insets.top);
		//graphics.drawString("This is my panel, bitch!", x, y);
		graphics.drawString("This is my panel, bitch!", 10, 20);
		for(int y = 0; y < level.tileCountY; y++)
		{
			for(int x = 0; x < level.tileCountY; x++)
			{
				graphics.drawImage(level.tiles[x + y * level.tileCountX].getBufferedImage(),
				x * 32 + offsetX, y * 32 + offsetY,
				32, 32, null);
			}
		}
		graphics.dispose();
	}
	/*
		Our event listeners
		TODO: Move this to another class
	*/
	public void mouseMoved(MouseEvent e)
	{
		//DO shit
	}
	
	public void mouseDragged(MouseEvent e)
	{
		System.out.println("Button " + e.getButton() + " was dragged!");
		Point mouseDelta = new Point(e.getX() - dragStartPosition.x, e.getY() - dragStartPosition.y);
		offsetX += mouseDelta.x;
		offsetY += mouseDelta.y;
		repaint();
		//revalidate();
		dragStartPosition = e.getPoint();
	}
	
	public void mousePressed(MouseEvent e)
	{
		System.out.println("Mouse Pressed Event");
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			System.out.println("Button was BUTTON1");
		}
		dragStartPosition = e.getPoint();
	}
	
	public void mouseReleased(MouseEvent e)
	{
		System.out.println("Mouse Released Event");
	}
	
	public void mouseEntered(MouseEvent e)
	{
		System.out.println("Mouse Entered Event");
	}
	
	public void mouseExited(MouseEvent e)
	{
		System.out.println("Mouse Exited Event");
	}
	
	public void mouseClicked(MouseEvent e)
	{
		System.out.println("Mouse Clicked Event");
	}
}