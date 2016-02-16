import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.image.*;
import javax.imageio.*;
//import java.awt.image.BufferedImage;
import java.io.*;

import java.util.*;



public class TileToolbar extends JToolBar implements ActionListener
{
	public TileToolbar(int orientation)
	{
		super("Tiles", orientation);
		
		Tile[] tileSet = Editor.getInstance().getTileSet();
		
		for(int i = 0; i < tileSet.length; i++)
		{
			addButton(tileSet[i].name, "A tile", "selectTile" + tileSet[i].name);
		}
		
	}
	
	private JButton addButton(String displayText, String tooltip, String actionCommand)
	{
		JButton button = new JButton();
		button.setActionCommand(actionCommand);
		button.setText(displayText);
		button.setToolTipText(tooltip);
		button.addActionListener(this);
		add(button);
		return button;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("test"))
		{
			System.out.println("BUTTON WAS PRESSED!");
		}
	}
}