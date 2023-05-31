package Person;

import com.example.kursova.Main;
import javafx.scene.image.Image;

public class gimel extends beta {
    public static Image gimelImage = new Image(Main.class.getResource("gimel.png").toString(), 100, 100, false, false);

    public gimel(String name, double health, double damage, int x, int y) {
        super(name, health, damage, x, y);
        this.imageView.setImage(gimelImage);
    }

    public gimel() {
        super();
    }

}
