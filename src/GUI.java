import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
/* 
 * this class handles the GUI
 */
public class GUI implements ActionListener{

	private BufferedImage[] sprites = new BufferedImage[21];
	private Minesweeper minesweeper;
	private JFrame frame;
	private JPanel gridPanel;
	private JButton[][] buttons; 
	private int width = 8;
	private int height = 8;
	private int mines = 10;
	private JButton smiley;

	/*
	 * Creates the GUI with all its contents
	 * also sets the GUIs corresponding Minesweeper object
	 */
	public GUI(Minesweeper minesweeper) {
		this.minesweeper = minesweeper;
		setup();
	}
	/*
	 * Creates the GUI
	 */
	private void setup() {
		createSprites(); // fix the pics
		frame = new JFrame("Minesweeper by sam");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(((width+2)*16), (height*16)+135)); // the pictures are 16*16
		frame.setResizable(false);
		frame.setIconImage(sprites[16]);
		JPanel contentPane = new JPanel();
		contentPane.setBackground(new Color(195, 195, 195));
		JPanel top = new JPanel();
		top.setBackground(new Color(195, 195, 195));
		gridPanel = new JPanel();
		GridLayout grid = new GridLayout(height, width);
		gridPanel.setLayout(grid);
		gridPanel.setPreferredSize(new Dimension (width*16, height*16)); //tile size 16*16 imported from picture
		smiley = new JButton("");
		smiley.setIcon(new ImageIcon(sprites[17]));
		smiley.setPreferredSize(new Dimension(52, 52));
		top.add(smiley);
		smiley.addActionListener(this);
		JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, gridPanel); // lazy
		sp.setDividerSize(1);
		contentPane.add(sp);


		JMenuBar menuBar = new JMenuBar();
		JMenu game = new JMenu("Game");
		JMenu help = new JMenu("Help");
		menuBar.add(game);
		menuBar.add(help);
		menuBar.setPreferredSize(new Dimension(200, 20));
		JMenuItem newGame = new JMenuItem("New Game");
		newGame.addActionListener(this);
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(this);


		JRadioButtonMenuItem beginner = new JRadioButtonMenuItem("Beginner");
		JRadioButtonMenuItem intermediate = new JRadioButtonMenuItem("Intermediate");
		JRadioButtonMenuItem expert = new JRadioButtonMenuItem("Expert");
		JRadioButtonMenuItem custom = new JRadioButtonMenuItem("Custom");
		AbstractButton abstract1 = beginner;
		beginner.addActionListener(this);
		intermediate.addActionListener(this);
		expert.addActionListener(this);
		custom.addActionListener(this);
		ButtonGroup group = new ButtonGroup();
		group.add(beginner);
		group.add(intermediate);
		group.add(expert);
		group.add(custom);
		group.setSelected(abstract1.getModel(), true);
		game.add(newGame);
		game.addSeparator();
		game.add(beginner);
		game.add(intermediate);
		game.add(expert);
		game.add(custom);
		game.addSeparator();
		game.add(exit);
		help.add(about);
		buttonFix();
		frame.setJMenuBar(menuBar);
		frame.setContentPane(contentPane);
		frame.pack();
		frame.setVisible(true);

	}
	/*
	 * Creates images taken from the internet and stores them in sprites[]
	 * includes the tiles, mines, flags, smileys etc.
	 */
	private void createSprites() {
		BufferedImage sheet = null;
		BufferedImage frameMine = null;
		BufferedImage happySmiley = null;
		BufferedImage loseSmiley = null;
		BufferedImage winSmiley = null;
		BufferedImage scaredSmiley = null;
		try {
			
		
			sheet = ImageIO.read(getClass().getResource("/images/vafan.png"));
			frameMine = ImageIO.read(getClass().getResource("/images/images.png"));
			happySmiley = ImageIO.read(getClass().getResource("/images/tao-smile.gif"));
			loseSmiley = ImageIO.read(getClass().getResource("/images/tao-dead.gif"));
			winSmiley =  ImageIO.read(getClass().getResource("/images/tao-cool.gif"));
			scaredSmiley = ImageIO.read(getClass().getResource("/images/tao-oh.gif"));
		
		} catch (IOException e) {    
			System.out.println("Unable to retrieve image");    
			e.printStackTrace();  
		}
		for(int i= 0; i < 16; i++) {
			sprites[i] = sheet.getSubimage(0, i*16, 16, 16);
		}
		sprites[16] = frameMine;
		sprites[17] = happySmiley;
		sprites[18] = loseSmiley;
		sprites[19] = winSmiley; 
		sprites[20] = scaredSmiley;
	}
	/*
	 * Repaint the frame and grid according to the selected difficulty
	 */
	public void repaint(int width, int height, int mines) {
		this.width = width;
		this.height = height;
		this.mines = mines;
		smiley.setIcon(new ImageIcon(sprites[17]));
		frame.setSize(new Dimension(((width+2)*16), (height*16)+135));
		gridPanel.removeAll();
		GridLayout newGrid = new GridLayout(height, width);
		gridPanel.setLayout(newGrid);
		gridPanel.setPreferredSize(new Dimension((width*16), (height*16)));
		buttonFix();
		gridPanel.updateUI(); 
	}

	/*
	 * Creates all the buttons, also sets the mouselistener
	 */
	private void buttonFix() {
		buttons = new JButton[height][width];
		for (int x = 0; x<height; x++) {              
			for (int y= 0; y<width; y++) {
				final int i = x;
				final int j = y;
				final JButton button = new JButton();
				button.setIcon(new ImageIcon(sprites[0]));
				button.addMouseListener(new MouseListener() {

					/*
					 * MouseListener for the cell buttons
					 */
					public void mouseClicked(MouseEvent e) {
						if(SwingUtilities.isRightMouseButton(e)) {
							if(minesweeper.field[i][j].isCleared() == false) {
								if(minesweeper.field[i][j].isFlagged() == false) {
									minesweeper.field[i][j].setIsFlagged(true);
									button.setIcon(new ImageIcon(sprites[1]));
								} else {
									minesweeper.field[i][j].setIsFlagged(false);
									button.setIcon(new ImageIcon(sprites[0]));
								}
							}
							return;
						}
						else if(minesweeper.field[i][j].isCleared() | minesweeper.field[i][j].isFlagged()) {
							return; 

							// if cleared or flagged do nothing

						} else if (minesweeper.getFirst()) {
							minesweeper.show(i, j);
							if (minesweeper.field[i][j].getNeighbouringMines()==0) {
								minesweeper.clearZeros(i, j);
							}
						}  
						else if(minesweeper.field[i][j].getNeighbouringMines()==0) {
							minesweeper.clearZeros(i, j);
						}

						else {
							minesweeper.show(i, j);
						}
					}


					public void mousePressed(MouseEvent e) {
						if(SwingUtilities.isRightMouseButton(e)) {
							return;
						}
						if(!minesweeper.field[i][j].isFlagged() && !minesweeper.field[i][j].isCleared()) {
							smiley.setIcon(new ImageIcon(sprites[20]));
						}
					}

					public void mouseReleased(MouseEvent e) {
						if(minesweeper.getClearAble() == -1) {
							smiley.setIcon(new ImageIcon(sprites[18]));
						} else if(minesweeper.getClearAble() == 0){
							smiley.setIcon(new ImageIcon(sprites[19]));
						} else {
							smiley.setIcon(new ImageIcon(sprites[17]));
						}

					}

					public void mouseEntered(MouseEvent e) {
					}

					public void mouseExited(MouseEvent e) {
					}
				});
				buttons[x][y] = button;
				gridPanel.add(button);
			}
		}
	}

	/* 
	 * Sets the buttons images according to neighbouringmines
	 */
	public void show(int x, int y) {
		switch(minesweeper.field[x][y].getNeighbouringMines()) {
		case 0: buttons[x][y].setIcon(new ImageIcon(sprites[15]));
		break;
		case 1: buttons[x][y].setIcon(new ImageIcon(sprites[14]));
		break;
		case 2:	buttons[x][y].setIcon(new ImageIcon(sprites[13]));
		break;
		case 3:	buttons[x][y].setIcon(new ImageIcon(sprites[12]));
		break;
		case 4:	buttons[x][y].setIcon(new ImageIcon(sprites[11]));
		break;
		case 5:	buttons[x][y].setIcon(new ImageIcon(sprites[10]));
		break;
		case 6: buttons[x][y].setIcon(new ImageIcon(sprites[9]));
		break;
		case 7: buttons[x][y].setIcon(new ImageIcon(sprites[8]));
		break;
		case 8: buttons[x][y].setIcon(new ImageIcon(sprites[7]));
		break;
		}
	}


	public void showWin() {
		smiley.setIcon(new ImageIcon(sprites[19]));
	}

	/* 
	 * If we lose, show all the mines and highlight the mine we clicked,
	 */
	public void showLose(int x, int y) {
		buttons[x][y].setIcon(new ImageIcon(sprites[3]));
		for (int i = 0; i<height; i++) {                  
			for (int j= 0; j<width; j++) {
				if(minesweeper.field[i][j].isMine() && (x != i || y != j)) {
					buttons[i][j].setIcon(new ImageIcon(sprites[4]));
				}
			}
		}
		smiley.setIcon(new ImageIcon(sprites[18]));
	}



	/*
	 * Handles menu buttons
	 * 
	 */
	public void actionPerformed(ActionEvent E) {
		if(E.getActionCommand() == "New Game" | E.getSource() == smiley ) {
			minesweeper.newGame();
		}

		if(E.getActionCommand() == "Beginner") {
			width = 8;
			height = 8;
			mines = 10;
			minesweeper.setWidth(width);
			minesweeper.setHeight(height);
			minesweeper.setMines(mines);
			minesweeper.newGame();
		}
		if(E.getActionCommand() == "Intermediate") {
			width = 16;
			height = 16;
			mines = 40;
			minesweeper.setWidth(width);
			minesweeper.setHeight(height);
			minesweeper.setMines(mines);
			minesweeper.newGame();
		}
		if(E.getActionCommand() == "Expert") {
			width = 30;
			height = 16;
			mines = 99;
			minesweeper.setWidth(width);
			minesweeper.setHeight(height);
			minesweeper.setMines(mines);
			minesweeper.newGame();
		}
		if(E.getActionCommand() == "Custom") {
			JPanel customPanel = new JPanel();
			GroupLayout groupLayout = new GroupLayout(customPanel);
			customPanel.setLayout(groupLayout);
			JTextField widthText = new JTextField(2);
			JTextField heightText = new JTextField(2);
			JTextField minesText = new JTextField(2);
			JLabel widthLabel= new JLabel("width");
			JLabel heightLabel = new JLabel("height");
			JLabel minesLabel = new JLabel("mines");
			groupLayout.setHorizontalGroup(
					groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(widthLabel)
							.addComponent(widthText, 40, 40, 40))
							.addGap(20)
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(heightLabel)
									.addComponent(heightText, 40, 40, 40))
									.addGap(20)
									.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
											.addComponent(minesLabel)
											.addComponent(minesText, 40, 40, 40))
					);

			groupLayout.setVerticalGroup(
					groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(widthLabel)
							.addComponent(heightLabel)
							.addComponent(minesLabel))
							.addGap(5)
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(widthText)
									.addComponent(heightText)
									.addComponent(minesText))

					);
			Object[] options = {"OK"};
			int n = JOptionPane.showOptionDialog(null, 
					customPanel, 
					"Create your custom field",
					JOptionPane.PLAIN_MESSAGE,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]);
			if(n == JOptionPane.OK_OPTION) {
				try {
					width = Integer.parseInt(widthText.getText());
					height = Integer.parseInt(heightText.getText());
					mines = Integer.parseInt(minesText.getText());
				} catch(NumberFormatException e) {  // easier but not as good as a formatted text area
					width = 8;
					height = 8;
					mines = 10;

				}
				// protect my gui from bad resizing
				if (width<4 | width>50) {
					width = 8;
					height = 8;
					mines = 10;


				}
				if (height<4 | height>50) {
					width = 8;
					height = 8;
					mines = 10;
				}
				if (mines<1 || mines>(width*height)) {
					mines = 4;
				}

				minesweeper.setWidth(width);
				minesweeper.setHeight(height);
				minesweeper.setMines(mines);
				minesweeper.newGame();

			}
		}
		if(E.getActionCommand() == "Exit") {
			System.exit(0);
		}
		if(E.getActionCommand() == "About")
		{
			JOptionPane.showMessageDialog(null, "Minesweeper clone \nby: Sam Hamra \n2013-04-07", "About", JOptionPane.PLAIN_MESSAGE);
		}

	}
}


