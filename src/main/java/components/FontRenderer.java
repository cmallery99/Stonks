package main.java.components;

public class FontRenderer extends Component {

    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found sprite main.java.renderer");
        }
    }

    @Override
    public void update(double dt) {

    }
}
