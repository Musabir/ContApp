package androlite.androidfragmentsearchview.Adapters;

import java.util.ArrayList;

/**
 * Created by Musabir on 6/16/2017.
 */

public class Objects {
    public String Name;
    public String Image;
    public String PID;
    public String City;
    public String tollFree;
    public String addr;
    public ArrayList<String> players=new ArrayList<String>();
    public ArrayList<String> cities=new ArrayList<String>();
    public ArrayList<String> TollFree=new ArrayList<String>();
    public ArrayList<String> add=new ArrayList<String>();

    public Objects(String Name,String Image,String PID,String City,String tollFree, String addr)
    {
        this.Image = Image;
        this.Name=Name;
        this.PID = PID;
        this.City =City;
        this.tollFree=tollFree;
        this.addr = addr;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return Name;
    }
}

