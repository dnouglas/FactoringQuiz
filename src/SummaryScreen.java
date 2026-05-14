import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SummaryScreen extends JComponent implements ActionListener {
	private ArrayList<QuadraticProblem> questions;

    // Number of the question currently being displayed on the summary screen
    private int questionNum;

    //GUI components needed for summaryScreen after quiz
	private JLabel lblScore = new JLabel("", JLabel.CENTER);
				
	private JPanel summaryPane = new JPanel(new BorderLayout());
		private JPanel pnlArrows = new JPanel(new BorderLayout());
			private JLabel lblQuesNav = new JLabel("", JLabel.CENTER);
			private JButton btnNext = new JButton("►");
			private JButton btnPrev = new JButton("◄");
		private JTextArea taStats = new JTextArea("");
					
	private JButton btnRestartQuiz = new JButton("Retake Quiz");

    /**
     * Constructor for the summary screen. Initializes the GUI components and sets up the layout of the screen.
     */
    public SummaryScreen() {
        //Set up summaryScreen
        this.setLayout(new BorderLayout());

		this.add(lblScore, BorderLayout.NORTH);
			lblScore.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		
		this.add(summaryPane, BorderLayout.CENTER);
			summaryPane.add(taStats, BorderLayout.CENTER);
				taStats.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				taStats.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
				taStats.setEditable(false);
				taStats.setOpaque(false);
			summaryPane.add(pnlArrows, BorderLayout.NORTH);
				pnlArrows.add(btnNext, BorderLayout.EAST);
					btnNext.addActionListener(this);
				pnlArrows.add(btnPrev, BorderLayout.WEST);
					btnPrev.addActionListener(this);
				pnlArrows.add(lblQuesNav, BorderLayout.CENTER);
					lblQuesNav.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
				
		this.add(btnRestartQuiz, BorderLayout.SOUTH);
			btnRestartQuiz.addActionListener(this);
    }

    /**
     * Initializes the summary screen with the quiz summary data. 
     * Calculates the user's score and sets up the initial display for the summary screen.
     * @param quizSummary
     */
    public void initSummaryScreen(ArrayList<QuadraticProblem> questions) {
        this.questions = questions;

        int numCorrect = 0;
        for (QuadraticProblem question : this.questions) {
            if (question.checkProposedFactorization())
                numCorrect++;
        }

        /*formatted percentage score to prevent repeating/long decimal sequences
		 * ex. if there are 3 questions and 2 were correct, 66.67 will be outputted instead of 66.666...
		 */
		lblScore.setText(String.format("Score: %.2f%%", (double)numCorrect/this.questions.size()*100));
		
		this.goToQuestion(1);
    }

    /**
     * Updates the summary screen to display the information for a specified question number.
     * @param questionNum - the number of the question to be displayed on the summary screen.
     */
    private void goToQuestion(int questionNum) {
        if (questionNum < 1 || questionNum > this.questions.size())
            return;

        this.questionNum = questionNum;
        QuadraticProblem question = this.questions.get(this.questionNum-1);

        lblQuesNav.setText("Question " + this.questionNum);

        ArrayList<String> answers = question.getAcceptedFactorizations();
        taStats.setText(question.getQuestion() + 
                "\n\nCorrect Answers: " + answers.get(0) + " ," + answers.get(1) +
                "\nYour Answer: " + question.getProposedFactorization() + 
                "\n\n" + (question.checkProposedFactorization() ? "Correct!" : "Incorrect."));
    }

    /**
     * Event handler for the buttons in the summary screen. 
     * Handles user input for navigating through the quiz summary and restarting the quiz.
     * @param e
     */
    @Override
	public void actionPerformed(ActionEvent e) {
        JButton btnPressed = (JButton) e.getSource();

        if (btnPressed == btnRestartQuiz) 
			this.fireActionPerformed();
		else if (btnPressed == btnNext)
			this.goToQuestion(questionNum+1);
		else if (btnPressed == btnPrev)
			this.goToQuestion(questionNum-1);
    }
    
    /*
     * ActionListener management
     */
    private final ArrayList<ActionListener> actionListeners = new ArrayList<>();

    /**
     * Adds an ActionListener to the summary screen.
     * @param listener - the ActionListener to be added to the summary screen.
     */
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    /**
     * Removes an ActionListener from the summary screen.
     * @param listener
     */
    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }

    /**
     * Fires an ActionEvent to all registered ActionListeners.
     */
    protected void fireActionPerformed() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "restartQuiz");
        for (ActionListener listener : actionListeners)
            listener.actionPerformed(event);
    }
}
