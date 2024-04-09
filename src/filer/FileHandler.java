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
	
	private static boolean chooseFile(){
		file = null;
		JFileChooser jf = new JFileChooser();
		int i = jf.showOpenDialog(null);

		if(i == JFileChooser.APPROVE_OPTION) file = jf.getSelectedFile();
		else return false;

		if (file == null) return false;
		String fileName = file.getName();
		if (!fileName.endsWith(".txt")) {
			JOptionPane.showMessageDialog(null,"Fel filtyp");
			return false;
		}
		return true;
	}
	
	public static LinkedList<String> readStudents()
	{
		if(!chooseFile()) return null;

		LinkedList<String> list = new LinkedList<>();
		
		try 
		{
			FileReader fi = new FileReader(file);
			BufferedReader bi = new BufferedReader(fi);
			
			String next = bi.readLine();
			
			while(next != null) {
				String trimmed = next.trim();
				if(!trimmed.isEmpty())
					list.add(trimmed);
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
