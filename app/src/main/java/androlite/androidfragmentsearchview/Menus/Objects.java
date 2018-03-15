package androlite.androidfragmentsearchview.Menus;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Musabir on 6/16/2017.
 */

public class Objects {
    public String Name;
    public Bitmap Image;
    public String PID;
    public String City;
    public String tollFree;
    public ArrayList<String> players=new ArrayList<String>();
    public ArrayList<String> cities=new ArrayList<String>();
    public ArrayList<String> TollFree=new ArrayList<String>();

    public Objects(String Name,Bitmap Image,String PID,String City,String tollFree)
    {
        this.Image = Image;
        this.Name=Name;
        this.PID = PID;
        this.City =City;
        this.tollFree=tollFree;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return Name;
    }
}

