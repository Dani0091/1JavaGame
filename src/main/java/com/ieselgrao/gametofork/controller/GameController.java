package com.ieselgrao.gametofork.controller;
import javafx.animation.TranslateTransition;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.util.Duration;
import com.ieselgrao.gametofork.model.GameModel;
import com.ieselgrao.gametofork.MainApplication;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

public class GameController {

    @FXML
    private Pane gamePane;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label livesLabel;

    private GameModel model;
    private AnimationTimer gameLoop;
    private Random random = new Random();

    // Parámetros de los círculos
    private final double MIN_RADIUS = 20;
    private final double MAX_RADIUS = 80;
    private final double FALL_SPEED = 1.5;
    private final double LOST_LINE_Y = 550; // Línea cerca del pie de la ventana (600px)

    @FXML
    public void initialize() {
        model = MainApplication.getGameModel();

        // Bindeo de etiquetas a las propiedades del modelo
        scoreLabel.textProperty().bind(model.scoreProperty().asString("Puntuación: %d"));
        livesLabel.textProperty().bind(model.livesProperty().asString("Vidas: %d"));

        // Dibuja la línea roja de pérdida de vida
        Line lossLine = new Line(0, LOST_LINE_Y, gamePane.getWidth(), LOST_LINE_Y);
        lossLine.setStroke(Color.RED);
        lossLine.setStrokeWidth(2);
        gamePane.getChildren().add(lossLine);

        // Inicia el ciclo del juego
        startGameLoop();
    }

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastSpawnTime = 0;
            private final long SPAWN_INTERVAL_NS = 1_000_000_000L; // Spawn cada 1 segundo

            @Override
            public void handle(long now) {
                // Generar nuevos círculos
                if (now - lastSpawnTime > SPAWN_INTERVAL_NS) {
                    createRandomCircle();
                    lastSpawnTime = now;
                }

                // Actualizar posición y revisar colisiones
                updateCircles();

                // Revisar fin del juego
                if (model.isGameOver()) {
                    stop();
                    try {
                        MainApplication.switchToGameOverView();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        gameLoop.start();
    }

    private void createRandomCircle() {
        double radius = MIN_RADIUS + (MAX_RADIUS - MIN_RADIUS) * random.nextDouble();
        double x = radius + (random.nextDouble() * (gamePane.getWidth() - 2 * radius));

        Circle circle = new Circle(radius, Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        circle.setLayoutX(x);
        circle.setLayoutY(-radius); // Inicia fuera de la parte superior

        // Asigna puntos según el tamaño (círculos más pequeños dan más puntos)
        int points = (int) (MAX_RADIUS - radius + 1);
        circle.setUserData(points);

        // Modificar evento de clic para generar explosiones
        circle.setOnMouseClicked(event -> {
            // Guardar posición y color antes de eliminar
            double explosionX = circle.getLayoutX();
            double explosionY = circle.getLayoutY();
            Color explosionColor = (Color) circle.getFill();

            // Crear efecto de explosión
            createExplosionEffect(explosionX, explosionY, explosionColor);

            // Sumar puntos y eliminar círculo
            model.addScore((int) circle.getUserData());
            gamePane.getChildren().remove(circle);
            event.consume();
        });

        gamePane.getChildren().add(circle);
    }
    //Efecto de explosión
    private void createExplosionEffect(double centerX, double centerY, Color color) {
        int particleCount = 90; // Número de partículas

        for (int i = 0; i < particleCount; i++) {
            // Crear partícula (círculo pequeño)
            Circle particle = new Circle(3 + random.nextDouble() * 3, color); // Radio entre 3-6
            particle.setLayoutX(centerX);
            particle.setLayoutY(centerY);

            gamePane.getChildren().add(particle);

            // Calcular dirección aleatoria
            double angle = random.nextDouble() * 2 * Math.PI;
            double distance = 40 + random.nextDouble() * 80; // Distancia de explosión
            double targetX = centerX + Math.cos(angle) * distance;
            double targetY = centerY + Math.sin(angle) * distance;

            // Animación de movimiento
            javafx.animation.TranslateTransition moveTransition =
                    new javafx.animation.TranslateTransition(javafx.util.Duration.millis(650), particle);
            moveTransition.setToX(targetX - centerX);
            moveTransition.setToY(targetY - centerY);

            // Animación de desvanecimiento
            javafx.animation.FadeTransition fadeTransition =
                    new javafx.animation.FadeTransition(javafx.util.Duration.millis(700), particle);
            fadeTransition.setToValue(0);

            // Animación de escala (partículas se hacen más pequeñas)
            javafx.animation.ScaleTransition scaleTransition =
                    new javafx.animation.ScaleTransition(javafx.util.Duration.millis(800), particle);
            scaleTransition.setToX(0.1);
            scaleTransition.setToY(0.1);

            // Ejecutar todas las animaciones en paralelo
            javafx.animation.ParallelTransition parallelTransition =
                    new javafx.animation.ParallelTransition(moveTransition, fadeTransition, scaleTransition);

            // Eliminar partícula cuando termine la animación
            parallelTransition.setOnFinished(event -> gamePane.getChildren().remove(particle));

            parallelTransition.play();
        }
    }
    private void updateCircles() {
        // Usamos un Iterator seguro para evitar errores al modificar la lista mientras iteramos
        Iterator<javafx.scene.Node> iterator = gamePane.getChildren().iterator();
        while (iterator.hasNext()) {
            javafx.scene.Node node = iterator.next();
            if (node instanceof Circle circle) {
                // Mover el círculo
                circle.setLayoutY(circle.getLayoutY() + FALL_SPEED);

                // Comprobar si ha rebasado la línea de pérdida de vida
                if (circle.getLayoutY() > LOST_LINE_Y) {
                    model.loseLife();
                    iterator.remove(); // Eliminar el círculo
                }
            }
        }
    }
}