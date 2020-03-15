import org.jgrapht.graph.DefaultEdge;

public class NamedEdge extends DefaultEdge {
    private int label;
    private String winningTeam;
    private String defeatedTeam;

    /**
     * Constructs a relationship edge
     *
     * @param label the label of the new edge.
     *
     */
    public NamedEdge(int label,String w, String d)
    {
        this.label = label;
        this.winningTeam = w;
        this.defeatedTeam = d;
    }

    /**
         * Gets the label associated with this edge.
         *
         * @return edge label
         */
        public String getLabel()
        {
            return Integer.toString(label);
        }
        public int season() {return this.label;}
        @Override
        public String toString()
        {
            return "(" + getSource() + " : " + getTarget() + " : " + Integer.toString(label) + ")";
        }

    public String getDefeatedTeam() {
        return defeatedTeam;
    }

    public void setDefeatedTeam(String defeatedTeam) {
        this.defeatedTeam = defeatedTeam;
    }

    public String getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(String winningTeam) {
        this.winningTeam = winningTeam;
    }
}

