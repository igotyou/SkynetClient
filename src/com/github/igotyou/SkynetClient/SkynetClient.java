package com.github.igotyou.SkynetClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class SkynetClient 
{
	private static Scanner input = new Scanner(System.in);
	
	private static String getInput(String message)
	{
		System.out.println(message);
		return input.nextLine();
	}
	
    public static void main(String[] args) throws Exception 
    {
    	boolean showSeconds = false;
    	boolean save = false;
    	File file = null;
    	String outputString = "";
		String playerName = getInput("Enter player name:");
		String secondsInput = getInput("include seconds?(yes/no)");
		secondsInput.toLowerCase();
		if(secondsInput.contains("yes"))
		{
			showSeconds = true;
		}
		else
		{
			showSeconds = false;
		}
		String fileQuestion = getInput("save to file?(yes/no)");
		if(fileQuestion.contains("yes"))
		{
			save = true;
			String fileName = getInput("enter file name");
			file = new File(fileName + ".txt");
		}
		else
		{
			save = false;
		}
        URL skynetUrl = new URL("http://skynet.nickg.org/events?player=" + playerName);
        URLConnection skynetConnection = skynetUrl.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(skynetConnection.getInputStream()));
        String inputLine = in.readLine();
        in.close();
        if (inputLine.length() == 2 || inputLine == null)
        {
        	System.out.println("No data about " + playerName + " on skynet!");
        }
        else
        {
            inputLine = inputLine.replace("[", "");
            inputLine = inputLine.replaceAll("]", "");
            inputLine = inputLine.replace(", " , "");
            inputLine = inputLine.replace("{", "");
            inputLine = inputLine.replace("\"", "");
            String[] elements = inputLine.split("}");
            boolean lastEntryOnline = false;
            for (int i = 0; i < elements.length; i++)
            {
                String[] subElements = elements[i].split(": ");
                String date = subElements[2].split("T")[0];
                String time;
                date = date.replace("-", "/");
                if (showSeconds == true)
                {
                	time = subElements[2].substring(11, 19);
                }
                else
                {
                	time = subElements[2].substring(11, 16);
                }

                if (lastEntryOnline == true)
                {
                	outputString = outputString + " - " + time + System.getProperty("line.separator");
                }
                else
                {
                	outputString = outputString + date + " : " + time;
                }
                lastEntryOnline = Boolean.parseBoolean(subElements[4]);
            }
            System.out.println(outputString);
            if (file != null && save == true)
            {
            	System.out.println("Saving to: " + file.getAbsolutePath());
            	FileOutputStream fileOutputStream = new FileOutputStream(file);
            	BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            	bufferedWriter.write(outputString);
            	bufferedWriter.flush();
            	fileOutputStream.close();
            }
        }
    }

}
