public class Driver
{
    public static void main(String[] args)
    {
        World world = new World(1920, 1080, 69696);
        Gui gui = new Gui(world);

        gui.showWorldPanel();   
    }
}
