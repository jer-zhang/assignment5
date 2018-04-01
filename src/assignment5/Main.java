package assignment5;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import assignment5.Critter.CritterShape;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;

public class Main extends Application {
		
	private static String myPackage;
	
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }
    private final int MAXVAL = 1000;
    private final int MINVAL = 1;
    private final int SPACING = 5;
    private final int SMALLBUTTONWIDTH = 75;
    private final int LARGEBUTTONWIDTH = 2*SMALLBUTTONWIDTH + SPACING;
    private final int TEXTFIELDWIDTH = 3*SMALLBUTTONWIDTH + 2*SPACING;
    
	private BorderPane pane;
	private VBox controlPane;
	private GridPane viewPane;
	private HBox makeBox;
	private HBox textFieldBox;
	private HBox stepBox;
	private HBox seedBox;
	private HBox stepFixedBox;
	private HBox animateBox;
	private HBox sliderBox;
	private VBox runStatsBox;
	private static VBox statsBox;
	private HBox resetBox;
	private HBox quitBox;
	private GraphicsContext gc;
	private int width;
	private int height;
	private ArrayList<String> critterStringList;
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Critters");
		pane = new BorderPane();
		controlPane = new VBox();
		viewPane = new GridPane();
		
        Scene scn = new Scene(pane);
	    
		Canvas canvas = new Canvas(width, height);
	    pane.setTop(canvas);
	    gc = canvas.getGraphicsContext2D();
	    
	    Text t = new Text();
        t.setText("CRITTERS BATTLEGROUNDS");
        t.setFill(Color.BLACK);
        Font f = new Font(40);
        t.setFont(f);
        
        File assignment5 = new File("C:\\Users\\Jerry Zhang\\eclipse-workspace\\assignment5\\src\\assignment5");
        String[] classList = assignment5.list();
        ArrayList<Class<?>> critterClassList = new ArrayList<Class<?>>();
        for (String s : classList) {
        	String className = s.substring(0, s.length()-5);
        	try {
			Class<?> c = Class.forName(myPackage + "." + className);
			Class<?> critterClass = Class.forName(myPackage + ".Critter");
        	if ((critterClass.isAssignableFrom(c)) && (!critterClass.equals(c))) {
        		critterClassList.add(c);
        	}
        	} catch (Exception e) {
        	}
        }
        
        critterStringList = new ArrayList<String>();
        for (Class<?> c : critterClassList) {
        	String critString = c.toString().substring(18);
        	critterStringList.add(critString);
        }

    	Map<String, Boolean> checkListMap = new HashMap<String, Boolean>();
    	for (String crit : critterStringList) {
        	checkListMap.put(crit, false);
    	}
        
// MAKE STUFF
        SpinnerValueFactory<Integer> makeValFac = new SpinnerValueFactory.IntegerSpinnerValueFactory(MINVAL, MAXVAL);
        makeValFac.setConverter(new StringConverter<Integer>() {
			@Override
			public Integer fromString(String arg0) {
				int temp = makeValFac.getValue();
				try {
					Integer newVal = Integer.parseInt(arg0);
					if ((newVal <= MAXVAL) && (newVal >= MINVAL)) {
						return Integer.parseInt(arg0);
					} else {
						return temp;
					}
				} catch (NumberFormatException e) {
					return temp;
				}
			}

			@Override
			public String toString(Integer arg0) {
				return null;
			}     	
        });
        
        Spinner<Integer> makeCount = new Spinner<Integer>();
        makeCount.setMaxWidth(SMALLBUTTONWIDTH);
        makeCount.setValueFactory(makeValFac);
        makeCount.setEditable(true);
        makeCount.getEditor().setOnAction(action -> {
        	String text = makeCount.getEditor().getText();
        	int value = makeValFac.getConverter().fromString(text);
        	makeValFac.setValue(value);
        	makeCount.getEditor().setText(String.valueOf(value));
        });
        TextField makeTextFieldCritter = new TextField();
        makeTextFieldCritter.setMinWidth(TEXTFIELDWIDTH);
        makeTextFieldCritter.setPromptText("Enter Valid Critter Name");


        ComboBox<String> makeComboBox = new ComboBox<String>();
        for (Class c : critterClassList) {
        	String critString = c.toString().substring(18);
        	makeComboBox.getItems().add(critString);	
        }
        makeComboBox.setMinWidth(TEXTFIELDWIDTH);
        if (!critterClassList.isEmpty()) {
        	makeComboBox.setValue(critterClassList.get(0).toString().substring(18));
        }
        Button makeButton = new Button();
        makeButton.setMinWidth(LARGEBUTTONWIDTH);
        makeButton.setText("Make");
        makeButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		try {
        			String critName = makeComboBox.getValue();
        			for (int i = 0; i < makeCount.getValue(); i++) {
        				Critter.makeCritter(critName);
        			}
        	        Critter.displayWorld(viewPane);
        	        Main.displayStats(critterStringList, checkListMap);
        		} catch (Exception ex) {
        			System.out.println("Error! Invalid Input.");
        		}
        	}
        });

// STEP STUFF
        SpinnerValueFactory<Integer> stepValFac = new SpinnerValueFactory.IntegerSpinnerValueFactory(MINVAL, MAXVAL);
        stepValFac.setConverter(new StringConverter<Integer>() {
			@Override
			public Integer fromString(String arg0) {
				int temp = stepValFac.getValue();
				try {
					Integer newVal = Integer.parseInt(arg0);
					if ((newVal <= MAXVAL) && (newVal >= MINVAL)) {
						return Integer.parseInt(arg0);
					} else {
						return temp;
					}
				} catch (NumberFormatException e) {
					return temp;
				}
			}

			@Override
			public String toString(Integer arg0) {
				return null;
			}     	
        });
        
        Spinner<Integer> stepCount = new Spinner<Integer>();
        stepCount.setMaxWidth(SMALLBUTTONWIDTH);
        stepCount.setValueFactory(stepValFac);
        stepCount.setEditable(true);
        stepCount.getEditor().setOnAction(action -> {
        	String text = stepCount.getEditor().getText();
        	int value = makeValFac.getConverter().fromString(text);
        	makeValFac.setValue(value);
        	stepCount.getEditor().setText(String.valueOf(value));
        });
        Button stepButton = new Button();
        stepButton.setMinWidth(LARGEBUTTONWIDTH);
        stepButton.setText("Step");
        stepButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		try {
        			for (int i = 0; i < stepCount.getValue(); i++) {
        				Critter.worldTimeStep();
        			}
        	        Critter.displayWorld(viewPane);
        	        Main.displayStats(critterStringList, checkListMap);
        		} catch (Exception ex){
        			System.out.println("Error! Invalid Input.");
        		}
        	}
        });
        
        Button step1Button = new Button();
        step1Button.setMinWidth(SMALLBUTTONWIDTH);
        step1Button.setText("Step 1");
        step1Button.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		try {
    				Critter.worldTimeStep();
    		        Critter.displayWorld(viewPane);
        	        Main.displayStats(critterStringList, checkListMap);
        		} catch (Exception ex){
        			System.out.println("Error! Invalid Input.");
        		}
        	}
        });
        
        Button step100Button = new Button();
        step100Button.setMinWidth(SMALLBUTTONWIDTH);
        step100Button.setText("Step 100");
        step100Button.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		try {
        			for (int i = 0; i < 100; i++) {
        				Critter.worldTimeStep();
        			}
        	        Critter.displayWorld(viewPane);
        	        Main.displayStats(critterStringList, checkListMap);
        		} catch (Exception ex){
        			System.out.println("Error! Invalid Input.");
        		}
        	}
        });
        
        Button step1000Button = new Button();
        step1000Button.setMinWidth(SMALLBUTTONWIDTH);
        step1000Button.setText("Step 1000");
        step1000Button.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		try {
        			for (int i = 0; i < 1000; i++) {
        				Critter.worldTimeStep();        				
        			}
        			Critter.displayWorld(viewPane);
        	        Main.displayStats(critterStringList, checkListMap);
        		} catch (Exception ex){
        			System.out.println("Error! Invalid Input.");
        		}
        	}
        });
        
// SEED STUFF
        SpinnerValueFactory<Integer> seedValFac = new SpinnerValueFactory.IntegerSpinnerValueFactory(MINVAL, MAXVAL);
        seedValFac.setConverter(new StringConverter<Integer>() {
			@Override
			public Integer fromString(String arg0) {
				int temp = seedValFac.getValue();
				try {
					Integer newVal = Integer.parseInt(arg0);
					if ((newVal <= MAXVAL) && (newVal >= MINVAL)) {
						return Integer.parseInt(arg0);
					} else {
						return temp;
					}
				} catch (NumberFormatException e) {
					return temp;
				}
			}

			@Override
			public String toString(Integer arg0) {
				return null;
			}     	
        });
        
        Spinner<Integer> seedCount = new Spinner<Integer>();
        seedCount.setMaxWidth(SMALLBUTTONWIDTH);
        seedCount.setValueFactory(seedValFac);
        seedCount.setEditable(true);
        seedCount.getEditor().setOnAction(action -> {
        	String text = seedCount.getEditor().getText();
        	int value = seedValFac.getConverter().fromString(text);
        	seedValFac.setValue(value);
        	seedCount.getEditor().setText(String.valueOf(value));
        });
        
        Button seedButton = new Button();
        seedButton.setMinWidth(LARGEBUTTONWIDTH);
        seedButton.setText("Seed");
        seedButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		try {
        			Critter.setSeed(seedCount.getValue());
        		} catch (Exception ex){
        			System.out.println("Error! Invalid Input.");
        		}
        	}
        });
        
        runStatsBox = new VBox();
        statsBox = new VBox();
        Text runStatsTitle = new Text("Display Stats For:");
        runStatsTitle.setFont(new Font(20));
        runStatsBox.getChildren().add(runStatsTitle);
        for (String c : critterStringList) {
        	Text temp = new Text("");
        	statsBox.getChildren().add(temp);
        }

        for (String c : critterStringList) {
        	CheckBox cb = new CheckBox(c);
        	cb.setText(c);
        	cb.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (cb.isSelected()) {
						checkListMap.replace(c, true);
					} else {
						checkListMap.replace(c, false);
					}
					Main.displayStats(critterStringList, checkListMap);
				}
        	});
        	runStatsBox.getChildren().add(cb);
        }
        
        Button resetButton = new Button();
        resetButton.setMinWidth(TEXTFIELDWIDTH);
        resetButton.setText("Reset World");
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		Critter.clearWorld();
        		Critter.displayWorld(viewPane);
        		Main.displayStats(critterStringList, checkListMap);
        	}
        });
        
        Button quitButton = new Button();
        quitButton.setMinWidth(TEXTFIELDWIDTH);
        quitButton.setText("Quit");
//      quitButton.setBackground(new Background(new BackgroundFill(Color.INDIANRED, CornerRadii.EMPTY, Insets.EMPTY)));
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		System.exit(0);
        	}
        });
        
// ANIMATE BUTTON   
        
        SpinnerValueFactory<Integer> animateValFac = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAXVAL);
        animateValFac.setConverter(new StringConverter<Integer>() {
			@Override
			public Integer fromString(String arg0) {
				int temp = animateValFac.getValue();
				try {
					Integer newVal = Integer.parseInt(arg0);
					if ((newVal <= MAXVAL) && (newVal >= 0)) {
						return Integer.parseInt(arg0);
					} else {
						return temp;
					}
				} catch (NumberFormatException e) {
					return temp;
				}
			}

			@Override
			public String toString(Integer arg0) {
				return null;
			}     	
        });
        
        Spinner<Integer> animateCount = new Spinner<Integer>();
        animateCount.setMaxWidth(SMALLBUTTONWIDTH);
        animateCount.setValueFactory(animateValFac);
        animateCount.setEditable(true);
        animateCount.getEditor().setOnAction(action -> {
        	String text = animateCount.getEditor().getText();
        	int value = animateValFac.getConverter().fromString(text);
        	animateValFac.setValue(value);
        	animateCount.getEditor().setText(String.valueOf(value));
        });
        
        Slider animateSlider = new Slider(1, 100, 1);
        animateSlider.setMinWidth(LARGEBUTTONWIDTH);
        animateSlider.setShowTickLabels(true);
        animateSlider.setMinorTickCount(10);
        animateSlider.setMajorTickUnit(49);
        animateSlider.setShowTickMarks(true);
        animateSlider.setBlockIncrement(1);
        animateSlider.setSnapToTicks(true);
        
        
        ToggleButton animateButton = new ToggleButton();
        animateButton.setMinWidth(LARGEBUTTONWIDTH);
        animateButton.setText("Animate");
        animateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (animateButton.isSelected()) {
					stepButton.setDisable(true);
					step1Button.setDisable(true);
					step100Button.setDisable(true);
					step1000Button.setDisable(true);
					makeButton.setDisable(true);
					seedButton.setDisable(true);
					resetButton.setDisable(true);
					int frames = animateCount.getValue() / (int) animateSlider.getValue();
					int modSteps = animateCount.getValue() % (int) animateSlider.getValue();
					
					Timeline animate = new Timeline();
					animate.setCycleCount(1);
					
					int stepsRemaining = animateCount.getValue();
					for (int i = 0; i < frames; i++) {
						for (int steps = 0; steps < (int) animateSlider.getValue(); steps++) {
							Critter.worldTimeStep();
							stepsRemaining--;
						}
						Critter.displayWorld(viewPane);
						animateCount.getEditor().setText(Integer.toString(stepsRemaining));
						
						EventHandler<ActionEvent> frameEvent = new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
							}
						};
						
						animate.getKeyFrames().add(new KeyFrame(Duration.millis(50), frameEvent));
					}
					for (int i = 0; i < modSteps; i++) {
						Critter.worldTimeStep();
					}
					Critter.displayWorld(viewPane);
					animateCount.getEditor().setText("0");
					animateValFac.setValue(0);
					
					animate.playFromStart();
					
					stepButton.setDisable(false);
					step1Button.setDisable(false);
					step100Button.setDisable(false);
					step1000Button.setDisable(false);
					makeButton.setDisable(false);
					seedButton.setDisable(false);
					resetButton.setDisable(false);
					animateButton.setSelected(false);
				}
			}
        });
        
        sliderBox = new HBox();
        Text txt = new Text("Speed: ");
        HBox txtBox = new HBox();
        txtBox.setMinWidth(SMALLBUTTONWIDTH);
        txtBox.getChildren().add(txt);
        sliderBox.getChildren().addAll(txtBox, animateSlider);
        
        makeBox = new HBox();
        makeBox.getChildren().addAll(makeButton, makeCount);
        makeBox.setSpacing(SPACING);
        controlPane.getChildren().add(makeBox);
        
        textFieldBox = new HBox();
        textFieldBox.getChildren().add(makeComboBox);
//        textFieldBox.getChildren().add(makeTextFieldCritter);
        controlPane.getChildren().add(textFieldBox);
        
        stepBox = new HBox();
        stepBox.getChildren().addAll(stepButton, stepCount);
        stepBox.setSpacing(SPACING);
        controlPane.getChildren().add(stepBox);
        
        stepFixedBox = new HBox();
        stepFixedBox.getChildren().addAll(step1Button, step100Button, step1000Button);
        stepFixedBox.setSpacing(SPACING);
        controlPane.getChildren().add(stepFixedBox);
        
        seedBox = new HBox();
        seedBox.getChildren().addAll(seedButton, seedCount);
        seedBox.setSpacing(SPACING);
        controlPane.getChildren().add(seedBox);
        
        animateBox = new HBox();
        animateBox.setSpacing(SPACING);
        animateBox.getChildren().addAll(animateButton, animateCount);
        controlPane.getChildren().add(animateBox);
        
        controlPane.getChildren().add(sliderBox);
        
        controlPane.getChildren().add(runStatsBox);
        controlPane.getChildren().add(statsBox);
        
        resetBox = new HBox();
        resetBox.getChildren().add(resetButton);
        resetBox.setSpacing(SPACING);
        controlPane.getChildren().add(resetBox);
        
        quitBox = new HBox();
        quitBox.getChildren().add(quitButton);
        quitBox.setSpacing(SPACING);
        controlPane.getChildren().add(quitBox);
        
        controlPane.setSpacing(SPACING);
        controlPane.setMinWidth(TEXTFIELDWIDTH + SPACING);
        
        pane.setLeft(controlPane);
        pane.setTop(t);
        pane.setCenter(viewPane);
        
        Critter.displayWorld(viewPane);
        stage.setScene(scn);
        stage.show();
	}
	
	private static void displayStats (ArrayList<String> critterStringList, Map<String, Boolean> checkListMap) {
		statsBox.getChildren().clear();
		int displayCount = 0;
		for (String crit : critterStringList) {
			if ((checkListMap.get(crit) != null) && (checkListMap.get(crit))) {
				try {
					HBox statLine = new HBox();
					HBox icon = new HBox();
                	List<Critter> critterInstances = new ArrayList<Critter>();		// Array of instances
                	critterInstances = Critter.getInstances(crit);				// Get list of instances of critter desired
                	Class<?> c = Class.forName(myPackage + "." + crit);		// Create class of critter
                	Method m = c.getMethod("runStats", List.class);					// Get the method "runStats" in that class
                	Text statsString = new Text();
                	statsString.setText(crit + ": " + (String) m.invoke(null, critterInstances));						// Invoke method passing list of instance
                	Critter tempCritter = (Critter) c.newInstance();
					CritterShape newShape = tempCritter.viewShape();
					Shape critShape = Critter.findShape(newShape);
					critShape.getTransforms().add(new Scale((double) 7, (double) 7, -1.0, -1.0));
					critShape.setFill(tempCritter.viewFillColor());
					critShape.setStroke(tempCritter.viewOutlineColor());
					critShape.setStrokeWidth(.1);
					icon.getChildren().add(critShape);
					icon.setPrefSize(20, 15);
					
                	statLine.getChildren().addAll(icon, statsString);
                	statsBox.getChildren().add(statLine);
					displayCount++;
				} catch (Exception e) {
					System.out.println("Run stats error");
				}
			}
		}
		for (; displayCount < critterStringList.size(); displayCount++) {
			Text temp = new Text("");
			statsBox.getChildren().add(temp);
		}
	}
}
