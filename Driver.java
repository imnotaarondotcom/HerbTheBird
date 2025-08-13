public class Driver
{
    public static void main(String[] args)
    {
        World world = new World(1920, 1080, 12345);
        Gui gui = new Gui(world);

        gui.showWorldPanel();   
    }
}
