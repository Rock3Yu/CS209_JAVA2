package sustech.cs209.javafxdemo1;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SpaceInvaderGame extends Application {

    private final Pane root = new Pane();
    private final Sprite player = new Sprite(300, 750, 40, 40, "player", Color.BLUE);

    private Parent createContent() {
        root.setPrefSize(600, 800);

        root.getChildren().add(player);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();

        createEnemies();

        return root;
    }

    private void createEnemies() {
        for (int i = 0; i < 5; i++) {
            Sprite s = new Sprite(90 + i * 100, 150, 30, 30, "enemy", Color.RED);

            root.getChildren().add(s);
        }
    }

    private List<Sprite> getSprites() {
        return root.getChildren().stream().map(n -> (Sprite) n).collect(Collectors.toList());
    }

    private void update() {
        getSprites().forEach(s -> {
            switch (s.type) {
                case "enemy_bullet" -> {
                    // enemy's bullet moves down
                    s.moveDown();
                    // enemy's bullet hits the player
                    if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
                        player.dead = true; // player is dead
                        s.dead = true; // bullet is dead
                    }
                }
                case "player_bullet" -> {
                    // note: player's bullet should move up
                    s.moveUp();
                    // note: should also check whether the bullet hits each enemy
                    for (Sprite ss : getSprites()) {
                        if (ss.type.equals("enemy")
                                && s.getBoundsInParent().intersects(ss.getBoundsInParent())) {
                            ss.dead = true;
                            s.dead = true;
                            break;
                        }
                    }
                }
                case "enemy" -> {
                    // note: enemies should shoot with random intervals
                    Random random = new Random();
                    if (random.nextDouble() < 0.002) {
                        shoot(s);
                    }
                }
            }
        });

        // remove dead sprites from the screen
        root.getChildren().removeIf(n -> {
            Sprite s = (Sprite) n;
            return s.dead;
        });

    }

    private void shoot(Sprite who) {
        if (who.dead) {
            return;
        }
        // a rectangle with width 5, which looks like a bullet
        Sprite s = new Sprite((int) who.getTranslateX() + 20, (int) who.getTranslateY(), 5, 20,
                who.type + "_bullet", Color.BLACK);

        root.getChildren().add(s);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT -> player.moveLeft();
                case RIGHT -> player.moveRight();
                case UP -> player.moveUp();
                case DOWN -> player.moveDown();
                case SPACE -> shoot(player);
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    private static class Sprite extends Rectangle {

        boolean dead = false;
        final String type;

        Sprite(int x, int y, int w, int h, String type, Color color) {
            super(w, h, color);

            this.type = type;
            setTranslateX(x);
            setTranslateY(y);
        }

        void moveLeft() {
            setTranslateX(getTranslateX() - 5);
        }

        void moveRight() {
            setTranslateX(getTranslateX() + 5);
        }

        void moveUp() {
            setTranslateY(getTranslateY() - 5);
        }

        void moveDown() {
            setTranslateY(getTranslateY() + 5);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}