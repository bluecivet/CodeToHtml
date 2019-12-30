import java.io.*;
import java.util.ArrayList;

class CodeToHtml
{
	private InputStream input;
	private OutputStream output;
	private BufferedReader reader;
	private BufferedWriter writer;
	private CommonChecker commonChecker;

	private String result;
	private static String OL_OPEN = "<ol class = \"sampleCodeBody\"> \r\n";
	private static String OL_CLOSE = "</ol>";
	private static String LI_OPEN = "<li class = \"codeLine\">";
	private static String LI_CLOSE = "</li>";
	private static String SPAN_OPEN = "<span style = \"color: #";
	private static String SPAN_CLOSE = "</span>";
	private static String COLOR = "ffc300\">";
	private static String SPACE = "&nbsp";
	private static String COMMON_OPEN = "<span class = \"common\">";
	private static String COMMON_CLOSE = "</span>";


	private static String[] keyWord =
            {
                  // java keyword
                 "abstract", "assert","boolean","break","byte","case","catch","char","class","const","continue",
                 "default","do","double","else","enum","extends","final","finally","float","for","goto",
                 "if","implements","import","instanceof","int","interface","long","main","native","new","null",
                 "package","private","protected","public","return","short","static","strictfp","super","switch", "synchronized",
                 "this","throw","throws","transient","try","void","volatile","while",

                  // c++
                    "alignas","const_cast","extern","noexcept","static_assert", "union",
                    "alignof","constexpr","false","nullptr","static_cast","unsigned",
                    "operator","struct","using","auto","decltype","virtual","bool","friend","template",
                    "delete","register","thread_local","twchar_t","inline","reinterpret_cast",
                    "dynamic_cast","true","mutable","signed","typedef","explicit","namespace","sizeof","typeid",
                    "export","typename", "nullptr", "NULL"

            };

	private class CommonChecker
	{
		boolean checkCommon;
	}


	//----------------------------------------------------------



	private void setup(String inputPath, String outputPath)
	{
		try
		{
			input = new FileInputStream(inputPath);
			output = new FileOutputStream(outputPath);
			reader = new BufferedReader(new InputStreamReader(input));
			writer = new BufferedWriter(new OutputStreamWriter(output));
			result = "";
			commonChecker = new CommonChecker();
			commonChecker.checkCommon = false;
		}
		catch(IOException e)
		{
			System.out.println("setup error");
			e.printStackTrace();
		}
	}


	//------------------------------------------------------------

	

	public CodeToHtml(String inputPath, String outputPath)
	{
		setup(inputPath, outputPath);
	}


	//------------------------------------------------------------

	public String read()
	{
		String text = "";
		try
		{
			String read = "";
			while(true)
			{
				read = reader.readLine();
				if(read == null)
				    break;
				text += read + "\r\n";
			}
		}
		catch(IOException e)
		{
			System.out.println("show code error");
		}

		return text;
	}

	//------------------------------------------------------------


	/*
		the method read code from the target file then output to std output
	 */
	public void showCode()
	{
		String text = read();
		System.out.println(text);
	}


	//------------------------------------------------------------

	/*
		the function output result on screen
	 */
	public void write()
	{
		try
		{
			writer.write(result);
			writer.flush();
		}
		catch(IOException e)
		{
			System.out.println("write error");
		}

	}

	//------------------------------------------------------------

    public void close()
    {
        try
        {
            input.close();
            output.close();
            reader.close();
            writer.close();
        }
        catch(IOException e)
        {
            System.out.println("close error");
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------




	public void convert()
	{
	    result = OL_OPEN;

		String text = read();
		String[] lines = text.split("\r\n");
		
		for(int i = 0; i < lines.length; i++)
		{
			// deal with block common
//			if(lines[i].indexOf("/*") != -1)
//			{
//				Integer index = i;
//				result += convertBlockCommon(lines, lines[i], index);
//				i = index;
//			}

			result += convertLine(lines[i]);
		}

		result += OL_CLOSE;
		write();
	}



	//---------------------------------------------------------------------------------

	/*
		the function covert block common into html code
	 */

//	String convertBlockCommon(String[] lines, String line, Integer index)
//	{
//		int commonIndex = line.indexOf("/*");
//		String beforeCommon = line.substring(0, commonIndex);
//		String common = COMMON_OPEN + findBlockCommon(lines, index);
//
//		String text = beforeCommon + common + ;//beforeCommon + common + " " + LI_CLOSE + "\r\n";
//		return text;
//	}



	//-------------------------------------------------------------------------------

	/*
		the method find the end of block common
	 */
//
//	String findBlockCommon(String[] lines, Integer index)
//	{
//		String text;
//		int commonIndex = lines[index].indexOf("/*");
//		int endCommonIndex = lines[index].indexOf("*/");
//		if(endCommonIndex != -1)
//		{
//
//		}
//		for(int i = index; i < lines.length; i++)
//		{
//
//			if(endCommonIndex != -1)
//			{
//				if(i == index)
//				{
//					text = lines[i].substring(commonIndex, endCommonIndex + 1);
//					break;
//				}
//				else
//				{
//
//				}
//			}
//		}
//	}


    //----------------------------------------------------------------------------------
	
	/*
		the function convert each line to html code
	 */
	
	private String convertLine(String line)
	{
		String text;
		text = "\t" + LI_OPEN /*+ indentOfLine(line)*/;


		text += checkEachWord(line);	// find keywords

		// deal wiht block common

		int blockIndex = text.indexOf("/*");
		if(blockIndex != -1)
		{
			System.out.println("inserting /* at " + blockIndex);
			StringBuffer theLine = new StringBuffer(text);
			theLine.insert(blockIndex, COMMON_OPEN);
			commonChecker.checkCommon = true;
			System.out.println(theLine);
			text = theLine.toString();
		}

		blockIndex = text.indexOf("*/");
		if(blockIndex != -1)
		{
			System.out.println("inserting /* at " + blockIndex);
			StringBuffer theLine = new StringBuffer(text);
			theLine.insert(blockIndex, COMMON_CLOSE);
			commonChecker.checkCommon = false;
			System.out.println(theLine);
			text = theLine.toString();
		}

		// deal with line common
		int commonIndex = line.indexOf("//");
		if(commonIndex != -1)
		{
			String beforeCommon = line.substring(0, commonIndex);
			String common = line.substring(commonIndex, line.length());
			common = COMMON_OPEN + common + COMMON_CLOSE;
			text += beforeCommon + common + " " + LI_CLOSE + "\r\n";
			return text;
		}


		text += " " + LI_CLOSE + "\r\n";
		return text;
		
	}
	
	
	//----------------------------------------------------------------------------------
	
	/*
		the function add html code to some keyWord
	 */
	
	private String checkEachWord(String line)
	{
		String text = "";
		String temp;  // for holding words
		char c;
		char d;

		for(int i = 0; i < line.length(); i++)
		{
			c = line.charAt(i);
			
			// if the character not a character 
			if((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && (c < '1' || c > '9') && c != '_')
			{
				text += findHtmlCharacter(c);
			}
			else	// if it find a word;
			{
				int end = i;
				int j;
				for(j = i; j < line.length(); j++)
				{
					d = line.charAt(j);
					if((d < 'a' || d > 'z') && (d < 'A' || d > 'Z') && (d < '0' || d > '9') && d != '_')
					{
						end = j;
						break;
					}

				}

				if(j >= line.length())
					end = line.length();
				temp = line.substring(i, end);
				i = end - 1; 	// because the for loop will i++ and want i to become end
				text += buildWord(temp);
			}// end else
		}// end for

		return text;
	}
	
	//-----------------------------------------------------------------------------------

    /*
        the method check is a word is a keyword or not
     */
    public boolean isKeyword(String word)
    {
    	//System.out.println(word);
        for(int i = 0; i < keyWord.length; i++)
        {
            if(word.equals(keyWord[i]))
                return true;
        }

        return false;
    }


    //---------------------------------------------------------------------------------

    /*
        the method add color to word if it is keyWord
     */

    private String buildWord(String word)
	{
		String text;
		if(commonChecker.checkCommon == false && isKeyword(word))
		{
			text = SPAN_OPEN + COLOR + word +SPAN_CLOSE;
		}
		else
		{
			text = word;
		}
		return text;
	}


    //-------------------------------------------------------------------------------------

	/*
		some character cannot directly convert to html code like < or >
		they need to change some with some code
		this is what this function do
	 */
	private String findHtmlCharacter(char c)
	{
		String text = new String("" + c);
		switch(c)
		{
			case '<': return "&lt";
			case '>': return "&gt";
			case '&': return "&amp";
			case ' ': return SPACE;
			case '\t': return SPACE + " " + SPACE;
		}

		return text;
	}

	//------------------------------------------------------------------------------------

    private String indentOfLine(String line)
    {
        String text = "";
        for(int i = 0; i < line.length(); i++)
        {
            if(line.charAt(i) == ' ')
                text += SPACE;
            else
                break;
        }
        return text;
    }






}


