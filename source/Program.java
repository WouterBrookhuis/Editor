import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.image.*;
import javax.imageio.*;
import javax.swing.filechooser.*;
//import java.awt.image.BufferedImage;
import java.io.*;

import java.util.*;

public class Program
{
	public final static javax.swing.filechooser.FileFilter fflvl = new FileNameExtensionFilter("lvl file", "dat", "lvl");
	public final static javax.swing.filechooser.FileFilter ffpng = new FileNameExtensionFilter("PNG file", "png");
	public final static JFileChooser fc = new JFileChooser(new File(System.getProperty("user.dir")));
	public static JFrame frame;
	
	public final static String resourceDir = "../resources/";
	public final static String iconDir = "../icons/";
	
	public static void main(String[] args)
	{
		Editor editor = new Editor();
		InitFrame();
	}
	
	private static void InitFrame()
	{
		fc.setFileFilter(fflvl);
		
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
		mapItem.addActionListener(new EditMenuListener());
		editMenu.add(mapItem);
		
		//Create tiles menu
		JMenu tileMenu = new JMenu("Tiles");
		menubar.add(tileMenu);
		JMenuItem addImage = new JMenuItem("Scan for tile images");
		addImage.addActionListener(new ImportImageListener());
		tileMenu.add(addImage);
		JMenuItem addTile = new JMenuItem("New tile");
		addTile.addActionListener(new CreateTileListener());
		tileMenu.add(addTile);
		
		
		
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
	
	public static void showMessage(String message)
	{
		
	}
}

class CreateTileListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		String[] tileImagesLong = new String[Editor.instance.tileImages.length];
		int lastValidIndex = 0;
		for(int i = 0; i < Editor.instance.tileImages.length; i++)
		{
			if(Editor.instance.tileImages[i] != null){
				tileImagesLong[i] = Editor.instance.tileImages[i].imageFileName;
				lastValidIndex = i;
			}else{
				break;
			}
		}
		String[] tileImages = new String[lastValidIndex + 1];
		System.arraycopy(tileImagesLong, 0, tileImages, 0, tileImages.length);
		
		JComboBox tileList = new JComboBox(tileImages);
		JTextField nameField = new JTextField();
		JCheckBox walkableField = new JCheckBox("Is walkable");
		JComponent[] inputs = new JComponent[] {
			new JLabel("Tile image"),
			tileList,
			new JLabel("Tile name"),
			nameField,
			walkableField
		};
		
		boolean canGo = false;
		while(!canGo)
		{
			int value = JOptionPane.showConfirmDialog(null, inputs, "New tile", JOptionPane.OK_CANCEL_OPTION);
			//If the user clicked cancel gtfo
			if(value == JOptionPane.CANCEL_OPTION)
				return;
			
			if(!nameField.getText().isEmpty()){
				canGo = true;
			}
		}
		
		String name = nameField.getText();
		boolean walkable = walkableField.isSelected();
		TileImage tileImage = Editor.instance.findTileImage((String)tileList.getSelectedItem());
		Editor.instance.createNewTile(name, walkable, tileImage);
		TileToolbar.instance.updateButtons();
	}
}

class ImportImageListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		Editor.instance.scanTiles();
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
			TileToolbar.instance.updateButtons();
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
			int value = JOptionPane.showConfirmDialog(null, inputs, "New map", JOptionPane.OK_CANCEL_OPTION);
			//If the user clicked cancel gtfo
			if(value == JOptionPane.CANCEL_OPTION)
				return;
			
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

class EditMenuListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		if(Editor.instance.loadedLevel != null)
		{
			//Our input fields filled with the current values
			JTextField widthField = new JTextField(Editor.instance.loadedLevel.tileCountX + "");
			JTextField heightField = new JTextField(Editor.instance.loadedLevel.tileCountY + "");
			JTextField nameField = new JTextField(Editor.instance.loadedLevel.name);
			JComponent[] inputs = new JComponent[] {
				new JLabel("Level name"),
				nameField,
				new JLabel("Map width"),
				widthField,
				new JLabel("Map height"),
				heightField
			};
			//Some variables
			boolean canGo = false;
			String name;
			int width;
			int height;
			//Be annoying and loop until all fields are filled in
			while(!canGo)
			{
				//Show the dialog
				int value = JOptionPane.showConfirmDialog(null, inputs, "New map", JOptionPane.OK_CANCEL_OPTION);
				//If the user clicked cancel gtfo
				if(value == JOptionPane.CANCEL_OPTION)
					return;
				//Make sure all fields are filled in
				if(!nameField.getText().isEmpty() && !widthField.getText().isEmpty() && !heightField.getText().isEmpty()){
					canGo = true;
				}
			}
			//Get the stuff
			name = nameField.getText();
			//TODO: Fix dit
			width = Math.max(1, Integer.parseInt(widthField.getText()));
			height = Math.max(1, Integer.parseInt(heightField.getText()));
			//Resize the level
			Editor.instance.loadedLevel.resize(width, height);
			Editor.instance.loadedLevel.setName(name);
			Program.frame.setTitle("Editor - " + Editor.instance.loadedLevel.name);
			//Repaint the viewport
			Viewport.getMain().repaint();
		}
	}
}

class ExportImageListener implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		if(Editor.instance.loadedLevel != null)
		{
			Program.fc.setFileFilter(Program.ffpng);
			int returnVal = Program.fc.showSaveDialog(Program.frame);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = Program.fc.getSelectedFile();
				Editor.instance.loadedLevel.saveTilemapAsImage(file);
			}
			Program.fc.setFileFilter(Program.fflvl);
		}
	}
}