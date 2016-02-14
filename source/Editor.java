import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Editor
{
	private static JFrame frame;
	
	public static void main(String[] args)
	{
		frame = new JFrame("Editor");
		Container contentPane = frame.getContentPane();
		
		Viewport viewport = new Viewport();
		viewport.addMouseListener(viewport);
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
}

/*
	Wut
*/
class Viewport extends JPanel implements MouseListener
{
	private int x;
	private int y;
	
	public Viewport()
	{
		super();
		x = 10;
		y = 10;
	}
	
	public void paintComponent(Graphics g)
	{
		//super.paintComponent(g);
		/*Graphics2D graphics = (Graphics2D)g.create();
		Insets insets = getInsets();
		graphics.translate(insets.left, insets.top);*/
		g.drawString("This is my panel, bitch!", 0, 0);
		//graphics.dispose();
	}
	/*
		Our event listeners
		TODO: Move this to another class
	*/
	public void mousePressed(MouseEvent e)
	{
		System.out.println("Mouse Pressed Event");
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			System.out.println("Button was BUTTON1");
		}
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
		x = MouseInfo.getPointerInfo().getLocation().x;
		y = MouseInfo.getPointerInfo().getLocation().y;
		repaint();
		revalidate();
	}
}