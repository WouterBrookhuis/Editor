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
	public static TileToolbar instance;
	
	public TileToolbar(int orientation)
	{
		super("Tiles", orientation);
		
		if(instance == null)
			instance = this;
		
		updateButtons();
		
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
	
	public void updateButtons()
	{
		removeAll();
		Tile[] tileSet = Editor.instance.tileSet;
		
		for(int i = 0; i < tileSet.length; i++)
		{
			if(tileSet[i] == null){
				break;
			}
			addButton(tileSet[i].name, "A tile", "selectTile" + i);
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String action = e.getActionCommand();
		if(action.substring(0, 10).equals("selectTile"))
		{
			int tileIndex = Integer.parseInt(action.substring(10));
			Editor.instance.pencilTool.setPaintTileIndex(tileIndex);
			Editor.instance.fillTool.setPaintTileIndex(tileIndex);
			Editor.instance.rectTool.setPaintTileIndex(tileIndex);
		}
	}
}