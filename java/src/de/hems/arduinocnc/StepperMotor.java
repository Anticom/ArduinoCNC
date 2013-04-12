package de.hems.arduinocnc;

public class StepperMotor {
	public enum Unit {
		mm, inch
	}
	
	protected String identifier;
	protected char axis;
	protected int stepsPerRev;	
	protected double feedPerRev;	//in mm
	protected int maxSpeed = 7500;
	protected int speed;
	
	public StepperMotor(String identifier, int stepsPerRev, int feedPerRev) {
		this.identifier		= identifier;
		this.stepsPerRev	= stepsPerRev;
		this.feedPerRev		= feedPerRev;
	}
	
	public StepperMotor(String identifier, int stepsPerRev, int feedPerRev, int maxSpeed) {
		this.identifier		= identifier;
		this.stepsPerRev	= stepsPerRev;
		this.feedPerRev		= feedPerRev;
		this.maxSpeed		= maxSpeed;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public char getAxis() {
		return axis;
	}
	
	public void setAxis(char axis) {
		this.axis = axis;
	}

	public int getStepsPerRev() {
		return stepsPerRev;
	}

	public void setStepsPerRev(int stepsPerRev) {
		this.stepsPerRev = stepsPerRev;
	}

	public double getFeedPerRev() {
		return feedPerRev;
	}

	public void setFeedPerRev(int feedPerRev) {
		this.feedPerRev = (double) feedPerRev;
	}
	
	public void setFeedPerRev(double feedPerRev) {
		this.feedPerRev = feedPerRev;
	}
	
	public void setFeedPerRev(double feedPerRev, Unit u) {
		switch(u) {
			case mm:
				this.feedPerRev = feedPerRev;
				break;
			case inch:
				this.feedPerRev = this.inch2mm(feedPerRev);
				break;
			default:
				//TODO: throw exception
				break;
		}
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	//extended functionalities:
	protected double inch2mm(double inch) {
		return (25.4 * inch);
	}
	
	public double feed2rev(double feed) {
		double revPerMm		= 1 / this.feedPerRev;
		//double revPerStep	= 1 / this.stepsPerRev;
		
		return feed * revPerMm;
	}
	
	public double feed2steps(double feed) {
		double revPerMm		= 1 / this.feedPerRev;
		//double revPerStep	= 1 / this.stepsPerRev;
		
		return feed * revPerMm * this.stepsPerRev;
	}
}