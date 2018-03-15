package androlite.androidfragmentsearchview.Adapters;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Musabir on 6/13/2017.
 */

public class Sub_Object {

        public String Name;
        public String Image;
        public String PID;
        public ArrayList<String> players=new ArrayList<String>();
        public ArrayList<String> img=new ArrayList<>();
        public Sub_Object(String Name,String Image,String PID)
        {
            this.Image = Image;
            this.Name=Name;
            this.PID = PID;
        }
        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return Name;
        }

}
