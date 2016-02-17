import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.image.*;
import javax.imageio.*;
//import java.awt.image.BufferedImage;
import java.io.*;

import java.util.*;



public class ToolToolbar extends JToolBar implements ActionListener
{
	public ToolToolbar(int orientation)
	{
		super("Tiles", orientation);
		
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
		addButton("Select", "The select tool", "select");
		addButton("Pencil", "The tile pencil tool", "pencil");
		addButton("Fill", "The fill tool", "fill");
		addButton("Rect", "The rect tool", "rect");
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String action = e.getActionCommand();
		switch(action)
		{
			case "select":{
				Editor.instance.setActiveTool(Editor.instance.dummyTool);
				break;
			}
			case "pencil":{
				Editor.instance.setActiveTool(Editor.instance.pencilTool);
				break;
			}
			case "fill":{
				Editor.instance.setActiveTool(Editor.instance.fillTool);
				break;
			}
			case "rect":{
				Editor.instance.setActiveTool(Editor.instance.rectTool);
				break;
			}
			default:{
				break;
			}
		}
	}
}