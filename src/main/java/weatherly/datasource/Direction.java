package weatherly.datasource;

public enum Direction {

    E, ENE, ESE, N, NE, NNE, NNW, NW, S, SE, SSE, SSW, SW, W, WNW, WSW;

    public static Direction from(double degrees) {
	degrees = Math.abs(degrees);
	final int floor = (int) Math.floor(degrees);
	degrees = floor % 360 + (degrees - floor);
	final int section = (int) Math.floor((degrees - 11.25) / 22.5);

	switch (section) {
	case 0:
	    return N;
	case 1:
	    return NNE;
	case 2:
	    return NE;
	case 3:
	    return ENE;
	case 4:
	    return E;
	case 5:
	    return ESE;
	case 6:
	    return SE;
	case 7:
	    return SSE;
	case 8:
	    return S;
	case 9:
	    return SSW;
	case 10:
	    return SW;
	case 11:
	    return WSW;
	case 12:
	    return W;
	case 13:
	    return WNW;
	case 14:
	    return NW;
	case 15:
	    return NNW;
	default:
	    return N;
	}

    }

}
