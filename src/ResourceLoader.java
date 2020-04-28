import java.net.URI;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class ResourceLoader
{
    private static ResourceLoader rl = new ResourceLoader();

    public static Image loadImage(String imageName)
    {
        URI s;
        String ss = "";
        try
        {
            s =  rl.getClass().getResource(imageName).toURI();
            ss = s.toString();
        }
        catch(Exception e) { System.out.println("image load error e="+e); }
        return new Image(ss);
    }

    public static AudioClip loadAudioClip( String clipName )
    {
        URI uri;
        String uriString = "";
        try
        {
            uri = rl.getClass().getResource(clipName).toURI();
            uriString = uri.toString();
        }
        catch(Exception e2) { System.out.println("clip load error e2="+e2); }
        return new AudioClip(uriString);
    }
}
