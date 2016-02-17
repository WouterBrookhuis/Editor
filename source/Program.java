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
	public final static JFileChooser fc = new JFileChooser();
	public static JFrame frame;
	
	public static void main(String[] args)
	{
		Editor editor = new Editor();
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
		JMenuItem newItem = new JMenuItem("New");
		newItem.addActionListener(new NewMenuListener());
		fileMenu.add(newItem);
		JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener(new OpenMenuListener());
		fileMenu.add(openItem);
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener(new SaveMenuListener());
		fileMenu.add(saveItem);
		JMenuItem closeItem = new JMenuItem("Close");
		fileMenu.add(closeItem);
		//Export sub menu
		JMenu exportMenu = new JMenu("Export");
		JMenuItem exportImageItem = new JMenuItem("Image");
		exportImageItem.addActionListener(new ExportImageListener());
		exportMenu.add(exportImageItem);
		fileMenu.add(exportMenu);
		//
		JMenuItem quitItem = new JMenuItem("Quit");
		fileMenu.add(quitItem);
		
		//Create edit menu
		JMenu editMenu = new JMenu("Edit");
		menubar.add(editMenu);
		JMenuItem mapItem = new JMenuItem("Edit level settings");
		editMenu.add(mapItem);
		JMenuItem resizeItem = new JMenuItem("Resize map");
		editMenu.add(resizeItem);
		
		
		//Create help menu
		JMenu helpMenu = new JMenu("Help");
		menubar.add(helpMenu);
		JMenuItem aboutItem = new JMenuItem("About");
		helpMenu.add(aboutItem);
		
		//Create the tool toolbar
		JToolBar toolbar = new ToolToolbar(JToolBar.HORIZONTAL);
		frame.add(toolbar, BorderLayout.NORTH);
		
		//Create the tile toolbar
		toolbar = new TileToolbar(JToolBar.VERTICAL);
		frame.add(toolbar, BorderLayout.EAST);
		
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
		int returnVal = Program.fc.showOpenDialog(Program.frame);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = Program.fc.getSelectedFile();
			Editor.instance.loadLevel(file);
		}
	}
}

class SaveMenuListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		if(Editor.instance.loadedLevel != null)
		{
			int returnVal = Program.fc.showSaveDialog(Program.frame);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = Program.fc.getSelectedFile();
				Editor.instance.saveLevel(file);
			}
		}
	}
}

class NewMenuListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		JTextField widthField = new JTextField();
		JTextField heightField = new JTextField();
		JTextField nameField = new JTextField();
		JComponent[] inputs = new JComponent[] {
			new JLabel("Level name"),
			nameField,
			new JLabel("Map width"),
			widthField,
			new JLabel("Map height"),
			heightField
		};
		boolean canGo = false;
		String name;
		int width;
		int height;
		while(!canGo)
		{
			JOptionPane.showMessageDialog(null, inputs, "New map", JOptionPane.PLAIN_MESSAGE);
			
			if(!nameField.getText().isEmpty() && !widthField.getText().isEmpty() && !heightField.getText().isEmpty()){
				canGo = true;
			}
		}
		name = nameField.getText();
		width = Math.max(1, Integer.parseInt(widthField.getText()));
		height = Math.max(1, Integer.parseInt(heightField.getText()));
		Editor.instance.newLevel(name, width, height);
	}
}

class ExportImageListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		if(Editor.instance.loadedLevel != null)
		{
			int returnVal = Program.fc.showSaveDialog(Program.frame);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = Program.fc.getSelectedFile();
				Editor.instance.loadedLevel.saveTilemapAsImage(file);
			}
		}
	}
}