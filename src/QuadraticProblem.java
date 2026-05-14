import java.util.ArrayList;

public class QuadraticProblem {
    //Problems of the form ax^2 + bx + c = 0, must be factorable into linear factors with integer coefficients
    private final int a;
    private final int b;
    private final int c;

    private final ArrayList<String> acceptedFactorizations;
    private String proposedFactorization;

    private boolean isCorrect;


    public QuadraticProblem() {
        int coef1 = (int)(Math.random()*3) == 1 ? 2 : 1; 
		int coef2 = ((int)(Math.random()*9) + 1) * ((int)(Math.random()*2) == 1 ? 1 : -1);
		int coef3 = (int)(Math.random()*3) == 1 ? 2 : 1;
		int coef4 = ((int)(Math.random()*9) + 1) * ((int)(Math.random()*2) == 1 ? 1 : -1);

        if (coef2 % coef1 == 0) {
            coef1 /= coef1;
            coef2 /= coef1;
        }
        if (coef4 % coef3 == 0) {
            coef3 /= coef3;
            coef4 /= coef3;
        }

        // Create terms
        this.a = coef1 * coef3;
        this.b = coef1 * coef4 + coef2 * coef3;
        this.c = coef2 * coef4;

        // Generate accepted factorizations
        String fact1 = "(" + (coef1 == 1 ? "" : coef1) + "x" + (coef2 > 0 ? "+" + coef2 : coef2) + ")";
		String fact2 = "(" + (coef3 == 1 ? "" : coef3) + "x" + (coef4 > 0 ? "+" + coef4 : coef4) + ")";
		String fact1inv = "(" + coef2 + "+" + (coef1 == 1 ? "" : coef1) + "x)";
		String fact2inv = "(" + coef4 + "+" + (coef3 == 1 ? "" : coef3) + "x)";

        acceptedFactorizations = new ArrayList<String>();
        
        if(fact1.equals(fact2)) {
            acceptedFactorizations.add(fact1 + fact2);
            acceptedFactorizations.add(fact1inv + fact2inv);
            acceptedFactorizations.add(fact1 + fact2inv);
            acceptedFactorizations.add(fact1inv + fact2);
        }
        else {
            acceptedFactorizations.add(fact1 + fact2);
            acceptedFactorizations.add(fact2 + fact1);
            acceptedFactorizations.add(fact1inv + fact2inv);
            acceptedFactorizations.add(fact2inv + fact1inv);
            acceptedFactorizations.add(fact1 + fact2inv);
            acceptedFactorizations.add(fact2inv + fact1);
            acceptedFactorizations.add(fact1inv + fact2);
            acceptedFactorizations.add(fact2 + fact1inv);
        }
    }

    public ArrayList<String> getAcceptedFactorizations() {
        return acceptedFactorizations;
    }

    public boolean proposeFactorization(String proposedFactorization) {
        this.proposedFactorization = proposedFactorization;
        return checkProposedFactorization();
    }

    public boolean checkProposedFactorization() {
        this.isCorrect = acceptedFactorizations.contains(this.proposedFactorization);
        return isCorrect;
    }
    
    public String getProposedFactorization() {
        return proposedFactorization;
    }

    public String getQuestion() {
        return "Factor: " + (this.a == 1 ? "x²" : this.a + "x²")
            + (this.b > 0 ? " + " + this.b + "x" : " - " + (-this.b) + "x")
            + (this.c > 0 ? " + " + this.c : " - " + (-this.c));
    }
}
