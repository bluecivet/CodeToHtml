public class Main
{
    /*
        this is a block common just for test
     */
    public static void main(String[] args)
    {

        String input = "C:\\Users\\Arvin\\Documents\\java\\project\\codeToHtml\\src\\Main.java";
        String output = "C:\\Users\\Arvin\\Desktop\\main.txt";

        CodeToHtml coverter = new CodeToHtml(input, output);
       // coverter.showCode();
        coverter.convert();
        coverter.close();
    }
}
