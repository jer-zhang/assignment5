package assignment5;
/* CRITTERS Main.java
 * EE422C Project 5 submission by
 * Jerry Zhang
 * jz9954
 * 15465
 * Celine Lillie
 * Cml3665
 * 15460
 * Slip days used: 0
 * Spring 2018
 */

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
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
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

/**
 * This class holds the GUI of the Critter World
 * @author Jerry Zhang, Celine Lillie
 *
 */
public class Main extends Application {
		
	private static String myPackage;
	
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }
    
    // Declare constants
    private final int MAXVAL = 1000;		// Maximum value in spinner
    private final int MINVAL = 1;			// Minimum value in spinner
    private final int SPACING = 5;			// Spacing between boxes on panes
    private final int SMALLBUTTONWIDTH = 75;// Width of a small button
    private final int LARGEBUTTONWIDTH = 2*SMALLBUTTONWIDTH + SPACING;	// Width of a large button
    private final int TEXTFIELDWIDTH = 3*SMALLBUTTONWIDTH + 2*SPACING;	// Width of a text field
    
    // Declare variables
	private BorderPane pane;		// Main layout pane
	private VBox controlPane;		// Left side, holds buttons and other controls
	private GridPane viewPane;		// Center side, holds grid
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
	private int width;
	private int height;
	private ArrayList<String> critterStringList;
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Starts GUI
	 * @param stage
	 */
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Critters");					// Title of our window
		pane = new BorderPane();					// Create new BorderPane for Main GUI layout
		controlPane = new VBox();					// Create a new VBox for controls
		viewPane = new GridPane();					// Create new GridPane for critter grid
		
        Scene scn = new Scene(pane);				// Create new scene including Main Gui layout
	    
		Canvas canvas = new Canvas(width, height);	// Create new canvas of width/height
	    pane.setTop(canvas);						// Set top of BorderPane with canvas
	    
	    // Create new black text and font. Set font to text
	    Text t = new Text();						
        t.setText("CRITTERS BATTLEGROUNDS");
        t.setFill(Color.BLACK);
        Font f = new Font(40);
        t.setFont(f);
        
        // Creates a list of all Critter files in selected path
        File assignment5 = new File("C:\\Users\\Jerry Zhang\\eclipse-workspace\\assignment5\\src\\assignment5");
        String[] classList = assignment5.list();									// List of files in selected path
        ArrayList<Class<?>> critterClassList = new ArrayList<Class<?>>();			// Create new ArrayList to hold Critter classes
        for (String s : classList) {
        	String className = s.substring(0, s.length()-5);						// Create string of className, removing .java
        	try {
				Class<?> c = Class.forName(myPackage + "." + className);			// Reflections, get class of files
				Class<?> critterClass = Class.forName(myPackage + ".Critter");		// Compare to Critter file
	        	if ((critterClass.isAssignableFrom(c)) && (!critterClass.equals(c))) {	// If class superclass is Critter, add to list
	        		critterClassList.add(c);
	        	}
        	} catch (Exception e) {
        		// Do Nothing
        	}
        }
        
        critterStringList = new ArrayList<String>();		// Create a new ArrayList to hold strings of critters
        for (Class<?> c : critterClassList) {				// For each Class in critterClassList, make string and add to string list
        	String critString = c.toString().substring(18);
        	critterStringList.add(critString);
        }

        // Create a map of String and Boolean that holds values if runStats checkbox is checked
    	Map<String, Boolean> checkListMap = new HashMap<String, Boolean>();	
    	for (String crit : critterStringList) {
        	checkListMap.put(crit, false);
    	}
        
// MAKE    	
    	// Make SpinnerValueFactory (Value in the spinner), if entered value > max or < min then reset to previous valid value
        SpinnerValueFactory<Integer> makeValFac = new SpinnerValueFactory.IntegerSpinnerValueFactory(MINVAL, MAXVAL);	// Create new make SpinnerValueFactory
        makeValFac.setConverter(new StringConverter<Integer>() {
			@Override
			public Integer fromString(String arg0) {
				int temp = makeValFac.getValue();				// Old value in spinner
				try {
					Integer newVal = Integer.parseInt(arg0);	// New value entered
					if ((newVal <= MAXVAL) && (newVal >= MINVAL)) {
						return Integer.parseInt(arg0);			// return new value if int within min/max range
					} else {
						return temp;							// return old value
					}
				} catch (NumberFormatException e) {
					return temp;								// Invalid input, return old value
				}
			}

			@Override
			public String toString(Integer arg0) {
				return null;
			}     	
        });
        
        // Make Spinner
        Spinner<Integer> makeCount = new Spinner<Integer>();	// Create a new make Spinner
        makeCount.setMaxWidth(SMALLBUTTONWIDTH);				// Set width to SMALLBUTTONWIDTH
        makeCount.setValueFactory(makeValFac);					// Set Spinner's value factory to above
        makeCount.setEditable(true);							// Enable edit
		// Convert string in spinner to int and set value and text
        makeCount.getEditor().setOnAction(action -> {
        	String text = makeCount.getEditor().getText();
        	int value = makeValFac.getConverter().fromString(text);
        	makeValFac.setValue(value);
        	makeCount.getEditor().setText(String.valueOf(value));
        });

        // Make ComboBox
        ComboBox<String> makeComboBox = new ComboBox<String>();
        for (Class<?> c : critterClassList) {					// Each Critter class gets added to ComboBox
        	String critString = c.toString().substring(18);
        	makeComboBox.getItems().add(critString);	
        }
        makeComboBox.setMinWidth(TEXTFIELDWIDTH);				// Set width
        if (!critterClassList.isEmpty()) {						// If critterClassList is not empty, set value to critter string
        	makeComboBox.setValue(critterClassList.get(0).toString().substring(18));
        }
        Button makeButton = new Button();						// Create a new make button
        makeButton.setMinWidth(LARGEBUTTONWIDTH);				// Set width
        makeButton.setText("Make");								// Set text
        makeButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		try {
        			String critName = makeComboBox.getValue();	// Temp string of Critter
        			for (int i = 0; i < makeCount.getValue(); i++) {
        				Critter.makeCritter(critName);			// Create makeCount of temp critters
        			}
        	        Critter.displayWorld(viewPane);				// Update world
        	        Main.displayStats(critterStringList, checkListMap);	// Update stats
        		} catch (Exception ex) {						// Display error if invalid Critter
        			System.out.println("Error! Invalid Input.");
        		}
        	}
        });

// STEP
        // Step SpinnerValueFactory (Value in the spinner), if entered value > max or < min then reset to previous valid value
        SpinnerValueFactory<Integer> stepValFac = new SpinnerValueFactory.IntegerSpinnerValueFactory(MINVAL, MAXVAL);
        stepValFac.setConverter(new StringConverter<Integer>() {	// Create new step SpinnerValueFactory
			@Override
			public Integer fromString(String arg0) {
				int temp = stepValFac.getValue();			// Old value in spinner
				try {
					Integer newVal = Integer.parseInt(arg0);// New value entered
					if ((newVal <= MAXVAL) && (newVal >= MINVAL)) {
						return Integer.parseInt(arg0);		// return new value if int within min/max range
					} else {
						return temp;						// return old value
					}
				} catch (NumberFormatException e) {
					return temp;							// Invalid input, return old value
				}
			}

			@Override
			public String toString(Integer arg0) {
				return null;
			}     	
        });
        
        Spinner<Integer> stepCount = new Spinner<Integer>();// Create a new step Spinner
        stepCount.setMaxWidth(SMALLBUTTONWIDTH);			// Set width to SMALLBUTTONWIDTH
        stepCount.setValueFactory(stepValFac);				// Set Spinner's value factory to above
        stepCount.setEditable(true);						// Enable edit
        // Convert string in spinner to int and set value and text
        stepCount.getEditor().setOnAction(action -> {
        	String text = stepCount.getEditor().getText();
        	int value = stepValFac.getConverter().fromString(text);
        	stepValFac.setValue(value);
        	stepCount.getEditor().setText(String.valueOf(value));
        });
        
        Button stepButton = new Button();				// Create a new step button
        stepButton.setMinWidth(LARGEBUTTONWIDTH);		// Set width
        stepButton.setText("Step");						// Set text
        stepButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		try {
        			for (int i = 0; i < stepCount.getValue(); i++) {
        				Critter.worldTimeStep();		// Do worldTimeStep stepCount number of times
        			}
        	        Critter.displayWorld(viewPane);		// Update world
        	        Main.displayStats(critterStringList, checkListMap); // Update stats
        		} catch (Exception ex){
        			System.out.println("Error! Invalid Input.");// Display error if invalid Step
        		}
        	}
        });
        
        Button step1Button = new Button();				// Create a new step 1 button
        step1Button.setMinWidth(SMALLBUTTONWIDTH);		// Set width
        step1Button.setText("Step 1");					// Set text
        step1Button.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		try {
    				Critter.worldTimeStep();			// Do worldTimeStep 1 time
    		        Critter.displayWorld(viewPane);		// Update world
        	        Main.displayStats(critterStringList, checkListMap);// Update stats
        		} catch (Exception ex){
        			System.out.println("Error! Invalid Input.");// Display error if invalid Step
        		}
        	}
        });
        
        Button step100Button = new Button();			// Create a new step 100 button
        step100Button.setMinWidth(SMALLBUTTONWIDTH);	// Set width
        step100Button.setText("Step 100");				// Set text
        step100Button.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		try {
        			for (int i = 0; i < 100; i++) {		// Do worldTimeStep 100 time
        				Critter.worldTimeStep();
        			}
        	        Critter.displayWorld(viewPane);		// Update world
        	        Main.displayStats(critterStringList, checkListMap);// Update stats
        		} catch (Exception ex){
        			System.out.println("Error! Invalid Input.");// Display error if invalid Step
        		}
        	}
        });
        
        Button step1000Button = new Button();			// Create a new step 1000 button
        step1000Button.setMinWidth(SMALLBUTTONWIDTH);	// Set width
        step1000Button.setText("Step 1000");			// Set text
        step1000Button.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		try {
        			for (int i = 0; i < 1000; i++) {	// Do worldTimeStep 1000 time
        				Critter.worldTimeStep();        				
        			}
        			Critter.displayWorld(viewPane);		// Update world
        	        Main.displayStats(critterStringList, checkListMap);	// Update stats
        		} catch (Exception ex){
        			System.out.println("Error! Invalid Input.");// Display error if invalid Step
        		}
        	}
        });
        
// SEED
        // Seed SpinnerValueFactory (Value in the spinner), if entered value > max or < min then reset to previous valid value
        SpinnerValueFactory<Integer> seedValFac = new SpinnerValueFactory.IntegerSpinnerValueFactory(MINVAL, MAXVAL);
        seedValFac.setConverter(new StringConverter<Integer>() {	// Create new seed SpinnerValueFactory
			@Override
			public Integer fromString(String arg0) {
				int temp = seedValFac.getValue();				// Old value in spinner
				try {
					Integer newVal = Integer.parseInt(arg0);	// New value entered
					if ((newVal <= MAXVAL) && (newVal >= MINVAL)) {
						return Integer.parseInt(arg0);			// return new value if int within min/max range
					} else {
						return temp;							// return old value
					}
				} catch (NumberFormatException e) {
					return temp;								// Invalid input, return old value
				}
			}

			@Override
			public String toString(Integer arg0) {
				return null;
			}     	
        });
        
        Spinner<Integer> seedCount = new Spinner<Integer>();	// Create a new step Spinner
        seedCount.setMaxWidth(SMALLBUTTONWIDTH);				// Set width to SMALLBUTTONWIDTH
        seedCount.setValueFactory(seedValFac);					// Set Spinner's value factory to above
        seedCount.setEditable(true);							// Enable edit
        // Convert string in spinner to int and set value and text
        seedCount.getEditor().setOnAction(action -> {
        	String text = seedCount.getEditor().getText();
        	int value = seedValFac.getConverter().fromString(text);
        	seedValFac.setValue(value);
        	seedCount.getEditor().setText(String.valueOf(value));
        });
        
        Button seedButton = new Button();				// Create a new seed button
        seedButton.setMinWidth(LARGEBUTTONWIDTH);		// Set width
        seedButton.setText("Seed");						// Set text
        seedButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		try {
        			Critter.setSeed(seedCount.getValue());	// Set seed
        		} catch (Exception ex){
        			System.out.println("Error! Invalid Input.");// Display error if invalid seed
        		}
        	}
        });
        
        runStatsBox = new VBox();		// Create 2 new VBoxs for stats
        statsBox = new VBox();
        Text runStatsTitle = new Text("Display Stats For:");	// Create text to add to a VBox
        runStatsTitle.setFont(new Font(20));
        runStatsBox.getChildren().add(runStatsTitle);
        for (String c : critterStringList) {					// Add a blank space for each critter present in StringList
        	Text temp = new Text("");
        	statsBox.getChildren().add(temp);
        }

        // For each critter in array
        for (String c : critterStringList) {
        	CheckBox cb = new CheckBox(c);		// Create a new checkbox
        	cb.setText(c);						// Set text to string
        	// If checkbox is selected, change checkListMap to true, else false.
        	cb.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (cb.isSelected()) {
						checkListMap.replace(c, true);
					} else {
						checkListMap.replace(c, false);
					}
					Main.displayStats(critterStringList, checkListMap);		// Display stats based on checkListMap values
				}
        	});
        	runStatsBox.getChildren().add(cb);	// Add checkbox to VBox
        }
        
        Button resetButton = new Button();			// Create a new reset button
        resetButton.setMinWidth(TEXTFIELDWIDTH);	// Set width
        resetButton.setText("Reset World");			// Set text
        // Clear world
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		Critter.clearWorld();
        		Critter.displayWorld(viewPane);
        		Main.displayStats(critterStringList, checkListMap);
        	}
        });
        
        Button quitButton = new Button();			// Create a new reset button
        quitButton.setMinWidth(TEXTFIELDWIDTH);		// Set width
        quitButton.setText("Quit");					// Set text
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent e) {
        		System.exit(0);						// Exit program
        	}
        });
        
// ANIMATE 
        // Animate SpinnerValueFactory (Value in the spinner), if entered value > max or < min then reset to previous valid value
        SpinnerValueFactory<Integer> animateValFac = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAXVAL);
        animateValFac.setConverter(new StringConverter<Integer>() {	// Create new seed SpinnerValueFactory
			@Override
			public Integer fromString(String arg0) {
				int temp = animateValFac.getValue();		// Old value in spinner
				try {
					Integer newVal = Integer.parseInt(arg0);// New value entered
					if ((newVal <= MAXVAL) && (newVal >= 0)) {
						return Integer.parseInt(arg0);		// return new value if int within min/max range
					} else {
						return temp;						// return old value
					}
				} catch (NumberFormatException e) {
					return temp;							// Invalid input, return old value
				}
			}

			@Override
			public String toString(Integer arg0) {
				return null;
			}     	
        });
        
        Spinner<Integer> animateCount = new Spinner<Integer>();	// Create a new animate Spinner
        animateCount.setMaxWidth(SMALLBUTTONWIDTH);				// Set width to SMALLBUTTONWIDTH
        animateCount.setValueFactory(animateValFac);			// Set Spinner's value factory to above
        animateCount.setEditable(true);							// Enable edit
        // Convert string in spinner to int and set value and text
        animateCount.getEditor().setOnAction(action -> {
        	String text = animateCount.getEditor().getText();
        	int value = animateValFac.getConverter().fromString(text);
        	animateValFac.setValue(value);
        	animateCount.getEditor().setText(String.valueOf(value));
        });
        
        Slider animateSlider = new Slider(1, 100, 1);		// Create a new animate Slider
        animateSlider.setMinWidth(LARGEBUTTONWIDTH);		// Set width to LARGEBUTTONWIDTH
        animateSlider.setShowTickLabels(true);				// Set and enable tick marks
        animateSlider.setMinorTickCount(10);
        animateSlider.setMajorTickUnit(49);
        animateSlider.setShowTickMarks(true);
        animateSlider.setBlockIncrement(1);
        animateSlider.setSnapToTicks(true);
        
        ArrayList<Integer> numSteps = new ArrayList<Integer>();
        numSteps.add(0);
        
        ToggleButton animateButton = new ToggleButton();
        animateButton.setMinWidth(LARGEBUTTONWIDTH);
        animateButton.setText("Animate");
        animateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Timeline timeline = new Timeline();		// Create a new Timeline
				
				// If animate button is selected, disable all other buttons
				if (animateButton.isSelected()) {
					stepButton.setDisable(true);
					step1Button.setDisable(true);
					step100Button.setDisable(true);
					step1000Button.setDisable(true);
					makeButton.setDisable(true);
					seedButton.setDisable(true);
					resetButton.setDisable(true);
					makeCount.setDisable(true);
					makeComboBox.setDisable(true);
					seedCount.setDisable(true);
					stepCount.setDisable(true);
					
					// Calculate number of frames required by desired step count and speed
					int frames = animateCount.getValue() / (int) animateSlider.getValue();
					timeline.setCycleCount(frames);					// Set cycleCount to frames
										
					timeline.getKeyFrames().add(new KeyFrame(Duration.millis(250), new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							if (!animateButton.isSelected()) {
								timeline.stop();					// If animate button is selected, stop timeline
							}
							if (animateCount.getValue() < animateSlider.getValue()) {
								timeline.stop();					// If animate count is ever less than slider value, stop timeline, goto handler
								timeline.getOnFinished().handle(new ActionEvent());
								return;
							}
							for (int steps = 0; steps < (int) animateSlider.getValue(); steps++) {
								numSteps.set(0, numSteps.get(0) + 1);	// Step snimatSlider number of times
								System.out.println(numSteps.get(0));
								Critter.worldTimeStep();
							}
							Critter.displayWorld(viewPane);			// Update World
							animateCount.getValueFactory().setValue(animateCount.getValue() - (int) animateSlider.getValue()); // Set animate spinner to spinner - slider
							animateCount.getEditor().setText(Integer.toString(animateValFac.getValue()));	// Set spinner count to steps remaining
							Main.displayStats(critterStringList, checkListMap);		// Display stats
						}
					}));
					
					timeline.onFinishedProperty().set(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							int modSteps = animateCount.getValue() % (int) animateSlider.getValue();

							// If animateCount is ever > modSteps, set the timeline cycle count to the division to update frame count and play
							if (animateCount.getValue() > modSteps) {
								timeline.setCycleCount(animateCount.getValue() / (int) animateSlider.getValue());
								timeline.play();
							} else {
								// Step remainder time if animateCount < than slider num
								for (int i = 0; i < modSteps; i++) {
									numSteps.set(0, numSteps.get(0) + 1);
									System.out.println(numSteps.get(0));
									Critter.worldTimeStep();
									animateCount.getValueFactory().setValue(animateCount.getValue() - 1);	// Set animate spinner to decrease 1
								}
								Critter.displayWorld(viewPane);		// Update world
																
								numSteps.set(0, 0);
	//							animateCount.getEditor().setText("0");
	//							animateValFac.setValue(0);
								
								// Enable all buttons
								
								stepButton.setDisable(false);
								step1Button.setDisable(false);
								step100Button.setDisable(false);
								step1000Button.setDisable(false);
								makeButton.setDisable(false);
								seedButton.setDisable(false);
								resetButton.setDisable(false);
								makeCount.setDisable(false);
								makeComboBox.setDisable(false);
								seedCount.setDisable(false);
								stepCount.setDisable(false);
								
								animateButton.setSelected(false);
							}
						}
					});
					
					// If cycleCount is less greater than 0, play the timeline, else, goto getOnGinish handler
					if(timeline.getCycleCount() > 0) {
						timeline.play();
					} else {
						timeline.getOnFinished().handle(new ActionEvent());
					}

				} else {
					// Stop timeline and enable all buttons
					timeline.stop();

					stepButton.setDisable(false);
					step1Button.setDisable(false);
					step100Button.setDisable(false);
					step1000Button.setDisable(false);
					makeButton.setDisable(false);
					seedButton.setDisable(false);
					resetButton.setDisable(false);
					makeCount.setDisable(false);
					makeComboBox.setDisable(false);
					seedCount.setDisable(false);
					stepCount.setDisable(false);
				}
			}
        });
        
        // Add everything to the controlPane, add spacing
        // Set slider box and text
        sliderBox = new HBox();
        Text txt = new Text("Speed: ");
        HBox txtBox = new HBox();
        txtBox.setMinWidth(SMALLBUTTONWIDTH);
        txtBox.getChildren().add(txt);
        sliderBox.getChildren().addAll(txtBox, animateSlider);
        
        // Add make button and spinner
        makeBox = new HBox();
        makeBox.getChildren().addAll(makeButton, makeCount);
        makeBox.setSpacing(SPACING);
        controlPane.getChildren().add(makeBox);
        
        // Add make ComboBox
        textFieldBox = new HBox();
        textFieldBox.getChildren().add(makeComboBox);
        controlPane.getChildren().add(textFieldBox);
        
        // Add step button and spinner
        stepBox = new HBox();
        stepBox.getChildren().addAll(stepButton, stepCount);
        stepBox.setSpacing(SPACING);
        controlPane.getChildren().add(stepBox);
        
        // Add step 1, 100, 1000 button
        stepFixedBox = new HBox();
        stepFixedBox.getChildren().addAll(step1Button, step100Button, step1000Button);
        stepFixedBox.setSpacing(SPACING);
        controlPane.getChildren().add(stepFixedBox);
        
        // Add seed button and spinner
        seedBox = new HBox();
        seedBox.getChildren().addAll(seedButton, seedCount);
        seedBox.setSpacing(SPACING);
        controlPane.getChildren().add(seedBox);
        
        // Add animate button and spinner
        animateBox = new HBox();
        animateBox.setSpacing(SPACING);
        animateBox.getChildren().addAll(animateButton, animateCount);
        controlPane.getChildren().add(animateBox);
        
        // Add slider
        controlPane.getChildren().add(sliderBox);
        
        // Add runstats
        controlPane.getChildren().add(runStatsBox);
        controlPane.getChildren().add(statsBox);
        
        // Add reset button
        resetBox = new HBox();
        resetBox.getChildren().add(resetButton);
        resetBox.setSpacing(SPACING);
        controlPane.getChildren().add(resetBox);
        
        // Add quit button
        quitBox = new HBox();
        quitBox.getChildren().add(quitButton);
        quitBox.setSpacing(SPACING);
        controlPane.getChildren().add(quitBox);
        
        // Set spacing and width
        controlPane.setSpacing(SPACING);
        controlPane.setMinWidth(TEXTFIELDWIDTH + SPACING);
        
        // Add controlPane, title, and viewPane to pane (Main BorderPane)
        pane.setLeft(controlPane);
        pane.setTop(t);
        pane.setCenter(viewPane);
        
        // DisplayWorld, set scene, show
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
