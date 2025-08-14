public class Driver
{
    public static void main(String[] args)
    {
        World world = new World(123123);
        Gui gui = new Gui(world); 
        gui.showWorldPanel();
    }
}
