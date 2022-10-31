package filer;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileHandler 
{
	private static File file;
	
	public FileHandler()
	{
		//chooseFile();
	}
	
	private static void chooseFile(){
		JFileChooser jf = new JFileChooser();
		int i = jf.showOpenDialog(null);
		if(i == JFileChooser.APPROVE_OPTION)
			file = jf.getSelectedFile();
		else {
			System.out.println("Du klickade fel");
			System.exit(0);
		}
	}
	
	public static LinkedList<String> readStudents()
	{
		chooseFile();
		LinkedList<String> list = new LinkedList<>();
		
		try 
		{
			FileReader fi = new FileReader(file);
			BufferedReader bi = new BufferedReader(fi);
			
			String next = bi.readLine();
			
			while(next != null)
			{
				if(next.length() > 0)
					list.add(next);
				next = bi.readLine();
			}
			bi.close();
			fi.close();
			
		} 
		catch(FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Hittade inte filen");
			return null;
		}
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Nåt gick fel");
			return null;
		}
		
		return list;
	}
}
