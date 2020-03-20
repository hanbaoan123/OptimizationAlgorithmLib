package Util;

import de.dislin.Dislin;

public class surfcpw {
	public static float zfun(float x, float y, int iopt) {
		double v;
		if (iopt == 1) {
			v = Math.cos((double) x) * (3. + Math.cos((double) y));
		} else if (iopt == 2) {
			v = Math.sin((double) x) * (3. + Math.cos((double) y));
		} else {
			v = Math.sin((double) y);
		}
		return (float) v;
	}

	public static void main(String args[]) {
		float p = 3.14159f, step;
		Dislin.metafl("cons");
		Dislin.setpag("da4p");
		Dislin.disini();
		Dislin.pagera();
		Dislin.complx();
		Dislin.axspos(200, 2400);
		Dislin.axslen(1800, 1800);
		Dislin.intax();
		Dislin.titlin("Surface Plot of the Parametric Function", 2);
		Dislin.titlin("[COS(t)+(3+COS(u)), SIN(t)*(3+COS(u)), SIN(u)]", 4);
		Dislin.name("X-axis", "x");
		Dislin.name("Y-axis", "y");
		Dislin.name("Z-axis", "z");
		Dislin.vkytit(-300);
		Dislin.zscale(-1.f, 1.f);
		Dislin.graf3d(-4.f, 4.f, -4.f, 1.f, -4.f, 4.f, -4.f, 1.f, -3.f, 3.f, -3.f, 1.f);
		Dislin.height(40);
		Dislin.title();
		Dislin.surmsh("on");
		step = 2.f * 3.14159f / 30.f;
		Dislin.surfcp("surfcpw.zfun", 0.f, 2 * p, step, 0.f, 2 * p, step);
		Dislin.disfin();
	}
}