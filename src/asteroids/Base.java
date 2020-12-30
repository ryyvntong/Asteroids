package asteroids;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import asteroids.Ship;


public class Base extends Application {

    public static int WIDTH=600;
    public static int HEIGHT=400;

    @Override
    public void start(Stage stage) throws Exception {
        Text text = new Text(10,20, "Points: 0");
        Pane pane = new Pane();
        pane.getChildren().add(text);
        pane.setPrefSize(WIDTH,HEIGHT);
        AtomicInteger points =new AtomicInteger();
        Ship ship = new Ship(WIDTH/2,HEIGHT/2);
        List<Asteroid> asteroids= new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();
        for(int i=0;i<5;i++){
            Random rnd = new Random();
            Asteroid asteroid=new Asteroid(rnd.nextInt(WIDTH/3),rnd.nextInt(HEIGHT/3));
            asteroids.add(asteroid);
        }
        //set initial position of the ship
        pane.getChildren().add(ship.getCharacter());
        asteroids.forEach(asteroid -> {pane.getChildren().add(asteroid.getCharacter());});



        System.out.println(ship.getCharacter());
        Scene scene = new Scene(pane);
        //turning functionality/movements
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();


        scene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
        }
        );


        new AnimationTimer(){
            @Override
            public void handle(long now) {
                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                } else if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }

                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }

                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 10) {
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);
                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));
                    pane.getChildren().add(projectile.getCharacter());

                }

                if(Math.random() < 0.005) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if(!asteroid.collide(ship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }

                ship.move();
                asteroids.forEach(asteroid -> {
                    asteroid.move();
                });

                projectiles.forEach(projectile -> {
                    projectile.move();
                });

                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if (projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            projectile.setCollided(true);
                            asteroid.setAlive(false);
                        }
                    });
                    if(projectile.isCollided()){
                        points.addAndGet(1000);
                        text.setText("Points "+points);
                    }
                });


                projectiles.stream().filter(projectile -> !projectile.isAlive()).forEach(
                        projectile -> pane.getChildren().remove(projectile.getCharacter()
                        )
                );
                projectiles.removeAll(projectiles.stream().filter(projectile -> !projectile.isAlive()).collect(Collectors.toList()));
                asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .forEach(asteroid -> pane.getChildren().remove(asteroid.getCharacter()));
                asteroids.removeAll(asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .collect(Collectors.toList()));



                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        stop();
                    }
                });
            }

        }.start();


        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
