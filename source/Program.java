import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.image.*;
import javax.imageio.*;
//import java.awt.image.BufferedImage;
import java.io.*;

import java.util.*;

public class Program
{
	private static JFrame frame;
	
	public static void main(String[] args)
	{
		InitFrame();
	}
	
	private static void InitFrame()
	{
		frame = new JFrame("Editor");
		Container contentPane = frame.getContentPane();
		
		Viewport viewport = new Viewport();
		viewport.addMouseListener(viewport);
		viewport.addMouseMotionListener(viewport);
		viewport.addKeyListener(viewport);
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
		openItem.addActionListener(new OpenMenuListener());
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
		frame.setMinimumSize(new Dimension(300, 100));
		frame.pack();
		//Speaks for itself
		frame.setVisible(true);
	}
	
	
}

class OpenMenuListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("An action was performed");
	}
}