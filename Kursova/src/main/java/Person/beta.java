package Person;

import com.example.kursova.Fraction;
import com.example.kursova.Main;
import javafx.scene.image.Image;

import java.util.Objects;

public class beta extends alef {
    public static Image betaImage = new Image(Objects.requireNonNull(Main.class.getResource("beta.png")).toString(), 100, 100, false, false);

    public beta(String name, double health, double damage, int x, int y) {
        super(name, health, damage, x, y);
        this.imageView.setImage(betaImage);

    }

    public beta() {
        super();
        this.imageView.setImage(betaImage);
    }

    public beta(alef obj) {
        super(obj.getName(), obj.getHealth(), obj.getDamage(), obj.getX(), obj.getY());
        this.imageView.setImage(betaImage);
        obj.getFraction().addObj(this);
    }

    @Override
    public void setFraction(Fraction fraction) {
        super.setFraction(fraction);
    }
}
