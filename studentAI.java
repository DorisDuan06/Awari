public class studentAI extends Player {
    private int maxDepth;

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public void move(BoardState state) {
        move = alphabetaSearch(state, maxDepth);
    }

    public int alphabetaSearch(BoardState state, int maxDepth) {
    	int move, v_max, i, alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE, v[] = new int[6];
    	for (i = 0; i < 6; i++)
    		v[i] = Integer.MIN_VALUE;
    	for (i = 0; i < 6; i++)
    		if (state.isLegalMove(1, i))
    			v[i] = minValue(state.applyMove(1, i), maxDepth, maxDepth - 1, alpha, beta);
    	
    	v_max = v[0];
    	move = 0;
    	for (i = 1; i < 6; i++)
    		if (v[i] > v_max) {
    			v_max = v[i];
    			move = i;
    		}
    	return move;
    }

    public int maxValue(BoardState state, int maxDepth, int currentDepth, int alpha, int beta) {
    	int legal = 0, i;
    	for (i = 0; i < 6; i++)
    		if (state.isLegalMove(1, i)) {
    			legal = 1;
    			break;
    		}
    	if ((currentDepth > 0 && legal == 0) || currentDepth == 0)
    		return sbe(state);
    	int v = Integer.MIN_VALUE;
    	for (i = 0; i < 6; i++)
    		if (state.isLegalMove(1, i)) {
    			v = Math.max(v, minValue(state.applyMove(1, i), maxDepth, currentDepth - 1, alpha, beta));
    			if (v >= beta)
    				return v;
    			alpha = Math.max(alpha, v);
    		}
    	return v;
    }

    public int minValue(BoardState state, int maxDepth, int currentDepth, int alpha, int beta) {
    	int legal = 0, i;
    	for (i = 0; i < 6; i++)
    		if (state.isLegalMove(2, i)) {
    			legal = 1;
    			break;
    		}
    	if ((currentDepth > 0 && legal == 0) || currentDepth == 0)
    		return sbe(state);
    	int v = Integer.MAX_VALUE;
    	for (i = 0; i < 6; i++)
    		if (state.isLegalMove(2, i)) {
    			v = Math.min(v, maxValue(state.applyMove(2, i), maxDepth, currentDepth - 1, alpha, beta));
    			if (alpha >= v)
    				return v;
    			beta = Math.min(beta, v);
    		}
    	return v;
    }

    public int sbe(BoardState state){
    	return state.getMyScore(1) - state.getMyScore(2);
    }
}