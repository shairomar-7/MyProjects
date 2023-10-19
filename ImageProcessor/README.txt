Changes made to existing code:
- we added an extra method in the EnhancedModel interface: the down size. This is it!

Overview of main method and starting program :
the program can be ran from the ImageProcessor class which exists inside the ImageProcessor package.
In order to start the program, a user can run the main method in this class.
When the main method is called, the user should see a list of possible commands that they can print to the console in order to create a new image file.
These commands allow the user to load and save images, flip images either horizontally or vertically, brighten/darken an image by a certain increment, and grey scale an image.
 If a user ever wants to exit the program, they can enter either “q”, or “quit” or "Q".
The list of available commands are the following:
"load file-path destination-name"
"save file-path image-name"
"flip-horizontal image-name destination-name"
"flip-vertical image-name destination-name"
"brighten increment image-name destination-name"
"red-component image-name destination-name"
"blue-component image-name destination-name"
"green-component image-name destination-name"
"value-component image-name destination-name"
"intensity-component image-name destination-name"
"luma-component image-name destination-name"
"q/Q/quit (quit the program)"

Overview of IModel interface:
A new Model object is created as a local variable inside the main method in order for the program to be ran.
The Model, which implements the IModel interface, is essentially the image that is to be created.
The IModel and IModelState interfaces, as well as the Model class exist within the imageprocessor.model package.
The Model class contains two fields. The first is a HashMap<String, List<List<Pixel>> called history.
This hash map contains the name of all of the images that have been created or loaded.
Each image is mapped to its unique 2D List of pixels, which represents the image itself.
The second field is an int called maxDepth, which represents the max depth of the value components for each pixel. In the case for ppm files, this value is 255.
This class contains two constructors. The first constructor takes in no parameters, and is used in the main method.
 This constructor initializes history to a new HashMap, and maxDepth to 255.
This allows the user to run the program without actually loading an image file path and name.
The second constructor allows the user to specify the 2D List of pixels, as well as the height and width of the image they want to create.
This constructor also initializes history to an empty HashMap, and maxDepth to 255.
Additionally, this constructor puts a new entry in history, with the image name “initialModel”, which is mapped to the given 2d List of pixels.
The Model class can perform several methods, which are primarily used to manipulate images.
These methods include all of the commands that can be called in the program, excluding load and save, which are located in the Controller class.
This class also includes three non-image-manipulation methods inherited from the IModelState.
 These methods are: getDepth(), which allows other classes to access the maxDepth; getPixelsFromHistory(String imageName), which takes in an image name and generates a new 2D List of pixels (shallow copy) based off of the list that that name is mapped to in history; and addToHistory(List<List<Pixel>> pixels, String imageName), which allows other classes to make entries into history.
 Note: if a client manipulates an image and his/her desired destination name for the image already exists in history, then it will be overridden.

Overview of EnhancedModel interface
The EnhancedModel interface is an extension of the original IModel interface, that exists inside the same package as the IModel interface.
 It is exactly the same as IModel, except it offers two new functionalities: the ability to filter images, and the ability to perform color transformations on images.
These new methods are implemented in the EnhancedModelImpl class. This class contains a hash map that stores
Each new command as an object of the ImageMatrixOperation interface (stored inside the imageprocessor.model.matrixoperations package).
When the filter and transform methods are called, the appropriate command is obtained from the hash map, and calls execute on this EnhancedModel in order to create a new image.

Overview of Pixel class
In order to represent the pixels of the image, we created a Pixel class, which exists inside the imageprocessor package.
The Pixel class contains one constructor, which takes in the three value components that make up each pixel.
The constructor checks to make sure that none of the values are less than 0, but does not check to make sure that any of the values are > 255, as this would mean that a broken file has been passed in, which is an error that is handled in the controller class.
The Pixel class is primarily used to manipulate individual pixels in a certain image.
In order to grey scale an image, a method called executeCommand(String command) is used.
This method takes in one of the possible grey scale commands, and then calls a private method to properly update the pixel (Ex, if “luma” is the command, the method will call the private method setToLuma(), which will use the luma calculation to create a new pixel with the appropriate components.
The class also contains a method called incrementPixel(int increment. Int maxDepth), which adjusts each component in the pixel by the given increment,
while also using a private helper method handleRange(int componentValue, int maxDepth) to make sure that all of the adjusted component values are valid.
Additionally, this class overrides hashCode() and equals(), and includes a method called getComponent(int component) which allows other classes to access individual components of the pixel.
This method is primarily used in the readFile  and saveImageAs methods in the Controller class, as this method needs to be able to read each individual component in the pixel in order to create a new image file.

Overview of IView interface
(Note: this interface is now only applicable for the text-commands implementation of this assignment.
For the version that uses the GUI, this interface is obsolete).
The IView interface and View class (which implements IView) exist inside the imageprocessor.view package.
Currently, the IView interface only contains one method, called RenderMessage(String message), which displays messages to the user on the console.
The View class contains an Appendable field, which it uses in the renderMessage method in order to append the given String to this appendable.
The view contains two constructor, both of which are used to initialize the Appendable. One constructor allows the user to specify which appendable they want to use, and the other sets it to a default of System.out.

Overview of IController interface
(Note: this interface is now only applicable for the text-commands implementation of this assignment. For the version that uses the GUI, this interface is obsolete).
The Controller class is used to take in input from the user, and update the model accordingly, while also sending messages to the view to be displayed.
The Controller class, as well as the IController interface that it implements, both exist inside the imageprocessor.controller package.
 The Controller class only implements one method from the IController interface: go().
This is the method that is used in the main method in order to run the program.
When go() is called, it first calls a private helper to display the menu of commands on the console, and then calls a private helper method called processInputs(). ProcessInputs waits for the user to input a certain command.
Each command in the program is represented as a class of the ImageCommand interface, which essentially represents a function object.
Each ImageCommand is stored inside a private HashMap in the Controller class.
The only two commands that are not ImageCommands are load and save, because the load and save methods exist in the controller class.
Each command function object is stored in the imageprocessor.controlller.commands package.
Each class that implements ImageCommand contains a constructor that takes in an image name, the name of the image that is to be created, as well as any other parameters that are necessary to run the method that the command is trying to execute.
Each class contains a method called execute(),  which takes in an IModel, and performs the necessary method in the IModel interface in order to manipulate the image.
The decision to use a command-design pattern was made in order to give flexibility to the number of operations that the program can perform, so that if a new command is to be added, only one more ImageCommand has to be created, and this command can then be stored inside the HashMap in Controller.
Updates to Controller:
The controller has been updated to take in more image formats. The io package contains the necessary classes to read in other image files. Also, new commands have been added to the commands package, in order to allow the controller to transform and filter images.

Overview of ImageCommand interface:
This interface is used to minimize code in the Controller class by following what's popularly known as the Command Design Pattern.
 This interface contains only one method, execute, that takes in an IModel as the input.

Overview of Interaction interface:
This interface is used to make testing I/O more elegant, and readable in the tests. It contains an apply method that takes in two StringBuilders.
The two subclasses of this interface are the PrintInteraction, which is specific to outputs, and the InputInteraction, which is specific to inputs.

-Changes to old code:
-the downscale method was added to the EnhancedModel interface. This was done because only adding one method that is not related to any other functionality in the interface would affect past implementations of the interface. No other previous methods in the interface were changed.
-A new class, DownSize, was added to the ImageCommands interface for the newly added downsizing operation.


*ADDITIONS TO CODE: JView and ControllerGUI, and DoubleEnhancedModel

DoubleEnhancedModel class:
This is an extension of EnhancedModel. This class overrides each method so that is can perform partial image manipulation.
This class has a method that creates a black and white mask version of a certain image, and will only execute operations on the original image if the corresponding pixel in this image is black in the mask version.

ControllerGUI/Features interface: This interface is responsible for interacting with the GUI view.
This controller has multiple responsibilities. It is still responsible for loading and saving images, as well as interacting with the model to manipulates images.
It uses the setView to help initialize the action listeners in the JView. It generates the four histograms to display value components. Also, it exits the program when the user decides to quit.

JView/IViewGUI/additional view interfaces:
The IViewGUI and corresponding classes are responsible for displaying the gui.
The currentImagePanel class are responsible for updating the image  according to the controller’s input.
The HistogramPanel class is responsible for updating each histogram when the image changes. The JView class is responsible for displaying the entire GUI. This class creates a main panel which stores all of the command buttons the combo box for the operations, as well as the image and histogram panels.


Extra credit:
- We were able to successfully implement the two extra credits.
- To view the results (images) of the mask image: unzip the MASKIMAGE.zip in the res folder.

Sources:
Each image that is stored in the res folder was created by me and my partner. Each array of pixels was created using the private randomPixelGenerator() method in the test directory.