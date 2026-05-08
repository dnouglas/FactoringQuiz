import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class IntroScreen extends JComponent implements ActionListener {
    // Number of questions the user has selected for the quiz
    private int numQuestions;

    // GUI Components for introScreen
	private JLabel lblTitle = new JLabel("Quiz: Factoring Quadratic Equations", JLabel.CENTER);
		
	private JPanel introPane = new JPanel(new BorderLayout());
		private JPanel pnlNumQues = new JPanel(new GridLayout(2, 1));
			private JLabel lblToggleInst = new JLabel("Set Question Amount:", JLabel.CENTER);
			private JPanel pnlToggleCont = new JPanel(new GridLayout(1, 3));
				private JPanel pnlToggleQues = new JPanel(new BorderLayout());
					private JLabel lblnumQues = new JLabel("", JLabel.CENTER);
					private JButton btnMore = new JButton("+");
					private JButton btnLess = new JButton("-");
        private JTextArea taInfo = new JTextArea("");
		
	private JButton btnStartQuiz = new JButton("Start Quiz");

    /**
     * Constructor for the intro screen. Initializes the GUI components and sets up the layout of the screen.
     */
    public IntroScreen() {
        this.setLayout(new BorderLayout());

        //Set up introScreen 
		this.add(lblTitle, BorderLayout.NORTH);
			lblTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
					
		this.add(introPane, BorderLayout.CENTER);
			introPane.add(taInfo, BorderLayout.CENTER);
				taInfo.setEditable(false);
				taInfo.setOpaque(false);
				taInfo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
				taInfo.append("About the Quiz:"
						+ "\n   - This quiz will test your knowledge on factoring simple quadratic equations"
						+ "\n   - Use the toggle bar above to change the number of questions in the quiz"
						+ "\n   - During the quiz, use the built-in keyboard to type in your answers"
						+ "\n   - Enter your answers as factors of the original equation"
						+ "\n   - Your progress will be shown on the top right of the screen"
						+ "\n   - You will be able to see your results and review answers after the quiz"
						+ "\n   - Click on the button below to begin"
						+ "\n   - Good Luck!");
			introPane.add(pnlNumQues, BorderLayout.NORTH);
				pnlNumQues.add(lblToggleInst);
					lblToggleInst.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));
				pnlNumQues.add(pnlToggleCont);
					pnlToggleCont.add(new JLabel(" "));
					pnlToggleCont.add(pnlToggleQues);
						pnlToggleQues.add(lblnumQues, BorderLayout.CENTER);
						pnlToggleQues.add(btnMore, BorderLayout.EAST);
							btnMore.addActionListener(this);
						pnlToggleQues.add(btnLess, BorderLayout.WEST);
							btnLess.addActionListener(this);
					pnlToggleCont.add(new JLabel(" "));
						
		this.add(btnStartQuiz, BorderLayout.SOUTH);
			btnStartQuiz.addActionListener(this);

        this.initIntroScreen();
    }

    /**
     * Event handler for the buttons in the intro screen.
     * Handles user input for starting the quiz and changing the number of questions in the quiz.
     * @param e - ActionEvent triggered by the button press.
     */
    @Override
	public void actionPerformed(ActionEvent e) {
        JButton btnPressed = (JButton) e.getSource();

        if (btnPressed == btnStartQuiz)
				this.fireActionPerformed();
		else {
			if (btnPressed == btnMore)
				numQuestions = numQuestions == 10 ? 10 : numQuestions + 1;
			else if (btnPressed == btnLess)
				numQuestions = numQuestions == 1 ? 1 : numQuestions - 1;
			lblnumQues.setText("" + numQuestions);
		}
    }

    /**
	 * Resets the intro screen to its default starting state.
	 */
    public void initIntroScreen() {
        numQuestions = 3;
        lblnumQues.setText("" + numQuestions);
    }

    /**
	 * Retrieve the number of questions the user has selected for the quiz.
     * @return numQuestions - the number of questions the user has selected for the quiz
	 */
    public int getNumQuestions() {
        return numQuestions;
    }

     /*
      * ActionListner management
      */
    private final ArrayList<ActionListener> actionListeners = new ArrayList<>();

    /**
     * Adds an ActionListener to the intro screen.
     * @param listener - the ActionListener to be added to the intro screen.
     */
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    /**
    * Removes an ActionListener from the intro screen.
    * @param listener - the ActionListener to be removed from the intro screen.
    */
    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }

    /**
     * Fires an ActionEvent to all ActionListeners registered to the intro screen. 
     * Used to signal that the user has requested to start the quiz.
     */
    protected void fireActionPerformed() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "startQuiz");
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(event);
        }
    }
}