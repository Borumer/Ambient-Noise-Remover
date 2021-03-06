package com.varunsingh.kalmanfilter;

/**
 * Filter using the Measure, Update, Predict algorithm that estimates position
 * and velocity
 */
public class AlphaBetaFilter implements EstimationFilter<Double>, Measurable<Double> {
    private SystemCycle cycleInfo;
    private Double alphaFilter;
    private Double betaFilter;
    private int iteration = 0;

    private final int TIME_INTERVAL = 5;

    /**
     * Constructor for AlphaBetaFilter
     * 
     * @param initialStateGuess    The initial guess of the system state
     * @param initialVelocityGuess The initial guess of the velocity
     * @param alphaFilter          The factor weight for the estimate of the
     *                             position
     * @param betaFilter           The factor weight for the estimate of the
     *                             velocity
     */
    public AlphaBetaFilter(double initialStateGuess, double initialVelocityGuess, Double alpha, Double beta) {
        cycleInfo = new SystemCycle(initialStateGuess, initialVelocityGuess, 0);
        cycleInfo.setStatePrediction(calculateStateExtrapolation());
        setAlphaFilter(alpha);
        setBetaFilter(beta);
    }

    public SystemCycle getCycleInfo() {
        return cycleInfo;
    }

    public void setCycleInfo(SystemCycle cycleInfo) {
        this.cycleInfo = cycleInfo;
    }

    public Double getBetaFilter() {
        return betaFilter;
    }

    public void setBetaFilter(Double betaFilter) {
        this.betaFilter = betaFilter;
    }

    public Double getAlphaFilter() {
        return alphaFilter;
    }

    public void setAlphaFilter(Double alphaFilter) {
        this.alphaFilter = alphaFilter;
    }

    public int getIteration() {
        return iteration;
    }

    @Override
    public Double calculateCurrentStateEstimate() {
        return KalmanFilterEquations.usePositionalStateUpdateEquation(
            cycleInfo.getStatePrediction(), 
            alphaFilter, 
            cycleInfo.getMeasurement()
        );
    }

    public Double calculateCurrentVelocity() {
        return KalmanFilterEquations.useVelocityStateUpdateEquation(
            cycleInfo.getStatePrediction(), 
            cycleInfo.getStateVelocity(), 
            betaFilter, 
            cycleInfo.getMeasurement(), 
            TIME_INTERVAL
        );
    }

    @Override
    public Double calculateStateExtrapolation() {
        return KalmanFilterEquations.usePositionalStateExtrapolationEquation(
            cycleInfo.getStateEstimate(), 
            TIME_INTERVAL, 
            cycleInfo.getStateVelocity()
        );
    }

    @Override
    public void measure(Double measurement) {
        iteration++;
        cycleInfo.setMeasurement(measurement);
        cycleInfo.setStateVelocity(calculateCurrentVelocity());
        cycleInfo.setStateEstimate(calculateCurrentStateEstimate());
        cycleInfo.setStatePrediction(calculateStateExtrapolation());
    }
    
}
