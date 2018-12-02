package main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static void main(String[] args) {
        launch(args);
    }
    
	private Circle start, end;
	private Line line;
	
	private Pane draggableGroup;
	
	private final double S = 700;
	
	private Text text;
	
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Rover Ruckus Measuring Tool by Kenny Ge");
        start = new Circle(5);
        start.setCenterX(100f);
        start.setCenterY(25f);
        end = new Circle(5);
        end.setCenterX(200f);
        end.setCenterY(75f);
/*        start.setOnMouseDragged((e)->{
        	System.out.println(start.getCenterX() + " " + start.getCenterY());
        	start.setCenterX(e.getX());
        	start.setCenterY(e.getY());
        	start.setLayoutX(e.getX());
        	start.setLayoutY(e.getY());
        });*/
        enableDrag(start);
        enableDrag(end);
        
        line = new Line();
        line.setStartX(start.getCenterX());
        line.setStartY(start.getCenterY());
        line.setEndX(end.getCenterX());
        line.setEndY(end.getCenterY());
        
        StackPane root = new StackPane();
        
        Image image = new Image(Main.class.getResourceAsStream("rover_ruckus.png"));
        ImageView view = new ImageView();
        view.setFitWidth(S);
        view.setFitHeight(S);
        
        view.setImage(image);
        
        root.getChildren().add(view);
        
        text = new Text();
        text.setFont(new Font(20));
        text.setText("Once you move the nodes, the distance here will update to reflect the length of the line");
        text.setX(100);
        text.setY(100);
        
        draggableGroup = new Pane();
        draggableGroup.getChildren().add(start);
        draggableGroup.getChildren().add(end);
        draggableGroup.getChildren().add(line);
        draggableGroup.getChildren().add(text);
        
        root.getChildren().add(draggableGroup);
        
        primaryStage.setScene(new Scene(root, S, S));
        primaryStage.show();
    }
    
    private double deltaX = 0f, deltaY = 0f;
    
    public void updateLine() {
    	double deltaXPix = Math.abs(line.getStartX() - line.getEndX());
    	double deltaYPix = Math.abs(line.getStartY() - line.getEndY());
    	
    	double deltaX = (deltaXPix / S) * 365.76;
    	double deltaY = (deltaYPix / S) * 365.76;
    	
    	double hypotenuse = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    	
    	text.setText(hypotenuse + " cm");
    }
    
    private void enableDrag(final Circle circle) {
    	circle.setOnMousePressed(new EventHandler<MouseEvent>() {
    	  @Override public void handle(MouseEvent mouseEvent) {
    	    // record a delta distance for the drag and drop operation.
    	    deltaX = circle.getCenterX() - mouseEvent.getX();
    	    deltaY = circle.getCenterY() - mouseEvent.getY();
    	    circle.getScene().setCursor(Cursor.MOVE);
    	  }
    	});
    	circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
    	  @Override public void handle(MouseEvent mouseEvent) {
    	    circle.getScene().setCursor(Cursor.HAND);
    	  }
    	});
    	circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
    	  @Override public void handle(MouseEvent mouseEvent) {
    	    circle.setCenterX(mouseEvent.getX() + deltaX);
    	    circle.setCenterY(mouseEvent.getY() + deltaY);
    	    if(circle == start) {
    	    	line.setStartX(circle.getCenterX());
    	    	line.setStartY(circle.getCenterY());
    	    }else{
    	    	line.setEndX(circle.getCenterX());
    	    	line.setEndY(circle.getCenterY());
    	    }
    	    updateLine();
    	  }
    	});
    	circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
    	  @Override public void handle(MouseEvent mouseEvent) {
    	    if (!mouseEvent.isPrimaryButtonDown()) {
    	      circle.getScene().setCursor(Cursor.HAND);
    	    }
    	  }
    	});
    	circle.setOnMouseExited(new EventHandler<MouseEvent>() {
    	  @Override public void handle(MouseEvent mouseEvent) {
    	    if (!mouseEvent.isPrimaryButtonDown()) {
    	      circle.getScene().setCursor(Cursor.DEFAULT);
    	    }
    	  }
    	});
    }
}
